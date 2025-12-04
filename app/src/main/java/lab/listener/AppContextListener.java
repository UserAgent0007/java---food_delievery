package lab.listener;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import lab.models.Delivery;
import lab.models.Restaurant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import lab.config.AppConfig;
import lab.persistence.PersistenceManager;
import lab.repository.*;
import lab.Service.LoadResult;
import lab.Service.loader.DataLoader;
import lab.Service.loader.ExecutorLoadingStrategy;

/**
 * Application Context Listener - manages application lifecycle
 *
 * LIFECYCLE DEMONSTRATION:
 * 1. contextInitialized() - called ON SERVER STARTUP
 *    - Loads data from JSON files
 *    - Creates repositories (ONCE)
 *    - Stores them in ServletContext as Singleton
 *
 * 2. contextDestroyed() - called ON SERVER SHUTDOWN
 *    - Logs final statistics
 *    - Resource cleanup
 */
@WebListener
public class AppContextListener implements ServletContextListener {

    private static final Logger logger = LoggerFactory.getLogger(AppContextListener.class);

    private long startTime;

    /**
     * contextInitialized() - APPLICATION STARTUP
     * Called ONCE when server starts
     */
    @Override
    public void contextInitialized(ServletContextEvent sce) {

        startTime = System.currentTimeMillis();

        logger.info("=================================================================");
        logger.info("===  APPLICATION CONTEXT INITIALIZED - STARTING UP           ===");
        logger.info("=================================================================");

        ServletContext context = sce.getServletContext();

        try {
            logger.info("Step 1: Creating AppConfig and PersistenceManager...");
            AppConfig config = new AppConfig();
            PersistenceManager persistenceManager = new PersistenceManager(config);
            logger.info("✓ Configuration initialized");

            logger.info("Step 2: Creating repositories...");
            DeliveryRepository deliveryRepository = new DeliveryRepository();
            RestaurantRepository restaurantRepository = new RestaurantRepository();
//            System.out.println("initialized repositories");
            logger.info("✓ All repositories created");

            logger.info("Step 3: Loading data from JSON files using ExecutorLoadingStrategy...");
            DataLoader dataLoader = new DataLoader(persistenceManager);

            LoadResult loadResult = dataLoader.load(
                    deliveryRepository,
                    restaurantRepository,
                    new ExecutorLoadingStrategy(4)
            );

//            System.out.println(loadResult.toString());

            logger.info("✓ Data loading completed");


            // Store repositories in ServletContext (as Singleton)
            logger.info("Step 4: Storing repositories in ServletContext...");
            context.setAttribute("deliveryRepository", deliveryRepository);
            context.setAttribute("restaurantRepository", restaurantRepository);

            context.setAttribute("persistenceManager", persistenceManager);
            logger.info("✓ Repositories stored in ServletContext");

            long initTime = System.currentTimeMillis() - startTime;
            logger.info("=================================================================");
            logger.info("===  APPLICATION STARTUP SUCCESSFUL in {}ms                  ===", initTime);
            logger.info("===  All servlets will now share these repository instances  ===");
            logger.info("=================================================================");

        } catch (Exception e) {
            logger.error("=================================================================");
            logger.error("===  CRITICAL ERROR DURING APPLICATION STARTUP               ===");
            logger.error("=================================================================");
            logger.error("Failed to initialize application context", e);
            throw new RuntimeException("Application initialization failed", e);
        }
    }

    /**
     * contextDestroyed() - APPLICATION SHUTDOWN
     * Called ONCE when server stops
     */
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        logger.info("=================================================================");
        logger.info("===  APPLICATION CONTEXT DESTROYED - SHUTTING DOWN           ===");
        logger.info("=================================================================");

        ServletContext context = sce.getServletContext();

        try {
            // Get repositories for final statistics and saving
            DeliveryRepository deliveryRepository =
                    (DeliveryRepository) context.getAttribute("deliveryRepository");
            RestaurantRepository restaurantRepository =
                    (RestaurantRepository) context.getAttribute("restaurantRepository");

            PersistenceManager persistenceManager =
                    (PersistenceManager) context.getAttribute("persistenceManager");

            // Log final statistics
            logger.info("Final statistics:");
            if (deliveryRepository != null) {
                logger.info("  - Deliveries: {}", deliveryRepository.size());
            }
            if (restaurantRepository != null) {
                logger.info("  - Restaurants: {}", restaurantRepository.size());
            }


            // Save all data to JSON files before shutdown
            if (persistenceManager != null) {
                logger.info("Saving all data to JSON files...");

                if (deliveryRepository != null) {
                    persistenceManager.save(
                            deliveryRepository.getAll(),
                            "delivery",
                            Delivery.class,
                            "JSON"
                    );
                    logger.info("✓ Deliveries saved");
                }

                if (restaurantRepository != null) {
                    persistenceManager.save(
                            restaurantRepository.getAll(),
                            "restaurants",
                            Restaurant.class,
                            "JSON"
                    );
                    logger.info("✓ Restaurants saved");
                }

                logger.info("All data successfully saved to JSON files");
            }

            long totalUptime = System.currentTimeMillis() - startTime;
            logger.info("Total application uptime: {}ms ({} seconds)",
                    totalUptime, totalUptime / 1000);

            // Clean up ServletContext
            logger.info("Cleaning up ServletContext attributes...");
            context.removeAttribute("deliveryRepository");
            context.removeAttribute("restaurantRepository");

            context.removeAttribute("persistenceManager");

            logger.info("=================================================================");
            logger.info("===  APPLICATION SHUTDOWN COMPLETE                           ===");
            logger.info("=================================================================");

        } catch (Exception e) {
            logger.error("Error during application shutdown", e);
        }
    }

}

