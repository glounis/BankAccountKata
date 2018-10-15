package com.bank.kata.services;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bank.kata.business.Account;


@Service
@Transactional
public class AccountService {
	
	@PersistenceContext
	private EntityManager entityManager;
	
	public Account findByPinCode(int pinCode) {
		Account acount = null;
		Query query = entityManager.createQuery("FROM Account a where a.pinCode = :pinCode", Account.class);
		query.setParameter("pinCode", pinCode);
		 try {
		acount = (Account) query.getSingleResult();
		 }catch(NoResultException re) {
			 
		 }
		return acount;
	}

}
