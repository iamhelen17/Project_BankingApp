package com.bagonationalbank.app.service.test;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import org.apache.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import com.bagonationalbank.app.service.impl.BankingOperationsServiceImpl;



//@RunWith attaches a runner with the test class to initialize the test data
@RunWith(MockitoJUnitRunner.class)

class BankingOperationsServiceImplTest {
	private static Logger log = Logger.getLogger(BankingOperationsServiceImplTest.class);
	
	
	@InjectMocks 
	private BankingOperationsServiceImpl bankService = new BankingOperationsServiceImpl();
	
	@BeforeEach
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void testIsValidGenderForSave() {
		boolean saved = bankService.isValidGender("F");  
		assertEquals(true, saved);
	}
	
	@Test
	public void testIsValidGenderForTrue() {
		boolean saved = bankService.isValidGender("F");  
		assertTrue(saved);
	}
	
	@Test
	public void testIsValidEmailForSave() {
		boolean saved = bankService.isValidEmail("hdggggd11@gmail.com");
		assertEquals(true, saved);
	}
	
	
	@Test
	public void testIsValidEmailForTrue() {
		boolean saved = bankService.isValidEmail("hdggggd11@gmail.com");
		assertTrue(saved);
	}
	
	@Test
	public void testIsValidDateForSave() {
		boolean saved = bankService.isValidDate("12/02/1999");
		assertEquals(true, saved);
	}
	
	
	@Test
	public void testIsValidDateForTrue() {
		boolean saved = bankService.isValidDate("10/12/2000");
		assertTrue(saved);
	}
	
	
	@Test
	public void testDepositFundsReturnsSingleValues() {
		List mocklist = mock(List.class);
		when(mocklist.size()).thenReturn(1);
		
		assertEquals(1, mocklist.size());
		assertEquals(1, mocklist.size());
		
		
		log.info(mocklist.size());
		log.info(mocklist);
	}
	
	@Test
	public void testDepositFundsReturnsaMultipleValues() {
		List mocklist = mock(List.class);
		when(mocklist.size()).thenReturn(1).thenReturn(2).thenReturn(3);
		
		assertEquals(1, mocklist.size());
		assertEquals(2, mocklist.size());
		assertEquals(3, mocklist.size());
		
		log.info(mocklist.size());
		log.info(mocklist);
	}
	
	
	@Test
	public void testDepositFundsForGet() {
		List mocklist = mock(List.class);
		when(mocklist.get(0)).thenReturn(100);
		
		assertEquals(100, mocklist.get(0));
		
		log.info(mocklist.get(0));
	}
	
	
	@Test
	public void testTransaferFundsForGet() {
		List mocklist = mock(List.class);
		when(mocklist.get(0)).thenReturn(200);
		
		assertEquals(200, mocklist.get(0));
		
		log.info(mocklist.get(1));
	}
	
}