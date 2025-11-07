package lab.models;

import java.time.LocalDate;

// import com.google.common.base.Objects;

import java.util.Objects;

import lab.utils.CustomerUtils;
import lab.utils.OrderUtils;

public class Order {
    private Customer customer = new Customer();
    private MenuItem[] items = new MenuItem[10];
    private int numberItems;
    private LocalDate orderDate;

    public Order(){}

    public Order(Customer customer, MenuItem[] items, LocalDate orderDate){

        setCustomer(customer);
        setItems(items);
        setOrderDate(orderDate);
    }

    public Customer getCustomer (){

        return this.customer;
    }

    public void setCustomer(Customer customer){

        if (CustomerUtils.validFirstName(customer.getFirstName()) && CustomerUtils.validLastName(customer.getLastName())){

            this.customer.setAddress(customer.getAddress());
            this.customer.setFirstName(customer.getFirstName());
            this.customer.setLastName(customer.getLastName());
        }

        else{

            throw new IllegalArgumentException("bad input for customer in order model");
        }
    }

    public MenuItem[] getItems(){

        return this.items;
    }

    public void setItems(MenuItem[] items){

        if(items == null){

            throw new IllegalArgumentException("bad input for items list");
        }
        
        setNumberItems(items.length);

        if (OrderUtils.validItems(items, numberItems)){

            for (int i = 0; i < numberItems; i++){

                this.items[i] = new MenuItem();

                this.items[i].setCategory(items[i].getCategory());
                this.items[i].setName(items[i].getName());
                this.items[i].setPrice(items[i].getPrice());
            }
        }

        else{

            throw new IllegalArgumentException("bad input for items list");
        }

        
    }

    public int getNumberItems(){

        return this.numberItems;
    }

    private void setNumberItems(int n){

        this.numberItems = n;
    }

    public LocalDate getOrderDate(){

        return this.orderDate;
    }

    public void setOrderDate(LocalDate orderDate){

        if (orderDate == null){
            throw new IllegalArgumentException("bad input for delivery time");
        }
        

        if (OrderUtils.validOrderDate(orderDate)){

            this.orderDate = orderDate;
        }

        else{

            throw new IllegalArgumentException("bad input for order date in Order model");
        }
    }

    public static Order createOrder(Customer customer, MenuItem[] items, LocalDate orderDate){

        int n = items.length;

        if (OrderUtils.validCustomer(customer) && OrderUtils.validItems(items, n) && OrderUtils.validOrderDate(orderDate)){

            return new Order(customer, items, orderDate);
        }

        else{

            return null;
        }
    }

    @Override 
    public String toString(){

        String orderList = OrderUtils.toStringItems(this.items, this.numberItems);

        return "Order:\n" + customer.toString() + "\n" + orderList + "\n" + orderDate.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Order obj = (Order) o;

        return Objects.equals(this.getCustomer(), obj.getCustomer()) && Objects.equals(this.getOrderDate(), obj.getOrderDate());
        
    }

    @Override
    public int hashCode() {
        return Objects.hash(customer, orderDate);
    }
}