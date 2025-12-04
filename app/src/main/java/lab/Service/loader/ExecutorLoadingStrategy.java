package lab.Service.loader;

import lab.models.Customer;
import lab.models.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import lab.exceptions.DataSerializationException;

import lab.repository.*;
import lab.Service.LoadResult;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ExecutorLoadingStrategy implements LoadingStrategy, LoadingStrategyNew {
    private static final Logger logger = LoggerFactory.getLogger(ExecutorLoadingStrategy.class);

    private final int threadPoolSize;

    public ExecutorLoadingStrategy(int threadPoolSize) {
        this.threadPoolSize = threadPoolSize;
    }

    public ExecutorLoadingStrategy() {
        this(4);
    }

    @Override
    public LoadResult load(DeliveryRepository deliveryRepository, RestaurantRepository restaurantRepository, DataLoader dataLoader) {
        logger.info("Starting loading with ExecutorService (pool size: {})...", threadPoolSize);

        long startTime = System.currentTimeMillis();
        ExecutorService executor = Executors.newFixedThreadPool(threadPoolSize);

        try {
            CompletableFuture<Integer> deliveryFuture = CompletableFuture
                    .supplyAsync(() -> loadEntity(dataLoader, lab.models.Delivery.class, deliveryRepository), executor);

            CompletableFuture<Integer> restaurantFuture = CompletableFuture
                    .supplyAsync(() -> loadEntity(dataLoader, lab.models.Restaurant.class, restaurantRepository), executor);
//            System.out.println(restaurantRepository);
            long duration = System.currentTimeMillis() - startTime;
            logger.info("ExecutorService loading completed in {} ms", duration);

            return new LoadResult(
                    deliveryFuture.join(),
                    restaurantFuture.join(),
                    duration
            );
        } finally {
            shutdownExecutor(executor);
        }
    }

    @Override
    public LoadResult load(
            CustomerRepository customerRepository,
            OrderRepository orderRepository,
            DataLoader dataLoader){
        logger.info("Starting loading with ExecutorService (pool size: {})...", threadPoolSize);

        long startTime = System.currentTimeMillis();
        ExecutorService executor = Executors.newFixedThreadPool(threadPoolSize);

        try{
            CompletableFuture<Integer> orderFuture = CompletableFuture
                    .supplyAsync(() -> loadEntity(dataLoader, Order.class,orderRepository), executor);

            CompletableFuture<Integer> customerFuture = CompletableFuture
                    .supplyAsync(() -> loadEntity(dataLoader, Customer.class, customerRepository), executor);

            long duration = System.currentTimeMillis() - startTime;
            logger.info("ExecutorService loading completed in {} ms", duration);

            return new LoadResult(
                    orderFuture.join(),
                    customerFuture.join(),
                    duration
            );
        }
        finally {
            shutdownExecutor(executor);
        }
    }

    private <T> int loadEntity(DataLoader dataLoader, Class<T> clazz, GenericRepository<T> repository) {
        try {
            return dataLoader.loadEntity(clazz, repository);
        } catch (DataSerializationException e) {
            throw new RuntimeException(e);
        }
    }

    private void shutdownExecutor(ExecutorService executor) {
        executor.shutdown();
        try {
            if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }


}
