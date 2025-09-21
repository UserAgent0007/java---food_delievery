package lab.models;

import lab.utils.CustomerUtils;

public record CustomerRecord(String firstName, String lastName, String address) {

    public CustomerRecord{

        if (!CustomerUtils.validFirstName(firstName) || !CustomerUtils.validLastName(lastName)){

            throw new IllegalArgumentException("Bad input for this record");
        }
    }
}
