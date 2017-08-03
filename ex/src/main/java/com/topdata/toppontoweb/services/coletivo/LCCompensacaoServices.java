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
import com.topdata.toppontoweb.entity.funcionario.Compensacao;
import com.topdata.toppontoweb.entity.funcionario.Funcionario;
import com.topdata.toppontoweb.services.ServiceException;
import com.topdata.toppontoweb.services.funcionario.compensacao.CompensacaoService;
import com.topdata.toppontoweb.utils.constantes.CONSTANTES;
import com.topdata.toppontoweb.utils.constantes.MSG;
//</editor-fold>

/**
 * Classe com as regras de Negócio para o lançamento coletivo da compencação
 *
 * @version 1.0.0.0 data 17/01/2016
 * @since 1.0.0.0 data 21/12/2016
 *
 * @author juliano.ezequiel
 */
@Service
public class LCCompensacaoServices extends ColetivoService
        implements ColetivoSeviceInterface<Compensacao> {

    @Autowired
    private CompensacaoService compensacaoService;

    private final static HashMap<Long, ProgressoTransfer> PROGRESSO_MAP = new HashMap<>();

    @Override
    public ColetivoTransfer buscarColetivo(HttpServletRequest request, Integer id) throws ServiceException {
        Coletivo coletivo = (Coletivo) this.buscar(Coletivo.class, id);
        Operador operador = getOperadorAtual(request);
        if (!coletivo.getCompensacaoList().isEmpty()) {
            ColetivoTransfer coletivoTransfer = new ColetivoTransfer(coletivo);
            //Seta a lista de compensacoes
            coletivoTransfer.setCompensacaoList(coletivo.getCompensacaoList());
            coletivoTransfer.setFuncionarioList(filtrarFuncionario(coletivoTransfer.getColetivo().getCompensacaoList(), operador));
            coletivoTransfer.setDepartamentoList(filtrarDepartamento(coletivoTransfer.getFuncionarioList(), operador));
            coletivoTransfer.setEmpresaList(filtrarEmpresa(coletivoTransfer.getDepartamentoList(), operador));
            //verifica se o coletivo posera ser editado, pelo operador autenticado
            coletivoTransfer.getColetivo().setPermitido(isColetivoPermitido(filtrarDepartamento(filtrarFuncionario(coletivoTransfer.getColetivo().getCompensacaoList(), operador), operador), operador));

            validacoesEdicao(coletivoTransfer.getCompensacaoList());
            return coletivoTransfer;
        } else {
            throw new ServiceException(getTopPontoResponse().alertaNaoCad(new Coletivo().toString()));
        }
    }

    @Override
    public List<ColetivoTransfer> listarColetivos(HttpServletRequest request) throws ServiceException {
        try {
            List<ColetivoTransfer> coletivoTransferlist = listarColetivoCompensacao(getColetivoDao().buscarColetivoCompencacaoes());
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
    public List<ColetivoTransfer> listarColetivoCompensacao(List<Coletivo> coletivoList) {
        List<ColetivoTransfer> coletivoTransferList = new ArrayList<>();
        coletivoList.stream().forEach((coletivo) -> {
            ColetivoTransfer coletivoTransfer = new ColetivoTransfer(coletivo);

            //Seta a lista de entidade para compensacao
            coletivoTransfer.setCompensacaoList(coletivo.getCompensacaoList());
            coletivoTransferList.add(coletivoTransfer);
        });
        return coletivoTransferList;
    }

    @Override
    public void salvarColetivo(HttpServletRequest request, ColetivoTransfer coletivoEntidade, ProgressoTransfer progresso) throws ServiceException {
        List<Compensacao> compensacaoList = new ArrayList<>();
        List<ColetivoResultado> resultadosColetivo = new ArrayList<>();

        coletivoEntidade.getCompensacaoList().stream().forEach(af -> {
            try {
                coletivoEntidade.setColetivo(criarColetivo(coletivoEntidade.getColetivo(), CONSTANTES.Enum_FUNCIONALIDADE.LC_COMPENSACOES));
                coletivoEntidade.getFuncionarioList().stream().forEach(funcionario -> {
                    try {
                        funcionario = this.getFuncionarioService().buscar(Funcionario.class, funcionario.getIdFuncionario());
                        Compensacao a = (Compensacao) af.clone();
                        a.setColetivo(coletivoEntidade.getColetivo());
                        a.setFuncionario(funcionario);
                        compensacaoList.add(a);
                    } catch (ServiceException | CloneNotSupportedException ex) {
                        LOGGER.error(LCCompensacaoServices.class.getName(), ex);
                    }
                });
            } catch (ServiceException ex) {
                LOGGER.error(LCCompensacaoServices.class.getName(), ex);
            }
        });

        coletivoEntidade.getColetivo().setCompensacaoList(compensacaoList);
        resultadosColetivo.addAll(compensacaoService.salvarCompensacaoColetivo(compensacaoList, progresso));

        Coletivo coletivo = coletivoEntidade.getColetivo();

        Boolean somenteErros = resultadosColetivo.stream().allMatch(r -> r.getResultado() == ColetivoResultado.Enum_ResultadoColetivo.ERRO);

        if (somenteErros == true) {
            //todos com erro, deve-se excluir o coletivo
            this.excluirColetivo(coletivo, CONSTANTES.Enum_FUNCIONALIDADE.LC_COMPENSACOES);
            coletivoEntidade.setColetivo(null);
        }
        //Adiciona no progresso
        progresso.setColetivoResultados(resultadosColetivo);
    }

    @Override
    public void atualizarColetivo(HttpServletRequest request, ColetivoTransfer coletivoEntidade, ProgressoTransfer progresso) throws ServiceException {
        List<Compensacao> compensacaoList = new ArrayList<>();
        List<ColetivoResultado> resultadosColetivo = new ArrayList<>();

        coletivoEntidade.getCompensacaoList().stream().forEach(af -> {
            try {
                coletivoEntidade.setColetivo(atualizarColetivo(coletivoEntidade.getColetivo(), CONSTANTES.Enum_FUNCIONALIDADE.LC_COMPENSACOES));
                coletivoEntidade.getFuncionarioList().stream().forEach(funcionario -> {
                    try {
                        Funcionario f = getFuncionarioService().buscar(Funcionario.class, funcionario.getIdFuncionario());
                        List<Compensacao> compensacoesBanco = compensacaoService.buscarPorColetivo(coletivoEntidade.getColetivo());
                        Compensacao a = compensacoesBanco
                                .stream()
                                .filter(af1
                                        -> Objects.equals(f.getIdFuncionario(), af1.getFuncionario().getIdFuncionario())
                                        && af1.getColetivo() != null
                                        && af1.getColetivo().getIdColetivo().equals(af.getColetivo().getIdColetivo()))
                                .findAny().get();

                        a.setDataFim(af.getDataFim());
                        a.setDataInicio(af.getDataInicio());
                        a.setDataCompensada(af.getDataCompensada());
                        a.setLimiteDiario(af.getLimiteDiario());
                        a.setFuncionario(funcionario);
                        a.setMotivo(af.getMotivo());
                        a.setConsideraDiasSemJornada(af.getConsideraDiasSemJornada());
                        a.setFuncionario(f);
                        compensacaoList.add(a);
                    } catch (ServiceException ex) {
                        LOGGER.error(LCCompensacaoServices.class.getName(), ex);
                    } catch (NoSuchElementException e) {
                        Compensacao compensacaoClone = getClone(af);
                        compensacaoClone.setIdCompensacao(null);
                        compensacaoClone.setFuncionario(funcionario);
                        compensacaoList.add(compensacaoClone);
                    }
                });
            } catch (ServiceException ex) {
                LOGGER.error(LCCompensacaoServices.class.getName(), ex);
            }
        });

        coletivoEntidade.getColetivo().setCompensacaoList(compensacaoList);
        resultadosColetivo.addAll(removerEntidadeTipoDoColetivo(coletivoEntidade.getColetivo()));

        resultadosColetivo.addAll(compensacaoService.salvarCompensacaoColetivo(compensacaoList, progresso));

        Coletivo coletivo = coletivoEntidade.getColetivo();
        Boolean somenteErros = resultadosColetivo.stream().allMatch(r -> r.getResultado() == ColetivoResultado.Enum_ResultadoColetivo.ERRO && r.isFechamento());

        if (somenteErros) {
            //todos com erro, deve-se excluir o coletivo
            this.excluirColetivo(coletivo, CONSTANTES.Enum_FUNCIONALIDADE.LC_COMPENSACOES);
            coletivoEntidade.setColetivo(null);
        }

        //Adiciona no progresso
        progresso.setColetivoResultados(resultadosColetivo);

    }

    /**
     * Realiza as validações de cada compensação vínculado ao funcionário
     * atravéz deste coletivo,em caso de existir uma compensação que não possa
     * ser excluído será lançada uma exceção
     *
     * @param request
     * @param id
     * @return
     * @throws ServiceException
     */
    @Override
    public Response excluirColetivo(HttpServletRequest request, Integer id) throws ServiceException {
        AtomicBoolean isPermitidoExclusao = new AtomicBoolean(Boolean.TRUE);
        Coletivo c = (Coletivo) this.buscar(Coletivo.class, id);
        List<Compensacao> compensacaoList = this.compensacaoService.buscarPorColetivo(c);
        //valida se este coletivo poderá ser excluído.
        //É utilizada as mesmas regras para a exclusão do registro manual
        compensacaoList.stream().forEach(a -> {
            try {
                this.compensacaoService.validarExcluir(a);
            } catch (ServiceException ex) {
                isPermitidoExclusao.set(false);
            }
        });

        //em caso exista pelo menos um registro que faça parte deste coletivo que não possa ser excluído
        //será lançada uma exceção
        if (isPermitidoExclusao.get() == Boolean.TRUE) {
            this.compensacaoService.excluirPorColetivo(c);
            return this.excluirColetivo(c, CONSTANTES.Enum_FUNCIONALIDADE.LC_COMPENSACOES);
        } else {
            return this.getTopPontoResponse().alertaValidacao(MSG.COLETIVO.ALERTA_EXCLUIR_COLETIVO.getResource());
        }
    }

    public ProgressoTransfer buscarProgresso(long id) {
        return LCCompensacaoServices.PROGRESSO_MAP.get(id);
    }

    public ProgressoTransfer criarProgresso() {
        removerProgressosObsoletos();
        ProgressoTransfer progresso = new ProgressoTransfer();
        LCCompensacaoServices.PROGRESSO_MAP.put(progresso.getId(), progresso);
        return progresso;
    }

    public void removerProgressosObsoletos() {
        HashMap<Long, ProgressoTransfer> progressoMapClone = (HashMap<Long, ProgressoTransfer>) LCCompensacaoServices.PROGRESSO_MAP.clone();
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
        LCCompensacaoServices.PROGRESSO_MAP.remove(id);
    }

    //<editor-fold defaultstate="collapsed" desc="FUNÇÕES AUXILIARES">
    @Override
    public List<ColetivoResultado> removerEntidadeTipoDoColetivo(Coletivo coletivo) throws ServiceException {
        List<Compensacao> listaNova = coletivo.getCompensacaoList();
        List<Compensacao> listaAntiga = compensacaoService.buscarPorColetivo(coletivo).stream().collect(Collectors.toList());
        listaAntiga.removeAll(listaNova);
        return compensacaoService.removerColetivo(listaAntiga, coletivo);
    }

    /**
     * realiza o filtro dos funcionários existentes para o coletivo
     *
     * @param compencacaoList
     * @param operador
     * @return
     */
    @Override
    public List<Funcionario> filtrarFuncionario(List<Compensacao> compencacaoList, Operador operador) {
        //lista os funcionários que possuem este coletivo
        List<Funcionario> funcionariosList = compencacaoList.stream().map(h -> h.getFuncionario()).distinct().collect(Collectors.toList());
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

    public class ConsumerImpl implements Consumer<ColetivoTransfer> {

        private final Operador operador;

        public ConsumerImpl(Operador operador) {
            this.operador = operador;
        }

        @Override
        public void accept(ColetivoTransfer coletivo) {

            //filtra os funcionários que o coletivo possui
            coletivo.setFuncionarioList(filtrarFuncionario(coletivo.getColetivo().getCompensacaoList(), operador));

            coletivo.getColetivo().setPermitido(isColetivoPermitido(filtrarDepartamento(filtrarFuncionario(coletivo.getColetivo().getCompensacaoList(), operador), operador), operador));
        }
    }

    /**
     * Retorna o clone ou uma entidade nova
     *
     * @param af
     * @return
     */
    private Compensacao getClone(Compensacao af) {
        try {
            return (Compensacao) af.clone();
        } catch (CloneNotSupportedException ex) {
            LOGGER.error(LCCompensacaoServices.class.getName(), ex);
            return new Compensacao();
        }
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="REGRAS PARA EDIÇÃO DO COLETIVO E EXCLUSÃO">
    @Override
    public void validacoesEdicao(Collection<Compensacao> compensacaoLista) {

        compensacaoLista.stream().forEach(f -> {
            String mensagem = isEditavel(f);
            //Coletivo
            f.getColetivo().setEditavel(mensagem == null);

            //Empresa
            compensacaoLista.stream()
                    .map(fb -> fb.getFuncionario()).distinct()
                    .map(fd -> fd.getDepartamento()).distinct()
                    .map(e -> e.getEmpresa())
                    .forEach(d -> {
                        d.setEditavel(mensagem == null);
                        d.setMensagem(mensagem);
                    });
            //Departamento
            compensacaoLista.stream()
                    .map(fb -> fb.getFuncionario()).distinct()
                    .map(fd -> fd.getDepartamento()).distinct()
                    .forEach(d -> {
                        d.setEditavel(mensagem == null);
                        d.setMensagem(mensagem);
                    });

            //Funcionario
            //obs: talvez esta função esteja errada. Trocar pela função abaixo
//            compensacaoLista.parallelStream()
//                    .map(fb -> fb.getFuncionario()).distinct()
//                    .forEach(d -> {
//                        d.setEditavel(mensagem == null);
//                        d.setMensagem(mensagem);
//                    });

            f.getFuncionario().setEditavel(mensagem == null);
            f.getFuncionario().setMensagem(mensagem);            
        });

    }

    private String isEditavel(Compensacao compensacao) {
        try {
            this.compensacaoService.validarExcluir(compensacao);
            return null;
        } catch (ServiceException ex) {
            MsgRetorno msg = (MsgRetorno) ex.getResponse().getEntity();
            return msg.getBody();
        }
    }

//</editor-fold>
}
