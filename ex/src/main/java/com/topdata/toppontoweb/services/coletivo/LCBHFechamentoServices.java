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
import com.topdata.toppontoweb.entity.funcionario.FuncionarioBancoHorasFechamento;
import com.topdata.toppontoweb.services.ServiceException;
import com.topdata.toppontoweb.services.funcionario.bancohoras.fechamento.FuncionarioBancoDeHorasFechamentoService;
import com.topdata.toppontoweb.utils.constantes.CONSTANTES;
import com.topdata.toppontoweb.utils.constantes.MSG;
//</editor-fold>

/**
 * Classe de regras de Negócio para o lançamento coletivo do fechamento do banco
 * de horas
 *
 * @version 1.0.0 data 03/07/2017
 * @since 1.0.0 data 03/07/2017
 *
 * @author juliano.ezequiel
 */
@Service
public class LCBHFechamentoServices extends ColetivoService
        implements ColetivoSeviceInterface<FuncionarioBancoHorasFechamento> {

    @Autowired
    private FuncionarioBancoDeHorasFechamentoService funcionarioBancoDeHorasFechamentoService;

    private final static HashMap<Long, ProgressoTransfer> PROGRESSO_MAP = new HashMap<>();

    @Override
    public ColetivoTransfer buscarColetivo(HttpServletRequest request, Integer id) throws ServiceException {
        Coletivo coletivo = (Coletivo) this.buscar(Coletivo.class, id);
        Operador operador = getOperadorAtual(request);
        if (!coletivo.getFuncionarioBancoHorasFechamentoList().isEmpty()) {
            ColetivoTransfer coletivoTransfer = new ColetivoTransfer(coletivo);
            //Seta a lista de banco de horas
            coletivoTransfer.setFuncionarioBancoHorasFechamentoList(coletivo.getFuncionarioBancoHorasFechamentoList());
            coletivoTransfer.setFuncionarioList(this.filtrarFuncionario(coletivoTransfer.getColetivo().getFuncionarioBancoHorasFechamentoList(), operador));
            coletivoTransfer.setDepartamentoList(this.filtrarDepartamento(coletivoTransfer.getFuncionarioList(), operador));
            coletivoTransfer.setEmpresaList(this.filtrarEmpresa(coletivoTransfer.getDepartamentoList(), operador));
            //verifica se o coletivo posera ser editado, pelo operador autenticado
            coletivoTransfer.getColetivo().setPermitido(isColetivoPermitido(filtrarDepartamento(filtrarFuncionario(coletivoTransfer.getColetivo().getFuncionarioBancoHorasFechamentoList(), operador), operador), operador));

            this.validacoesEdicao(coletivoTransfer.getColetivo().getFuncionarioBancoHorasFechamentoList());
            return coletivoTransfer;
        } else {
            throw new ServiceException(this.getTopPontoResponse().alertaNaoCad(new Coletivo().toString()));
        }
    }

    @Override
    public List<ColetivoTransfer> listarColetivos(HttpServletRequest request) throws ServiceException {
        try {

            List<ColetivoTransfer> coletivoTransferlist = this.listarColetivoBHFechamento(this.getColetivoDao().buscarColetivoBHFechamento());
            Operador operador = this.getOperadorAtual(request);
            coletivoTransferlist.stream().forEach(new ValidarPermissoesDoColetivo(operador));

            //filtra somente os permitido
            List<ColetivoTransfer> coletivoTransferlistResult
                    = coletivoTransferlist
                    .stream()
                    .filter(coletivoTransfer -> coletivoTransfer.getFuncionarioList()
                            .stream()
                            .anyMatch(funcionario -> funcionario.getPermitido()))
                    .collect(Collectors.toList());

            return coletivoTransferlistResult;
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erroBuscar(), ex);
        }
    }

    private class ValidarPermissoesDoColetivo implements Consumer<ColetivoTransfer> {

        private final Operador operador;

        public ValidarPermissoesDoColetivo(Operador operador) {
            this.operador = operador;
        }

        @Override
        public void accept(ColetivoTransfer coletivo) {
            //filtra os funcionários que o coletivo possui
            coletivo.setFuncionarioList(filtrarFuncionario(coletivo.getColetivo().getFuncionarioBancoHorasFechamentoList(), this.operador));
            coletivo.getColetivo().setPermitido(isColetivoPermitido(filtrarDepartamento(filtrarFuncionario(coletivo.getColetivo().getFuncionarioBancoHorasFechamentoList(), this.operador), this.operador), this.operador));
        }
    }

    /**
     * Converte a lista de Coletivo em lista de coletivo transfer
     *
     * @param coletivoList lista de coletivos
     * @return retorna uma lista de coletivo para tela!
     */
    public List<ColetivoTransfer> listarColetivoBHFechamento(List<Coletivo> coletivoList) {
        List<ColetivoTransfer> coletivoTransferList = new ArrayList<>();
        coletivoList.stream().forEach((coletivo) -> {
            ColetivoTransfer coletivoTransfer = new ColetivoTransfer(coletivo);
            //Seta a lista de entidade para banco de horas (manutencao)
            coletivoTransfer.setFuncionarioBancoHorasFechamentoList(coletivo.getFuncionarioBancoHorasFechamentoList());
            coletivoTransferList.add(coletivoTransfer);
        });
        return coletivoTransferList;
    }

    @Override
    public void salvarColetivo(HttpServletRequest request, ColetivoTransfer coletivoEntidade, ProgressoTransfer progresso) throws ServiceException {

        List<FuncionarioBancoHorasFechamento> funcionarioBancoHorasFechamentoList = new ArrayList<>();
        List<ColetivoResultado> resultadosColetivo = new ArrayList<>();
        //entidade de fechamento que será replicada para cada funcionario da lista
        FuncionarioBancoHorasFechamento funcionarioBancoHorasFechamento = coletivoEntidade.getFuncionarioBancoHorasFechamentoList().iterator().next();
        //salva um novo coletivo na base
        Coletivo coletivo = criarColetivo(coletivoEntidade.getColetivo(), CONSTANTES.Enum_FUNCIONALIDADE.LC_BANCO_HORAS_FECHAMENTOS);
        //adiciona o coletivo a lista de funcionário recebida 
        coletivoEntidade.setColetivo(coletivo);
        //cria uma entidade de fechamento para cada funcionário
        List<Funcionario> funcionarioList = coletivoEntidade.getFuncionarioList();

        funcionarioList.stream().forEach((funcionario) -> {
            try {
                funcionario = this.getFuncionarioService().buscar(Funcionario.class, funcionario.getIdFuncionario());
                FuncionarioBancoHorasFechamento fechamento = (FuncionarioBancoHorasFechamento) funcionarioBancoHorasFechamento.clone();
                fechamento.setColetivo(coletivoEntidade.getColetivo());
                fechamento.setFuncionario(funcionario);
                funcionarioBancoHorasFechamentoList.add(fechamento);
            } catch (ServiceException | CloneNotSupportedException ex) {
                LOGGER.error(LCBHManutencaoServices.class.getName(), ex);
            }
        });

        coletivoEntidade.getColetivo().setFuncionarioBancoHorasFechamentoList(funcionarioBancoHorasFechamentoList);
        resultadosColetivo.addAll(this.funcionarioBancoDeHorasFechamentoService.salvarFuncionarioBancoHorasFechamentoColetivo(funcionarioBancoHorasFechamentoList, progresso));

        //todos os resultados forem erros
        Boolean somenteErros = resultadosColetivo.stream().allMatch(r -> r.getResultado() == ColetivoResultado.Enum_ResultadoColetivo.ERRO);
        if (somenteErros == true) {
            //todos com erro, deve-se excluir o coletivo
            this.excluirColetivo(coletivo, CONSTANTES.Enum_FUNCIONALIDADE.LC_BANCO_HORAS_FECHAMENTOS);
            coletivoEntidade.setColetivo(null);
        }

        //Adiciona no progresso
        progresso.setColetivoResultados(resultadosColetivo);
    }

    @Override
    public void atualizarColetivo(HttpServletRequest request, ColetivoTransfer coletivoEntidade, ProgressoTransfer progresso) throws ServiceException {

        List<FuncionarioBancoHorasFechamento> funcionarioBancoHorasList = new ArrayList<>();
        List<ColetivoResultado> resultadosColetivo = new ArrayList<>();
        //entidade de fechamento que será replicada para cada funcionario da lista
        FuncionarioBancoHorasFechamento funcionarioBancoHorasFechamento = coletivoEntidade.getFuncionarioBancoHorasFechamentoList().iterator().next();
        //salva um novo coletivo na base
        Coletivo coletivo = atualizarColetivo(coletivoEntidade.getColetivo(), CONSTANTES.Enum_FUNCIONALIDADE.LC_BANCO_HORAS_FECHAMENTOS);
        //adiciona o coletivo a lista de funcionário recebida 
        coletivoEntidade.setColetivo(coletivo);
        //cria uma entidade de fechamento para cada funcionário
        List<Funcionario> funcionarioList = coletivoEntidade.getFuncionarioList();

        funcionarioList.stream().forEach(funcionario -> {
            try {
                Funcionario funcionarioBase = this.getFuncionarioService().buscar(Funcionario.class, funcionario.getIdFuncionario());
                List<FuncionarioBancoHorasFechamento> funcionarioBancoHorasFechamentoList
                        = this.funcionarioBancoDeHorasFechamentoService.buscarPorColetivo(coletivoEntidade.getColetivo());

                FuncionarioBancoHorasFechamento bancoHorasFechamento = funcionarioBancoHorasFechamentoList
                        .stream().filter(af1
                                -> Objects.equals(funcionarioBase.getIdFuncionario(), af1.getFuncionarioBancoHoras().getFuncionario().getIdFuncionario())
                                && af1.getColetivo() != null
                                && af1.getColetivo().getIdColetivo().equals(funcionarioBancoHorasFechamento.getColetivo().getIdColetivo()))
                        .findAny().get();

                bancoHorasFechamento.setCredito(funcionarioBancoHorasFechamento.getCredito());
                bancoHorasFechamento.setDataFechamento(funcionarioBancoHorasFechamento.getDataFechamento());
                bancoHorasFechamento.setDebito(funcionarioBancoHorasFechamento.getDebito());
                bancoHorasFechamento.setFuncionario(funcionarioBase);
                bancoHorasFechamento.setGatilho(funcionarioBancoHorasFechamento.getGatilho());
                funcionarioBancoHorasList.add(bancoHorasFechamento);
                bancoHorasFechamento.setTipoAcerto(funcionarioBancoHorasFechamento.getTipoAcerto());
                bancoHorasFechamento.setTipoFechamento(funcionarioBancoHorasFechamento.getTipoFechamento());
            } catch (ServiceException ex) {
                LOGGER.error(LCAfastamentoServices.class.getName(), ex);
            } catch (NoSuchElementException e) {
                FuncionarioBancoHorasFechamento a = this.getClone(funcionarioBancoHorasFechamento);
                a.setIdFuncionarioBancoHorasFechamento(null);
                a.setFuncionario(funcionario);
                funcionarioBancoHorasList.add(a);
            }
        });

        coletivoEntidade.getColetivo().setFuncionarioBancoHorasFechamentoList(funcionarioBancoHorasList);
        resultadosColetivo.addAll(removerEntidadeTipoDoColetivo(coletivoEntidade.getColetivo()));

        resultadosColetivo.addAll(this.funcionarioBancoDeHorasFechamentoService.salvarFuncionarioBancoHorasFechamentoColetivo(funcionarioBancoHorasList, progresso));

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
        AtomicBoolean isPermitidoExclusao = new AtomicBoolean(Boolean.TRUE);
        Coletivo c = (Coletivo) this.buscar(Coletivo.class, id);
        List<FuncionarioBancoHorasFechamento> funcionarioBancoHorasFechamentoList = this.funcionarioBancoDeHorasFechamentoService.buscarPorColetivo(c);
        //valida se este coletivo poderá ser excluído.
        //É utilizada as mesmas regras para a exclusão do registro manual
        funcionarioBancoHorasFechamentoList.stream().forEach(a -> {
            try {
                this.funcionarioBancoDeHorasFechamentoService.validarExcluir(a);
            } catch (ServiceException ex) {
                isPermitidoExclusao.set(Boolean.FALSE);
            }
        });
        //em caso exista pelo menos um registro que faça parte deste coletivo que não possa ser excluído
        //será lançada uma exceção
        if (isPermitidoExclusao.get() == Boolean.TRUE) {
            //solicita a exclusão de todos os registros que faça parte deste coletivo    
            this.funcionarioBancoDeHorasFechamentoService.excluirPorColetivo(c);

            //Adiciona na auditoria para cada um deles
            funcionarioBancoHorasFechamentoList.stream().forEach(funcBH -> {
                try {
                    this.funcionarioBancoDeHorasFechamentoService.getAuditoriaServices().auditar(CONSTANTES.Enum_FUNCIONALIDADE.CAD_FUNCIONARIO_BANCO_DE_HORAS, CONSTANTES.Enum_AUDITORIA.DEFAULT, CONSTANTES.Enum_OPERACAO.EXCLUIR, funcBH);
                } catch (ServiceException ex) {
                    LOGGER.error(LCBHFechamentoServices.class.getName(), ex);
                }
            });

            this.excluirColetivo(c, CONSTANTES.Enum_FUNCIONALIDADE.LC_BANCO_HORAS_FECHAMENTOS);
            return this.getTopPontoResponse().sucessoExcluir(c.toString());
        } else {
            return this.getTopPontoResponse().alertaValidacao(MSG.COLETIVO.ALERTA_EXCLUIR_COLETIVO.getResource());
        }
    }

    public ProgressoTransfer buscarProgresso(long id) {
        return LCBHFechamentoServices.PROGRESSO_MAP.get(id);
    }

    public ProgressoTransfer criarProgresso() {
        removerProgressosObsoletos();

        ProgressoTransfer progresso = new ProgressoTransfer();
        LCBHFechamentoServices.PROGRESSO_MAP.put(progresso.getId(), progresso);

        return progresso;
    }

    public void removerProgressosObsoletos() {
        HashMap<Long, ProgressoTransfer> progressoMapClone = (HashMap<Long, ProgressoTransfer>) LCBHFechamentoServices.PROGRESSO_MAP.clone();
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
        LCBHFechamentoServices.PROGRESSO_MAP.remove(id);
    }

    //<editor-fold defaultstate="collapsed" desc="FUNÇÕES AUXILIARES">
    @Override
    public List<Funcionario> filtrarFuncionario(List<FuncionarioBancoHorasFechamento> funcionarioBancoHorasList, Operador operador) {
        //lista os funcionários que possuem este coletivo
        List<Funcionario> funcionariosList = funcionarioBancoHorasList.stream().map(h -> h.getFuncionarioBancoHoras().getFuncionario()).distinct().collect(Collectors.toList());
        //verifica os departamentos do operador autenticado
        List<Departamento> deptosValidos = operador.getGrupo().getDepartamentoList().stream().distinct().collect(Collectors.toList());
        //caso não possua departamentos seta todos os funcionários para editavel
        if (deptosValidos.isEmpty()) {

            funcionariosList.stream().forEach((d1) -> {
                d1.setPermitido(true);
            });

        } else {
            funcionariosList.stream().forEach((d1) -> {
                d1.setPermitido(deptosValidos.contains(d1.getDepartamento()));
            });

        }
        return funcionariosList;

    }

    @Override
    public List<ColetivoResultado> removerEntidadeTipoDoColetivo(Coletivo coletivo) throws ServiceException {
        List<FuncionarioBancoHorasFechamento> listaNova = coletivo.getFuncionarioBancoHorasFechamentoList();
        List<FuncionarioBancoHorasFechamento> listaAntiga = this.funcionarioBancoDeHorasFechamentoService.buscarPorColetivo(coletivo).stream().collect(Collectors.toList());

        listaAntiga.removeAll(listaNova);

        return this.funcionarioBancoDeHorasFechamentoService.removerColetivo(listaAntiga, coletivo);
    }

    /**
     * Retorna o clone ou uma entidade nova
     *
     * @param af
     * @return
     */
    private FuncionarioBancoHorasFechamento getClone(FuncionarioBancoHorasFechamento af) {
        try {
            return (FuncionarioBancoHorasFechamento) af.clone();
        } catch (CloneNotSupportedException ex) {
            LOGGER.error(LCBHManutencaoServices.class.getName(), ex);
            return new FuncionarioBancoHorasFechamento();
        }
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="REGRAS PARA EDIÇÃO DO COLETIVO E EXCLUSÃO">
    @Override
    public void validacoesEdicao(Collection<FuncionarioBancoHorasFechamento> funcionarioBancoHorasFechamentos) {

        funcionarioBancoHorasFechamentos.stream().forEach(f -> {
            String mensagem = isEditavel(f);
            //Coletivo
            f.getColetivo().setEditavel(mensagem == null);

            //Empresa
            funcionarioBancoHorasFechamentos.stream()
                    .map(fb -> fb.getFuncionarioBancoHoras().getFuncionario()).distinct()
                    .map(fd -> fd.getDepartamento()).distinct()
                    .map(e -> e.getEmpresa())
                    .forEach(d -> {
                        d.setEditavel(mensagem == null);
                        d.setMensagem(mensagem);
                    });

            //Departamento
            funcionarioBancoHorasFechamentos.stream()
                    .map(fb -> fb.getFuncionarioBancoHoras().getFuncionario()).distinct()
                    .map(fd -> fd.getDepartamento()).distinct()
                    .forEach(d -> {
                        d.setEditavel(mensagem == null);
                        d.setMensagem(mensagem);
                    });

            //Funcionario
            //obs: talvez esta função esteja errada. Trocar pela função abaixo
//            funcionarioBancoHorasFechamentos.parallelStream()
//                    .map(fb -> fb.getFuncionarioBancoHoras().getFuncionario()).distinct()
//                    .forEach(d -> {
//                        d.setEditavel(mensagem == null);
//                        d.setMensagem(mensagem);
//                    });
            f.getFuncionarioBancoHoras().getFuncionario().setEditavel(mensagem == null);
            f.getFuncionarioBancoHoras().getFuncionario().setMensagem(mensagem);
        });

    }

    private String isEditavel(FuncionarioBancoHorasFechamento funcionarioBancoHorasFechamento) {
        try {
            funcionarioBancoDeHorasFechamentoService.validarExcluir(funcionarioBancoHorasFechamento);
            return null;
        } catch (ServiceException ex) {
            MsgRetorno msg = (MsgRetorno) ex.getResponse().getEntity();

            return msg.getBody();
        }
    }

//</editor-fold>
}
