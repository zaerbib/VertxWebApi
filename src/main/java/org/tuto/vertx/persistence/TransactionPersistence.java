package org.tuto.vertx.persistence;

import org.tuto.vertx.models.Transaction;
import org.tuto.vertx.persistence.impl.TransactionPersistenceImpl;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public interface TransactionPersistence {

    static TransactionPersistence create() {
        return new TransactionPersistenceImpl();
    }

    List<Transaction> getFilteredTransactions(Predicate<Transaction> p);

    Optional<Transaction> getTransaction(String transactionId);

    Transaction addTransaction(Transaction t);

    boolean removeTransaction(String transactionId);

    boolean updateTransaction(String transactionId, Transaction transaction);
}
