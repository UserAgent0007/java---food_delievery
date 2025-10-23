package lab.Service;

import lab.models.*;

public class OrderGenerator {
    
    public static String showEntireOrder(Order order1){

        return generateOrder(order1.getItems()) + order1.getOrderStatus().toString();
    }

    static String generateOrder(MenuItem[] items){

        String res = "";

        for (var item : items){
            res += ", " + item.toString();
        }

        return res;
    }

    public static boolean addAdditionalMenuItem (Order order1, MenuItem item){

        if (order1.getNumberItems() >= 10){
            return false;
        }

        order1.getItems()[order1.getNumberItems()] = item;
        order1.incrementNumberItems();

        return true;
    }

}
