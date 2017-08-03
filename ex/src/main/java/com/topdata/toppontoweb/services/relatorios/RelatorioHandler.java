package com.topdata.toppontoweb.services.relatorios;

import com.topdata.toppontoweb.dto.gerafrequencia.GeraFrequenciaStatusTransfer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import com.topdata.toppontoweb.dto.relatorios.RelatoriosGeraFrequenciaTransfer;
import com.topdata.toppontoweb.dto.relatorios.SubTipoRelatorio;
import com.topdata.toppontoweb.dto.relatorios.cadastros.RelatorioCadastroFuncionarioTransfer;
import com.topdata.toppontoweb.entity.empresa.Departamento;
import com.topdata.toppontoweb.entity.empresa.Empresa;
import com.topdata.toppontoweb.services.gerafrequencia.integracao.SaidaPadrao;
import com.topdata.toppontoweb.services.gerafrequencia.integracao.SaidaPadraoDepartamento;
import com.topdata.toppontoweb.services.gerafrequencia.integracao.SaidaPadraoEmpresa;
import com.topdata.toppontoweb.dto.gerafrequencia.IGeraFrequenciaTransfer;
import com.topdata.toppontoweb.services.gerafrequencia.entity.calculo.EntradaApi;
import com.topdata.toppontoweb.services.gerafrequencia.integracao.SaidaPadraoEmpresaDepartamento;
import com.topdata.toppontoweb.utils.constantes.CONSTANTES;

/**
 *
 * @author juliano.ezequiel
 */
public class RelatorioHandler {

    //CONTANTES UTILIZADAS PARA SERM ENVIADAS POR PARAMENTRO PARA O JASPER REPORT
    public static final String FILTRO_PERIODO_INICIO = "FILTRO_PERIODO_INICIO";
    public static final String FILTRO_PERIODO_TERMINO = "FILTRO_PERIODO_TERMINO";
    public static final String FILTRO_IS_CEI = "isCEI";
    public static final String FILTRO_IS_DEPARTAMENTO = "isDepartamento";
    public static final String FILTRO_IMG_LOGO_CABECALHO = "IMG_LOGO_CABECALHO";
    public static final String FILTRO_IS_CAMPOS_ZERADO = "FILTRO_IS_CAMPOS_ZERADO";
    public static final String FILTRO_IS_TOTAL_HORAS_JORNADA = "FILTRO_IS_TOTAL_HORAS_JORNADA";
    public static final String SUBREPORT_ABSENTEISMO = "SUBREPORT_ABSENTEISMO";
    public static final String REPORT_PATH = "REPORT_PATH";
    public static final String TIPO_DIA_LIST = "TIPO_DIA_LIST";

    /**
     * Tipos de estados do processo do relatório
     */
    public enum ESTADO_PROCESSO {
        ERRO,
        ACESSANDO_BASE,
        PROCESSANDO_RELATORIO,
        CRIANDO_ARQUIVO,
        EXCLUINDO_REGISTROS,
        PERSISTINDO_DADOS,
        AGENDANDO_PROCESSO,
        CALCULO_CONCLUIDO
    }

    public enum TIPO_RELATORIO {

        ESPELHO {

            @Override
            public String getTemplate(IGeraFrequenciaTransfer relatorioTransfer) {
                return "relatorioEspelho";
            }

            @Override
            public Collection<?> getAgrupamento(IGeraFrequenciaTransfer relatorioTransfer, Collection<SaidaPadrao> collection) {
                return validarAgrupamento(relatorioTransfer, collection);
            }

            @Override
            public void configurarParametros(GeraFrequenciaStatusTransfer statusTransfer, IGeraFrequenciaTransfer frequenciaTransfer) {
            }

            @Override
            public void configurarEntradaApi(EntradaApi entradaApi) {
                entradaApi.setProcessaCalculo(false);
                entradaApi.setProcessaAFDT(false);
                entradaApi.setProcessaACJEF(false);
                entradaApi.setProcessaDSR(false);
                entradaApi.setProcessaAbsenteismo(false);
                entradaApi.setProcessaManutencaoMarcacoes(false);
            }

            @Override
            public CONSTANTES.Enum_FUNCIONALIDADE getFuncionalidadeAuditoria() {
                return CONSTANTES.Enum_FUNCIONALIDADE.REL_ESPELHO;
            }
        },
        ESPELHO_FISCAL {
            @Override
            public Collection<?> getAgrupamento(IGeraFrequenciaTransfer relatorioTransfer, Collection<SaidaPadrao> collection) {
                return validarAgrupamento(relatorioTransfer, collection);
            }

            @Override
            public String getTemplate(IGeraFrequenciaTransfer relatorioTransfer) {
                return "relatorioEspelhoFiscal";
            }

            @Override
            public void configurarParametros(GeraFrequenciaStatusTransfer statusTransfer, IGeraFrequenciaTransfer frequenciaTransfer) {

            }

            @Override
            public void configurarEntradaApi(EntradaApi entradaApi) {
                entradaApi.setProcessaCalculo(false);
                entradaApi.setProcessaAFDT(false);
                entradaApi.setProcessaACJEF(false);
                entradaApi.setProcessaDSR(false);
                entradaApi.setProcessaAbsenteismo(false);
                entradaApi.setProcessaManutencaoMarcacoes(false);
            }
            
            @Override
            public CONSTANTES.Enum_FUNCIONALIDADE getFuncionalidadeAuditoria() {
                return CONSTANTES.Enum_FUNCIONALIDADE.ARQ_FISCAL;
            }
        },
        PRESENCA {

            @Override
            public String getTemplate(IGeraFrequenciaTransfer relatorioTransfer) {
                return "relatorioPresenca";
            }

            @Override
            public Collection<?> getAgrupamento(IGeraFrequenciaTransfer relatorioTransfer, Collection<SaidaPadrao> collection) {
                RelatoriosGeraFrequenciaTransfer apiTransfer = (RelatoriosGeraFrequenciaTransfer) relatorioTransfer;
                //Agrupa por empresa
                apiTransfer.setTipoRelatorio(new SubTipoRelatorio(SUB_TIPO_RELATORIO.EMPRESA));
                return validarAgrupamento(apiTransfer, collection);
            }

            @Override
            public void configurarParametros(GeraFrequenciaStatusTransfer statusTransfer, IGeraFrequenciaTransfer frequenciaTransfer) {

            }

            @Override
            public void configurarEntradaApi(EntradaApi entradaApi) {
                entradaApi.setProcessaCalculo(false);
                entradaApi.setProcessaAFDT(false);
                entradaApi.setProcessaACJEF(false);
                entradaApi.setProcessaDSR(false);
                entradaApi.setProcessaAbsenteismo(false);
                entradaApi.setProcessaManutencaoMarcacoes(false);
            }
            
            @Override
            public CONSTANTES.Enum_FUNCIONALIDADE getFuncionalidadeAuditoria() {
                return CONSTANTES.Enum_FUNCIONALIDADE.REL_PRESENCA;
            }
        },
        FREQUENCIA {

            @Override
            public String getTemplate(IGeraFrequenciaTransfer relatorioTransfer) {
                RelatoriosGeraFrequenciaTransfer apiTransfer = (RelatoriosGeraFrequenciaTransfer) relatorioTransfer;
                if ((apiTransfer.getTipoRelatorio().getIdTipoRelatorio().equals(SUB_TIPO_RELATORIO.DEPARTAMENTO.getIdTipoRelatorio()))) {
                    return "relatorioFrequenciaPorDepartamento";
                } else if (apiTransfer.getTipoRelatorio().getIdTipoRelatorio().equals(SUB_TIPO_RELATORIO.EMPRESA.getIdTipoRelatorio())) {
                    return "relatorioFrequenciaPorEmpresa";
                }
                return "relatorioFrequenciaIndividual";
            }

            @Override
            public Collection<?> getAgrupamento(IGeraFrequenciaTransfer relatorioTransfer, Collection<SaidaPadrao> collection) {
                return validarAgrupamento(relatorioTransfer, collection);
            }

            @Override
            public void configurarParametros(GeraFrequenciaStatusTransfer statusTransfer, IGeraFrequenciaTransfer frequenciaTransfer) {

            }

            @Override
            public void configurarEntradaApi(EntradaApi entradaApi) {
                entradaApi.setProcessaCalculo(true);
                entradaApi.setProcessaAFDT(false);
                entradaApi.setProcessaACJEF(false);
                entradaApi.setProcessaDSR(false);
                entradaApi.setProcessaAbsenteismo(false);
                entradaApi.setProcessaManutencaoMarcacoes(false);
            }
            
            @Override
            public CONSTANTES.Enum_FUNCIONALIDADE getFuncionalidadeAuditoria() {
                return CONSTANTES.Enum_FUNCIONALIDADE.REL_FREQUENCIA;
            }
        },
        HORAS_EXTRAS {

            @Override
            public String getTemplate(IGeraFrequenciaTransfer relatorioTransfer) {
                RelatoriosGeraFrequenciaTransfer apiTransfer = (RelatoriosGeraFrequenciaTransfer) relatorioTransfer;
                if ((apiTransfer.getTipoRelatorio().getIdTipoRelatorio().equals(SUB_TIPO_RELATORIO.DEPARTAMENTO.getIdTipoRelatorio()))) {
                    return "relatorioExtrasPorDepartamento";
                } else if (apiTransfer.getTipoRelatorio().getIdTipoRelatorio().equals(SUB_TIPO_RELATORIO.EMPRESA.getIdTipoRelatorio())) {
                    return "relatorioExtrasPorEmpresa";
                }

                return "relatorioExtrasIndividual";
            }

            @Override
            public Collection<?> getAgrupamento(IGeraFrequenciaTransfer relatorioTransfer, Collection<SaidaPadrao> collection) {
                return validarAgrupamento(relatorioTransfer, collection);
            }

            @Override
            public void configurarParametros(GeraFrequenciaStatusTransfer statusTransfer, IGeraFrequenciaTransfer frequenciaTransfer) {
            }

            @Override
            public void configurarEntradaApi(EntradaApi entradaApi) {
                entradaApi.setProcessaCalculo(true);
                entradaApi.setProcessaAFDT(false);
                entradaApi.setProcessaACJEF(false);
                entradaApi.setProcessaDSR(false);
                entradaApi.setProcessaAbsenteismo(false);
                entradaApi.setProcessaManutencaoMarcacoes(false);
            }
            
            @Override
            public CONSTANTES.Enum_FUNCIONALIDADE getFuncionalidadeAuditoria() {
                return CONSTANTES.Enum_FUNCIONALIDADE.REL_HORAS_EXTRA;
            }
        },
        BANCO_DE_HORAS {

            @Override
            public String getTemplate(IGeraFrequenciaTransfer relatorioTransfer) {

                RelatoriosGeraFrequenciaTransfer apiTransfer = (RelatoriosGeraFrequenciaTransfer) relatorioTransfer;
                if ((apiTransfer.getTipoRelatorio().getIdTipoRelatorio().equals(SUB_TIPO_RELATORIO.DEPARTAMENTO.getIdTipoRelatorio()))) {
                    return "relatorioBancodeHorasPorDepartamento";
                } else if (apiTransfer.getTipoRelatorio().getIdTipoRelatorio().equals(SUB_TIPO_RELATORIO.EMPRESA.getIdTipoRelatorio())) {
                    return "relatorioBancodeHorasPorEmpresa";
                } else if (apiTransfer.getTipoRelatorio().getIdTipoRelatorio().equals(SUB_TIPO_RELATORIO.SALDO_FUNCIONARIO.getIdTipoRelatorio())) {
                    return "relatorioBancodeHorasSaldoPorFuncionario";
                } else if (apiTransfer.getTipoRelatorio().getIdTipoRelatorio().equals(SUB_TIPO_RELATORIO.SALDO_FUNCIONARIO_LIMITE.getIdTipoRelatorio())) {
                    return "relatorioBancodeHorasLimitesSaldoPorFuncionario";
                } else {
                    return "relatorioBancodeHorasIndividual";
                }
            }

            @Override
            public Collection<?> getAgrupamento(IGeraFrequenciaTransfer relatorioTransfer, Collection<SaidaPadrao> collection) {
                return validarAgrupamento(relatorioTransfer, collection);
            }

            @Override
            public void configurarParametros(GeraFrequenciaStatusTransfer statusTransfer, IGeraFrequenciaTransfer frequenciaTransfer) {
            }

            @Override
            public void configurarEntradaApi(EntradaApi entradaApi) {
                entradaApi.setProcessaCalculo(true);
                entradaApi.setProcessaAFDT(false);
                entradaApi.setProcessaACJEF(false);
                entradaApi.setProcessaDSR(false);
                entradaApi.setProcessaAbsenteismo(false);
                entradaApi.setProcessaManutencaoMarcacoes(false);
            }
            
            @Override
            public CONSTANTES.Enum_FUNCIONALIDADE getFuncionalidadeAuditoria() {
                return CONSTANTES.Enum_FUNCIONALIDADE.REL_BANCO_HORAS;
            }
        },
        ABSENTEISMO {

            @Override
            public String getTemplate(IGeraFrequenciaTransfer relatorioTransfer) {
                return "relatorioAbsenteismo";
            }

            @Override
            public Collection<?> getAgrupamento(IGeraFrequenciaTransfer relatorioTransfer, Collection<SaidaPadrao> collection) {
                RelatoriosGeraFrequenciaTransfer apiTransfer = (RelatoriosGeraFrequenciaTransfer) relatorioTransfer;

                //Agrupa por empresa
                apiTransfer.setTipoRelatorio(new SubTipoRelatorio(SUB_TIPO_RELATORIO.EMPRESA_DEPARTAMENTO));
                return validarAgrupamento(apiTransfer, collection);
            }

            @Override
            public void configurarParametros(GeraFrequenciaStatusTransfer statusTransfer, IGeraFrequenciaTransfer frequenciaTransfer) {
            }

            @Override
            public void configurarEntradaApi(EntradaApi entradaApi) {
                entradaApi.setProcessaCalculo(true);
                entradaApi.setProcessaAFDT(false);
                entradaApi.setProcessaACJEF(false);
                entradaApi.setProcessaDSR(false);
                entradaApi.setProcessaAbsenteismo(true);
                entradaApi.setProcessaManutencaoMarcacoes(false);
            }
            @Override
            public CONSTANTES.Enum_FUNCIONALIDADE getFuncionalidadeAuditoria() {
                return CONSTANTES.Enum_FUNCIONALIDADE.REL_ABSENTEISMO;
            }
            
        },
        INTERJORNADA {

            @Override
            public String getTemplate(IGeraFrequenciaTransfer relatorioTransfer) {
                return "relatorioInterJornada";
            }

            @Override
            public Collection<?> getAgrupamento(IGeraFrequenciaTransfer relatorioTransfer, Collection<SaidaPadrao> collection) {
                return validarAgrupamento(relatorioTransfer, collection);
            }

            @Override
            public void configurarParametros(GeraFrequenciaStatusTransfer statusTransfer, IGeraFrequenciaTransfer frequenciaTransfer) {
            }

            @Override
            public void configurarEntradaApi(EntradaApi entradaApi) {
                entradaApi.setProcessaCalculo(false);
                entradaApi.setProcessaAFDT(false);
                entradaApi.setProcessaACJEF(false);
                entradaApi.setProcessaDSR(false);
                entradaApi.setProcessaAbsenteismo(false);
                entradaApi.setProcessaManutencaoMarcacoes(false);
            }
            
            @Override
            public CONSTANTES.Enum_FUNCIONALIDADE getFuncionalidadeAuditoria() {
                return CONSTANTES.Enum_FUNCIONALIDADE.REL_INTERJORNADA;
            }
        },
        INTRAJORNADA {

            @Override
            public String getTemplate(IGeraFrequenciaTransfer relatorioTransfer) {
                return "relatorioIntraJornada";
            }

            @Override
            public Collection<?> getAgrupamento(IGeraFrequenciaTransfer relatorioTransfer, Collection<SaidaPadrao> collection) {
                return validarAgrupamento(relatorioTransfer, collection);
            }

            @Override
            public void configurarParametros(GeraFrequenciaStatusTransfer statusTransfer, IGeraFrequenciaTransfer frequenciaTransfer) {
            }

            @Override
            public void configurarEntradaApi(EntradaApi entradaApi) {
                entradaApi.setProcessaCalculo(false);
                entradaApi.setProcessaAFDT(false);
                entradaApi.setProcessaACJEF(false);
                entradaApi.setProcessaDSR(false);
                entradaApi.setProcessaAbsenteismo(false);
                entradaApi.setProcessaManutencaoMarcacoes(false);
            }
            
            @Override
            public CONSTANTES.Enum_FUNCIONALIDADE getFuncionalidadeAuditoria() {
                return CONSTANTES.Enum_FUNCIONALIDADE.REL_INTRAJORNADA;
            }
        },
        AUDITORIA {

            @Override
            public String getTemplate(IGeraFrequenciaTransfer relatorioTransfer) {
                return "relatorioAuditoria";
            }

            @Override
            public Collection<?> getAgrupamento(IGeraFrequenciaTransfer relatorioTransfer, Collection<SaidaPadrao> collection) {
                return validarAgrupamento(relatorioTransfer, collection);
            }

            @Override
            public void configurarParametros(GeraFrequenciaStatusTransfer statusTransfer, IGeraFrequenciaTransfer frequenciaTransfer) {
            }

            @Override
            public void configurarEntradaApi(EntradaApi entradaApi) {
                entradaApi.setProcessaCalculo(false);
                entradaApi.setProcessaAFDT(false);
                entradaApi.setProcessaACJEF(false);
                entradaApi.setProcessaDSR(false);
                entradaApi.setProcessaAbsenteismo(false);
                entradaApi.setProcessaManutencaoMarcacoes(false);
            }
            
            @Override
            public CONSTANTES.Enum_FUNCIONALIDADE getFuncionalidadeAuditoria() {
                return null;
            }
        },
        CAD_EMPRESA {

            @Override
            public String getTemplate(IGeraFrequenciaTransfer relatorioTransfer) {
                return "cadastros/relatorioCadEmpresaGeral";
            }

            @Override
            public Collection<?> getAgrupamento(IGeraFrequenciaTransfer relatorioTransfer, Collection<SaidaPadrao> collection) {
                return validarAgrupamento(relatorioTransfer, collection);
            }

            @Override
            public void configurarParametros(GeraFrequenciaStatusTransfer statusTransfer, IGeraFrequenciaTransfer frequenciaTransfer) {
            }

            @Override
            public void configurarEntradaApi(EntradaApi entradaApi) {
            }
            
            @Override
            public CONSTANTES.Enum_FUNCIONALIDADE getFuncionalidadeAuditoria() {
                return CONSTANTES.Enum_FUNCIONALIDADE.REL_CAD_EMPRESA;
            }
            

        },
        CAD_FUNCIONARIO {

            @Override
            public String getTemplate(IGeraFrequenciaTransfer relatorioTransfer) {
                return ((RelatorioCadastroFuncionarioTransfer) relatorioTransfer).isRelatorioDetalhado()
                        ? "cadastros/relatorioCadFuncionarioDetalhes" : "cadastros/relatorioCadFuncionario";
            }

            @Override
            public Collection<?> getAgrupamento(IGeraFrequenciaTransfer relatorioTransfer, Collection<SaidaPadrao> collection) {
                return validarAgrupamento(relatorioTransfer, collection);
            }

            @Override
            public void configurarParametros(GeraFrequenciaStatusTransfer statusTransfer, IGeraFrequenciaTransfer frequenciaTransfer) {
            }

            @Override
            public void configurarEntradaApi(EntradaApi entradaApi) {
            }
            
            @Override
            public CONSTANTES.Enum_FUNCIONALIDADE getFuncionalidadeAuditoria() {
                return CONSTANTES.Enum_FUNCIONALIDADE.REL_CAD_FUNCIONARIO;
            }

        },
        CAD_CALENDARIO {

            @Override
            public String getTemplate(IGeraFrequenciaTransfer relatorioTransfer) {
                return "cadastros/relatorioCadCalendario";
            }

            @Override
            public Collection<?> getAgrupamento(IGeraFrequenciaTransfer relatorioTransfer, Collection<SaidaPadrao> collection) {
                return validarAgrupamento(relatorioTransfer, collection);
            }

            @Override
            public void configurarParametros(GeraFrequenciaStatusTransfer statusTransfer, IGeraFrequenciaTransfer frequenciaTransfer) {
            }

            @Override
            public void configurarEntradaApi(EntradaApi entradaApi) {
            }
            
            @Override
            public CONSTANTES.Enum_FUNCIONALIDADE getFuncionalidadeAuditoria() {
                return CONSTANTES.Enum_FUNCIONALIDADE.REL_CAD_CALENDARIO;
            }

        },
        CAD_HORARIO {

            @Override
            public String getTemplate(IGeraFrequenciaTransfer relatorioTransfer) {
                return "cadastros/relatorioCadHorario";
            }

            @Override
            public Collection<?> getAgrupamento(IGeraFrequenciaTransfer relatorioTransfer, Collection<SaidaPadrao> collection) {
                return validarAgrupamento(relatorioTransfer, collection);
            }

            @Override
            public void configurarParametros(GeraFrequenciaStatusTransfer statusTransfer, IGeraFrequenciaTransfer frequenciaTransfer) {
            }

            @Override
            public void configurarEntradaApi(EntradaApi entradaApi) {
            }
            
            @Override
            public CONSTANTES.Enum_FUNCIONALIDADE getFuncionalidadeAuditoria() {
                return CONSTANTES.Enum_FUNCIONALIDADE.REL_CAD_HORARIO;
            }
        },
        CAD_BANCO_DE_HORAS {

            @Override
            public String getTemplate(IGeraFrequenciaTransfer relatorioTransfer) {
                return "cadastros/relatorioCadBancoHoras";
            }

            @Override
            public Collection<?> getAgrupamento(IGeraFrequenciaTransfer relatorioTransfer, Collection<SaidaPadrao> collection) {
                return validarAgrupamento(relatorioTransfer, collection);
            }

            @Override
            public void configurarParametros(GeraFrequenciaStatusTransfer statusTransfer, IGeraFrequenciaTransfer frequenciaTransfer) {
            }

            @Override
            public void configurarEntradaApi(EntradaApi entradaApi) {
            }
            
            @Override
            public CONSTANTES.Enum_FUNCIONALIDADE getFuncionalidadeAuditoria() {
                return CONSTANTES.Enum_FUNCIONALIDADE.REL_CAD_BANCO_HORAS;
            }
        },
        CAD_AFASTAMENTO {

            @Override
            public String getTemplate(IGeraFrequenciaTransfer relatorioTransfer) {
                return "cadastros/relatorioCadAfastamento";
            }

            @Override
            public Collection<?> getAgrupamento(IGeraFrequenciaTransfer relatorioTransfer, Collection<SaidaPadrao> collection) {
                return validarAgrupamento(relatorioTransfer, collection);
            }

            @Override
            public void configurarParametros(GeraFrequenciaStatusTransfer statusTransfer, IGeraFrequenciaTransfer frequenciaTransfer) {
            }

            @Override
            public void configurarEntradaApi(EntradaApi entradaApi) {
            }
            
            @Override
            public CONSTANTES.Enum_FUNCIONALIDADE getFuncionalidadeAuditoria() {
                return CONSTANTES.Enum_FUNCIONALIDADE.REL_AFASTAMENTO;
            }
        }, FECHAMENTO {
            @Override
            public Collection<?> getAgrupamento(IGeraFrequenciaTransfer relatorioTransfer, Collection<SaidaPadrao> collection) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public String getTemplate(IGeraFrequenciaTransfer relatorioTransfer) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void configurarParametros(GeraFrequenciaStatusTransfer statusTransfer, IGeraFrequenciaTransfer frequenciaTransfer) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void configurarEntradaApi(EntradaApi entradaApi) {
                entradaApi.setProcessaCalculo(true);
                entradaApi.setProcessaAFDT(false);
                entradaApi.setProcessaACJEF(false);
                entradaApi.setProcessaDSR(true);
                entradaApi.setProcessaAbsenteismo(false);
                entradaApi.setProcessaManutencaoMarcacoes(false);
            }
            
            @Override
            public CONSTANTES.Enum_FUNCIONALIDADE getFuncionalidadeAuditoria() {
                return null;
            }

        }, 
        AFDT{
            @Override
            public Collection<?> getAgrupamento(IGeraFrequenciaTransfer apiTransfer, Collection<SaidaPadrao> collection) {
                return validarAgrupamento(apiTransfer, collection);
            }

            @Override
            public String getTemplate(IGeraFrequenciaTransfer relatorioTransfer) {
                return "";
            }

            @Override
            public void configurarParametros(GeraFrequenciaStatusTransfer statusTransfer, IGeraFrequenciaTransfer frequenciaTransfer) {
            }

            @Override
            public void configurarEntradaApi(EntradaApi entradaApi) {
                entradaApi.setProcessaCalculo(false);
                entradaApi.setProcessaAFDT(true);
                entradaApi.setProcessaACJEF(false);
                entradaApi.setProcessaDSR(false);
                entradaApi.setProcessaAbsenteismo(false);
                entradaApi.setProcessaManutencaoMarcacoes(false);
            }
            
            @Override
            public CONSTANTES.Enum_FUNCIONALIDADE getFuncionalidadeAuditoria() {
                return CONSTANTES.Enum_FUNCIONALIDADE.ARQ_AFDT;
            }
        },
        ACJEF{
            @Override
            public Collection<?> getAgrupamento(IGeraFrequenciaTransfer apiTransfer, Collection<SaidaPadrao> collection) {
                return validarAgrupamento(apiTransfer, collection);
            }

            @Override
            public String getTemplate(IGeraFrequenciaTransfer relatorioTransfer) {
                return "";
            }

            @Override
            public void configurarParametros(GeraFrequenciaStatusTransfer statusTransfer, IGeraFrequenciaTransfer frequenciaTransfer) {
            }

            @Override
            public void configurarEntradaApi(EntradaApi entradaApi) {
                entradaApi.setProcessaCalculo(true);
                entradaApi.setProcessaAFDT(false);
                entradaApi.setProcessaACJEF(true);
                entradaApi.setProcessaDSR(false);
                entradaApi.setProcessaAbsenteismo(false);
                entradaApi.setProcessaManutencaoMarcacoes(false);
            }
            
            @Override
            public CONSTANTES.Enum_FUNCIONALIDADE getFuncionalidadeAuditoria() {
                return CONSTANTES.Enum_FUNCIONALIDADE.ARQ_ACJEF;
            }
        }, FECHAMENTO_BANCO_HORAS {
            @Override
            public Collection<?> getAgrupamento(IGeraFrequenciaTransfer relatorioTransfer, Collection<SaidaPadrao> collection) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public String getTemplate(IGeraFrequenciaTransfer relatorioTransfer) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void configurarParametros(GeraFrequenciaStatusTransfer statusTransfer, IGeraFrequenciaTransfer frequenciaTransfer) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void configurarEntradaApi(EntradaApi entradaApi) {
                entradaApi.setProcessaCalculo(true);
                entradaApi.setProcessaAFDT(false);
                entradaApi.setProcessaACJEF(false);
                entradaApi.setProcessaDSR(false);
                entradaApi.setProcessaAbsenteismo(false);
                entradaApi.setProcessaManutencaoMarcacoes(false);
            }
            
            @Override
            public CONSTANTES.Enum_FUNCIONALIDADE getFuncionalidadeAuditoria() {
                return null;
            }

        }, MANUTENCAO_MARCACOES {
            @Override
            public Collection<?> getAgrupamento(IGeraFrequenciaTransfer relatorioTransfer, Collection<SaidaPadrao> collection) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public String getTemplate(IGeraFrequenciaTransfer relatorioTransfer) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void configurarParametros(GeraFrequenciaStatusTransfer statusTransfer, IGeraFrequenciaTransfer frequenciaTransfer) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void configurarEntradaApi(EntradaApi entradaApi) {
                entradaApi.setProcessaCalculo(true);
                entradaApi.setProcessaAFDT(false);
                entradaApi.setProcessaACJEF(false);
                entradaApi.setProcessaDSR(false);
                entradaApi.setProcessaAbsenteismo(false);
                entradaApi.setProcessaManutencaoMarcacoes(true);
            }

            @Override
            public CONSTANTES.Enum_FUNCIONALIDADE getFuncionalidadeAuditoria() {
                return null;
            }
        };


        /**
         * Carrega a coleção de acordo com o tipo de agrupamento existente no
         * SUB_TIPO_RELATORIO<br>
         * Exitem 3 tipos de Agrupamento<br>
         * - INDIIDUAL<br>
         * - DEPARTAMENTO<br>
         * - EMPRESA<br>
         *
         * @param relatorioTransfer contendo o tipo de agrupamento
         * @param collection lista para ser agrupada
         * @return Collection<?>
         */
        public abstract Collection<?> getAgrupamento(IGeraFrequenciaTransfer relatorioTransfer, Collection<SaidaPadrao> collection);

        /**
         * Retorna o template do jasper que será utilizado para gerar o
         * relatório
         *
         * @param relatorioTransfer
         * @return String contendo o nome do template
         */
        public abstract String getTemplate(IGeraFrequenciaTransfer relatorioTransfer);

        public abstract void configurarParametros(GeraFrequenciaStatusTransfer statusTransfer, IGeraFrequenciaTransfer frequenciaTransfer);

        public abstract void configurarEntradaApi(EntradaApi entradaApi);

        public abstract CONSTANTES.Enum_FUNCIONALIDADE getFuncionalidadeAuditoria();
        
    }

    /**
     * Tipos de formatos de arquivos
     */
    public enum SUB_TIPO_RELATORIO {

        INDIVIDUAL("INDIVIDUAL", "Individual"),
        DEPARTAMENTO("DEPARTAMENTO", "Resumido por departamento"),
        EMPRESA("EMPRESA", "Resumido por empresa"),
        EMPRESA_DEPARTAMENTO("EMPRESA_Departamento", "Resumido por empresa e por departamento"),
        SALDO_FUNCIONARIO("SALDO_FUNCIONARIO", "Resumo de saldo por funcionário"),
        SALDO_FUNCIONARIO_LIMITE("SALDO_FUNCIONARIO_LIMITE", "Resumo dos limites de saldo por funcionário");

        private final String idTipoRelatorio;
        private final String descricao;

        private SUB_TIPO_RELATORIO(String idTipoRelatorio, String descricao) {
            this.descricao = descricao;
            this.idTipoRelatorio = idTipoRelatorio;
        }

        public String getDescricao() {
            return descricao;
        }

        public String getIdTipoRelatorio() {
            return idTipoRelatorio;
        }

        public static SubTipoRelatorio getFormatoTransfer(String idTipoRelatorio) {
            for (SUB_TIPO_RELATORIO eTipoRelatorio : values()) {
                if (eTipoRelatorio.idTipoRelatorio.equalsIgnoreCase(idTipoRelatorio)) {
                    return new SubTipoRelatorio(eTipoRelatorio);
                }
            }
            return new SubTipoRelatorio();
        }

    }

    /**
     * Realiza o agrupamento da coleção por departamento
     *
     * @param collection<SaidaApiPadrao> para ser agrupada
     * @return collection<SaidaApiPadraoDepartamento> agrupada
     */
    private static Collection<?> agruparPorDepartamento(Collection<SaidaPadrao> collection) {
        List<SaidaPadraoDepartamento> saidaApiPadraoDepartamentoList = new ArrayList<>();
        collection
                .stream()
                .collect(Collectors.groupingBy(a -> a.getFuncionario().getDepartamento() != null
                        ? a.getFuncionario().getDepartamento() : new Departamento()))
                .forEach((Departamento d, List<SaidaPadrao> s) -> {
                    saidaApiPadraoDepartamentoList.add(new SaidaPadraoDepartamento(d, s));
                });
        return saidaApiPadraoDepartamentoList;
    }

    /**
     * Realiza o agrupamento da coleção por empresa
     *
     * @param collection<SaidaApiPadrao> para ser agrupada
     * @return collection<SaidaApiPadraoEmpresa> agrupada
     */
    private static Collection<?> agruparPorEmpesa(Collection<SaidaPadrao> collection) {
        List<SaidaPadraoEmpresa> saidaApiPadraoEmpresaList = new ArrayList<>();
        collection
                .stream()
                .collect(Collectors.groupingBy(a -> a.getFuncionario().getDepartamento() != null
                        ? a.getFuncionario().getDepartamento().getEmpresa() : new Empresa()))
                .forEach((Empresa e, List<SaidaPadrao> s) -> {
                    saidaApiPadraoEmpresaList.add(new SaidaPadraoEmpresa(e, s));
                });
        return saidaApiPadraoEmpresaList;
    }

    /**
     * Realiza o agrupamento da coleção por departamento e depois agrupa por
     * empresa
     *
     * @param collection<SaidaApiPadrao> para ser agrupada
     * @return collection<SaidaApiPadraoEmpresa> agrupada
     */
    private static Collection<?> agruparPorEmpesaDepartamento(Collection<SaidaPadrao> collection) {
        List<SaidaPadraoEmpresaDepartamento> saidaPadraoEmpresaDepartamentoList = new ArrayList<>();
        List<SaidaPadraoDepartamento> saidaPadraoDepartamentoList = (List<SaidaPadraoDepartamento>) agruparPorDepartamento(collection);

        saidaPadraoDepartamentoList.stream().collect(Collectors
                .groupingBy(dep -> dep.getDepartamento() != null ? dep.getDepartamento().getEmpresa() : new Empresa()))
                .forEach((Empresa emp, List<SaidaPadraoDepartamento> list) -> {
                    saidaPadraoEmpresaDepartamentoList.add(new SaidaPadraoEmpresaDepartamento(emp, list));
                });

        return saidaPadraoEmpresaDepartamentoList;
    }

    /**
     * Verifica qual será o tipo de agrupamento, por padrão o agrupamento sempre
     * sera por funcionário(INDIVIDUAL)
     *
     * @param relatorioTransfer contendo os dados de SUB_TIPO_RELATORIO
     * @param collection<SaidaApiPadrao> para ser agrupada
     * @return collection<?> agrudapa
     */
    private static Collection<?> validarAgrupamento(IGeraFrequenciaTransfer relatorioTransfer, Collection<SaidaPadrao> collection) {
        RelatoriosGeraFrequenciaTransfer apiTransfer = (RelatoriosGeraFrequenciaTransfer) relatorioTransfer;
        if (apiTransfer.getTipoRelatorio() != null) {
            if ((apiTransfer.getTipoRelatorio().getIdTipoRelatorio().equals(SUB_TIPO_RELATORIO.DEPARTAMENTO.getIdTipoRelatorio()))) {
                return agruparPorDepartamento(collection);
            } else if (apiTransfer.getTipoRelatorio().getIdTipoRelatorio().equals(SUB_TIPO_RELATORIO.EMPRESA.getIdTipoRelatorio())) {
                return agruparPorEmpesa(collection);
            } else if (apiTransfer.getTipoRelatorio().getIdTipoRelatorio().equals(SUB_TIPO_RELATORIO.EMPRESA_DEPARTAMENTO.getIdTipoRelatorio())
                    || apiTransfer.getTipoRelatorio().getIdTipoRelatorio().equals(SUB_TIPO_RELATORIO.SALDO_FUNCIONARIO.getIdTipoRelatorio())
                    || apiTransfer.getTipoRelatorio().getIdTipoRelatorio().equals(SUB_TIPO_RELATORIO.SALDO_FUNCIONARIO_LIMITE.getIdTipoRelatorio())) {
                return agruparPorEmpesaDepartamento(collection);
            }
        }

        return collection;
    }

}
