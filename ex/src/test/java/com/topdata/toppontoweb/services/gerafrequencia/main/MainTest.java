package com.topdata.toppontoweb.services.gerafrequencia.main;

import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Cenários de testes do código da API, toda vez que for rodar validar os
 * códigos e regras, validar somente um dia por cenário, será possível
 * visualizar o local que apresentou falha, caso algum cálculo não esteja
 * atendendo o resultado esperado
 *
 *************************************************************
 * @author enio.junior
 */
public class MainTest extends TestCase {

    public MainTest() {
    }

    public static TestSuite suite() {
        TestSuite suite = new TestSuite(MainTest.class);
        return suite;
    }

    public void testCenario01() {
        TestCenario01.suite();
    }

    public void testCenario02() {
        TestCenario02.suite();
    }
    
}
