package com.bank.kata.services;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

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
			em.merge(transaction);
			return true;
		}
	}

}
