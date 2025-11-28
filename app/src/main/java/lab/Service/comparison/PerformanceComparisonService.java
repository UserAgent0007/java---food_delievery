package lab.Service.comparison;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import lab.models.Order;
import lab.models.Customer;
import lab.repository.*;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class PerformanceComparisonService {
    private static final Logger logger = LoggerFactory.getLogger(PerformanceComparisonService.class);

    public ComparisonResult compareOrderFiltering(
        OrderRepository repository, LocalDate date
    ){
        long startSequential = System.currentTimeMillis();
        List<Order> sequentialResult = repository.getAll().stream()
            .filter(o -> o.getOrderDate().isEqual(date))
            .collect(Collectors.toList());
        long sequentialTime = System.currentTimeMillis() - startSequential;

        long startParallel = System.currentTimeMillis();
        List<Order> parallelResult = repository.getAll().parallelStream()
            .filter(o -> o.getOrderDate().isEqual(date))
            .collect(Collectors.toList());

        long parallelTime = System.currentTimeMillis() - startParallel;

        long startExecutor = System.currentTimeMillis();
        ExecutorService executor = Executors.newFixedThreadPool(4);
        try {
            List<Order> allOrders = repository.getAll();
            int chunkSize = Math.max(1, allOrders.size() / 4);

            List<CompletableFuture<List<Order>>> futures = new ArrayList<>();

            for (int i = 0; i < allOrders.size(); i += chunkSize) {
                int start = i;
                int end = Math.min(i + chunkSize, allOrders.size());
                List<Order> chunk = allOrders.subList(start, end);

                CompletableFuture<List<Order>> future = CompletableFuture.supplyAsync(() ->
                                chunk.stream()
                                        .filter(o -> o.getOrderDate().isEqual(date))
                                        .collect(Collectors.toList()),
                        executor
                );

                futures.add(future);
            }

            List<Order> executorResult = futures.stream()
                    .map(CompletableFuture::join)
                    .flatMap(List::stream)
                    .collect(Collectors.toList());

        } finally {
            executor.shutdown();
        }
        long executorTime = System.currentTimeMillis() - startExecutor;

        ComparisonResult result = new ComparisonResult(
                "Order Filtering by Date",
                sequentialResult.size(),
                sequentialTime,
                parallelTime,
                executorTime
        );

        logger.info("Comparison completed: {}", result);
        return result;
    }

    public ComparisonResult compareCustomerFiltering (CustomerRepository repository, String address){
        long startSequential = System.currentTimeMillis();
        List<Customer> sequentialResult = repository.getAll().stream()
                .filter(c -> c.getAddress().equals(address))
                .collect(Collectors.toList());
        long sequentialTime = System.currentTimeMillis() - startSequential;

        long startParallel = System.currentTimeMillis();
        List<Customer> parallelResult = repository.getAll().parallelStream()
                .filter(c -> c.getAddress().equals(address))
                .collect(Collectors.toList());
        long parallelTime = System.currentTimeMillis() - startParallel;

        long startExecutor = System.currentTimeMillis();
        ExecutorService executor = Executors.newFixedThreadPool(4);
        try {
            List<Customer> allCustomers = repository.getAll();
            int chunkSize = Math.max(1, allCustomers.size() / 4);

            List<CompletableFuture<List<Customer>>> futures = new ArrayList<>();

            for (int i = 0; i < allCustomers.size(); i += chunkSize) {
                int start = i;
                int end = Math.min(i + chunkSize, allCustomers.size());
                List<Customer> chunk = allCustomers.subList(start, end);

                CompletableFuture<List<Customer>> future = CompletableFuture.supplyAsync(() ->
                                chunk.stream()
                                        .filter(c -> c.getAddress().equals(address))
                                        .collect(Collectors.toList()),
                        executor
                );

                futures.add(future);
            }

            List<Customer> executorResult = futures.stream()
                    .map(CompletableFuture::join)
                    .flatMap(List::stream)
                    .collect(Collectors.toList());

        } finally {
            executor.shutdown();
        }
        long executorTime = System.currentTimeMillis() - startExecutor;

        ComparisonResult result = new ComparisonResult(
                "Customer Filtering by Address",
                sequentialResult.size(),
                sequentialTime,
                parallelTime,
                executorTime
        );

        logger.info("Comparison completed: {}", result);
        return result;

    }
}
