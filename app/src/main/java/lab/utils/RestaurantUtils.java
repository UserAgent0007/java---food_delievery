package lab.utils;

public class RestaurantUtils {
    
    private RestaurantUtils(){}

    public static String capitalize(String text){

        if (text == null || text.trim().isEmpty()) {
            return text;
        }

        String trimmed = text.trim();
        return trimmed.substring(0, 1).toUpperCase() +
                trimmed.substring(1).toLowerCase();
    }

    public static boolean validLocation (String text){

        return ValidationHelper.isEmptyString(text);
    }
}
