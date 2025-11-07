package lab.models;

import java.util.Objects;

import lab.utils.CustomerUtils;

public class Customer {
    
    private String firstName;
    private String lastName;
    private String address;
    
    public Customer (){}

    public Customer(String firstName, String lastName, String address){

        setAddress(address);
        setFirstName(firstName);
        setLastName(lastName);
    }

    public String getFirstName(){

        return this.firstName;
    }

    public void setFirstName(String firstName){

        if (CustomerUtils.validFirstName(firstName)){

            this.firstName = firstName;
        }

        else{

            throw new IllegalArgumentException("bad input for Customer's FirstName");
        }
    }

    public String getLastName(){

        return this.lastName;
    }

    public void setLastName(String lastName){

        if (CustomerUtils.validLastName(lastName)){

            this.lastName = lastName;
        }

        else{

            throw new IllegalArgumentException("bad input for Customer's LastName");
        }
    }

    public String getAddress(){

        return this.address;
    }

    public void setAddress (String address){

        if (CustomerUtils.validAddress(address)){

            this.address = address;
        }

        else {
            throw new IllegalArgumentException("bad input for address");
        }
        
    }

    public static Customer createCustomer (String firstName, String lastName, String address){

        if (CustomerUtils.validFirstName(firstName) && CustomerUtils.validLastName(lastName)){

            return new Customer(firstName, lastName, address);
        }

        else{

            return null;
        }
    }

    @Override
    public String toString(){

        return "Customer:\n" + this.firstName + "\n" + this.lastName + "\n" + this.address + "\n";
    }

    @Override
    public boolean equals(Object o){

        if (this == o){
            return true;
        }
        if (o == null || o.getClass() != this.getClass()){

            return false;
        }

        Customer customer1 = (Customer) o;
        
        return Objects.equals(this.firstName, customer1.firstName) && Objects.equals(this.lastName, customer1.lastName) 
                && Objects.equals(this.address, customer1.address);
    }

    @Override
    public int hashCode(){

        return Objects.hash(firstName, lastName, address);
    }
}
