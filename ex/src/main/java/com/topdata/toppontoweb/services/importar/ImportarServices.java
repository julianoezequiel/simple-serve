package com.topdata.toppontoweb.services.importar;

import com.sun.jersey.core.header.FormDataContentDisposition;
import com.topdata.toppontoweb.dto.importar.ImportarHandler;
import com.topdata.toppontoweb.dto.importar.ImportarStatusTransfer;
import com.topdata.toppontoweb.entity.autenticacao.Operador;
import com.topdata.toppontoweb.entity.empresa.Empresa;
import com.topdata.toppontoweb.entity.marcacoes.Importacao;
import com.topdata.toppontoweb.services.ServiceException;
import com.topdata.toppontoweb.services.TopPontoService;
import static com.topdata.toppontoweb.services.TopPontoService.LOGGER;
import com.topdata.toppontoweb.services.empresa.EmpresaService;
import com.topdata.toppontoweb.services.funcionario.FuncionarioService;
import com.topdata.toppontoweb.services.funcionario.motivo.MotivoService;
import com.topdata.toppontoweb.services.marcacoes.EncaixeMarcacaoService;
import com.topdata.toppontoweb.services.marcacoes.ImportacaoService;
import com.topdata.toppontoweb.services.marcacoes.MarcacoesService;
import com.topdata.toppontoweb.services.permissoes.OperadorService;
import com.topdata.toppontoweb.services.rep.RepService;
import com.topdata.toppontoweb.utils.Utils;
import com.topdata.toppontoweb.utils.constantes.CONSTANTES;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import javax.activation.MimetypesFileTypeMap;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author tharle.camargo
 */
@Service
public class ImportarServices extends TopPontoService<Importacao, Object> {
    @Autowired
    private EmpresaService empresaService;
    
    @Autowired
    private FuncionarioService funcionarioService;
    
    @Autowired
    private RepService repService;
    
    @Autowired
    private ImportacaoService importacaoService;
    
    @Autowired
    private MarcacoesService marcacoesService;
    
    @Autowired
    private EncaixeMarcacaoService encaixeMarcacaoService;
    
    @Autowired
    private OperadorService operadorService;
    
    @Autowired
    private TopPontoService topPontoService;
    
    @Autowired
    private MotivoService motivoService;
    
    private final static HashMap<Integer, ImportarStatusTransfer> PROGRESSO_MAP = new HashMap<>();
    
    private static final long DELAY = 1000;
    private static final long PERIOD = 1000;
    
    public Response ImportarArquivoMarcacoes(HttpServletRequest request, InputStream arquivoImportadoInputStream, FormDataContentDisposition arquivoImportadoDetalhes, Integer idEmpresa, ImportarHandler handler) throws ServiceException {
        Empresa empresa = idEmpresa != null? getEmpresaService().buscar(idEmpresa) : null ;
        
        //Altera status para "CARREGANDO_ARQUIVO" 5%

        //Salva e retorna a Importacao
        Importacao importacao = getImportacaoService().criarImportacao(request);
        
        Operador operador = operadorService.getOperadorDaSessao(request);
        
        
        try {
            //Precisa salvar arquivo em disco.
            final String nomeCompletoArquivo = System.getProperty("java.io.tmpdir") + File.separator + arquivoImportadoDetalhes.getFileName();
            File arquivo = Utils.salvarArquivo(nomeCompletoArquivo, arquivoImportadoInputStream);
            
            if(!isArquivoTexto(arquivo)){
                return getTopPontoResponse().sucessoSalvar(CONSTANTES.PROCESSO_STATUS.ERRO_FORMATO_ARQUIVO);
            }
            
            //Le o arquivo do disco
            InputStreamReader isr = new InputStreamReader(new FileInputStream(nomeCompletoArquivo));
            BufferedReader reader = new BufferedReader(isr);
            
            //Total de linhas do arquivo (Isso pode demorar um pouco dependendo da quantidade de linhas)
            Integer totalLinhas = Utils.quantidadeLinhasArquivo(reader);
            
            
            ImportarStatusTransfer status = criarStatus(importacao.getIdImportacao());
            status.setProgresso(5);
            
            Double percentualPorLinha = 90d/totalLinhas;
            Thread thread = new Thread(() -> {
                processarArquivoMarcacoes(nomeCompletoArquivo, importacao, empresa, percentualPorLinha, status, handler, operador);
            }, "Thread-importacao-maarcacoes-id-"+status.getId());
            thread.start();
            
            status.setThread(thread);
            
            //Cria o monitor de timeout
            criarTimerMonitor(status);
            
            
            
            return getTopPontoResponse().sucessoSalvar(status);
            
        } catch (IOException ex) {
            return getTopPontoResponse().erro("Ocorreu um erro ao abir o arquivo.");
        }
    }
    
    public void processarArquivoMarcacoes(String nomeCompletoArquivo, Importacao importacao, Empresa empresa, Double percentualPorLinha, ImportarStatusTransfer status, ImportarHandler handler, Operador operador){
         LOGGER.debug("LENDO ARQUIVO!");

        
        InputStream is = null;
        try {
            
            //Le o arquivo (denovo) do disco
            is = new FileInputStream(nomeCompletoArquivo);
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader reader = new BufferedReader(isr);
            try {
                //Importar arquivo
                handler.importarArquivo(status, empresa, importacao, this, reader, percentualPorLinha);
                
                //TODO adicionar a mensagem de erro em algum lugar
                
                if(status.isCancelar()){
                    LOGGER.debug(" Processo (" + status.getId()+") foi cancelado, removendo as marcações adicionadas...");
                    //Cancela e remove todas as marcações da Importacao
                    status.setMensagem(CONSTANTES.PROCESSO_STATUS.CANCELADO);
                    
                    if(importacao.getQuantidadeValidas() > 0 || importacao.getQuantidadeInvalidas() > 0){
                        int rowsAfetadas = getMarcacoesService().removerTodosPelaImportacao(importacao);
                        LOGGER.debug(" Processo (" + status.getId()+") foram removidas "+rowsAfetadas+" marcações.");
                    }else{
                        LOGGER.debug(" Processo (" + status.getId()+") todas as marcações foram ignoradas, logo não precisa remover.");
                    }
                    
                    //Atualiza a importacao com os resultados zerados
                    importacao.setQuantidadeValidas(0);
                    importacao.setQuantidadeInvalidas(0);
                    importacao.setQuantidadeIgnoradas(0);
                }
                
                status.setProgresso(100);
                status.setImportacao(importacao);
                getImportacaoService().atualizar(importacao);
                getImportacaoService().auditar(importacao, handler, operador);
                
            } catch (ServiceException ex) {
                status.setProgresso(100);
                status.setImportacao(importacao);
                status.setMensagem(CONSTANTES.PROCESSO_STATUS.ERRO_AUDITAR);
            }
            
        }catch (IOException ex) {
            status.setProgresso(100);
            status.setImportacao(importacao);
            status.setMensagem(CONSTANTES.PROCESSO_STATUS.ERRO_ABRIR_ARQUIVO);
        }catch(ParseException | NumberFormatException e){
            e.printStackTrace();
            status.setProgresso(100);
            status.setImportacao(importacao);
            status.setMensagem(CONSTANTES.PROCESSO_STATUS.ERRO_FORMATO_ARQUIVO);
        }finally{
                try {
                    if (is != null) {
                        is.close();
                    }
                    File arquivo = new File(nomeCompletoArquivo);
                    if(arquivo.exists()){
                        //Remove arquivo dos temporarios
                        arquivo.delete();
                    }
                } catch (IOException ex) {
                    LOGGER.error(this.getClass().getSimpleName(), ex);
                }
        }
        
    }
    
    public Response validarCabecalho(HttpServletRequest request, String cabecalho, ImportarHandler importarHandler) throws ServiceException {
        
        return importarHandler.validarCabecalho(cabecalho, this);
        
    }
    
    public boolean isArquivoTexto(File f){
        MimetypesFileTypeMap fileTypeMap = new MimetypesFileTypeMap();
        String type = fileTypeMap.getContentType(f);
        
        return type.equals("text/plain");
    }
    
    
    
    public Response cancelarImportacao(Integer id){
        ImportarStatusTransfer status = buscarStatus(id);
        status.setCancelar(true);
        
        return getTopPontoResponse().sucessoAtualizar(status); 
    }
    
    public Response buscarImportacao(Integer id){
        ImportarStatusTransfer status = this.buscarStatus(id);
        
        //Atualiza data
        status.atualizarDataAtualizacao();
        
        //Existe resultado? Então é a ultima vez que está sendo chamado
        if(status.getMensagem().equals(CONSTANTES.PROCESSO_STATUS.CONCLUIDO)
            || status.getMensagem().equals(CONSTANTES.PROCESSO_STATUS.ERRO_ABRIR_ARQUIVO)
            || status.getMensagem().equals(CONSTANTES.PROCESSO_STATUS.ERRO_AUDITAR) ){
            this.removerStatus(id);
        }
        
        return this.getTopPontoResponse().sucessoSalvar(status);
    }
    
    public ImportarStatusTransfer buscarStatus(Integer id) {
        
        return ImportarServices.PROGRESSO_MAP.get(id);
    }

    public ImportarStatusTransfer criarStatus(Integer id) {
        removerStatusObsoletos();
        
        ImportarStatusTransfer progresso = new ImportarStatusTransfer(id);
        ImportarServices.PROGRESSO_MAP.put(id, progresso);
        
        return progresso;
    }
    
    public void removerStatusObsoletos(){
        HashMap<Integer, ImportarStatusTransfer> progressoMapClone =  (HashMap<Integer, ImportarStatusTransfer>) ImportarServices.PROGRESSO_MAP.clone();
        progressoMapClone.forEach((id, progresso)->{
            //Calcula a diferença em minutos do tempo da ultima atualizacao até agora
            long emMili = new Date().getTime() - progresso.getDataAtualizacao().getTime();
            long emMinutos = emMili / (1000 * 60);
            
            //Se passou mais que 5 minutos, então remove o progresso
            if(emMinutos > 5 ){
                removerStatus(progresso.getId());
            }
        });
    }
    
    public void removerStatus(Integer id) {
        ImportarServices.PROGRESSO_MAP.remove(id);
    }
    
    
    /**
     * Cria o Timer que servirá como monitor da Thread principal. Este timer tem
     * como objetivo cancelar a importacao caso ela não seja verificada pelo front em 10s. 
     * O tempo de Reset é determinado pelas contantes DELAY e PERIOD.
     *
     * @param statusTransfer
     */
    private void criarTimerMonitor(ImportarStatusTransfer status) {
        Timer timer = new Timer("Importação - Monitor processo - " + status.getId() + " - " + status.getMensagem() );
        
        TimerTask task = new TimerTask() {
            
            @Override
            public void run() {
                if(status.getThread() == null || !status.getThread().isAlive() || status.isCancelar() || status.getProgresso() >= 100 ){
                    LOGGER.debug("Importação - Monitor finalizado - Processo - " + status.getId());
                    this.cancel();
                }else{
                    Calendar c = Calendar.getInstance();
                    c.add(Calendar.SECOND, -4);

                    if (status.getDataAtualizacao().before(c.getTime())) {
                        LOGGER.debug("Importação - Processo cancelado por TimeOut : Processo - " + status.getId());
                        status.setCancelar(true);
                    }
                }
                
                
            }
        };
        
        timer.schedule(task, DELAY, PERIOD);
    }

    /**
     * @return the empresaService
     */
    public EmpresaService getEmpresaService() {
        return empresaService;
    }

    /**
     * @return the funcionarioService
     */
    public FuncionarioService getFuncionarioService() {
        return funcionarioService;
    }

    /**
     * @return the repService
     */
    public RepService getRepService() {
        return repService;
    }

    /**
     * @return the importacaoService
     */
    public ImportacaoService getImportacaoService() {
        return importacaoService;
    }

    /**
     * @return the marcacoesService
     */
    public MarcacoesService getMarcacoesService() {
        return marcacoesService;
    }

    /**
     * @return the encaixeMarcacaoService
     */
    public EncaixeMarcacaoService getEncaixeMarcacaoService() {
        return encaixeMarcacaoService;
    }

    /**
     * @return the topPontoService
     */
    public TopPontoService getTopPontoService() {
        return topPontoService;
    }

    /**
     * @return the motivoService
     */
    public MotivoService getMotivoService() {
        return motivoService;
    }

    
    
        
}
