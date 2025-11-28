package lab.Service.loader;


import lab.Service.LoadResult;
import lab.config.AppConfig;
import lab.models.Customer;
import lab.models.MenuItem;
import lab.models.Order;
import lab.persistence.PersistenceManager;
import lab.repository.CustomerRepository;
import lab.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ParallelLoadingStratagyTest {
//LoadResult{orders=21, customers=23, total=44, duration=6ms}

    Customer customer1;
    Customer customer2;
    Customer customer3;
    MenuItem pizza;
    MenuItem sushi;
    MenuItem cola;
    Order order1;
    Order order2;
    Order order3;
    AppConfig config;
    PersistenceManager manager;
    OrderRepository orderRepository;
    CustomerRepository rep_cus;


    @BeforeEach
    public void setUp() {
        customer1 = new Customer("Akdhakjsdh", "Fsjkfnkjsn", "dalkdkajdlkj");
        customer2 = new Customer("Akdhdsjfsdfakjsdh", "Fsjkfnasndnmadbmnkjsn", "dalkdkajdlasdmansdmnaskj");
        customer3 = new Customer("Akdhasndmasndmasndmsandkjsdh", "Fsjansdfsafnsanfsadnfkfnkjsn",
                "dalkdnfnsa,mfns,dafnasnkajdlkj");

        pizza = new MenuItem("Pizza", 150, "Main");
        sushi = new MenuItem("Sushi", 550, "Main");
        cola = new MenuItem("Cola", 130, "Drink");

        orderRepository = new OrderRepository();
        rep_cus = new CustomerRepository();

        config = new AppConfig();
        manager = new PersistenceManager(config);
    }

    @Test
    public void testCorrectnessResult(){
        ParallelLoadingStrategy parallelLoadingStrategy = new ParallelLoadingStrategy();
        LoadResult res = parallelLoadingStrategy.load(rep_cus, orderRepository, new DataLoader(manager));

        assertEquals("LoadResult{orders=21, customers=23, total=44, duration=6ms}", res.toString());
    }
}
