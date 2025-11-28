package lab.repository;

import lab.models.Order;

public class OrderRepository extends GenericRepository<Order> {

    public OrderRepository() {
        super(o-> String.valueOf(o.getCustomer() + o.getCustomer().getAddress()), "Order");
    }


}
