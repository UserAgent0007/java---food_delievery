package lab.repository;

import java.time.LocalDateTime;
// import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
// import java.util.stream.Collector;
import java.util.stream.Collectors;

import lab.models.Delivery;
import lab.models.MenuItem;
// import lab.models.Order;
import lab.models.Order;

public class DeliveryRepository extends GenericRepository<Delivery> {

    public DeliveryRepository() {
        super(d -> String.valueOf(d.getId()), "Delivery");
    }

    public List<Delivery> sortRepository() {

        List<Delivery> allItems = this.getAll();
        allItems.sort(
                Comparator.comparing(Delivery::getDeliveryPerson)
                        .thenComparing(Delivery::getDeliveryTime));
        return allItems;
    }

    public Map<List<MenuItem>, Long> countEqualOrders() {
        return this.getAll().stream()
                .map(Delivery::getOrder)
                .map(order -> Arrays.stream(order.getItems())
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList()))
                .collect(Collectors.groupingBy(
                        list -> list,
                        Collectors.counting()));
    }

    public Map<MenuItem, Long> countOrderedDishes() {

        List<MenuItem> orders = this.getAll().stream().map(Delivery::getOrder)
                .flatMap(order -> Arrays.stream(order.getItems()))
                .filter(Objects::nonNull)

                .collect(Collectors.toList());

        Map<MenuItem, Long> res = orders.stream().collect(Collectors.groupingBy(item -> item, Collectors.counting()));

        return res;
    }

    public Long countOrderInTimeRange(LocalDateTime time_start, LocalDateTime time_end) {
        return this.getAll().stream()
                .filter(item -> item.getDeliveryTime().isAfter(time_start) && item.getDeliveryTime().isBefore(time_end))
                .count();
    }

    public List<Integer> priceOfOrders() {

        return this.getAll().stream()
                .map(Delivery::getOrder)
                .map(order -> Arrays.asList(order.getItems()))

                .map(item -> item.stream().filter(Objects::nonNull).map(MenuItem::getPrice).reduce(0, Integer::sum))
                .collect(Collectors.toList());
    }
}
