/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.topdata.toppontoweb.services.gerafrequencia.services.calculo;

import com.topdata.toppontoweb.services.gerafrequencia.entity.calculo.EntradaApi;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.topdata.toppontoweb.services.gerafrequencia.entity.calculo.SaidaDia;
import com.topdata.toppontoweb.services.gerafrequencia.main.DadosCenario01;
import com.topdata.toppontoweb.services.gerafrequencia.main.DadosSaida;
import com.topdata.toppontoweb.services.gerafrequencia.main.Main;

/**
 *
 * @author juliano.ezequiel
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
        locations = {"file:src/main/webapp/WEB-INF/context.xml"})
public class CalculoPeriodoServiceTest {
    
    public CalculoPeriodoServiceTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of getSaidaAPI method, of class CalculoPeriodoService.
     */
    @Test
    public void testGetSaidaAPI() {
        
//        DadosCenario01.setEntrada();;
//        Main.Processar();
//                
//        System.out.println("getSaidaAPI");
//        CalculoPeriodoService calculoPeriodoService = new CalculoPeriodoService(DadosCenario01.get);
//        List<SaidaDia> expResult = "seu rtesultado";
//        List<SaidaDia> result = calculoPeriodoService.getSaidaAPI();
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }

    /**
     * Test of isValido method, of class CalculoPeriodoService.
     */
    @Test
    public void testIsValido() {
        System.out.println("isValido");
        CalculoPeriodoService instance = null;
        boolean expResult = false;
        boolean result = instance.isValido();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
