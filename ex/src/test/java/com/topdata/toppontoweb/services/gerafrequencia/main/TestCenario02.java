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
public final class TestCenario02 extends TestCase {

    private final DadosSaida dadosSaida;
    
    public TestCenario02() {            
        DadosCenario02.setEntrada();        
        Main.Processar();
        dadosSaida = new DadosSaida();
    }

    public static TestSuite suite() {
        return new TestSuite(TestCenario02.class);
    }
    
    public void testResultado01() {
      //  assertEquals(DadosCenario02.getSaldoAusenciasDiurnas(), dadosSaida.getSaldoAusenciasDiurnas());
    }

    public void testResultado02() {
      //  assertEquals(DadosCenario02.getSaldoAusenciasNoturnas(), dadosSaida.getSaldoAusenciasNoturnas());
    }

}
