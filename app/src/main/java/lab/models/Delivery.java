package lab.models;

import java.time.LocalDateTime;
import java.util.Objects;

import lab.utils.DelieveryUtils;

public class Delivery {

    private Order order;
    private String deliveryPerson;
    private LocalDateTime deliveryTime;

    public Delivery(){}

    public Delivery (Order order1, String deliveryPerson, LocalDateTime deliveryTime){

        order = new Order();

        setDeliveryPerson(deliveryPerson);
        setDeliveryTime(deliveryTime);
        setOrder(order1);
    }

    public Order getOrder(){

        return this.order;
    }

    public void setOrder(Order order){

        if (DelieveryUtils.validOrder(order)){

            this.order = order;
        }
        else{

            throw new IllegalArgumentException("bad input for order");
        }
    }

    public String getDeliveryPerson(){

        return this.deliveryPerson;
    }

    public void setDeliveryPerson(String deliveryPerson){

        if (DelieveryUtils.validDelieveryPerson(deliveryPerson)){

            this.deliveryPerson = deliveryPerson;
        }

        else{

            throw new IllegalArgumentException("bad input for deliveryPerson");
        }
    }

    public LocalDateTime getDeliveryTime(){

        return this.deliveryTime;
    }

    public void setDeliveryTime(LocalDateTime time){

        if (time == null){
            throw new IllegalArgumentException("bad input for delivery time");
        }

        if (DelieveryUtils.validDelieveryTime(time)){

            this.deliveryTime = time;
        }
        else{

            throw new IllegalArgumentException("bad input for delivery time");
        }
    }

    public static Delivery createDelivery(Order order, String deliveryPerson, LocalDateTime deliveryTime){

        if (DelieveryUtils.validDelieveryPerson(deliveryPerson) && DelieveryUtils.validDelieveryTime(deliveryTime) && DelieveryUtils.validOrder(order)){

                return new Delivery(order, deliveryPerson, deliveryTime);
        }

        else{

            return null;
        }
    }

    @Override
    public String toString(){

        return "Delivery:\n" + order.toString() + "\n" + deliveryPerson + "\n" + deliveryTime;
    }

    @Override
    public boolean equals (Object o){

        if (this == o){
            return true;
        }
        if (o == null || o.getClass() != this.getClass()){

            return false;
        }

        Delivery deliver = (Delivery) o;

        return Objects.equals(this.order, deliver.order) && Objects.equals(this.deliveryTime, deliver.deliveryTime);
    }

    @Override
    public int hashCode(){

        return Objects.hash(order, deliveryTime);
    }
}
