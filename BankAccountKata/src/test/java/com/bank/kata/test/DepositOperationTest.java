package com.bank.kata.test;

import static junit.framework.Assert.assertEquals;

import java.util.Date;

import javax.persistence.NoResultException;

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

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext.xml" })
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DbUnitTestExecutionListener.class })
public class DepositOperationTest {

	@Autowired
	private AccountService accountService;

	@Autowired
	private TransactionService transactionService;

	private Account account;

	/**
	 * Before doing any operation, the client will login on its account with a pin
	 * code, like bank terminals. if the pinCode is not OK, he could not do any
	 * thing.
	 */
	@Test
	@DatabaseSetup("/sampleData.xml")
	public void testAccountFound() {
		account = accountService.findByPinCode(123);
		assertEquals("123ABC", account.getCountNumer());
	}

	@Test
	@DatabaseSetup("/sampleData.xml")
	public void testAccountNotFound() {
		try {
			account = accountService.findByPinCode(1235);
			assertEquals("123ABC", account.getCountNumer());
		} catch (NoResultException e) {
			assertEquals(account, null);
		}
	}

	@Test
	@DatabaseSetup("/sampleData.xml")
	public void testDepositOperationOK() {
		try {
			//login on the account using pinCode...
			account = accountService.findByPinCode(123);
			
			//If the account is founded, the transaction must be done 
			Transaction transaction = new Transaction();
			transaction.setAccount(account);
			transaction.setAmount(550);
			transaction.setTransactionDate(new Date());
			transaction.setTransactionType(BankAccountConstant.TRANSACTTION_TYPE_DEPOT);

			boolean accepted = transactionService.addTransaction(transaction);
			assertEquals(accepted, true);

		} catch (NoResultException e) {
			assertEquals(account, null);
		}

	}
	
	@Test
	@DatabaseSetup("/sampleData.xml")
	public void testDepositOperationFailed() {
		try {
			//login on the account using pinCode...
			account = accountService.findByPinCode(123);
			
			//If the account is founded, the transaction must be done 
			Transaction transaction = new Transaction();
			transaction.setAccount(account);
			transaction.setAmount(-550);
			transaction.setTransactionDate(new Date());
			transaction.setTransactionType(BankAccountConstant.TRANSACTTION_TYPE_DEPOT);

			boolean accepted = transactionService.addTransaction(transaction);
			assertEquals(accepted, false);

		} catch (NoResultException e) {
			assertEquals(account, null);
		}

	}
}
