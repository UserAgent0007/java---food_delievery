package lab.repository;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import lab.parser.CustomerFileParser;
import lab.utils.DelieveryUtils;
import lab.utils.OrderStatus;
import lab.utils.RestaurantUtils;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

import lab.exceptions.CustomerException;

import java.util.List;
import java.util.Optional;

import lab.models.*;
import lab.repository.*;

class GenericRepositoryTest {
    
    private GenericRepository<Delivery> repository;
    private IdentityExtractor<Delivery> extractor;

    @BeforeEach
    public void setUp(){
        extractor = (Delivery d) -> String.valueOf(d.getId());
        repository = new GenericRepository<>(extractor, "Delivery");
    }

    private Delivery createSampleDelivery(int id) {
        MenuItem item1 = new MenuItem("Pizza", 150, "Food");
        MenuItem item2 = new MenuItem("Coke", 500, "Drink");

        Customer customer = new Customer("John", "Doe", "ajhdkjash@gmail.com");
        MenuItem[] items = new MenuItem[]{ item1, item2 };
        LocalDate orderDate = LocalDate.of(2026, 9, 15);
        OrderStatus status = OrderStatus.PENDING;
        Order order = new Order(customer, items, orderDate, status);

        return new Delivery(order, "Courier Name", LocalDateTime.of(2026, 9, 15, 20, 0), id);
    }

    @Test
    public void testAddAndFindByIdentity() {
        Delivery d1 = createSampleDelivery(1);
        assertTrue(repository.add(d1));
        assertFalse(repository.add(d1)); // Дублікати не додаються

        Optional<Delivery> found = repository.findByIdentity("1");
        assertTrue(found.isPresent());
        assertEquals(d1, found.get());
    }

    @Test
    public void testRemoveByEntity() {
        Delivery d1 = createSampleDelivery(2);
        repository.add(d1);
        assertTrue(repository.remove(d1));
        assertFalse(repository.remove(d1)); // Уже немає у репозиторії
    }

    @Test
    public void testRemoveByIdentity(){
        Delivery d1 = createSampleDelivery(1);
        repository.add(d1);

        assertTrue(repository.removeByIdentity(extractor.extractIdentity(d1)));
        assertFalse(repository.removeByIdentity(extractor.extractIdentity(d1)));
    }

    @Test
    public void testContainsObject(){
        Delivery d1 = createSampleDelivery(2);

        assertFalse(repository.contains(d1));

        repository.add(d1);
        assertTrue(repository.contains(d1));
    }

    @Test
    void testContainsObjectIdentity(){
        Delivery d1 = createSampleDelivery(2);

        assertFalse(repository.containsIdentity(extractor.extractIdentity(d1)));

        repository.add(d1);
        assertTrue(repository.containsIdentity(extractor.extractIdentity(d1)));
    }

    @Test
    void testSizeRepositoryItems(){
        Delivery d1 = createSampleDelivery(2);
        Delivery d2 = createSampleDelivery(2);
        Delivery d3 = createSampleDelivery(3);

        List<Delivery> list1 = List.of(d1, d2, d3);

        repository.addList(list1);

        assertEquals(repository.getItemsForTesting().size(), 2);

    }

    @Test
    void testRemoval(){
        Delivery d1 = createSampleDelivery(1);
        Delivery d2 = createSampleDelivery(2);

        List<Delivery> list1 = List.of(d1, d2);
        repository.addList(list1);

        assertTrue(repository.remove(d1));
        assertTrue(repository.removeByIdentity(extractor.extractIdentity(d2)));
        assertFalse(repository.remove(d1));
    }
}
