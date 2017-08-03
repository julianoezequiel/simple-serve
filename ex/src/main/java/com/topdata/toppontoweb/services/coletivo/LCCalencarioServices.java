package com.topdata.toppontoweb.services.coletivo;

//<editor-fold defaultstate="collapsed" desc="IMPORTS">
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
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
import com.topdata.toppontoweb.entity.funcionario.FuncionarioCalendario;
import com.topdata.toppontoweb.entity.funcionario.FuncionarioJornada;
import com.topdata.toppontoweb.services.ServiceException;
import com.topdata.toppontoweb.services.funcionario.calendario.FuncionarioCalendarioService;
import com.topdata.toppontoweb.utils.constantes.CONSTANTES;
import com.topdata.toppontoweb.utils.constantes.MSG;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;
//</editor-fold>

/**
 * Classe com as regras de Negócio para o lançamento coletivo do calendário
 *
 * @version 1.0.0 data 05/07/2016
 * @since 1.0.0 data 05/07/2016
 *
 * @author juliano.ezequiel
 */
@Service
public class LCCalencarioServices extends ColetivoService
        implements ColetivoSeviceInterface<FuncionarioCalendario> {

    @Autowired
    private FuncionarioCalendarioService funcionarioCalendarioService;

    private final static HashMap<Long, ProgressoTransfer> PROGRESSO_MAP = new HashMap<>();

    @Override
    public ColetivoTransfer buscarColetivo(HttpServletRequest request, Integer id) throws ServiceException {
        Coletivo coletivo = (Coletivo) this.buscar(Coletivo.class, id);
        Operador operador = getOperadorAtual(request);
        if (!coletivo.getFuncionarioCalendarioList().isEmpty()) {
            ColetivoTransfer coletivoTransfer = new ColetivoTransfer(coletivo);
            //Seta a lista de calendarios
            coletivoTransfer.setFuncionarioCalendarioList(coletivo.getFuncionarioCalendarioList());
            coletivoTransfer.setFuncionarioList(filtrarFuncionario(coletivoTransfer.getColetivo().getFuncionarioCalendarioList(), operador));
            coletivoTransfer.setDepartamentoList(filtrarDepartamento(coletivoTransfer.getFuncionarioList(), operador));
            coletivoTransfer.setEmpresaList(filtrarEmpresa(coletivoTransfer.getDepartamentoList(), operador));
            //verifica se o coletivo posera ser editado, pelo operador autenticado
            coletivoTransfer.getColetivo().setPermitido(isColetivoPermitido(filtrarDepartamento(filtrarFuncionario(coletivoTransfer.getColetivo().getFuncionarioCalendarioList(), operador), operador), operador));

            validacoesEdicao(coletivoTransfer.getFuncionarioCalendarioList());
            return coletivoTransfer;
        } else {
            throw new ServiceException(getTopPontoResponse().alertaNaoCad(new Coletivo().toString()));
        }
    }

    @Override
    public List<ColetivoTransfer> listarColetivos(HttpServletRequest request) throws ServiceException {
        try {
            List<ColetivoTransfer> coletivoTransferlist = listarColetivoCalendario(getColetivoDao().buscarColetivoFuncionarioCalendario());
            Operador operador = getOperadorAtual(request);
            coletivoTransferlist.stream().forEach(new ConsumerImpl(operador));

            List<ColetivoTransfer> coletivoTransferlistResult = coletivoTransferlist
                    .stream()
                    .filter(coletivoTransfer -> coletivoTransfer.getFuncionarioList()
                            .stream().anyMatch(funcionario -> funcionario.getPermitido()))
                    .collect(Collectors.toList());

            return coletivoTransferlistResult;
        } catch (DaoException ex) {
            throw new ServiceException(getTopPontoResponse().erroBuscar(), ex);
        }
    }

    /**
     * Esse parametro foi alterado, pois, tem que setar qual é a entidade para o
     * coletivoTransfer
     *
     * @param coletivoList lista de coletivos
     * @return retorna uma lista de coletivo para tela!
     */
    public List<ColetivoTransfer> listarColetivoCalendario(List<Coletivo> coletivoList) {
        List<ColetivoTransfer> coletivoTransferList = new ArrayList<>();
        coletivoList.stream().forEach(coletivo -> {
            ColetivoTransfer coletivoTransfer = new ColetivoTransfer(coletivo);

            //Seta a lista de entidade para calendario funcionário
            coletivoTransfer.setFuncionarioCalendarioList(coletivo.getFuncionarioCalendarioList());
            coletivoTransferList.add(coletivoTransfer);
        });
        return coletivoTransferList;
    }

    @Override
    public void salvarColetivo(HttpServletRequest request, ColetivoTransfer coletivoEntidade, ProgressoTransfer progresso) throws ServiceException {
        List<FuncionarioCalendario> funcionarioCalendarioList = new ArrayList<>();
        List<ColetivoResultado> resultadosColetivo = new ArrayList<>();

        coletivoEntidade.getFuncionarioCalendarioList().stream().forEach(calendario -> {
            try {
                coletivoEntidade.setColetivo(criarColetivo(coletivoEntidade.getColetivo(), CONSTANTES.Enum_FUNCIONALIDADE.LC_CALENDARIO));
                coletivoEntidade.getFuncionarioList().stream().forEach(funcionario -> {
                    try {

                        funcionario = this.getFuncionarioService().buscar(Funcionario.class, funcionario.getIdFuncionario());

                        FuncionarioCalendario calendarioClone = (FuncionarioCalendario) calendario.clone();
                        calendarioClone.setColetivo(coletivoEntidade.getColetivo());
                        calendarioClone.setFuncionario(funcionario);
                        funcionarioCalendarioList.add(calendarioClone);
                    } catch (ServiceException | CloneNotSupportedException ex) {
                        LOGGER.error(LCCalencarioServices.class.getName(), ex);
                    }
                });
            } catch (ServiceException ex) {
                LOGGER.error(LCCalencarioServices.class.getName(), ex);
            }
        });

        coletivoEntidade.getColetivo().setFuncionarioCalendarioList(funcionarioCalendarioList);
        resultadosColetivo.addAll(funcionarioCalendarioService.salvarCalendarioColetivo(funcionarioCalendarioList, progresso));

        Coletivo coletivo = coletivoEntidade.getColetivo();
        Boolean somenteErros = resultadosColetivo.stream().allMatch(r -> r.getResultado() == ColetivoResultado.Enum_ResultadoColetivo.ERRO);

        if (somenteErros == true) {
            //todos com erro, deve-se excluir o coletivo
            this.excluirColetivo(coletivo, CONSTANTES.Enum_FUNCIONALIDADE.LC_CALENDARIO);
            coletivoEntidade.setColetivo(null);
        }

        //Adiciona no progresso
        progresso.setColetivoResultados(resultadosColetivo);
    }

    @Override
    public void atualizarColetivo(HttpServletRequest request, ColetivoTransfer coletivoEntidade, ProgressoTransfer progresso) throws ServiceException {
        List<FuncionarioCalendario> funcionarioCalendarioList = new ArrayList<>();
        List<ColetivoResultado> resultadosColetivo = new ArrayList<>();

        coletivoEntidade.getFuncionarioCalendarioList().stream().forEach(af -> {
            try {
                coletivoEntidade.setColetivo(atualizarColetivo(coletivoEntidade.getColetivo(), CONSTANTES.Enum_FUNCIONALIDADE.LC_CALENDARIO));
                coletivoEntidade.getFuncionarioList().stream().forEach(funcionario -> {
                    try {
                        Funcionario f = getFuncionarioService().buscar(Funcionario.class, funcionario.getIdFuncionario());
                        List<FuncionarioCalendario> calendariosBanco = funcionarioCalendarioService.buscarPorColetivo(coletivoEntidade.getColetivo());

                        FuncionarioCalendario funcionarioJornada = calendariosBanco
                                .stream()
                                .filter((FuncionarioCalendario af1)
                                        -> Objects.equals(f.getIdFuncionario(), af1.getFuncionario().getIdFuncionario())
                                        && af1.getColetivo() != null
                                        && af1.getColetivo().getIdColetivo().equals(af.getColetivo().getIdColetivo()))
                                .findAny().get();

                        funcionarioJornada.setDataInicio(af.getDataInicio());
                        funcionarioJornada.setCalendario(af.getCalendario());
                        funcionarioJornada.setFuncionario(f);

                        funcionarioCalendarioList.add(funcionarioJornada);
                    } catch (ServiceException ex) {
                        LOGGER.error(LCCalencarioServices.class.getName(), ex);
                    } catch (NoSuchElementException e) {
                        FuncionarioCalendario a = getClone(af);
                        a.setIdFuncionarioCalendario(null);
                        a.setFuncionario(funcionario);
                        funcionarioCalendarioList.add(a);
                    }
                });
            } catch (ServiceException ex) {
                LOGGER.error(LCCalencarioServices.class.getName(), ex);
            }
        });

        coletivoEntidade.getColetivo().setFuncionarioCalendarioList(funcionarioCalendarioList);
        resultadosColetivo.addAll(removerEntidadeTipoDoColetivo(coletivoEntidade.getColetivo()));

        resultadosColetivo.addAll(funcionarioCalendarioService.salvarCalendarioColetivo(funcionarioCalendarioList, progresso));

        Coletivo coletivo = coletivoEntidade.getColetivo();

        Boolean somenteErros = resultadosColetivo.stream().allMatch(r -> r.getResultado() == ColetivoResultado.Enum_ResultadoColetivo.ERRO && !r.isFechamento());

        if (somenteErros == true) {
            //todos com erro, deve-se excluir o coletivo
            this.excluirColetivo(coletivo, CONSTANTES.Enum_FUNCIONALIDADE.LC_CALENDARIO);
            coletivoEntidade.setColetivo(null);
        }

        //Adiciona no progresso
        progresso.setColetivoResultados(resultadosColetivo);
    }

    /**
     * Realiza as validações de cada calendário vínculado ao funcionário atravéz
     * deste coletivo,em caso de existir um calendário que não possa ser
     * excluído será lançada uma exceção
     *
     * @param request
     * @param id coletivo
     * @return Response
     * @throws ServiceException
     */
    @Override
    public Response excluirColetivo(HttpServletRequest request, Integer id) throws ServiceException {
        AtomicBoolean isPermitidoExclusao = new AtomicBoolean(Boolean.TRUE);
        Coletivo c = (Coletivo) this.buscar(Coletivo.class, id);
        List<FuncionarioCalendario> funcionarioCalendarioList = this.funcionarioCalendarioService.buscarPorColetivo(c);
        //valida se este coletivo poderá ser excluído.
        //É utilizada as mesmas regras para a exclusão do registro manual
        funcionarioCalendarioList.stream().forEach(a -> {
            try {
                this.funcionarioCalendarioService.validarExcluir(a);
            } catch (ServiceException ex) {
                isPermitidoExclusao.set(false);
            }
        });

        //em caso exista pelo menos um registro que faça parte deste coletivo que não possa ser excluído
        //será lançada uma exceção
        if (isPermitidoExclusao.get() == Boolean.TRUE) {
            //solicita a exclusão de todos os registros que faça parte deste coletivo    
            this.funcionarioCalendarioService.excluirPorColetivo(c);
            return this.excluirColetivo(c, CONSTANTES.Enum_FUNCIONALIDADE.LC_CALENDARIO);
        } else {
            return this.getTopPontoResponse().alertaValidacao(MSG.COLETIVO.ALERTA_EXCLUIR_COLETIVO.getResource());
        }

    }

    //<editor-fold defaultstate="" desc="FUNÇÕES AUXILIARES">
    @Override
    public List<Funcionario> filtrarFuncionario(List<FuncionarioCalendario> calendarioList, Operador operador) {
        //lista os funcionários que possuem este coletivo
        List<Funcionario> funcionariosList = calendarioList.stream().map(h -> h.getFuncionario()).distinct().collect(Collectors.toList());
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
        List<FuncionarioCalendario> listaNova = coletivo.getFuncionarioCalendarioList();
        List<FuncionarioCalendario> listaAntiga = funcionarioCalendarioService.buscarPorColetivo(coletivo).stream().collect(Collectors.toList());
        listaAntiga.removeAll(listaNova);
        return funcionarioCalendarioService.removerColetivo(listaAntiga, coletivo, true);
    }

    public class ConsumerImpl implements Consumer<ColetivoTransfer> {

        private final Operador operador;

        public ConsumerImpl(Operador operador) {
            this.operador = operador;
        }

        @Override
        public void accept(ColetivoTransfer coletivo) {
            //filtra os funcionários que o coletivo possui
            coletivo.setFuncionarioList(filtrarFuncionario(coletivo.getColetivo().getFuncionarioCalendarioList(), operador));

            coletivo.getColetivo().setPermitido(isColetivoPermitido(filtrarDepartamento(filtrarFuncionario(coletivo.getColetivo().getFuncionarioCalendarioList(), operador), operador), operador));
        }
    }

    /**
     * Retorna o clone ou uma entidade nova
     *
     * @param af
     * @return
     */
    private FuncionarioCalendario getClone(FuncionarioCalendario af) {
        try {
            return (FuncionarioCalendario) af.clone();
        } catch (CloneNotSupportedException ex) {
            LOGGER.error(LCCalencarioServices.class.getName(), ex);
            return new FuncionarioCalendario();
        }
    }
//</editor-fold>

    //<editor-fold defaultstate="" desc="REGRAS PARA EDIÇÃO DO COLETIVO E EXCLUSÃO">
    @Override
    public void validacoesEdicao(Collection<FuncionarioCalendario> funcionarioCalendarioLista) {

        funcionarioCalendarioLista.stream().forEach(f -> {
            String mensagem = isEditavel(f);
            //Coletivo
            f.getColetivo().setEditavel(mensagem == null);

            //Empresa
            funcionarioCalendarioLista.stream()
                    .map(fb -> fb.getFuncionario()).distinct()
                    .map(fd -> fd.getDepartamento()).distinct()
                    .map(e -> e.getEmpresa())
                    .forEach(empresa -> {
                        empresa.setEditavel(mensagem == null);
                        empresa.setMensagem(mensagem);
                    });
            //Departamento
            funcionarioCalendarioLista.stream()
                    .map(fb -> fb.getFuncionario()).distinct()
                    .map(fd -> fd.getDepartamento()).distinct()
                    .forEach(departamento -> {
                        departamento.setEditavel(mensagem == null);
                        departamento.setMensagem(mensagem);
                    });

            //Funcionario
//            funcionarioCalendarioLista.parallelStream()
//                    .map(fb -> fb.getFuncionario()).distinct()
//                    .forEach(funcionario -> {
//                        funcionario.setEditavel(mensagem == null);
//                        funcionario.setMensagem(mensagem);
//                    });
            f.getFuncionario().setEditavel(mensagem == null);
            f.getFuncionario().setMensagem(mensagem);
        });

    }

    private String isEditavel(FuncionarioCalendario funcionarioCalendario) {
        try {
            funcionarioCalendarioService.validarExcluir(funcionarioCalendario);
            return null;
        } catch (ServiceException ex) {
            MsgRetorno msg = (MsgRetorno) ex.getResponse().getEntity();

            return msg.getBody();
        }
    }

    public ProgressoTransfer buscarProgresso(long id) {
        return LCCalencarioServices.PROGRESSO_MAP.get(id);
    }

    public ProgressoTransfer criarProgresso() {
        removerProgressosObsoletos();
        ProgressoTransfer progresso = new ProgressoTransfer();
        LCCalencarioServices.PROGRESSO_MAP.put(progresso.getId(), progresso);
        return progresso;
    }

    public void removerProgressosObsoletos() {
        HashMap<Long, ProgressoTransfer> progressoMapClone = (HashMap<Long, ProgressoTransfer>) LCCalencarioServices.PROGRESSO_MAP.clone();
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
        LCCalencarioServices.PROGRESSO_MAP.remove(id);
    }

//</editor-fold>
}
