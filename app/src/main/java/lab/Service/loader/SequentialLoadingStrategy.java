package lab.Service.loader;

import lab.models.Customer;
import lab.models.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import lab.exceptions.DataSerializationException;

import lab.repository.*;
import lab.Service.LoadResult;

public class SequentialLoadingStrategy implements LoadingStrategy{
    private static final Logger logger = LoggerFactory.getLogger(SequentialLoadingStrategy.class);

    @Override
    public LoadResult load(
            CustomerRepository customerRepository,
            OrderRepository orderRepository,
            DataLoader dataLoader) {

        logger.info("Starting sequential loading...");

        long startTime = System.currentTimeMillis();

        try{
            int orders = dataLoader.loadEntity(Order.class, orderRepository);
            int customers = dataLoader.loadEntity(Customer.class, customerRepository);
            long duration = System.currentTimeMillis() - startTime;
            logger.info("Sequential loading completed in {} ms", duration);

            return new LoadResult(orders, customers, duration);
        }
        catch (DataSerializationException e) {
            long duration = System.currentTimeMillis() - startTime;
            logger.error("Sequential loading failed after {} ms: {}", duration, e.getMessage());
            throw new RuntimeException("Failed to load data", e);
        }

    }
}
