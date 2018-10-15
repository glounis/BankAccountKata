package com.bank.kata.test;

import static junit.framework.Assert.assertEquals;

import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import com.bank.kata.business.Account;
import com.bank.kata.business.Transaction;
import com.bank.kata.services.AccountService;
import com.bank.kata.services.TransactionService;
import com.bank.kata.utils.BankAccountConstant;
import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.ExpectedDatabase;



@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext.xml" })
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DbUnitTestExecutionListener.class })
public class AccountServiceTest {

	@Autowired
	private AccountService accountService;
	
	@Autowired
	private TransactionService transactionService;
	
	private Account account;

	@Test
	@DatabaseSetup("/sampleData.xml")
	public void testFind() throws Exception {
		 account = accountService.findByPinCode(123);
		assertEquals("123ABC", account.getCountNumer());
	}
	
	@Test
	@DatabaseSetup("/sampleData.xml")
	public void testTransaction() {
		Transaction transaction = new Transaction();
		 account = accountService.findByPinCode(123);
		transaction.setAccount(account);
		transaction.setAmount(550);
		transaction.setTransactionDate(new Date());
		transaction.setTransactionType(BankAccountConstant.TRANSACTTION_TYPE_DEPOT);
		
		boolean accepted = transactionService.addTransaction(transaction);
		assertEquals(accepted, true);
		
	}
}
