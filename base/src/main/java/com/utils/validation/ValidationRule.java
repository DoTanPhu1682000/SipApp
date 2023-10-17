package com.utils.validation;

public class ValidationRule {
    public static final String REQUIRED = "required";
    public static final String VALID_PHONE = "valid_phone";
    public static final String VALID_NAME = "valid_name";
    public static final String VALID_USERNAME = "valid_username";
    public static final String VALID_PASSWORD = "valid_password";
    public static final String VALID_EMAIL = "valid_email";
    public static final String INTEGER = "integer";
    public static final String NUMERIC = "numeric";
    public static final String NATURAL = "natural";
    public static final String NATURAL_NO_ZERO = "natural_no_zero";

    //Not visible
    protected static final String MIN_LENGTH = "min_length";
    protected static final String MAX_LENGTH = "max_length";
    protected static final String EXTRACT_LENGTH = "exact_length";
    protected static final String GREATER_THAN = "greater_than";
    protected static final String GREATER_THAN_EQUAL_TO = "greater_than_equal_to";
    protected static final String LESS_THAN = "less_than";
    protected static final String LESS_THAN_EQUAL_TO = "less_than_equal_to";


    //Private
    private static final String FORMAT="%s[%s]";

    public static String MIN_LENGTH(int minLength) {
        return String.format(FORMAT, MIN_LENGTH, minLength);
    }

    public static String MAX_LENGTH(int maxLength) {
        return String.format(FORMAT, MAX_LENGTH, maxLength);
    }

    public static String EXTRACT_LENGTH(int exactLength) {
        return String.format(FORMAT, EXTRACT_LENGTH, exactLength);
    }

    public static String GREATER_THAN(int number) {
        return String.format(FORMAT, GREATER_THAN, number);
    }

    public static String GREATER_THAN_EQUAL_TO(int number) {
        return String.format(FORMAT, GREATER_THAN_EQUAL_TO, number);
    }

    public static String LESS_THAN(int number) {
        return String.format(FORMAT, LESS_THAN, number);
    }

    public static String LESS_THAN_EQUAL_TO(int number) {
        return String.format(FORMAT, LESS_THAN_EQUAL_TO, number);
    }

}