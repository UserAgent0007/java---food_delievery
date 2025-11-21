package lab.validation;


import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;

public class YearValidator implements ConstraintValidator<ValidYear, LocalDate> {

    @Override
    public boolean isValid(LocalDate year, ConstraintValidatorContext context) {

        int yearValue = year.getYear();

//        if (year == null) {
//            return false;
//        }

        int currentYear = LocalDate.now().getYear();
        int minYear = currentYear + 2;

        return yearValue <= minYear && yearValue >= currentYear;
    }
}
