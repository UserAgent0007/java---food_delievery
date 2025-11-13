package lab.percistance;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import lab.repository.CustomerRepository;
import lab.repository.DeliveryRepository;
import lab.repository.RestaurantRepository;
import lab.utils.CuisineType;
import lab.utils.OrderStatus;
import lab.config.AppConfig;
import lab.exceptions.DataSerializationException;
import lab.models.*;
import lab.persistence.PersistenceManager;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class serealizerTest {

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
    CustomerRepository rep_cus;
    RestaurantRepository rest_rep;
    AppConfig config;
    PersistenceManager manager;

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

        rep_cus = new CustomerRepository();
        rep_cus.add(customer1);
        rep_cus.add(customer2);
        rep_cus.add(customer3);

        Restaurant rest1 = new Restaurant("skjdfskhf", CuisineType.AMERICAN, "skjdhfjksdhf");
        Restaurant rest2 = new Restaurant("skjfjhkfjhdfskhf", CuisineType.CHINESE,
                "skjdhfjkfnfhjflhjfkljhlkhjfghjfsdhf");
        Restaurant rest3 = new Restaurant("skjdfskdhfgjfhghjfhf", CuisineType.ITALIAN, "skjdhfjksdnnghf");

        rest_rep = new RestaurantRepository();
        rest_rep.add(rest1);
        rest_rep.add(rest2);
        rest_rep.add(rest3);

        config = new AppConfig();
        manager = new PersistenceManager(config);
    }

    @Test
    public void serealizationJsonTest() {

        try {
            manager.save(rest_rep.getAll(), "restaurant", Restaurant.class, "JSON");
            RestaurantRepository new_rep = new RestaurantRepository();
            new_rep.addList(manager.load("restaurant", Restaurant.class, "JSON"));

        } catch (DataSerializationException d) {
            fail("Data serialization failed: " + d.getMessage());
        }
    }

    @Test
    public void testSaveWithNullEntityTypeThrowsException() {
        DataSerializationException thrown = assertThrows(
                DataSerializationException.class,
                () -> manager.save(rest_rep.getAll(), null, Restaurant.class, "JSON"),
                "Expected save() to throw, but it didn't");
        assertTrue(thrown.getMessage().contains("Entity type cannot be null or empty"));
    }

    @Test
    public void serealizationYamlTest() {

        try {
            manager.save(rest_rep.getAll(), "restaurant", Restaurant.class, "YAML");
            RestaurantRepository new_rep = new RestaurantRepository();
            new_rep.addList(manager.load("restaurant", Restaurant.class, "YAML"));

        } catch (DataSerializationException d) {
            fail("Data serialization failed: " + d.getMessage());
        }
    }

    @Test
    public void unsuportedFormatTest(){
        DataSerializationException thrown = assertThrows(
                DataSerializationException.class,
                () -> manager.save(rest_rep.getAll(), null, Restaurant.class, "JSOON"),
                "Expected save() to throw, but it didn't");
    }
}
