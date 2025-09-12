package lab.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.regex.Pattern;

class ValidationHelper {

    static boolean isStringMatchPattern(String text, String pattern) {
        if (text == null || pattern == null) {
            return false;
        }
        return Pattern.matches(pattern, text);
    }

    static boolean dateFormatCheck (String text){

        try{
            DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            LocalDate inputDate = LocalDate.parse(text, format);
            LocalDate now = LocalDate.now();

            return inputDate.isAfter(now);

        }catch (DateTimeParseException e){
            System.out.println(e);
            return false;
        }
    }

    static boolean dateTimeFormatCheck (String text){

        try{
            DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

            LocalDateTime inputDate = LocalDateTime.parse(text, format);
            LocalDateTime now = LocalDateTime.now();

            return inputDate.isAfter(now);

        }catch (DateTimeParseException e){
            System.out.println(e);
            return false;
        }
    }

    
}
