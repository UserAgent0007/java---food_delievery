package lab.models;

import java.time.LocalDateTime;

import lab.utils.DelieveryUtils;

public record DeliveryRecord(Order order, String deliveryPerson, LocalDateTime deliveryTime) {

    public DeliveryRecord{

        if (!DelieveryUtils.validDelieveryPerson(deliveryPerson) || !DelieveryUtils.validDelieveryTime(deliveryTime) || !DelieveryUtils.validOrder(order)){

            throw new IllegalArgumentException("Bad input for this record");
        }
 
    }
}
