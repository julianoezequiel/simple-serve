package com.topdata.toppontoweb.services.coletivo;

//<editor-fold defaultstate="collapsed" desc="IMPORTS">
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topdata.toppontoweb.dao.DaoException;
import com.topdata.toppontoweb.dto.ColetivoResultado;
import com.topdata.toppontoweb.dto.ColetivoTransfer;
import com.topdata.toppontoweb.dto.MsgRetorno;
import com.topdata.toppontoweb.dto.ProgressoTransfer;
import com.topdata.toppontoweb.entity.autenticacao.Operador;
import com.topdata.toppontoweb.entity.empresa.Departamento;
import com.topdata.toppontoweb.entity.funcionario.Coletivo;
import com.topdata.toppontoweb.entity.funcionario.Funcionario;
import com.topdata.toppontoweb.entity.funcionario.FuncionarioBancoHoras;
import com.topdata.toppontoweb.services.ServiceException;
import com.topdata.toppontoweb.services.funcionario.bancohoras.FuncionarioBancoHorasService;
import com.topdata.toppontoweb.services.funcionario.bancohoras.fechamento.FuncionarioBancoDeHorasFechamentoService;
import com.topdata.toppontoweb.utils.constantes.CONSTANTES;
import com.topdata.toppontoweb.utils.constantes.MSG;
//</editor-fold>

/**
 * Classe com as regras de Negócio para o lançamento coletivo da manutenção do
 * banco de horas
 *
 * @version 1.0.0 data 05/07/2017
 * @since 1.0.0 data 21/12/2016
 *
 * @author juliano.ezequiel
 */
@Service
public class LCBHManutencaoServices extends ColetivoService
        implements ColetivoSeviceInterface<FuncionarioBancoHoras> {

    @Autowired
    private FuncionarioBancoHorasService funcionarioBancoHorasService;
    @Autowired
    private FuncionarioBancoDeHorasFechamentoService funcionarioBancoDeHorasFechamentoService;

    private final static HashMap<Long, ProgressoTransfer> PROGRESSO_MAP = new HashMap<>();

    @Override
    public ColetivoTransfer buscarColetivo(HttpServletRequest request, Integer id) throws ServiceException {
        Coletivo coletivo = (Coletivo) this.buscar(Coletivo.class, id);
        Operador operador = getOperadorAtual(request);
        if (!coletivo.getFuncionarioBancoHorasList().isEmpty()) {
            ColetivoTransfer coletivoTransfer = new ColetivoTransfer(coletivo);
            //Seta a lista de banco de horas
            coletivoTransfer.setFuncionarioBancoHorasList(coletivo.getFuncionarioBancoHorasList());
            coletivoTransfer.setFuncionarioList(this.filtrarFuncionario(coletivoTransfer.getColetivo().getFuncionarioBancoHorasList(), operador));
            coletivoTransfer.setDepartamentoList(this.filtrarDepartamento(coletivoTransfer.getFuncionarioList(), operador));
            coletivoTransfer.setEmpresaList(this.filtrarEmpresa(coletivoTransfer.getDepartamentoList(), operador));
            //verifica se o coletivo posera ser editado, pelo operador autenticado
            coletivoTransfer.getColetivo().setPermitido(isColetivoPermitido(filtrarDepartamento(filtrarFuncionario(coletivoTransfer.getColetivo().getFuncionarioBancoHorasList(), operador), operador), operador));

            this.validacoesEdicao(coletivoTransfer.getColetivo().getFuncionarioBancoHorasList());
            return coletivoTransfer;
        } else {
            throw new ServiceException(this.getTopPontoResponse().alertaNaoCad(new Coletivo().toString()));
        }
    }

    @Override
    public List<ColetivoTransfer> listarColetivos(HttpServletRequest request) throws ServiceException {
        try {
            List<ColetivoTransfer> coletivoTransferlist = this.listarColetivoBHManutencao(this.getColetivoDao().buscarColetivoBHManutencao());
            Operador operador = this.getOperadorAtual(request);
            coletivoTransferlist.stream().forEach(new ConsumerImpl(operador));

            List<ColetivoTransfer> coletivoTransferlistResult = coletivoTransferlist.stream().filter(coletivoTransfer
                    -> coletivoTransfer.getFuncionarioList()
                    .stream().anyMatch(funcionario -> funcionario.getPermitido()))
                    .collect(Collectors.toList());

            return coletivoTransferlistResult;
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erroBuscar(), ex);
        }
    }

    /**
     * Esse parametro foi alterado, pois, tem que setar qual é a entidade para o
     * coletivoTransfer
     *
     * @param coletivoList lista de coletivos
     * @return retorna uma lista de coletivo para tela!
     */
    public List<ColetivoTransfer> listarColetivoBHManutencao(List<Coletivo> coletivoList) {
        List<ColetivoTransfer> coletivoTransferList = new ArrayList<>();
        coletivoList.stream().forEach(coletivo -> {
            ColetivoTransfer coletivoTransfer = new ColetivoTransfer(coletivo);

            //Seta a lista de entidade para banco de horas (manutencao)
            coletivoTransfer.setFuncionarioBancoHorasList(coletivo.getFuncionarioBancoHorasList());
            coletivoTransferList.add(coletivoTransfer);
        });
        return coletivoTransferList;
    }

    @Override
    public void salvarColetivo(HttpServletRequest request, ColetivoTransfer coletivoEntidade, ProgressoTransfer progresso) throws ServiceException {

        List<FuncionarioBancoHoras> funcionarioBancoHorasList = new ArrayList<>();
        List<ColetivoResultado> resultadosColetivo = new ArrayList<>();

        coletivoEntidade.getFuncionarioBancoHorasList().stream().forEach(af -> {
            try {

                coletivoEntidade.setColetivo(criarColetivo(coletivoEntidade.getColetivo(), CONSTANTES.Enum_FUNCIONALIDADE.LC_BANCO_HORAS_MANUTENCAOES));
                coletivoEntidade.getFuncionarioList().stream().forEach(funcionario -> {
                    try {
                        funcionario = this.getFuncionarioService().buscar(Funcionario.class, funcionario.getIdFuncionario());

                        FuncionarioBancoHoras a = (FuncionarioBancoHoras) af.clone();
                        a.setColetivo(coletivoEntidade.getColetivo());
                        a.setFuncionario(funcionario);
                        funcionarioBancoHorasList.add(a);
                    } catch (CloneNotSupportedException | ServiceException ex) {
                        LOGGER.error(LCCalencarioServices.class.getName(), ex);
                    }
                });
            } catch (ServiceException ex) {
                LOGGER.error(LCBHManutencaoServices.class.getName(), ex);
            }
        });

        coletivoEntidade.getColetivo().setFuncionarioBancoHorasList(funcionarioBancoHorasList);
        resultadosColetivo.addAll(this.funcionarioBancoHorasService.salvarFuncionarioBancoHorasColetivo(funcionarioBancoHorasList, progresso));

        Coletivo coletivo = coletivoEntidade.getColetivo();
        Boolean somenteErros = resultadosColetivo.stream().allMatch(r
                -> r.getResultado() == ColetivoResultado.Enum_ResultadoColetivo.ERRO && !r.isFechamento());

        if (somenteErros) {
            //todos com erro, deve-se excluir o coletivo
            this.excluirColetivo(coletivo, CONSTANTES.Enum_FUNCIONALIDADE.LC_BANCO_HORAS_MANUTENCAOES);
            coletivoEntidade.setColetivo(null);
        }
        //Adiciona no progresso
        progresso.setColetivoResultados(resultadosColetivo);
    }

    @Override
    public void atualizarColetivo(HttpServletRequest request, ColetivoTransfer coletivoEntidade, ProgressoTransfer progresso) throws ServiceException {
        List<FuncionarioBancoHoras> funcionarioBancoHorasList = new ArrayList<>();
        List<ColetivoResultado> resultadosColetivo = new ArrayList<>();

        coletivoEntidade.getFuncionarioBancoHorasList().stream().forEach(af -> {
            try {
                coletivoEntidade.setColetivo(atualizarColetivo(coletivoEntidade.getColetivo(), CONSTANTES.Enum_FUNCIONALIDADE.LC_BANCO_HORAS_MANUTENCAOES));
                coletivoEntidade.getFuncionarioList().stream().forEach(funcionario -> {
                    try {
                        Funcionario f = getFuncionarioService().buscar(Funcionario.class, funcionario.getIdFuncionario());
                        List<FuncionarioBancoHoras> bancoHorasBanco = funcionarioBancoHorasService.buscarPorColetivo(coletivoEntidade.getColetivo());
                        FuncionarioBancoHoras a = bancoHorasBanco
                                .stream()
                                .filter(af1
                                        -> Objects.equals(f.getIdFuncionario(), af1.getFuncionario().getIdFuncionario())
                                        && af1.getColetivo() != null
                                        && af1.getColetivo().getIdColetivo().equals(af.getColetivo().getIdColetivo()))
                                .findAny().get();

                        a.setDataFim(af.getDataFim());
                        a.setDataInicio(af.getDataInicio());
                        a.setBancoHoras(af.getBancoHoras());
                        a.setFuncionario(f);
                        funcionarioBancoHorasList.add(a);
                    } catch (ServiceException ex) {
                        LOGGER.error(LCBHManutencaoServices.class.getName(), ex);
                    } catch (NoSuchElementException e) {
                        FuncionarioBancoHoras a = this.getClone(af);
                        a.setIdFuncionarioBancoHoras(null);
                        a.setFuncionario(funcionario);
                        funcionarioBancoHorasList.add(a);
                    }
                });
            } catch (ServiceException ex) {
                LOGGER.error(LCBHManutencaoServices.class.getName(), ex);
            }
        });

        coletivoEntidade.getColetivo().setFuncionarioBancoHorasList(funcionarioBancoHorasList);
        resultadosColetivo.addAll(removerEntidadeTipoDoColetivo(coletivoEntidade.getColetivo()));

        resultadosColetivo.addAll(this.funcionarioBancoHorasService.salvarFuncionarioBancoHorasColetivo(funcionarioBancoHorasList, progresso));

        Coletivo coletivo = coletivoEntidade.getColetivo();
        Boolean somenteErros = resultadosColetivo.stream().allMatch(r
                -> r.getResultado() == ColetivoResultado.Enum_ResultadoColetivo.ERRO && !r.isFechamento());

        if (somenteErros) {
            //todos com erro, deve-se excluir o coletivo
            this.excluirColetivo(coletivo, CONSTANTES.Enum_FUNCIONALIDADE.LC_BANCO_HORAS_MANUTENCAOES);
            coletivoEntidade.setColetivo(null);
        }
        //Adiciona no progresso
        progresso.setColetivoResultados(resultadosColetivo);
    }

    /**
     * Realiza as validações de cada Banco de horas vínculado ao funcionário
     * através deste coletivo, em caso de existir um banco que não possa ser
     * excluido, será lançada uma exceção.
     *
     * @param request
     * @param id do coletivo
     * @return Response
     * @throws ServiceException
     */
    @Override
    public Response excluirColetivo(HttpServletRequest request, Integer id) throws ServiceException {
        LOGGER.debug("Início do processo de exclusão");
        AtomicBoolean isPermitidoExclusao = new AtomicBoolean(Boolean.TRUE);
        Coletivo c = (Coletivo) this.buscar(Coletivo.class, id);
        List<FuncionarioBancoHoras> funcionarioBancoHorasList = this.funcionarioBancoHorasService.buscarPorColetivo(c);
        //valida se este coletivo poderá ser excluído.
        //É utilizada as mesmas regras para a exclusão do registro manual
        funcionarioBancoHorasList.stream().forEach(a -> {
            try {
                this.funcionarioBancoHorasService.validarExcluir(a);
            } catch (ServiceException ex) {
                isPermitidoExclusao.set(Boolean.FALSE);
            }
        });
        //em caso exista pelo menos um registro que faça parte deste coletivo que não possa ser excluído
        //será lançada uma exceção
        if (isPermitidoExclusao.get() == Boolean.TRUE) {

            funcionarioBancoHorasList.stream().forEach(fb -> {
                try {
                    this.funcionarioBancoDeHorasFechamentoService.removerVinculos(fb);
                } catch (ServiceException ex) {
                    LOGGER.error(LCBHManutencaoServices.class.getName(), ex);
                }
            });

            //solicita a exclusão de todos os registros que faça parte deste coletivo    
            this.funcionarioBancoHorasService.excluirPorColetivo(c);

            //Adiciona na auditoria para cada um deles
            funcionarioBancoHorasList.stream().forEach(funcBH -> {
                try {
                    this.funcionarioBancoHorasService.getAuditoriaServices().auditar(CONSTANTES.Enum_FUNCIONALIDADE.CAD_FUNCIONARIO_BANCO_DE_HORAS, CONSTANTES.Enum_AUDITORIA.DEFAULT, CONSTANTES.Enum_OPERACAO.EXCLUIR, funcBH);
                } catch (ServiceException ex) {
                    LOGGER.error(LCBHManutencaoServices.class.getName(), ex);
                }
            });

            this.excluirColetivo(c, CONSTANTES.Enum_FUNCIONALIDADE.LC_BANCO_HORAS_FECHAMENTOS);
            LOGGER.debug("Término do processo de exclusão");
            return this.getTopPontoResponse().sucessoExcluir(c.toString());
        } else {
            return this.getTopPontoResponse().alertaValidacao(MSG.COLETIVO.ALERTA_EXCLUIR_COLETIVO.getResource());
        }
    }

    public ProgressoTransfer buscarProgresso(long id) {
        return LCBHManutencaoServices.PROGRESSO_MAP.get(id);
    }

    public ProgressoTransfer criarProgresso() {
        removerProgressosObsoletos();
        ProgressoTransfer progresso = new ProgressoTransfer();
        LCBHManutencaoServices.PROGRESSO_MAP.put(progresso.getId(), progresso);
        return progresso;
    }

    public void removerProgressosObsoletos() {
        HashMap<Long, ProgressoTransfer> progressoMapClone = (HashMap<Long, ProgressoTransfer>) LCBHManutencaoServices.PROGRESSO_MAP.clone();
        progressoMapClone.forEach((id, progresso) -> {
            //Calcula a diferença em minutos do tempo da ultima atualizacao até agora
            long emMili = new Date().getTime() - progresso.getDataAtualizacao().getTime();
            long emMinutos = emMili / (1000 * 60);

            //Se passou mais que 5 minutos, então remove o progresso
            if (emMinutos > 5) {
                removerProgresso(progresso.getId());
            }
        });
    }

    public void removerProgresso(long id) {
        LCBHManutencaoServices.PROGRESSO_MAP.remove(id);
    }

    //<editor-fold defaultstate="collapsed" desc="FUNÇÕES AUXILIARES">
    @Override
    public List<Funcionario> filtrarFuncionario(List<FuncionarioBancoHoras> funcionarioBancoHorasList, Operador operador) {
        //lista os funcionários que possuem este coletivo
        List<Funcionario> funcionariosList = funcionarioBancoHorasList.stream().map(h -> h.getFuncionario()).distinct().collect(Collectors.toList());
        //verifica os departamentos do operador autenticado
        List<Departamento> deptosValidos = operador.getGrupo().getDepartamentoList().stream().distinct().collect(Collectors.toList());
        //caso não possua departamentos seta todos os funcionários para editavel
        if (deptosValidos.isEmpty()) {
            funcionariosList.stream().forEach(d1 -> d1.setPermitido(true));
        } else {
            funcionariosList.stream().forEach(d1 -> d1.setPermitido(deptosValidos.contains(d1.getDepartamento())));
        }
        return funcionariosList;
    }

    @Override
    public List<ColetivoResultado> removerEntidadeTipoDoColetivo(Coletivo coletivo) throws ServiceException {
        List<FuncionarioBancoHoras> listaNova = coletivo.getFuncionarioBancoHorasList();
        List<FuncionarioBancoHoras> listaAntiga = this.funcionarioBancoHorasService.buscarPorColetivo(coletivo).stream().collect(Collectors.toList());
        listaAntiga.removeAll(listaNova);
        return this.funcionarioBancoHorasService.removerColetivo(listaAntiga, coletivo);
    }

    public class ConsumerImpl implements Consumer<ColetivoTransfer> {

        private final Operador operador;

        public ConsumerImpl(Operador operador) {
            this.operador = operador;
        }

        @Override
        public void accept(ColetivoTransfer coletivo) {
            //filtra os funcionários que o coletivo possui
            coletivo.setFuncionarioList(filtrarFuncionario(coletivo.getColetivo().getFuncionarioBancoHorasList(), operador));

            coletivo.getColetivo().setPermitido(isColetivoPermitido(filtrarDepartamento(filtrarFuncionario(coletivo.getColetivo().getFuncionarioBancoHorasList(), operador), operador), operador));
        }
    }

    /**
     * Retorna o clone ou uma entidade nova
     *
     * @param af
     * @return
     */
    private FuncionarioBancoHoras getClone(FuncionarioBancoHoras af) {
        try {
            return (FuncionarioBancoHoras) af.clone();
        } catch (CloneNotSupportedException ex) {
            LOGGER.error(LCBHManutencaoServices.class.getName(), ex);
            return new FuncionarioBancoHoras();
        }
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="REGRAS PARA EDIÇÃO DO COLETIVO E EXCLUSÃO">
    @Override
    public void validacoesEdicao(Collection<FuncionarioBancoHoras> funcionarioBHLista) {

        funcionarioBHLista.parallelStream().forEach(funcionarioBH -> {
            LOGGER.debug("validação funcionario banco de horas editavel funcionário : {}", funcionarioBH.getFuncionario().getNome());
            String mensagem = isEditavel(funcionarioBH);
            //Coletivo
            funcionarioBH.getColetivo().setEditavel(mensagem == null);

            //Empresa
            funcionarioBHLista.parallelStream()
                    .map(fb -> fb.getFuncionario()).distinct()
                    .map(fd -> fd.getDepartamento()).distinct()
                    .map(e -> e.getEmpresa())
                    .forEach(d -> {
                        d.setEditavel(mensagem == null);
                        d.setMensagem(mensagem);
                    });
            //Departamento
            funcionarioBHLista.parallelStream()
                    .map(fb -> fb.getFuncionario()).distinct()
                    .map(fd -> fd.getDepartamento()).distinct()
                    .forEach(d -> {
                        d.setEditavel(mensagem == null);
                        d.setMensagem(mensagem);
                    });

            //Funcionario
            //obs: talvez esta função esteja errada. Trocar pela função abaixo
//            funcionarioBHLista.parallelStream()
//                    .map(fb -> fb.getFuncionario()).distinct()
//                    .forEach(d -> {
//                        d.setEditavel(mensagem == null);
//                        d.setMensagem(mensagem);
//                    });
            funcionarioBH.getFuncionario().setEditavel(mensagem == null);
            funcionarioBH.getFuncionario().setMensagem(mensagem);
        });

    }

    private String isEditavel(FuncionarioBancoHoras funcionarioBH) {
        try {
            this.funcionarioBancoHorasService.validarExcluir(funcionarioBH);
            return null;
        } catch (ServiceException ex) {
            MsgRetorno msg = (MsgRetorno) ex.getResponse().getEntity();
            return msg.getBody();
        }
    }

//</editor-fold>
}
