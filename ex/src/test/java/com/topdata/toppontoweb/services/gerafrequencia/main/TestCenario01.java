package com.topdata.toppontoweb.services.gerafrequencia.main;

import com.topdata.toppontoweb.services.gerafrequencia.main.Main;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.junit.Ignore;

/**
 *
 * @author enio.junior
 */
@Ignore
public final class TestCenario01 extends TestCase {

    private final DadosSaida dadosSaida;
    
    public TestCenario01() {            
        DadosCenario01.setEntrada();        
        Main.Processar();
        dadosSaida = new DadosSaida();
    }

    public static TestSuite suite() {
        return new TestSuite(TestCenario01.class);
    }
    
    public void testResultado01() {
        assertEquals(DadosCenario01.getSaldoNormaisDiurnas(), dadosSaida.getSaldoNormaisDiurnas());
    }

    public void testResultado02() {
        assertEquals(DadosCenario01.getSaldoNormaisNoturnas(), dadosSaida.getSaldoNormaisNoturnas());
    }
    
    public void testResultado03() {
        assertEquals(DadosCenario01.getSaldoExtrasDiurnas(), dadosSaida.getSaldoExtrasDiurnas());
    }

    public void testResultado04() {
        assertEquals(DadosCenario01.getSaldoExtrasNoturnas(), dadosSaida.getSaldoExtrasNoturnas());
    }
    
    public void testResultado05() {
       // assertEquals(DadosCenario01.getSaldoAusenciasDiurnas(), dadosSaida.getSaldoAusenciasDiurnas());
    }

    public void testResultado06() {
       // assertEquals(DadosCenario01.getSaldoAusenciasNoturnas(), dadosSaida.getSaldoAusenciasNoturnas());
    }
    
    public void testResultado07() {
        assertEquals(DadosCenario01.getSaldoDiaBH(), dadosSaida.getSaldoDiaBH());
    }

 }
