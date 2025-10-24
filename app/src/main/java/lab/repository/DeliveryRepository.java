package lab.repository;

import java.util.Comparator;
import java.util.List;

import lab.models.Delivery;

public class DeliveryRepository extends GenericRepository<Delivery> {

    public DeliveryRepository(){
        super(d -> String.valueOf(d.getId()), "Delivery");
    }

    public List<Delivery> sortRepository(){
        
        List<Delivery> allItems = this.getAll();
        allItems.sort(
            Comparator.comparing(Delivery::getDeliveryPerson)
            .thenComparing(Delivery::getDeliveryTime)
        );
        return allItems;
    }
}
