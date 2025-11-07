package lab.utils;

import java.time.LocalDate;

import lab.models.Customer;
import lab.models.MenuItem;

public class OrderUtils {

    private OrderUtils(){}

    public static boolean validCustomer (Customer customer){

        return CustomerUtils.validFirstName(customer.getFirstName()) && CustomerUtils.validLastName(customer.getLastName());
    }

    public static boolean validItems (MenuItem[] items, int numberItems){

        int n = numberItems;

        if (n <= 0){

            return false;
        }

        for (int i = 0; i < n; i++){

            if (MenuItemUtils.validPrice(items[i].getPrice()) && MenuItemUtils.validCategory(items[i].getCategory()) && 
                MenuItemUtils.validName(items[i].getName())){

                    continue;
            }

            else{

                return false;
            }

        }

        return true;
    }

    public static boolean validOrderDate(LocalDate date){

        return ValidationHelper.dateCheck(date);
    }

    public static String toStringItems(MenuItem[] items, int numberItems){

        int n = numberItems;

        String result = "";

        for (int i = 0; i < n; i++){

            result += items[i].getName() + " ";

        }

        return result;

    }
}
