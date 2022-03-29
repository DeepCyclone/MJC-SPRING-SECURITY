package com.epam.esm.service.validation;

public class SignValidator {
    public static boolean isPositiveLong(long number){
        return number > 0;
    }

    public static boolean isNonNegative(long number){
        return number>=0;
    }
}
