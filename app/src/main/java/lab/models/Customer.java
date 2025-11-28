package lab.models;

import java.util.Objects;


import lab.exceptions.CustomerException;
import lab.utils.CustomerUtils;
import lab.utils.ValidationUtils;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import java.util.ArrayList;
import java.util.List;
import lab.parser.CustomerFileParser;
import jakarta.validation.constraints.*;
import lab.validation.*;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Customer implements Comparable<Customer>{
    
    private static final Logger logger = LoggerFactory.getLogger(Customer.class);

    @NotBlank
    @Pattern(
        regexp = "^[A-Z][a-z]+$",
        message = "Name must be like this pattern"
    )
    private String firstName;

    @NotBlank
    @Pattern(
        regexp = "^[A-Z][a-z]+$",
        message = "Name must be like this pattern"
    )
    private String lastName;

    @NotBlank
    private String address;

    public static Customer createValidCustomer(String firstName, String lastName, String address){
        Customer new_c = new Customer();
        ValidationUtils.validate(new_c);
        return new_c;
    }
    
    public Customer (){}

    public Customer(String firstName, String lastName, String address){

        try{
            setAddress(address);
            setFirstName(firstName);
            setLastName(lastName);

            logger.info("Object customer created succsesfully");
        }
        catch (CustomerException d){
            this.address = "undefiend";
            this.firstName = "undefiend";
            this.lastName = "undefiend";
            logger.error("Object is not created due to bad input");
            throw new CustomerException("bad input for parameters");
        }
    }

    public String getFirstName(){

        return this.firstName;
    }

    public void setFirstName(String firstName) throws CustomerException{

        if (CustomerUtils.validFirstName(firstName)){

            this.firstName = firstName;
        }

        else{

            
            throw new CustomerException("bad input for Customer's FirstName");
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

    public static List<Customer> createObjectsFromCSV(String filePath){

        List<Customer> customers = new ArrayList<>();
        try(BufferedReader br = new BufferedReader(new FileReader("input.txt"))){
            
            List<String> lines = new ArrayList<>();
            String line;


        
            while ((line = br.readLine()) != null) {
                lines.add(line); // Додаємо зчитаний рядок у наш список
            }
            return CustomerFileParser.parseFromCSV(lines);

        }catch(IOException ex){
            logger.warn("File not found: {0}");
            return customers;
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

    // Можна для колекції використовувати просто sort

    @Override
    public int compareTo(Customer other) {
        return this.firstName.compareTo(other.firstName);
    }

    @JsonCreator
    public static Customer fromJson(
        @JsonProperty("firstName") String firstName,
        @JsonProperty("lastName") String lastName,
        @JsonProperty("address") String address
    ){
        Customer customer = new Customer(firstName, lastName, address);
        ValidationUtils.validate(customer);
        return customer;
    }
}
