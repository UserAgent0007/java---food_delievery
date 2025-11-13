package lab.repository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

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

    public String findByAddress (String address){

        List<Customer> allItems = this.getAll();
        
        Optional<Customer> result = allItems.stream()
        .filter(x -> x.getAddress().equals(address))
        .findFirst();

        return result.map(Customer::toString).orElse(null);
    }

    
}
