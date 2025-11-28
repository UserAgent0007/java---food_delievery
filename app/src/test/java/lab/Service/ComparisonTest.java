package lab.Service;

import lab.Service.LoadResult;
import lab.Service.comparison.PerformanceComparisonService;
import lab.config.AppConfig;
import lab.models.Customer;
import lab.models.MenuItem;
import lab.models.Order;
import lab.persistence.PersistenceManager;
import lab.repository.CustomerRepository;
import lab.repository.OrderRepository;
import lab.utils.OrderStatus;
import org.junit.jupiter.api.BeforeEach;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class ComparisonTest {
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

        order1 = new Order(customer1, new MenuItem[] { pizza, cola },
                LocalDate.now().plusDays(1), OrderStatus.DELIVERED);
        order2 = new Order(customer2, new MenuItem[] { pizza, cola },
                LocalDate.now().plusDays(2), OrderStatus.DELIVERED);
        order3 = new Order(customer3, new MenuItem[] { sushi },
                LocalDate.now().plusDays(3), OrderStatus.PENDING);

        orderRepository.add(order1);
        orderRepository.add(order2);
        orderRepository.add(order3);
    }

    @Test
    public void testComparisonOrderFiltering(){
        PerformanceComparisonService performanceComparisonService = new PerformanceComparisonService();
        String res = String.valueOf(performanceComparisonService.compareOrderFiltering(orderRepository,
                LocalDate.of(2026, 2, 15)));

        assertTrue(res.contains("Fastest: ParallelStream"));
    }
}
