package lab.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
// import java.time.format.DateTimeFormatter;
// import java.time.format.DateTimeParseException;
import java.util.regex.Pattern;

class ValidationHelper {

    static boolean isStringMatchPattern(String text, String pattern) {
        if (text == null || pattern == null) {
            return false;
        }
        return Pattern.matches(pattern, text);
    }

    static boolean dateCheck (LocalDate inputDate){

        // try{
            // DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            // LocalDate inputDate = LocalDate.parse(text, format);
        LocalDate now = LocalDate.now();

        return inputDate.isAfter(now);

        // }catch (DateTimeParseException e){
        //     System.out.println(e);
        //     return false;
        // }
    }

    static boolean dateTimeCheck (LocalDateTime inputDate){

        // try{
        //     DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        //     LocalDateTime inputDate = LocalDateTime.parse(text, format);
        LocalDateTime now = LocalDateTime.now();

        return inputDate.isAfter(now);

        // }catch (DateTimeParseException e){
        //     System.out.println(e);
        //     return false;
        // }
    }

    static boolean betweenNumber(int number, int low, int high){

        return number >= low && number <= high;
    }

    static boolean isEmptyString(String text){

        if (text == null || text.trim().isEmpty()) {
            return false;
        }

        return true;
    }
}
