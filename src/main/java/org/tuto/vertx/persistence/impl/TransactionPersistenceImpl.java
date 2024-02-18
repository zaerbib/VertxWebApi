package org.tuto.vertx.persistence.impl;

import org.tuto.vertx.models.Transaction;
import org.tuto.vertx.persistence.TransactionPersistence;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class TransactionPersistenceImpl implements TransactionPersistence {
    private Map<String, Transaction> transactions;

    public TransactionPersistenceImpl() {
        transactions = new HashMap<>();
    }

    @Override
    public List<Transaction> getFilteredTransactions(Predicate<Transaction> p) {
        return transactions.values().stream().filter(p).collect(Collectors.toList());
    }

    @Override
    public Optional<Transaction> getTransaction(String transactionId) {
        return Optional.of(transactions.get(transactionId));
    }

    @Override
    public Transaction addTransaction(Transaction t) {
        transactions.put(t.getId(), t);
        return t;
    }

    @Override
    public boolean removeTransaction(String transactionId) {
        Transaction t = transactions.remove(transactionId);
        return t != null;
    }

    @Override
    public boolean updateTransaction(String transactionId, Transaction transaction) {
        Transaction t = transactions.replace(transactionId, transaction);
        return t != null;
    }
}
