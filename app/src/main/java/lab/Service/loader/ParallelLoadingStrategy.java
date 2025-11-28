package lab.Service.loader;

import lab.models.Customer;
import lab.models.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import lab.exceptions.DataSerializationException;

import lab.repository.*;
import lab.Service.LoadResult;

import java.util.concurrent.CompletableFuture;

public class ParallelLoadingStrategy implements LoadingStrategy{

    private static final Logger logger = LoggerFactory.getLogger(ParallelLoadingStrategy.class);

    @Override
    public LoadResult load(
            CustomerRepository customerRepository,
            OrderRepository orderRepository,
            DataLoader dataLoader){

        logger.info("Starting parallel loading with CompletableFuture...");
        long startTime = System.currentTimeMillis();


        CompletableFuture<Integer> Orderfuture = CompletableFuture.supplyAsync(
                () -> loadEntity(dataLoader, Order.class,orderRepository))
                .exceptionally(ex -> handleError("students", ex));

        CompletableFuture<Integer> Customerfuture = CompletableFuture.supplyAsync(
                () -> loadEntity(dataLoader, Customer.class, customerRepository))
                .exceptionally(ex -> handleError("customers", ex));

        long duration = System.currentTimeMillis() - startTime;
        logger.info("Parallel loading completed in {} ms", duration);

        return new LoadResult(
                Orderfuture.join(),
                Customerfuture.join(),
                duration
        );
    }

    private <T> int loadEntity(DataLoader dataLoader, Class<T> clazz, GenericRepository<T> repository) {
        try {
            return dataLoader.loadEntity(clazz, repository);
        } catch (DataSerializationException e) {
            throw new RuntimeException(e);
        }
    }

    private int handleError(String entityType, Throwable ex) {
        logger.error("Failed to load {}: {}", entityType, ex.getMessage());
        return 0;
    }
}
