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
import com.topdata.toppontoweb.entity.funcionario.Afastamento;
import com.topdata.toppontoweb.entity.funcionario.Coletivo;
import com.topdata.toppontoweb.entity.funcionario.Funcionario;
import com.topdata.toppontoweb.services.ServiceException;
import com.topdata.toppontoweb.services.funcionario.afastamento.AfastamentoService;
import com.topdata.toppontoweb.utils.constantes.CONSTANTES;
import com.topdata.toppontoweb.utils.constantes.MSG;
//</editor-fold>

/**
 * Classe com as regras de Negócio para o lançamento coletivo do afastamento
 *
 * @version 1.0.0 data 05/07/2017
 * @since 1.0.0 data 21/12/2016
 *
 * @author juliano.ezequiel
 */
@Service
public class LCAfastamentoServices extends ColetivoService
        implements ColetivoSeviceInterface<Afastamento> {

    @Autowired
    private AfastamentoService afastamentoService;
    private final static HashMap<Long, ProgressoTransfer> PROGRESSO_MAP = new HashMap<>();

    @Override
    public ColetivoTransfer buscarColetivo(HttpServletRequest request, Integer id) throws ServiceException {
        Coletivo coletivo = (Coletivo) this.buscar(Coletivo.class, id);
        Operador operador = getOperadorAtual(request);
        if (!coletivo.getAfastamentoList().isEmpty()) {
            //inicia o objeto de transferencia
            ColetivoTransfer coletivoTransfer = new ColetivoTransfer(coletivo);
            //TODO: <Fazer para o resto> Criar um metodo que saiba qual e a entidade correta
            //Seta a lista de entidade para afastamento
            coletivoTransfer.setAfastamentoList(coletivo.getAfastamentoList());
            //filtra os funcionários que o coletivo possui
            coletivoTransfer.setFuncionarioList(filtrarFuncionario(coletivoTransfer.getColetivo().getAfastamentoList(), operador));
            //filtra os departamentos que o coletivo possui
            coletivoTransfer.setDepartamentoList(filtrarDepartamento(coletivoTransfer.getFuncionarioList(), operador));
            //filtra as empresa que o coletivo possui
            coletivoTransfer.setEmpresaList(filtrarEmpresa(coletivoTransfer.getDepartamentoList(), operador));
            //verifica se o coletivo posera ser editado, pelo operador autenticado
            coletivoTransfer.getColetivo().setPermitido(isColetivoPermitido(filtrarDepartamento(filtrarFuncionario(coletivoTransfer.getColetivo().getAfastamentoList(), operador), operador), operador));
            validacoesEdicao(coletivoTransfer.getColetivo().getAfastamentoList());
            return coletivoTransfer;
        } else {
            throw new ServiceException(getTopPontoResponse().alertaNaoCad(new Coletivo().toString()));
        }
    }

    @Override
    public List<ColetivoTransfer> listarColetivos(HttpServletRequest request) throws ServiceException {
        try {
            //lista os coletivos de acordo com o tipo
            List<ColetivoTransfer> coletivoTransferlist = listarColetivoAfastamento(getColetivoDao().buscarColetivoAfastamentos());
            Operador operador = getOperadorAtual(request);
            coletivoTransferlist.stream().forEach(new ConsumerImplListar(operador));

            //Busca e filtra os coletivos por permissão
            //TODO: Pode ocorrer algum tipo de Lentidão
            List<ColetivoTransfer> coletivoTransferlistResult = coletivoTransferlist.stream().filter(coletivoTransfer
                    -> coletivoTransfer.getFuncionarioList()
                    .stream().anyMatch(funcionario -> funcionario.getPermitido()))
                    .collect(Collectors.toList());

            return coletivoTransferlistResult;
        } catch (DaoException ex) {
            throw new ServiceException(getTopPontoResponse().erroBuscar(), ex);
        }
    }

    /**
     * Realiza a consulta dos afastamento do coletivo coletivoTransfer
     *
     * @param coletivoList lista de coletivos
     * @return retorna uma lista de coletivo para tela!
     */
    public List<ColetivoTransfer> listarColetivoAfastamento(List<Coletivo> coletivoList) {
        List<ColetivoTransfer> coletivoTransferList = new ArrayList<>();
        coletivoList.stream().distinct().forEach((coletivo) -> {
            ColetivoTransfer coletivoTransfer = new ColetivoTransfer(coletivo);

            //Seta a lista de entidade para afastamento
            coletivoTransfer.setAfastamentoList(coletivo.getAfastamentoList());
            coletivoTransferList.add(coletivoTransfer);
        });
        return coletivoTransferList;
    }

    @Override
    public void salvarColetivo(HttpServletRequest request, ColetivoTransfer coletivoEntidade, ProgressoTransfer progresso) throws ServiceException {

        List<Afastamento> afastamentoList = new ArrayList<>();
        List<ColetivoResultado> resultadosColetivo = new ArrayList<>();

        coletivoEntidade.getAfastamentoList().stream().forEach(afastamento -> {
            try {
                coletivoEntidade.setColetivo(criarColetivo(coletivoEntidade.getColetivo(), CONSTANTES.Enum_FUNCIONALIDADE.LC_AFASTAMENTO));
                coletivoEntidade.getFuncionarioList().stream().forEach(funcionario -> {
                    try {

                        Funcionario funcionarioBase = getFuncionarioService().buscar(Funcionario.class, funcionario.getIdFuncionario());

                        Afastamento afastamentoClone = new Afastamento(afastamento);
                        afastamentoClone.setFuncionario(funcionarioBase);
                        afastamentoClone.setDataFim(afastamento.getDataFim());
                        afastamentoClone.setDataInicio(afastamento.getDataInicio());
                        afastamentoClone.setFuncionario(funcionario);
                        afastamentoClone.setMotivo(afastamento.getMotivo());
                        afastamentoClone.setAbonado(afastamento.getAbonado());
                        afastamentoClone.setColetivo(coletivoEntidade.getColetivo());
                        afastamentoList.add(afastamentoClone);
                    } catch (ServiceException ex) {
                        LOGGER.error(LCAfastamentoServices.class.getName(), ex);
                    } catch (NoSuchElementException e) {
                        Afastamento afastamentoClone = getClone(afastamento);
                        afastamentoClone.setIdAfastamento(null);
                        afastamentoClone.setFuncionario(funcionario);
                        afastamentoList.add(afastamentoClone);
                    }
                });
            } catch (ServiceException ex) {
                LOGGER.error(LCAfastamentoServices.class.getName(), ex);
            }
        });

        coletivoEntidade.getColetivo().setAfastamentoList(afastamentoList);
        resultadosColetivo.addAll(afastamentoService.salvarAfastamentoColetivo(afastamentoList, progresso));

        Boolean somenteErros = resultadosColetivo.stream().allMatch(r -> r.getResultado() == ColetivoResultado.Enum_ResultadoColetivo.ERRO && !r.isFechamento());

        if (somenteErros == true) {
            //todos com erro, deve-se excluir o coletivo
            this.excluirColetivo(coletivoEntidade.getColetivo(), CONSTANTES.Enum_FUNCIONALIDADE.LC_AFASTAMENTO);
            coletivoEntidade.setColetivo(null);
        }

        //Adiciona no progresso
        progresso.setColetivoResultados(resultadosColetivo);

    }

    @Override
    public void atualizarColetivo(HttpServletRequest request, ColetivoTransfer coletivoEntidade, ProgressoTransfer progresso) throws ServiceException {

        List<Afastamento> afastamentoList = new ArrayList<>();
        List<ColetivoResultado> resultadosColetivo = new ArrayList<>();

        coletivoEntidade.getAfastamentoList().stream().forEach(af -> {
            try {
                coletivoEntidade.setColetivo(atualizarColetivo(coletivoEntidade.getColetivo(), CONSTANTES.Enum_FUNCIONALIDADE.LC_AFASTAMENTO));
                coletivoEntidade.getFuncionarioList().stream().forEach(funcionario -> {
                    try {
                        Funcionario funcionarioBase = getFuncionarioService().buscar(Funcionario.class, funcionario.getIdFuncionario());
                        List<Afastamento> afastamentosBancoList = afastamentoService.buscarPorColetivo(coletivoEntidade.getColetivo());
                        Afastamento afastamento = afastamentosBancoList.stream()
                                .filter(af1
                                        -> Objects.equals(funcionarioBase.getIdFuncionario(), af1.getFuncionario().getIdFuncionario())
                                        && af1.getColetivo() != null
                                        && af1.getColetivo().getIdColetivo().equals(af.getColetivo().getIdColetivo()))
                                .findAny().get();

                        afastamento.setFuncionario(funcionarioBase);
                        afastamento.setDataFim(af.getDataFim());
                        afastamento.setDataInicio(af.getDataInicio());
                        afastamento.setFuncionario(funcionario);
                        afastamento.setMotivo(af.getMotivo());
                        afastamento.setAbonado(af.getAbonado());
                        afastamentoList.add(afastamento);
                    } catch (ServiceException ex) {
                        LOGGER.error(LCAfastamentoServices.class.getName(), ex);
                    } catch (NoSuchElementException e) {
                        Afastamento afastamentoClone = getClone(af);
                        afastamentoClone.setIdAfastamento(null);
                        afastamentoClone.setFuncionario(funcionario);
                        afastamentoList.add(afastamentoClone);
                    }
                });
            } catch (ServiceException ex) {
                LOGGER.error(LCAfastamentoServices.class.getName(), ex);
            }
        });

        coletivoEntidade.getColetivo().setAfastamentoList(afastamentoList);
        resultadosColetivo.addAll(removerEntidadeTipoDoColetivo(coletivoEntidade.getColetivo()));

        resultadosColetivo.addAll(afastamentoService.salvarAfastamentoColetivo(afastamentoList, progresso));

        Boolean somenteErros = resultadosColetivo.stream().allMatch(r -> r.getResultado() == ColetivoResultado.Enum_ResultadoColetivo.ERRO);

        if (somenteErros == true) {
            //todos com erro, deve-se excluir o coletivo
            this.excluirColetivo(coletivoEntidade.getColetivo(), CONSTANTES.Enum_FUNCIONALIDADE.LC_AFASTAMENTO);
            coletivoEntidade.setColetivo(null);
        }

        //Adiciona no progresso
        progresso.setColetivoResultados(resultadosColetivo);

    }

    /**
     * Realiza as validações de cada afastamento vínculado ao funcionário
     * atravéz deste coletivo,em caso de existir um afastamento que não possa
     * ser excluído será lançada uma exceção
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
        List<Afastamento> afastamentoList = this.afastamentoService.buscarPorColetivo(c);
        //valida se este coletivo poderá ser excluído.
        //É utilizada as mesmas regras para a exclusão do registro manual
        afastamentoList.stream().forEach(a -> {
            try {
                this.afastamentoService.validarExcluir(a);
            } catch (ServiceException ex) {
                isPermitidoExclusao.set(Boolean.FALSE);
            }
        });

        //em caso exista pelo menos um registro que faça parte deste coletivo que não possa ser excluído
        //será lançada uma exceção
        if (isPermitidoExclusao.get() == Boolean.TRUE) {
            this.afastamentoService.excluirPorColetivo(c);
            this.excluirColetivo(c, CONSTANTES.Enum_FUNCIONALIDADE.LC_AFASTAMENTO);
            return this.getTopPontoResponse().sucessoExcluir(c.toString());
        } else {
            return getTopPontoResponse().alertaValidacao(MSG.COLETIVO.ALERTA_EXCLUIR_COLETIVO.getResource());
        }
    }

    //<editor-fold defaultstate="collapsed" desc="FUNÇÕES AUXILIARES">
    /**
     * Remove o coletivo dos afastamentos que não estão mais listados
     *
     * @param coletivo
     * @return
     * @throws ServiceException
     */
    @Override
    public List<ColetivoResultado> removerEntidadeTipoDoColetivo(Coletivo coletivo) throws ServiceException {
        List<Afastamento> listaNova = coletivo.getAfastamentoList();
        List<Afastamento> listaAntiga = afastamentoService.buscarPorColetivo(coletivo).stream().collect(Collectors.toList());

        listaAntiga.removeAll(listaNova);

        return afastamentoService.removerColetivo(listaAntiga, coletivo);
    }

    @Override
    public List<Funcionario> filtrarFuncionario(List<Afastamento> afastamentoList, Operador operador) {
        //lista os funcionários que possuem este coletivo
        List<Funcionario> funcionariosList = afastamentoList.stream().map(h -> h.getFuncionario()).distinct().collect(Collectors.toList());
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

    public ProgressoTransfer buscarProgresso(long id) {
        return LCAfastamentoServices.PROGRESSO_MAP.get(id);
    }

    public ProgressoTransfer criarProgresso() {
        removerProgressosObsoletos();
        ProgressoTransfer progresso = new ProgressoTransfer();
        LCAfastamentoServices.PROGRESSO_MAP.put(progresso.getId(), progresso);
        return progresso;
    }

    public void removerProgressosObsoletos() {
        HashMap<Long, ProgressoTransfer> progressoMapClone = (HashMap<Long, ProgressoTransfer>) LCAfastamentoServices.PROGRESSO_MAP.clone();
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
        LCAfastamentoServices.PROGRESSO_MAP.remove(id);
    }

    public class ConsumerImplListar implements Consumer<ColetivoTransfer> {

        private final Operador operador;

        public ConsumerImplListar(Operador operador) {
            this.operador = operador;
        }

        @Override
        public void accept(ColetivoTransfer coletivoTransfer) {
            //filtra os funcionários que o coletivo possui
            coletivoTransfer.setFuncionarioList(filtrarFuncionario(coletivoTransfer.getColetivo().getAfastamentoList(), operador));
            //filtra os departamentos que o coletivo possui
            coletivoTransfer.setDepartamentoList(filtrarDepartamento(coletivoTransfer.getFuncionarioList(), operador));
            //filtra as empresa que o coletivo possui
            coletivoTransfer.setEmpresaList(filtrarEmpresa(coletivoTransfer.getDepartamentoList(), operador));

            coletivoTransfer.getColetivo().setPermitido(isColetivoPermitido(coletivoTransfer.getDepartamentoList(), operador));
        }
    }

    /**
     * Retorna o clone ou uma entidade nova
     *
     * @param af
     * @return
     */
    private Afastamento getClone(Afastamento af) {
        try {
            return (Afastamento) af.clone();
        } catch (CloneNotSupportedException ex) {
            LOGGER.error(LCAfastamentoServices.class.getName(), ex);
            return new Afastamento();
        }
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="REGRAS PARA EDIÇÃO DO COLETIVO E EXCLUSÃO">
    @Override
    public void validacoesEdicao(Collection<Afastamento> afastamentoLista) {

        afastamentoLista.stream().forEach(f -> {
            String mensagem = isEditavel(f);
            //Coletivo
            f.getColetivo().setEditavel(mensagem == null);

            //Empresa
            afastamentoLista.stream()
                    .map(fb -> fb.getFuncionario()).distinct()
                    .map(fd -> fd.getDepartamento()).distinct()
                    .map(e -> e.getEmpresa())
                    .forEach(d -> {
                        d.setEditavel(mensagem == null);
                        d.setMensagem(mensagem);
                    });
            //Departamento
            afastamentoLista.stream()
                    .map(fb -> fb.getFuncionario()).distinct()
                    .map(fd -> fd.getDepartamento()).distinct()
                    .forEach(d -> {
                        d.setEditavel(mensagem == null);
                        d.setMensagem(mensagem);
                    });

            //Funcionario
            //obs: talvez esta função esteja errada. Trocar pela função abaixo
//            afastamentoLista.parallelStream()
//                    .map(fb -> fb.getFuncionario()).distinct()
//                    .forEach(d -> {
//                        d.setEditavel(mensagem == null);
//                        d.setMensagem(mensagem);
//                    });
            f.getFuncionario().setEditavel(mensagem == null);
            f.getFuncionario().setMensagem(mensagem);

        });

    }

    private String isEditavel(Afastamento afastamento) {
        try {
            this.afastamentoService.validarExcluir(afastamento);
            return null;
        } catch (ServiceException ex) {
            MsgRetorno msg = (MsgRetorno) ex.getResponse().getEntity();
            return msg.getBody();
        }
    }
//</editor-fold>
}
