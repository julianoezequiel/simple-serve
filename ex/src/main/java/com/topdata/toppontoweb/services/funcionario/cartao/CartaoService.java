package com.topdata.toppontoweb.services.funcionario.cartao;

//<editor-fold defaultstate="collapsed" desc="IMPORTS">
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topdata.toppontoweb.dao.DaoException;
import com.topdata.toppontoweb.dao.funcionario.CartaoDao;
import com.topdata.toppontoweb.entity.empresa.Empresa;
import com.topdata.toppontoweb.entity.funcionario.Cartao;
import com.topdata.toppontoweb.entity.funcionario.Cartao_;
import com.topdata.toppontoweb.entity.funcionario.Funcionario;
import com.topdata.toppontoweb.services.ServiceException;
import com.topdata.toppontoweb.services.TopPontoService;
import com.topdata.toppontoweb.services.ValidacoesCadastro;
import com.topdata.toppontoweb.services.empresa.EmpresaService;
import com.topdata.toppontoweb.services.fechamento.FechamentoServices;
import com.topdata.toppontoweb.services.funcionario.FuncionarioService;
import com.topdata.toppontoweb.services.marcacoes.MarcacoesService;
import com.topdata.toppontoweb.utils.DateHelper;
import com.topdata.toppontoweb.utils.Utils;
import com.topdata.toppontoweb.utils.constantes.CONSTANTES;
import com.topdata.toppontoweb.utils.constantes.MSG;
import java.util.Comparator;
import java.util.Date;
//</editor-fold>

/**
 * @version 1.0.0.0 data 18/01/2017
 * @since 1.0.0.0 data 05/09/2016
 *
 * @author juliano.ezequiel
 */
@Service
public class CartaoService extends TopPontoService<Cartao, Object> implements ValidacoesCadastro<Cartao, Object> {

    //<editor-fold defaultstate="collapsed" desc="CDI">
    @Autowired
    private CartaoDao cartaoDao;
    @Autowired
    private FuncionarioService funcionarioService;
    @Autowired
    private EmpresaService empresaService;
    
    @Autowired
    private MarcacoesService marcacoesService;
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="VARIÁVEIS">
    private HashMap<String, Object> map;
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="CONSULTAS">
    /**
     * Busca os cartões pelo id fdi funcionário
     *
     * @param id
     * @return
     * @throws ServiceException
     */
    public List<Cartao> buscarPorFuncionario(Integer id) throws ServiceException {
        try {
            Funcionario f = funcionarioService.buscar(Funcionario.class, id);
            this.map = new HashMap<>();
            this.map.put(Cartao_.idFuncionario.getName(), f);
            return this.cartaoDao.findbyAttributes(this.map, Cartao.class);
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erroBuscar(), ex);
        }
    }

    public List<Cartao> buscarPorNumeroDataFuncionario(String numeroCartao, Date dataMarcacao, Empresa empresa) {
        return this.cartaoDao.buscarPorNumeroDataEmpresa(numeroCartao, dataMarcacao, empresa);
    }

    /**
     * Busca os cartoes pelo número do cartão
     *
     * @param numero
     * @return
     * @throws ServiceException
     */
    public List<Cartao> buscarPorNumero(String numero) throws ServiceException {
        try {
            this.map = new HashMap<>();
            this.map.put(Cartao_.numero.getName(), numero);
            return cartaoDao.findbyAttributes(this.map, Cartao.class);
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erroBuscar(), ex);
        }
    }

    public Cartao buscarPrimeiroCartao(Cartao cartao) throws ServiceException {
        try {
            this.map = new HashMap<>();
            map.put(Cartao_.idFuncionario.getName(), cartao.getIdFuncionario());
            List<Cartao> cartoesList = this.getDao().findbyAttributes(map, Cartao.class);
            return cartoesList.isEmpty() ? null : cartoesList.iterator().next();
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erroBuscar(), ex);
        }
    }

    /**
     * Busca o cartao atual de um funcionario dentro de uma data
     *
     * @param funcionario
     * @param data
     * @return
     */
    public Cartao buscarAtualPorFuncinarioDataInicio(Funcionario funcionario, Date data) {
        List<Cartao> cartaoList = cartaoDao.buscarPorFuncinarioDataInicio(funcionario, data);
        Cartao cartaoMaiorData = new Cartao();
        for (Cartao cartao : cartaoList) {
            if (cartaoMaiorData.getDataInicio() == null
                    || cartaoMaiorData.getDataInicio().getTime() < cartao.getDataInicio().getTime()) {
                cartaoMaiorData = cartao;
            }
        }

        return cartaoMaiorData;
    }
    
    public Date buscarDataFimDoCartao(Cartao cartao){
        
        if(cartao.getProvisorio()){ //Se for provisorio, é só retornar a data fim
            return cartao.getDataValidade();
        }else{
            //busca todos os cartoes do funcionario (com data inicial maior que a data do cartao atual)
            Date maiorData = null;
            List<Cartao> cartaoList = cartaoDao.buscarCartaoDataFuncionario(cartao.getIdFuncionario(), cartao.getDataInicio());
            for (Cartao c : cartaoList){
                if(!Objects.equals(c.getIdCartao(), cartao.getIdCartao())){
                    if(maiorData == null || c.getDataInicio().before(maiorData)){
                        maiorData = c.getDataInicio();
                    }
                }
            }
            
            return maiorData != null? maiorData : DateHelper.Max() ;
        }
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="CRUD">
    @Override
    public Cartao buscar(Class<Cartao> entidade, Object id) throws ServiceException {
        try {
            Cartao c = cartaoDao.find(Cartao.class, id);
            if (c == null) {
                throw new ServiceException(this.getTopPontoResponse().alertaNaoCad(new Cartao().toString()));
            }
            return c;
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erroBuscar(), ex);
        }
    }

    @Override
    public Response salvar(Cartao cartao) throws ServiceException {
        try {
            //Ignorando os zeros a esquerda
            cartao.setNumero(Utils.ignorarZerosAEsquerda(cartao.getNumero()));

            cartao = validarSalvar(cartao);
            cartao = (Cartao) cartaoDao.save(cartao);
            this.getAuditoriaServices().auditar(CONSTANTES.Enum_FUNCIONALIDADE.CAD_FUNCIONARIO_CARTAO, CONSTANTES.Enum_AUDITORIA.DEFAULT, CONSTANTES.Enum_OPERACAO.INCLUIR, cartao);
            
            if(cartao != null && cartao.getIdCartao() != null){
                Date dataFim = buscarDataFimDoCartao(cartao);
                this.marcacoesService.atualizarMarcacoesInvalidasPorCartao(cartao.getIdFuncionario(), cartao.getNumero() ,cartao.getDataInicio(), dataFim);
            }
            
            return this.getTopPontoResponse().sucessoSalvar(cartao.toString(), cartao);
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erroSalvar(cartao.toString()), ex);
        }
    }
    
    @Override
    public Response atualizar(Cartao cartao) throws ServiceException {
        try {
            cartao.setNumero(Utils.ignorarZerosAEsquerda(cartao.getNumero()));
            cartao = validarAtualizar(cartao);
            cartao = (Cartao) cartaoDao.save(cartao);
            this.getAuditoriaServices().auditar(CONSTANTES.Enum_FUNCIONALIDADE.CAD_FUNCIONARIO_CARTAO, CONSTANTES.Enum_AUDITORIA.DEFAULT, CONSTANTES.Enum_OPERACAO.EXCLUIR, cartao);
            
            if(cartao != null && cartao.getIdCartao() != null){
                Date dataFim = buscarDataFimDoCartao(cartao);
                this.marcacoesService.atualizarMarcacoesInvalidasPorCartao(cartao.getIdFuncionario(), cartao.getNumero() ,cartao.getDataInicio(), dataFim);
            }
            
            return this.getTopPontoResponse().sucessoAtualizar(cartao.toString(), cartao);
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erroAtualizar(cartao.toString()), ex);
        }
    }

    @Override
    public Response excluir(Class<Cartao> c, Object id) throws ServiceException {
        try {
            Cartao cartao = validarExcluir(new Cartao((Integer) id));
            cartaoDao.delete(cartao);
            this.getAuditoriaServices().auditar(CONSTANTES.Enum_FUNCIONALIDADE.CAD_FUNCIONARIO_CARTAO, CONSTANTES.Enum_AUDITORIA.DEFAULT, CONSTANTES.Enum_OPERACAO.EXCLUIR, cartao);
            return this.getTopPontoResponse().sucessoExcluir(cartao.toString());
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erroExcluir(new Cartao().toString()), ex);
        }
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="VALIDAÇÕES OBRIGATÓRIAS">
    @Override
    public Cartao validarSalvar(Cartao cartao) throws ServiceException {

        try {
            Cartao c;
            Empresa empresa = this.empresaService.buscarPorFuncionario(cartao.getIdFuncionario());

//            validarPossuiFechamento(cartao, empresa);

            //verifica se já existe um cartão com este número e por empresa
            List<Cartao> cartaoLst = cartaoDao.buscarPorNumeroEmpresa(cartao.getNumero(), empresa);
            if (!cartaoLst.isEmpty()) {
                c = cartaoLst.get(0);
                if (!Objects.equals(c.getIdFuncionario(), cartao.getIdFuncionario())) {
                    throw new ServiceException(this.getTopPontoResponse().alertaValidacao(MSG.CARTOES.ALERTA_CARTAO_EM_USO.getResource()));
                }
            }
            //verifica se a data de início é anterior a data de admissão do funcionário
            Funcionario funcionario = funcionarioService.buscar(Funcionario.class, cartao.getIdFuncionario().getIdFuncionario());
            if (funcionario.getDataAdmissao() != null) {
                if (cartao.getDataInicio().before(funcionario.getDataAdmissao())) {
                    throw new ServiceException(this.getTopPontoResponse().alertaValidacao(MSG.CARTOES.ALERTA_DATA_INICIO_ANTERIOR_ADMISSAO.getResource()));
                }
            }
            //verifica se a data de início é posterior a a data de demissão do funcionário
            if (funcionario.getDataDemissao() != null) {
                if (cartao.getDataInicio().after(funcionario.getDataDemissao())) {
                    throw new ServiceException(this.getTopPontoResponse().alertaValidacao(MSG.CARTOES.ALERTA_DATA_INICIO_POSTERIO_DEMISSAO.getResource()));
                }
            }

            //verifica se é um cartão provisório e realiza as regras para os cartões existentes
            validarCartaoProvisorio(cartao, funcionario);

            //verifica se o funcionário já possúi um cartão com início na data do cartão
            if (funcionario.getCartaoList().stream().filter(cr -> {
                return cr.getDataInicio().compareTo(cartao.getDataInicio()) == 0;
            }).findAny().orElse(null) != null) {
                throw new ServiceException(this.getTopPontoResponse().alertaValidacao(MSG.CARTOES.ALERTA_JA_POSSUI_ESTA_DATA_INICIO.getResource()));
            }

            return cartao;
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erroSalvar(cartao.toString()), ex);
        }
    }

    @Override
    public Cartao validarExcluir(Cartao cartao) throws ServiceException {
        if (cartao.getIdCartao() == null) {
            throw new ServiceException(this.getTopPontoResponse().alertaNaoCad(cartao.toString()));
        }
        cartao = buscar(Cartao.class, cartao.getIdCartao());
        Empresa empresa = empresaService.buscarPorFuncionario(cartao.getIdFuncionario());
//        validarPossuiFechamento(cartao, empresa);
        return cartao;
    }

    @Override
    public Cartao validarAtualizar(Cartao cartao) throws ServiceException {
        try {
            Cartao c;

            //verifica o id
            if (cartao.getIdCartao() == null) {
                throw new ServiceException(this.getTopPontoResponse().alertaNaoCad(cartao.toString()));
            }

            Empresa empresa = empresaService.buscarPorFuncionario(cartao.getIdFuncionario());

            //valida o cartão existente na base
//            Cartao cartaoOriginal;
//            if (cartao.getIdCartao() != null) {
//                cartaoOriginal = this.buscar(Cartao.class, cartao.getIdCartao());
//                validarPossuiFechamento(cartaoOriginal, empresa);
//            }
//
//            //valida os novos dados do cartao
//            validarPossuiFechamento(cartao, empresa);
            //valida se existe na base
            buscar(Cartao.class, cartao.getIdCartao());

            //verifica se já existe um cartão com este número
            List<Cartao> list = cartaoDao.buscarPorNumeroEmpresa(cartao.getNumero(), empresa);
            if (!list.isEmpty()) {
                c = list.get(0);
                if (!Objects.equals(c.getIdFuncionario(), cartao.getIdFuncionario())) {
                    throw new ServiceException(this.getTopPontoResponse().alertaValidacao(MSG.CARTOES.ALERTA_CARTAO_EM_USO.getResource()));
                }
            }
            
            Funcionario funcionario = funcionarioService.buscar(Funcionario.class, cartao.getIdFuncionario().getIdFuncionario());
            //verifica se a data de início é anterior a data de admissão do funcionário
            if (funcionario.getDataAdmissao() != null) {
                if (cartao.getDataInicio().before(funcionario.getDataAdmissao())) {
                    throw new ServiceException(this.getTopPontoResponse().alertaValidacao(MSG.CARTOES.ALERTA_DATA_INICIO_ANTERIOR_ADMISSAO.getResource()));
                }
            }
            //verifica se a data de início é posterio a a data de demissão do funcionário
            if (funcionario.getDataDemissao() != null) {
                if (cartao.getDataInicio().after(funcionario.getDataDemissao())) {
                    throw new ServiceException(this.getTopPontoResponse().alertaValidacao(MSG.CARTOES.ALERTA_DATA_INICIO_POSTERIO_DEMISSAO.getResource()));
                }
            }

            //verifica se é um cartão provisório e realiza as regras para os cartões existentes
            validarCartaoProvisorio(cartao, funcionario);

            //verifica se o funcionário já possúi um cartão com início na data do cartão
            if (funcionario.getCartaoList().stream().filter(cr -> {
                return cr.getDataInicio().compareTo(cartao.getDataInicio()) == 0 && !cr.equals(cartao);
            }).findAny().orElse(null) != null) {
                throw new ServiceException(this.getTopPontoResponse().alertaValidacao(MSG.CARTOES.ALERTA_JA_POSSUI_ESTA_DATA_INICIO.getResource()));
            }

            return cartao;
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erroSalvar(cartao.toString()), ex);
        }
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="REGRAS DE VALIDAÇÕES">
//    private void validarPossuiFechamento(Cartao cartao, Empresa empresa) throws ServiceException {
//
//        List<Cartao> cartaoList = this.cartaoDao.buscarPorFuncionario(cartao.getIdFuncionario());
//        cartaoList = cartaoList
//                .stream()
//                .filter(cr
//                        -> !cr.getIdCartao().equals(cartao.getIdCartao())
//                        && cr.getDataInicio().compareTo(cartao.getDataInicio()) <= 0)
//                .sorted(Comparator.comparing(Cartao::getDataInicio))
//                .collect(Collectors.toList());
//
//        Date dataFim;
//        if (cartao.getDataValidade() != null) {
//            dataFim = cartao.getDataValidade();
//        } else if (!cartaoList.isEmpty()) {
//            dataFim = DateHelper.addDay(cartaoList.iterator().next().getDataInicio(), -1);
//        } else {
//            dataFim = DateHelper.Max();
//        }
//
//        //valida se exsite fechamento
//        this.getFechamentoServices().isPossuiFechamentoPeriodo(empresa, cartao.getDataInicio(), dataFim);
//
//    }

    /**
     * Valida os cartões provisórios
     *
     * @param cartao
     * @param funcionario
     * @return
     * @throws ServiceException
     * @throws DaoException
     */
    private Cartao validarCartaoProvisorio(Cartao cartao, Funcionario funcionario) throws ServiceException, DaoException {
        try {

            //se for um cartão provisório
            if (Objects.equals(cartao.getProvisorio(), Boolean.TRUE)) {

                List<Cartao> list = cartaoDao.buscarCartoesEntreDatas(funcionario, cartao);

                //se existir cartões para validar
                list = list.stream().filter(c -> !Objects.equals(c.getIdCartao(), cartao.getIdCartao())).collect(Collectors.toList());

                if (!list.isEmpty()) {

                    if (list.size() > 1) {
                        Cartao posterior = list.get(0);
                        Cartao anterior = list.get(list.size() - 1);

                        //exclui os cartoes que existirem nesse periodo
                        for (Cartao c1 : list) {
                            if (!c1.equals(anterior) && !c1.equals(posterior)) {
                                cartaoDao.delete(c1);
                            }
                        }

                        Calendar c = Calendar.getInstance();
                        c.setTime(cartao.getDataValidade());
                        c.add(Calendar.DAY_OF_MONTH, -1);
                        anterior.setDataValidade(c.getTime());

                        cartaoDao.save(anterior);

                        c.setTime(cartao.getDataInicio());
                        c.add(Calendar.DAY_OF_MONTH, 1);
                        posterior.setDataInicio(c.getTime());
                        if (cartao.getDataValidade().getTime() >= posterior.getDataValidade().getTime()) {
                            cartaoDao.delete(posterior);
                        } else {
                            cartaoDao.save(posterior);
                        }
                    } else {

                        Cartao c = list.get(0);

                        //exclui os cartoes que existirem nesse periodo
                        for (Cartao c1 : list) {
                            if (!c1.equals(c)) {
                                cartaoDao.delete(c1);
                            }
                        }

                        //atualiza o cartao anterio para iniciar depois do termino do atual
                        Calendar calendar = Calendar.getInstance();

                        Cartao c2 = (Cartao) c.clone();
                        if (cartao.getDataInicio().getTime() > c.getDataInicio().getTime()) {
                            calendar.setTime(cartao.getDataInicio());
                            calendar.add(Calendar.DAY_OF_MONTH, -1);
                            c2.setDataValidade(calendar.getTime());
                            c2.setIdCartao(null);
                            cartaoDao.save(c2);
                        }

                        calendar.setTime(cartao.getDataValidade());
                        calendar.add(Calendar.DAY_OF_MONTH, 1);
                        c.setDataInicio(calendar.getTime());
                        if (c.getDataInicio().getTime() > c.getDataValidade().getTime()) {
                            cartaoDao.delete(c);
                        } else {
                            //salva o cartão com a data +1 dia
                            cartaoDao.save(c);
                        }
                    }

                }

            }
        } catch (DaoException | CloneNotSupportedException ex) {
            Logger.getLogger(CartaoService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return cartao;
    }

//</editor-fold>
}
