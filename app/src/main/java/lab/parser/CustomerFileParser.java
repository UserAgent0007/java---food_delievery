package lab.parser;

import lab.exceptions.CustomerException;
import lab.models.Customer;


// import java.nio.file.Files;
// import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CustomerFileParser {
    private static final Logger logger = Logger.getLogger(CustomerFileParser.class.getName());

    /**
     * Parses a single CSV line into a Customer objects
     *
     * @param line CSV line
     * @return parsed Customer object
     * @throws CustomerException if line has invalid format or data
     */
    public static Customer parseCustomerFromLine(String line) throws CustomerException {
        String[] parts = line.split(",");
        if (parts.length != 3) {
            throw new CustomerException(
                    "Expected address, first name, last name" + line
            );
        }

        String name = parts[0].trim();
        String surname = parts[1].trim();
        String address = parts[2].trim();

        return new Customer(name, surname, address);
        
    }

    /**
     * Reads Customers from a CSV file.
     *
     * @param lines all CSV lines
     * @return list of parsed Customer objects
     * @throws CustomerException if CSV contains invalid data (for individual lines)
     */
    public static List<Customer> parseFromCSV(List<String> lines) throws CustomerException {
        List<Customer> customers = new ArrayList<>();

        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i).trim();
            if (line.isEmpty() || line.startsWith("#")) continue;

            try {
                Customer customer = parseCustomerFromLine(line);
                customers.add(customer);
                logger.log(Level.INFO, "Parsed Customer from line {0}: {1}",
                        new Object[]{i + 1, customer.getFirstName()});
            } catch (CustomerException e) {
                logger.log(Level.WARNING, "Failed to parse line {0}: {1}",
                        new Object[]{i + 1, e.getMessage()});
            }
        }

        logger.log(Level.INFO, "Successfully parsed {0} Customers from file", customers.size());
        return customers;
    }

    
    
}
