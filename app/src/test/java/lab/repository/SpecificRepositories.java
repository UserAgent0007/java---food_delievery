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

import lab.exceptions.CustomerException;

import java.util.List;
import java.util.Optional;

import lab.models.*;
import lab.repository.*;

public class SpecificRepositories {
    
    Order order1, order2;
    Customer customer1, customer2;
    Delivery del1, del2;
    Restaurant rest1, rest2;

    @BeforeEach
    public void setUp(){
        MenuItem item1 = new MenuItem("Pizza", 150, "Food");
        MenuItem item2 = new MenuItem("Coke", 500, "Drink");

        MenuItem item3 = new MenuItem("Potato", 150, "Food");
        MenuItem item4 = new MenuItem("Juice", 500, "Drink");

        customer1 = new Customer("John", "Doe", "ajhdkjash@gmail.com");
        customer2 = new Customer("Bob", "Doe", "ajhdkjash@gmail.com");

        MenuItem[] items = new MenuItem[]{ item1, item2 };
        LocalDate orderDate = LocalDate.of(2026, 9, 15);
        LocalDate orderDate2 = LocalDate.of(2026, 8, 15);
        OrderStatus status = OrderStatus.PENDING;

        order1 = new Order(customer1, items, orderDate, status);
        order2 = new Order(customer2, items, orderDate2, status);

        del1 = new Delivery(order1, "Courier Name", LocalDateTime.of(2026, 9, 15, 20, 0), 1);
        del2 = new Delivery(order2, "Courier Name", LocalDateTime.of(2026, 8, 15, 20, 0), 2);

        rest1 = new Restaurant("Zbc", CuisineType.AMERICAN, "jsdkjad");
        rest2 = new Restaurant("Bcd", CuisineType.AMERICAN, "jsdkjad");

    }

    @Test
    public void testDeliverySort(){

        DeliveryRepository repo = new DeliveryRepository();
        repo.add(del1);
        repo.add(del2);

        List<Delivery> sorted = repo.sortRepository();

        assertEquals(Arrays.asList(del2,del1), sorted);
    }

    @Test
    public void testRestaurantSort(){

        RestaurantRepository repo = new RestaurantRepository();

        repo.add(rest1);
        repo.add(rest2);

        List<Restaurant> sorted = repo.sortedRepository();

        assertEquals(Arrays.asList(rest2, rest1), sorted);
    }

    @Test
    public void testCustomerRepository(){
        CustomerRepository repo = new CustomerRepository();

        repo.add(customer1);
        repo.add(customer2);

        List<Customer> sorted = repo.sortedRepository();

        assertEquals(Arrays.asList(customer2, customer1), sorted);
    }

    
}
