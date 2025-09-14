package lab.utils;

import java.time.LocalDateTime;

import lab.models.Order;

public class DelieveryUtils {

    private DelieveryUtils(){}

    public static boolean validDelieveryTime(LocalDateTime time){

        return ValidationHelper.dateTimeCheck(time);
    }

    public static boolean validDelieveryPerson(String text){

        String pattern = "^[A-Z][a-z]+ [A-Z][a-z]+$";

        return ValidationHelper.isStringMatchPattern(text, pattern);
    }

    public static boolean validOrder (Order order){

        return OrderUtils.validCustomer(order.getCustomer()) && OrderUtils.validItems(order.getItems(), order.getNumberItems()) && OrderUtils.validOrderDate(order.getOrderDate());
    }
}   
