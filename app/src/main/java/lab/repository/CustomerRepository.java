package lab.repository;

import java.util.Collections;
import java.util.List;

import lab.models.Customer;


public class CustomerRepository extends GenericRepository<Customer> {
    
    public CustomerRepository(){
        
        super(Customer::getFirstName, "Customer");
    }

    public List<Customer> sortedRepository(){

        List<Customer> allItems = this.getAll();
        Collections.sort(allItems);
        return allItems;
    }
}
