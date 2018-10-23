package com.bank.kata.services;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bank.kata.business.Account;
import com.bank.kata.business.Transaction;
import com.bank.kata.utils.BankAccountConstant;

@Service
@Transactional
public class TransactionService {
	
	@PersistenceContext
	EntityManager em;
	
	public boolean addTransaction(Transaction transaction) {		
		if(transaction.getTransactionType().equals(BankAccountConstant.TRANSACTTION_TYPE_DEPOSIT)) {
			if(transaction.getAmount() < 0) {
				return false; 
			}else {
				Account account = transaction.getAccount();
				account.setBalance(account.getBalance() + transaction.getAmount());
		        em.merge(account);
				transaction.setAccount(account);
				em.merge(transaction);
				return true;
			}
		}else {
			Account account = transaction.getAccount();
			//test if there is enough balance in the account
			if(account.getBalance() < transaction.getAmount()) {
				return false;
			}			
			account.setBalance(account.getBalance() - transaction.getAmount());
	        em.merge(account);
			transaction.setAccount(account);
			em.merge(transaction);
			return true;
		}
	}
	
	
	public List<Transaction> getTransactionFromAccountPin(int pinCode){
		List<Transaction> transactions = new ArrayList<Transaction>();
		
		Query query = em.createQuery("From Transaction t where t.account.pinCode = :pinCode", Transaction.class);
		query.setParameter("pinCode", pinCode);
		
		transactions =  query.getResultList();
		
		return  transactions;
	}

}
