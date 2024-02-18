package org.tuto.vertx;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.openapi.RouterBuilder;
import io.vertx.serviceproxy.ServiceBinder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tuto.vertx.persistence.TransactionPersistence;
import org.tuto.vertx.services.TransactionManagerService;

/**
 * Hello world!
 *
 */
public class App extends AbstractVerticle {
    HttpServer server;
    ServiceBinder serviceBinder;
    MessageConsumer<JsonObject> consumer;
    Logger log = LoggerFactory.getLogger(this.getClass());

    private void startTransactionService() {
        serviceBinder = new ServiceBinder(vertx);
        TransactionPersistence persistence = TransactionPersistence.create();

        TransactionManagerService transactionManagerService = TransactionManagerService.create(persistence);
        consumer = serviceBinder.setAddress("transactions_manager.myapp")
                .register(TransactionManagerService.class, transactionManagerService);
    }

    private Future<Void> startHttpServer() {
        return RouterBuilder.create(this.vertx, "openapi.json")
                .onFailure(Throwable::printStackTrace)
                .compose(routerBuilder -> {
                   routerBuilder.mountServicesFromExtensions();
                   Router router = routerBuilder.createRouter();
                   router.errorHandler(400, ctx -> {
                      log.debug("Bad Request", ctx.failure());
                   });
                   server = vertx.createHttpServer(new HttpServerOptions()
                           .setPort(8080)
                           .setHost("localhost"));
                   server.requestHandler(router);
                   return server.listen().mapEmpty();
                });
    }

    @Override
    public void start(Promise<Void> promise) {
        startTransactionService();
        startHttpServer().onComplete(promise);
    }

    /**
     * This method closes the http server and unregister all services loaded to Event Bus
     */
    @Override
    public void stop(){
        this.server.close();
        consumer.unregister();
    }

    public static void main( String[] args ) {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new App());
    }
}
