package com.dbiagi.authorizer.repository

import com.dbiagi.authorizer.model.Transaction
import org.springframework.data.repository.CrudRepository

interface TransactionRepository : CrudRepository<Transaction, Int>
