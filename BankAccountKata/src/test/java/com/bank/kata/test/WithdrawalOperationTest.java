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
public class WithdrawalOperationTest {
	
	@Autowired
	private AccountService accountService;

	@Autowired
	private TransactionService transactionService;

	private Account account;
	
	@Test
	@DatabaseSetup("/sampleData.xml")
	public void withdrawalOperationOK() {
		try {
			//login on the account using pinCode...
			account = accountService.findByPinCode(123);
			
			//If the account is founded, the transaction must be done 
			Transaction transaction = new Transaction();
			transaction.setAccount(account);
			transaction.setAmount(431);
			transaction.setTransactionDate(new Date());
			transaction.setTransactionType(BankAccountConstant.TRANSACTTION_TYPE_WITHDRAWAL);

			boolean accepted = transactionService.addTransaction(transaction);
			assertEquals(accepted, true);

		} catch (NoResultException e) {
			assertEquals(account, null);
		}
	}
	
	@Test
	@DatabaseSetup("/sampleData.xml")
	public void withdrawalOperationKO() {
		try {
			//login on the account using pinCode...
			account = accountService.findByPinCode(123);
			
			//If the account is founded, the transaction must be done 
			Transaction transaction = new Transaction();
			transaction.setAccount(account);
			transaction.setAmount(431.5);
			transaction.setTransactionDate(new Date());
			transaction.setTransactionType(BankAccountConstant.TRANSACTTION_TYPE_WITHDRAWAL);

			boolean accepted = transactionService.addTransaction(transaction);
			assertEquals(accepted, false);

		} catch (NoResultException e) {
			assertEquals(account, null);
		}
	}

}
