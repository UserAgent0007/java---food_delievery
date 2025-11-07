package lab.utils;

public class CustomerUtils {

    private CustomerUtils(){}

    public static boolean validFirstName (String text){

        String template = "^[A-Z][a-z]+$";

        return ValidationHelper.isStringMatchPattern(text, template);
    }

    public static boolean validLastName (String text){

        String template = "^[A-Z][a-z]+$";

        return ValidationHelper.isStringMatchPattern(text, template);
    }

    public static boolean validAddress (String text){

        return ValidationHelper.isEmptyString(text);
    }
}
