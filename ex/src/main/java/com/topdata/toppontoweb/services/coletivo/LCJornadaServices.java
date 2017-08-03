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
import com.topdata.toppontoweb.entity.funcionario.FuncionarioJornada;
import com.topdata.toppontoweb.services.ServiceException;
import com.topdata.toppontoweb.services.funcionario.jornada.FuncionarioJornadaService;
import com.topdata.toppontoweb.utils.constantes.CONSTANTES;
import com.topdata.toppontoweb.utils.constantes.MSG;
//</editor-fold>

/**
 * Classe com as regras de Negócio para o lançamento coletivo da jornada
 *
 * @version 1.0.0.0 data 17/01/2017
 * @since 1.0.0.0 data 21/12/2016
 *
 * @author juliano.ezequiel
 */
@Service
public class LCJornadaServices extends ColetivoService
        implements ColetivoSeviceInterface<FuncionarioJornada> {

    @Autowired
    private FuncionarioJornadaService funcionarioJornadaService;

    private final static HashMap<Long, ProgressoTransfer> PROGRESSO_MAP = new HashMap<>();

    @Override
    public ColetivoTransfer buscarColetivo(HttpServletRequest request, Integer id) throws ServiceException {
        Coletivo coletivo = (Coletivo) this.buscar(Coletivo.class, id);
        Operador operador = getOperadorAtual(request);
        if (!coletivo.getFuncionarioJornadaList().isEmpty()) {
            ColetivoTransfer coletivoTransfer = new ColetivoTransfer(coletivo);
            //Seta a lista de jornadas
            coletivoTransfer.setFuncionarioJornadaList(coletivo.getFuncionarioJornadaList());
            coletivoTransfer.setFuncionarioList(filtrarFuncionario(coletivoTransfer.getColetivo().getFuncionarioJornadaList(), operador));
            coletivoTransfer.setDepartamentoList(filtrarDepartamento(coletivoTransfer.getFuncionarioList(), operador));
            coletivoTransfer.setEmpresaList(filtrarEmpresa(coletivoTransfer.getDepartamentoList(), operador));

            //verifica se o coletivo posera ser editado, pelo operador autenticado
            coletivoTransfer.getColetivo().setPermitido(isColetivoPermitido(filtrarDepartamento(filtrarFuncionario(coletivoTransfer.getColetivo().getFuncionarioJornadaList(), operador), operador), operador));

            validacoesEdicao(coletivoTransfer.getFuncionarioJornadaList());
            return coletivoTransfer;
        } else {
            throw new ServiceException(getTopPontoResponse().alertaNaoCad(new Coletivo().toString()));
        }
    }

    @Override
    public List<ColetivoTransfer> listarColetivos(HttpServletRequest request) throws ServiceException {
        try {
            List<ColetivoTransfer> coletivoTransferlist = listarColetivoJornada(getColetivoDao().buscarColetivoFuncionarioJornada());
            Operador operador = getOperadorAtual(request);
            coletivoTransferlist.parallelStream().forEach(new ConsumerImpl(operador));

            List<ColetivoTransfer> coletivoTransferlistResult = coletivoTransferlist
                    .stream()
                    .filter(coletivoTransfer -> coletivoTransfer.getFuncionarioList()
                            .parallelStream()
                            .anyMatch(funcionario -> funcionario.getPermitido()))
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
    public List<ColetivoTransfer> listarColetivoJornada(List<Coletivo> coletivoList) {
        List<ColetivoTransfer> coletivoTransferList = new ArrayList<>();
        coletivoList.stream().forEach((coletivo) -> {
            ColetivoTransfer coletivoTransfer = new ColetivoTransfer(coletivo);

            //Seta a lista de entidade para jornada
            coletivoTransfer.setFuncionarioJornadaList(coletivo.getFuncionarioJornadaList());
            coletivoTransferList.add(coletivoTransfer);
        });
        return coletivoTransferList;
    }

    @Override
    public void salvarColetivo(HttpServletRequest request, ColetivoTransfer coletivoEntidade, ProgressoTransfer progresso) throws ServiceException {
        List<FuncionarioJornada> funcionarioJornadaList = new ArrayList<>();
        List<ColetivoResultado> resultadosColetivo = new ArrayList<>();

        coletivoEntidade.getFuncionarioJornadaList().stream().forEach(af -> {
            try {
                coletivoEntidade.setColetivo(criarColetivo(coletivoEntidade.getColetivo(), CONSTANTES.Enum_FUNCIONALIDADE.LC_JORNADAS));
                coletivoEntidade.getFuncionarioList().stream().forEach(funcionario -> {

                    try {
                        funcionario = this.getFuncionarioService().buscar(Funcionario.class, funcionario.getIdFuncionario());

                        //NOVO FUNCIONARIO JORNADA
                        FuncionarioJornada funcionarioJornada = new FuncionarioJornada();
                        funcionarioJornada.setIdJornadaFuncionario(null);
                        funcionarioJornada.setDataFim(af.getDataFim());
                        funcionarioJornada.setDataInicio(af.getDataInicio());
                        funcionarioJornada.setExcecaoJornada(af.getExcecaoJornada());
                        funcionarioJornada.setJornada(af.getJornada());
                        funcionarioJornada.setSequenciaInicial(af.getSequenciaInicial());
                        funcionarioJornada.setColetivo(coletivoEntidade.getColetivo());
                        funcionarioJornada.setFuncionario(funcionario);
                        funcionarioJornadaList.add(funcionarioJornada);
                    } catch (ServiceException ex) {
                        LOGGER.error(LCJornadaServices.class.getName(), ex);
                    }
                });
            } catch (ServiceException ex) {
                LOGGER.error(LCJornadaServices.class.getName(), ex);
            }
        });

        coletivoEntidade.getColetivo().setFuncionarioJornadaList(funcionarioJornadaList);
        //LISTA OS RESULTADOS DAS OPERAÇÕES
        resultadosColetivo.addAll(funcionarioJornadaService.salvarFuncionarioJornadaColetivo(funcionarioJornadaList, progresso));

        Coletivo coletivo = coletivoEntidade.getColetivo();

        Boolean somenteErros = resultadosColetivo.parallelStream().allMatch(r -> r.getResultado() == ColetivoResultado.Enum_ResultadoColetivo.ERRO);

        if (somenteErros == true) {
            //todos com erro, deve-se excluir o coletivo
            this.excluirColetivo(coletivo, CONSTANTES.Enum_FUNCIONALIDADE.LC_JORNADAS);
            coletivoEntidade.setColetivo(null);
        }
        //Adiciona no progresso
        progresso.setColetivoResultados(resultadosColetivo);
    }

    @Override
    public void atualizarColetivo(HttpServletRequest request, ColetivoTransfer coletivoEntidade, ProgressoTransfer progresso) throws ServiceException {
        List<FuncionarioJornada> funcionarioJornadaList = new ArrayList<>();
        List<ColetivoResultado> resultadosColetivo = new ArrayList<>();

        coletivoEntidade.getFuncionarioJornadaList().stream().forEach(af -> {
            try {
                coletivoEntidade.setColetivo(atualizarColetivo(coletivoEntidade.getColetivo(), CONSTANTES.Enum_FUNCIONALIDADE.LC_JORNADAS));
                coletivoEntidade.getFuncionarioList().stream().forEach(funcionario -> {
                    try {
                        Funcionario f = getFuncionarioService().buscar(Funcionario.class, funcionario.getIdFuncionario());
                        List<FuncionarioJornada> jornadasBanco = funcionarioJornadaService.buscarPorColetivo(coletivoEntidade.getColetivo());
                        FuncionarioJornada funcionarioJornada = jornadasBanco
                                .parallelStream()
                                .filter((FuncionarioJornada af1) -> Objects.equals(f.getIdFuncionario(), af1.getFuncionario().getIdFuncionario())
                                        && af1.getColetivo() != null
                                        && af1.getColetivo().getIdColetivo().equals(af.getColetivo().getIdColetivo()))
                                .findAny().get();

                        funcionarioJornada.setDataFim(af.getDataFim());
                        funcionarioJornada.setDataInicio(af.getDataInicio());
                        funcionarioJornada.setFuncionario(funcionario);
                        funcionarioJornada.setExcecaoJornada(af.getExcecaoJornada());
                        funcionarioJornada.setJornada(af.getJornada());
                        funcionarioJornada.setSequenciaInicial(af.getSequenciaInicial());
                        funcionarioJornada.setColetivo(coletivoEntidade.getColetivo());
                        funcionarioJornada.setFuncionario(f);
                        funcionarioJornadaList.add(funcionarioJornada);
                    } catch (ServiceException ex) {
                        LOGGER.error(LCJornadaServices.class.getName(), ex);
                    } catch (NoSuchElementException e) {
                        FuncionarioJornada a = getClone(af);
                        a.setIdJornadaFuncionario(null);
                        a.setFuncionario(funcionario);
                        funcionarioJornadaList.add(a);
                    }
                });
            } catch (ServiceException ex) {
                LOGGER.error(LCJornadaServices.class.getName(), ex);
            }
        });

        coletivoEntidade.getColetivo().setFuncionarioJornadaList(funcionarioJornadaList);
        resultadosColetivo.addAll(removerEntidadeTipoDoColetivo(coletivoEntidade.getColetivo()));

        resultadosColetivo.addAll(funcionarioJornadaService.salvarFuncionarioJornadaColetivo(funcionarioJornadaList, progresso));

        Coletivo coletivo = coletivoEntidade.getColetivo();

        Boolean somenteErros = resultadosColetivo.parallelStream().allMatch(r -> r.getResultado() == ColetivoResultado.Enum_ResultadoColetivo.ERRO && !r.isFechamento());

        if (somenteErros == true) {
            //todos com erro, deve-se excluir o coletivo
            this.excluirColetivo(coletivo, CONSTANTES.Enum_FUNCIONALIDADE.LC_JORNADAS);
            coletivoEntidade.setColetivo(null);
        }

        //Adiciona no progresso
        progresso.setColetivoResultados(resultadosColetivo);

    }

    /**
     * Realiza as validações de cada Jornada vínculado ao funcionário através
     * deste coletivo, em caso de existir uma jornada que não possa ser
     * excluido, será lançada uma exceção.
     *
     * @param request
     * @param id
     * @return Response
     * @throws ServiceException
     */
    @Override
    public Response excluirColetivo(HttpServletRequest request, Integer id) throws ServiceException {
        AtomicBoolean isPermitidoExclusao = new AtomicBoolean(Boolean.TRUE);
        Coletivo c = (Coletivo) this.buscar(Coletivo.class, id);
        List<FuncionarioJornada> funcionarioJornadaList = this.funcionarioJornadaService.buscarPorColetivo(c);
        //valida se este coletivo poderá ser excluído.
        //É utilizada as mesmas regras para a exclusão do registro manual
        funcionarioJornadaList.stream().forEach(a -> {
            try {
                this.funcionarioJornadaService.validarExcluir(a);
            } catch (ServiceException ex) {
                isPermitidoExclusao.set(Boolean.FALSE);
            }
        });
        //em caso exista pelo menos um registro que faça parte deste coletivo que não possa ser excluído
        //será lançada uma exceção
        if (isPermitidoExclusao.get() == Boolean.TRUE) {
            //solicita a exclusão de todos os registros que faça parte deste coletivo    
            this.funcionarioJornadaService.excluirPorColetivo(c);

            //Adiciona na auditoria para cada um deles
            funcionarioJornadaList.stream().forEach(funcJornada -> {
                try {
                    this.funcionarioJornadaService.getAuditoriaServices().auditar(CONSTANTES.Enum_FUNCIONALIDADE.CAD_FUNCIONARIO_JORNADAS, CONSTANTES.Enum_AUDITORIA.DEFAULT, CONSTANTES.Enum_OPERACAO.EXCLUIR, funcJornada);
                } catch (ServiceException ex) {
                    LOGGER.error(LCJornadaServices.class.getName(), ex);
                }
            });

            return this.excluirColetivo(c, CONSTANTES.Enum_FUNCIONALIDADE.LC_JORNADAS);
        } else {
            return this.getTopPontoResponse().alertaValidacao(MSG.COLETIVO.ALERTA_EXCLUIR_COLETIVO.getResource());
        }
    }

    public ProgressoTransfer buscarProgresso(long id) {
        return LCJornadaServices.PROGRESSO_MAP.get(id);
    }

    public ProgressoTransfer criarProgresso() {
        removerProgressosObsoletos();
        ProgressoTransfer progresso = new ProgressoTransfer();
        LCJornadaServices.PROGRESSO_MAP.put(progresso.getId(), progresso);
        return progresso;
    }

    public void removerProgressosObsoletos() {
        HashMap<Long, ProgressoTransfer> progressoMapClone = (HashMap<Long, ProgressoTransfer>) LCJornadaServices.PROGRESSO_MAP.clone();
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
        LCJornadaServices.PROGRESSO_MAP.remove(id);
    }

    //<editor-fold defaultstate="collapsed" desc="FUNÇÕES AUXILIARES">
    @Override
    public List<Funcionario> filtrarFuncionario(List<FuncionarioJornada> funcionarioJornadaList, Operador operador) {
        //lista os funcionários que possuem este coletivo
        List<Funcionario> funcionariosList = funcionarioJornadaList.stream().map(h -> h.getFuncionario()).distinct().collect(Collectors.toList());
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
        List<FuncionarioJornada> listaNova = coletivo.getFuncionarioJornadaList();
        List<FuncionarioJornada> listaAntiga = funcionarioJornadaService.buscarPorColetivo(coletivo).stream().collect(Collectors.toList());

        listaAntiga.removeAll(listaNova);

        return funcionarioJornadaService.removerColetivo(listaAntiga, coletivo);
    }

    public class ConsumerImpl implements Consumer<ColetivoTransfer> {

        private final Operador operador;

        public ConsumerImpl(Operador operador) {
            this.operador = operador;
        }

        @Override
        public void accept(ColetivoTransfer coletivoTransfer) {
            //filtra os funcionários que o coletivo possui
            coletivoTransfer.setFuncionarioList(filtrarFuncionario(coletivoTransfer.getColetivo().getFuncionarioJornadaList(), this.operador));
            //filtra os departamentos que o coletivo possui
            coletivoTransfer.setDepartamentoList(filtrarDepartamento(coletivoTransfer.getFuncionarioList(), this.operador));
            //filtra as empresa que o coletivo possui
            coletivoTransfer.setEmpresaList(filtrarEmpresa(coletivoTransfer.getDepartamentoList(), this.operador));

            coletivoTransfer.getColetivo().setPermitido(isColetivoPermitido(coletivoTransfer.getDepartamentoList(), this.operador));
        }
    }

    /**
     * Retorna o clone ou uma entidade nova
     *
     * @param af
     * @return
     */
    private FuncionarioJornada getClone(FuncionarioJornada af) {
        try {
            return (FuncionarioJornada) af.clone();
        } catch (CloneNotSupportedException ex) {
            LOGGER.error(LCJornadaServices.class.getName(), ex);
            return new FuncionarioJornada();
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="REGRAS PARA EDIÇÃO DO COLETIVO E EXCLUSÃO">
    @Override
    public void validacoesEdicao(Collection<FuncionarioJornada> funcionarioJornadaList) {

        funcionarioJornadaList.stream().forEach(f -> {
            String mensagem = isEditavel(f);
            //Coletivo
            f.getColetivo().setEditavel(mensagem == null);

            //Empresa
            funcionarioJornadaList.stream()
                    .map(fb -> fb.getFuncionario()).distinct()
                    .map(fd -> fd.getDepartamento()).distinct()
                    .map(e -> e.getEmpresa())
                    .forEach(d -> {
                        d.setEditavel(mensagem == null);
                        d.setMensagem(mensagem);
                    });
            //Departamento
            funcionarioJornadaList.stream()
                    .map(fb -> fb.getFuncionario()).distinct()
                    .map(fd -> fd.getDepartamento()).distinct()
                    .forEach(d -> {
                        d.setEditavel(mensagem == null);
                        d.setMensagem(mensagem);
                    });

            //Funcionario
            //obs: talvez esta função esteja errada. Trocar pela função abaixo            
//            funcionarioJornadaList.parallelStream()
//                    .map(fb -> fb.getFuncionario()).distinct()
//                    .forEach(d -> {
//                        d.setEditavel(mensagem == null);
//                        d.setMensagem(mensagem);
//                    });
            f.getFuncionario().setEditavel(mensagem == null);
            f.getFuncionario().setMensagem(mensagem);
        });

    }

    private String isEditavel(FuncionarioJornada funcionarioJornada) {
        try {
            funcionarioJornada.setValidarColetivo(Boolean.TRUE);
            funcionarioJornadaService.validarExcluir(funcionarioJornada);
            return null;
        } catch (ServiceException ex) {
            MsgRetorno msg = (MsgRetorno) ex.getResponse().getEntity();

            return msg.getBody();
        }
    }

//</editor-fold>
}
