package com.utils.validation;

import android.content.Context;
import android.text.TextUtils;
import android.util.Patterns;

import androidx.annotation.StringRes;

import com.exception.ValidException;
import com.utils.LogUtil;
import com.widget.R;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.utils.validation.ValidationRule.EXTRACT_LENGTH;
import static com.utils.validation.ValidationRule.GREATER_THAN;
import static com.utils.validation.ValidationRule.GREATER_THAN_EQUAL_TO;
import static com.utils.validation.ValidationRule.INTEGER;
import static com.utils.validation.ValidationRule.LESS_THAN;
import static com.utils.validation.ValidationRule.LESS_THAN_EQUAL_TO;
import static com.utils.validation.ValidationRule.MAX_LENGTH;
import static com.utils.validation.ValidationRule.MIN_LENGTH;
import static com.utils.validation.ValidationRule.NATURAL;
import static com.utils.validation.ValidationRule.NATURAL_NO_ZERO;
import static com.utils.validation.ValidationRule.NUMERIC;
import static com.utils.validation.ValidationRule.REQUIRED;
import static com.utils.validation.ValidationRule.VALID_EMAIL;
import static com.utils.validation.ValidationRule.VALID_NAME;
import static com.utils.validation.ValidationRule.VALID_PASSWORD;
import static com.utils.validation.ValidationRule.VALID_PHONE;
import static com.utils.validation.ValidationRule.VALID_USERNAME;

public class ValidationHelper {
    private static final int USERNAME_MAX_LENGTH = 20;
    private static final int USERNAME_MIN_LENGTH = 4;

    private static final int PASSWORD_MAX_LENGTH = 15;
    private static final int PASSWORD_MIN_LENGTH = 6;

    private static final int FULL_NAME_MAX_LENGTH = 100;
    private static final int FULL_NAME_MIN_LENGTH = 4;

    private static final int PHONE_LENGTH = 10;

    public static void valid(Context context, String s, @StringRes int fieldName, final String... args) throws ValidException {
        valid(context, s, context.getString(fieldName), args);
    }

    public static void valid(Context context, String s, String fieldName, final String... args) throws ValidException {
        //LogUtil.i("%s %s", fieldName, Arrays.toString(args));

        boolean isRequired = false;
        boolean isValidPhone = false;
        boolean isValidPassword = false;
        boolean isValidUsername = false;
        boolean isValidName = false;
        boolean isValidEmail = false;
        boolean isNatural = false;
        boolean isisNaturalNoZero = false;
        boolean isNumeric = false;
        boolean isInteger = false;

        boolean isGreatThan = false;
        Integer greatThan = null;

        boolean isGreatThanEqualTo = false;
        Integer greatThanEqualTo = null;

        boolean isLessThan = false;
        Integer lessThan = null;

        boolean isLessThanEqualTo = false;
        Integer lessThanEqualTo = null;

        boolean isMinLength = false;
        Integer minLength = null;

        boolean isMaxLength = false;
        Integer maxLength = null;

        boolean isExtractLength = false;
        Integer exactLength = null;

        for (String arg : args) {
            switch (arg) {
                case REQUIRED:
                    isRequired = true;
                    break;
                case NATURAL:
                    isNatural = true;
                    break;
                case NUMERIC:
                    isNumeric = true;
                    break;
                case INTEGER:
                    isInteger = true;
                    break;
                case VALID_PHONE:
                    isValidPhone = true;
                    break;
                case VALID_PASSWORD:
                    isValidPassword = true;
                    break;
                case VALID_USERNAME:
                    isValidUsername = true;
                    break;
                case VALID_EMAIL:
                    isValidEmail = true;
                    break;
                case VALID_NAME:
                    isValidName = true;
                    break;
                case NATURAL_NO_ZERO:
                    isisNaturalNoZero = true;
                    break;
                default:
                    if (arg.contains(MIN_LENGTH)) {
                        isMinLength = true;
                        minLength = getValue(arg);
                    } else if (arg.contains(MAX_LENGTH)) {
                        isMaxLength = true;
                        maxLength = getValue(arg);
                    } else if (arg.contains(EXTRACT_LENGTH)) {
                        isExtractLength = true;
                        exactLength = getValue(arg);
                    } else if (arg.contains(GREATER_THAN_EQUAL_TO)) {
                        isGreatThanEqualTo = true;
                        greatThanEqualTo = getValue(arg);
                    } else if (arg.contains(GREATER_THAN)) {
                        isGreatThan = true;
                        greatThan = getValue(arg);
                    } else if (arg.contains(LESS_THAN_EQUAL_TO)) {
                        isLessThanEqualTo = true;
                        lessThanEqualTo = getValue(arg);
                    } else if (arg.contains(LESS_THAN)) {
                        isLessThan = true;
                        lessThan = getValue(arg);
                    }
                    break;
            }
        }

        if (isRequired)
            required(context, s, fieldName);

        if (isMinLength && minLength != null)
            minLength(context, s, fieldName, isRequired, minLength);

        if (isMaxLength && maxLength != null)
            maxLength(context, s, fieldName, isRequired, maxLength);

        if (isExtractLength && exactLength != null)
            exactLength(context, s, fieldName, isRequired, exactLength);

        if (isNatural)
            natural(context, s, fieldName, isRequired);

        if (isisNaturalNoZero)
            naturalNoZero(context, s, fieldName, isRequired);

        if (isInteger)
            integer(context, s, fieldName, isRequired);

        if (isNumeric)
            numeric(context, s, fieldName, isRequired);

        if (isGreatThan && greatThan != null)
            greatThan(context, s, fieldName, isRequired, greatThan);

        if (isGreatThanEqualTo && greatThanEqualTo != null)
            greatThanEqualTo(context, s, fieldName, isRequired, greatThanEqualTo);

        if (isLessThan && lessThan != null)
            lessThan(context, s, fieldName, isRequired, lessThan);

        if (isLessThanEqualTo && lessThanEqualTo != null)
            lessThanEqualTo(context, s, fieldName, isRequired, lessThanEqualTo);

        if (isValidName) {
            minLength(context, s, fieldName, isRequired, FULL_NAME_MIN_LENGTH);
            maxLength(context, s, fieldName, isRequired, FULL_NAME_MAX_LENGTH);
            alphaUtf8Space(context, s, fieldName, isRequired);
        }

        if (isValidPhone) {
            exactLength(context, s, fieldName, isRequired, PHONE_LENGTH);
            numeric(context, s, fieldName, isRequired);
            boolean condition = isRequired ? s.startsWith("0") : TextUtils.isEmpty(s) || s.startsWith("0");
            if (!condition)
                throw new ValidException(context.getString(R.string.valid_start_zero, fieldName));
        }

        if (isValidEmail)
            validEmail(context, s, fieldName, isRequired);

        if (isValidUsername) {
            minLength(context, s, fieldName, isRequired, USERNAME_MIN_LENGTH);
            maxLength(context, s, fieldName, isRequired, USERNAME_MAX_LENGTH);
            alphaNumeric(context, s, fieldName, isRequired);
        }

        if (isValidPassword) {
            minLength(context, s, fieldName, isRequired, PASSWORD_MIN_LENGTH);
            maxLength(context, s, fieldName, isRequired, PASSWORD_MAX_LENGTH);
        }

    }


    private static void greatThan(Context context, String s, String fieldName, boolean isRequired, int number) throws ValidException {
        boolean condition = isRequired ? validGreaterThan(s, number) : TextUtils.isEmpty(s) || validGreaterThan(s, number);
        if (!condition)
            throw new ValidException(context.getString(R.string.valid_greater_than, fieldName, number));
    }

    private static void greatThanEqualTo(Context context, String s, String fieldName, boolean isRequired, int number) throws ValidException {
        boolean condition = isRequired ? validGreaterThanEqualTo(s, number) : TextUtils.isEmpty(s) || validGreaterThanEqualTo(s, number);
        if (!condition)
            throw new ValidException(context.getString(R.string.valid_greater_than_equal_to, fieldName, number));
    }

    private static void lessThan(Context context, String s, String fieldName, boolean isRequired, int number) throws ValidException {
        boolean condition = isRequired ? validLessThan(s, number) : TextUtils.isEmpty(s) || validLessThan(s, number);
        if (!condition)
            throw new ValidException(context.getString(R.string.valid_less_than, fieldName, number));
    }

    private static void lessThanEqualTo(Context context, String s, String fieldName, boolean isRequired, int number) throws ValidException {
        boolean condition = isRequired ? validLessThanEqualTo(s, number) : TextUtils.isEmpty(s) || validLessThanEqualTo(s, number);
        if (!condition)
            throw new ValidException(context.getString(R.string.valid_less_than_equal_to, fieldName, number));
    }

    private static Integer getValue(String s) {
        Integer temp = null;
        Matcher m = Pattern.compile("\\[(.*?)\\]").matcher(s);
        if (m.find()) {
            try {
                temp = Integer.parseInt(m.group(1));
            } catch (Exception e) {
                LogUtil.e(e.getMessage());
            }

        }
        return temp;
    }

    private static void required(Context context, String s, String fieldName) throws ValidException {
        boolean condition = !TextUtils.isEmpty(s);
        if (!condition)
            throw new ValidException(context.getString(R.string.valid_required, fieldName));
    }

    private static void minLength(Context context, String s, String fieldName, boolean isRequired, int minLength) throws ValidException {
        boolean condition = isRequired ? validMinLength(s, minLength) : TextUtils.isEmpty(s) || validMinLength(s, minLength);
        if (!condition)
            throw new ValidException(context.getString(R.string.valid_min_length, fieldName, minLength));
    }

    private static void maxLength(Context context, String s, String fieldName, boolean isRequired, int maxLength) throws ValidException {
        boolean condition = isRequired ? validMaxLength(s, maxLength) : TextUtils.isEmpty(s) || validMaxLength(s, maxLength);
        if (!condition)
            throw new ValidException(context.getString(R.string.valid_max_length, fieldName, maxLength));
    }

    private static void exactLength(Context context, String s, String fieldName, boolean isRequired, int exactLength) throws ValidException {
        boolean condition = isRequired ? validExactLength(s, exactLength) : TextUtils.isEmpty(s) || validExactLength(s, exactLength);
        if (!condition)
            throw new ValidException(context.getString(R.string.valid_exact_length, fieldName, exactLength));
    }

    private static void alphaUtf8Space(Context context, String s, String fieldName, boolean isRequired) throws ValidException {
        boolean condition = isRequired ? validAlphaUtf8Space(s) : TextUtils.isEmpty(s) || validAlphaUtf8Space(s);
        if (!condition)
            throw new ValidException(context.getString(R.string.valid_alpha_utf8_space, fieldName));
    }

    private static void alphaNumeric(Context context, String s, String fieldName, boolean isRequired) throws ValidException {
        boolean condition = isRequired ? validAlphaNumeric(s) : TextUtils.isEmpty(s) || validAlphaNumeric(s);
        if (!condition)
            throw new ValidException(context.getString(R.string.valid_alpha_numeric, fieldName));
    }

    private static void numeric(Context context, String s, String fieldName, boolean isRequired) throws ValidException {
        boolean condition = isRequired ? validNumeric(s) : TextUtils.isEmpty(s) || validNumeric(s);
        if (!condition)
            throw new ValidException(context.getString(R.string.valid_numeric, fieldName));
    }

    private static void integer(Context context, String s, String fieldName, boolean isRequired) throws ValidException {
        boolean condition = isRequired ? validInteger(s) : TextUtils.isEmpty(s) || validInteger(s);
        if (!condition)
            throw new ValidException(context.getString(R.string.valid_integer, fieldName));
    }

    private static void validEmail(Context context, String s, String fieldName, boolean isRequired) throws ValidException {
        boolean condition = isRequired ? validEmail(s) : TextUtils.isEmpty(s) || validEmail(s);
        if (!condition)
            throw new ValidException(context.getString(R.string.valid_email, fieldName));
    }

    private static void natural(Context context, String s, String fieldName, boolean isRequired) throws ValidException {
        boolean condition = isRequired ? validNatural(s) : TextUtils.isEmpty(s) || validNatural(s);
        if (!condition)
            throw new ValidException(context.getString(R.string.valid_natural, fieldName));
    }

    private static void naturalNoZero(Context context, String s, String fieldName, boolean isRequired) throws ValidException {
        boolean condition = isRequired ? validNaturalNoZero(s) : TextUtils.isEmpty(s) || validNaturalNoZero(s);
        if (!condition)
            throw new ValidException(context.getString(R.string.valid_natural_no_zero, fieldName));
    }

    //Valid
    public static boolean validMinLength(String s, int minLength) {
        return s.length() >= minLength;
    }

    public static boolean validMaxLength(String s, int maxLength) {
        return s.length() <= maxLength;
    }

    public static boolean validExactLength(String s, int exactLength) {
        return s.length() == exactLength;
    }

    public static boolean validRegexMatch(String s, String pattern) {
        return s.matches(pattern);
    }

    public static boolean validEmail(String s) {
        return Patterns.EMAIL_ADDRESS.matcher(s).matches();
    }

    public static boolean validNumeric(String s) {
        return validRegexMatch(s, "^[0-9_]+");
    }

    public static boolean validAlphaUtf8Space(String s) {
        return validRegexMatch(s, "^[\\p{L} .'-]+$");
    }

    public static boolean validAlphaNumeric(String s) {
        return validRegexMatch(s, "[A-Za-z0-9_]+");
    }

    public static boolean validNatural(String s) {
        return validRegexMatch(s, "(0|[1-9]\\d*)");
    }

    public static boolean validNaturalNoZero(String s) {
        boolean isNatural = validNatural(s);
        try {
            return isNatural && Integer.parseInt(s) > 0;
        } catch (Exception ignore) {
            //ignore
        }
        return false;
    }

    public static boolean validInteger(String s) {
        return validRegexMatch(s, "-?(0|[1-9]\\d*)");
    }

    public static boolean validGreaterThan(String s, int number) {
        try {
            return Integer.parseInt(s) > number;
        } catch (Exception ignore) {
            //ignore
        }
        return false;
    }

    public static boolean validGreaterThanEqualTo(String s, int number) {
        try {
            return Integer.parseInt(s) >= number;
        } catch (Exception ignore) {
            //ignore
        }
        return false;
    }

    public static boolean validLessThan(String s, int number) {
        try {
            return Integer.parseInt(s) < number;
        } catch (Exception ignore) {
            //ignore
        }
        return false;
    }

    public static boolean validLessThanEqualTo(String s, int number) {
        try {
            return Integer.parseInt(s) <= number;
        } catch (Exception ignore) {
            //ignore
        }
        return false;
    }
}
