package org.tuto.vertx.services;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.ext.web.api.service.ServiceRequest;
import io.vertx.ext.web.api.service.ServiceResponse;
import io.vertx.ext.web.api.service.WebApiServiceGen;
import org.tuto.vertx.models.Transaction;
import org.tuto.vertx.persistence.TransactionPersistence;
import org.tuto.vertx.services.impl.TransactionManagerServiceImpl;

import java.util.List;

@WebApiServiceGen
public interface TransactionManagerService {

    static TransactionManagerService create(TransactionPersistence persistence) {
        return new TransactionManagerServiceImpl(persistence);
    }

    void getTransactionsList(
            List<String> from,
            List<String> to,
            List<String> message,
            ServiceRequest request, Handler<AsyncResult<ServiceResponse>> resultHandler);

    void createTransaction(
            Transaction body,
            ServiceRequest request, Handler<AsyncResult<ServiceResponse>> resultHandler);

    void getTransaction(
            String transactionId,
            ServiceRequest request, Handler<AsyncResult<ServiceResponse>> resultHandler);

    void updateTransaction(
            String transactionId,
            Transaction body,
            ServiceRequest request, Handler<AsyncResult<ServiceResponse>> resultHandler);

    void deleteTransaction(
            String transactionId,
            ServiceRequest request, Handler<AsyncResult<ServiceResponse>> resultHandler);
}
