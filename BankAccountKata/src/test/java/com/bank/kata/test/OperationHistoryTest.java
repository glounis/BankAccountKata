package com.bank.kata.test;

import static junit.framework.Assert.assertEquals;

import java.awt.font.TextAttribute;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

import javax.persistence.NoResultException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import com.bank.kata.business.Account;
import com.bank.kata.business.Client;
import com.bank.kata.business.Transaction;
import com.bank.kata.services.AccountService;
import com.bank.kata.services.TransactionService;
import com.bank.kata.utils.BankAccountConstant;
import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext.xml" })
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DbUnitTestExecutionListener.class })
public class OperationHistoryTest {
	
	@Autowired
	private AccountService accountService;

	@Autowired
	private TransactionService transactionService;

	private Account account;
	
	/**
	 * Method to test interactions between the client and the bank system.
	 */
	@Test
	@DatabaseSetup("/sampleData.xml")
	public void test() {
//	   int i = 0;
//	   String pinCode = "";
//	   while(i<3 && pinCode.isEmpty()) {
//		   i++;
//		   try {
//			   pinCode = enterPinCode(); 
//		   }catch (NumberFormatException e) {
//			// TODO: handle exception
//		}
//		
//	   }
		
		try {
			//login on the account using pinCode...
			account = accountService.findByPinCode(123);
			
			//If the account is founded, the transaction must be done 
			Transaction transaction = new Transaction();
			transaction.setAccount(account);
			transaction.setAmount(550);
			transaction.setTransactionDate(new Date());
			transaction.setTransactionType(BankAccountConstant.TRANSACTTION_TYPE_DEPOSIT);

			boolean accepted = transactionService.addTransaction(transaction);
			assertEquals(accepted, true);
			
			
			//add another transaction of withdrawal type and it will be refused 
			// because there is no more  balance in the account.
			transaction = new Transaction();
			transaction.setAccount(account);
			transaction.setAmount(1250);
			transaction.setTransactionDate(new Date());
			transaction.setTransactionType(BankAccountConstant.TRANSACTTION_TYPE_WITHDRAWAL);
			
			accepted = transactionService.addTransaction(transaction);
			assertEquals(accepted, false);
			
			//add a new withdrawal with a shorter amount  
			transaction = new Transaction();
			transaction.setAccount(account);
			transaction.setAmount(900);
			transaction.setTransactionDate(new Date());
			transaction.setTransactionType(BankAccountConstant.TRANSACTTION_TYPE_WITHDRAWAL);
			
			accepted = transactionService.addTransaction(transaction);
			assertEquals(accepted, true);
			
			//GET all transactions for the account
			List<Transaction> transactions = transactionService.getTransactionFromAccountPin(account.getPinCode());
			displayAccountOperations(transactions);
			
			account = transactions.get(0).getAccount();
	
			
			assertEquals(81.42, account.getBalance(), 0);

		} catch (NoResultException e) {
			assertEquals(account, null);
		}
	}
	
	
	private void displayAccountOperations(List<Transaction> transactions) {
		if(transactions.isEmpty()) {
			System.out.println("There are no transactions in your account !");
		}else {
			Client client = transactions.get(0).getAccount().getClient();
			System.out.println("Client : " + client.getFirstName() + " " + client.getLastName());
			System.out.println("This is the list of transactions in your account ");
			System.out.println("Transaction ID | Transaction amount | Transaction type | Transaction date");
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			for(Transaction transaction : transactions) {
				System.out.println(transaction.getId() + " | " + transaction.getAmount() + " | " +
			 transaction.getTransactionType() + " | " + sdf.format(transaction.getTransactionDate()));
			}
		}
		
	}
}
