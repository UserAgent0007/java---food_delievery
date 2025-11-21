package lab.validationTest;

import lab.exceptions.InvalidDataException;
import lab.models.Customer;
import lab.models.MenuItem;
import lab.models.Order;
import lab.utils.OrderStatus;
import lab.utils.ValidationUtils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class ValidationTest {
    
    private Validator validator;

    Customer customer1;
    Customer customer2;
    Customer customer3;
    MenuItem pizza;
    MenuItem sushi;
    MenuItem cola;
    Order order1;
    Order order2;
    Order order3;
    
    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

        Customer customer = new Customer("Hjdsfs", "Hfskjhfkjsh", "kjahdhsad");

        customer1 = new Customer("Akdhakjsdh", "Fsjkfnkjsn", "dalkdkajdlkj");
        customer2 = new Customer("Akdhdsjfsdfakjsdh", "Fsjkfnasndnmadbmnkjsn", "dalkdkajdlasdmansdmnaskj");
        customer3 = new Customer("Akdhasndmasndmasndmsandkjsdh", "Fsjansdfsafnsanfsadnfkfnkjsn",
                "dalkdnfnsa,mfns,dafnasnkajdlkj");

        pizza = new MenuItem("Pizza", 500, "Main");
        sushi = new MenuItem("Sushi", 110, "Main"); // bad input for annotation
        cola = new MenuItem("Cola", 150, "Drink"); 

        order1 = new Order(customer1, new MenuItem[] { pizza, cola },
                LocalDate.now().plusDays(1), OrderStatus.DELIVERED);
        order2 = new Order(customer2, new MenuItem[] { pizza, cola },
                LocalDate.now().plusDays(2), OrderStatus.DELIVERED);
        order3 = new Order(customer3, new MenuItem[] { sushi },
                LocalDate.now().plusDays(3), OrderStatus.PENDING);
    }
    
    @Test
    public void testValidOrderItems() {
        Order order = new Order(customer2, new MenuItem[] { sushi},
                LocalDate.now().plusDays(2), OrderStatus.DELIVERED);
        
        
        assertThrows(InvalidDataException.class, () -> {ValidationUtils.validate(order);});
    }
    
    
    @Test
    public void testYear() {
        Order order = new Order(customer2, new MenuItem[] { sushi},
                LocalDate.now().plusYears(3), OrderStatus.DELIVERED);
        
        assertThrows(InvalidDataException.class, () -> {ValidationUtils.validate(order);});
    }
    
    
}
