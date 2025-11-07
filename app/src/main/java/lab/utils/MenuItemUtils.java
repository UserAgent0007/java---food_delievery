package lab.utils;

public class MenuItemUtils {

    private MenuItemUtils (){}

    public static boolean validPrice (int price){

        return ValidationHelper.betweenNumber(price, 100, 1000);
    }

    public static boolean validName (String text){

        return ValidationHelper.isEmptyString(text);
    }

    public static boolean validCategory (String text){

        return ValidationHelper.isEmptyString(text);
    }
}
