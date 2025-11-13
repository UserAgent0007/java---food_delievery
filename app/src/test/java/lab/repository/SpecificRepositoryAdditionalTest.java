package lab.repository;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import lab.parser.CustomerFileParser;
import lab.utils.CuisineType;
import lab.utils.DelieveryUtils;
import lab.utils.OrderStatus;
import lab.utils.RestaurantUtils;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import lab.exceptions.CustomerException;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import lab.models.*;
import lab.repository.*;

public class SpecificRepositoryAdditionalTest {

    Customer customer1;
    Customer customer2;
    Customer customer3;
    MenuItem pizza;
    MenuItem sushi;
    MenuItem cola;
    Order order1;
    Order order2;
    Order order3;
    Delivery delivery1;
    Delivery delivery2;
    Delivery delivery3;
    DeliveryRepository repo;

    @BeforeEach
    public void setUp() {
        customer1 = new Customer("Akdhakjsdh", "Fsjkfnkjsn", "dalkdkajdlkj");
        customer2 = new Customer("Akdhdsjfsdfakjsdh", "Fsjkfnasndnmadbmnkjsn", "dalkdkajdlasdmansdmnaskj");
        customer3 = new Customer("Akdhasndmasndmasndmsandkjsdh", "Fsjansdfsafnsanfsadnfkfnkjsn",
                "dalkdnfnsa,mfns,dafnasnkajdlkj");

        pizza = new MenuItem("Pizza", 100, "Main");
        sushi = new MenuItem("Sushi", 150, "Main");
        cola = new MenuItem("Cola", 500, "Drink");

        order1 = new Order(customer1, new MenuItem[] { pizza, cola },
                LocalDate.now().plusDays(1), OrderStatus.DELIVERED);
        order2 = new Order(customer2, new MenuItem[] { pizza, cola },
                LocalDate.now().plusDays(2), OrderStatus.DELIVERED);
        order3 = new Order(customer3, new MenuItem[] { sushi },
                LocalDate.now().plusDays(3), OrderStatus.PENDING);

        delivery1 = new Delivery(order1, "John Dowhe", LocalDateTime.now().plusDays(1), 1);
        delivery2 = new Delivery(order2, "John Dowhe", LocalDateTime.now().plusDays(2), 2);
        delivery3 = new Delivery(order3, "Jane Dowhe", LocalDateTime.now().plusDays(3), 3);

        repo = new DeliveryRepository();
        repo.add(delivery1);
        repo.add(delivery2);
        repo.add(delivery3);
    }

    @Test
    void testCountEqualPositions() {
        Map<List<MenuItem>, Long> result = repo.countEqualOrders();
        List<MenuItem> pizzaCola = Arrays.asList(pizza, cola);
        List<MenuItem> sushiOnly = Collections.singletonList(sushi);
        Long real = result.get(pizzaCola);
        assertEquals(Long.valueOf(2), result.get(pizzaCola));
        assertEquals(Long.valueOf(1), result.get(sushiOnly));
    }

    @Test
    void testCountOrderInTimeRange() {
        LocalDateTime start = LocalDateTime.now().plusDays(1);
        LocalDateTime end = LocalDateTime.now().plusDays(3);
        Long count = repo.countOrderInTimeRange(start, end);
        assertEquals(Long.valueOf(2), count); // delivery1 і delivery2 у діапазоні
    }

    @Test
    void testCountOrderedDishes() {
        Map<MenuItem, Long> result = repo.countOrderedDishes();
        assertEquals(Long.valueOf(2), result.get(pizza)); // Pizza у двох orders
        assertEquals(Long.valueOf(2), result.get(cola)); // Cola у двох orders
        assertEquals(Long.valueOf(1), result.get(sushi)); // Sushi у одному order
    }

    @Test
    void testPriceOfOrders() {
        List<Integer> prices = repo.priceOfOrders();
        assertTrue(prices.contains(150)); // Pizza + Cola
        assertTrue(prices.contains(150)); // Другий такий самий Pizza + Cola
        assertTrue(prices.contains(150)); // Sushi тільки (150)
        assertEquals(3, prices.size());
    }

}
