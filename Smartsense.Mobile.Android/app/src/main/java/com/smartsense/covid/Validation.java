package com.smartsense.covid;

import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validation {
    public boolean isValidEmail(String string){
        final String EMAIL_PATTERN = "[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,64}";
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(string);
        return matcher.matches();
    }

    public boolean isValidPassword(String string, boolean allowSpecialChars){
          /*String PATTERN;
        if(allowSpecialChars){
            PATTERN = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{6,20})";
            //PATTERN = "^[a-zA-Z@#$%]\\w{6,18}$";
        }else{
            //PATTERN = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{6,20})";
            //PATTERN = "^\\w{6,18}$";
        }

        Pattern pattern = Pattern.compile(PATTERN);
        Matcher matcher = pattern.matcher(string);
        return matcher.matches();*/
        if(string.length()>5){
            return true;
        }else{
            return false;
        }
    }

    public boolean isNullOrEmpty(String string){
        return TextUtils.isEmpty(string);
    }

    public boolean isNumeric(String string){
        return TextUtils.isDigitsOnly(string);
    }

    public boolean isValidName(String string){
        // Length be 18 characters max and 2 characters minimum
        final String s = "^\\w{2,18}$";
        Pattern pattern = Pattern.compile(s);
        Matcher matcher = pattern.matcher(string);
        return matcher.matches();
    }

    /*public boolean isValidName(String string){
        // Length be 18 characters max and 2 characters minimum
        if (!isNullOrEmpty(string)){
            return false;
        }
        if (string.trim().length()<2){
            return false;
        }
        return true;
    }*/

    public boolean isValidPhoneNumber(String string){
        final String p = "^((\\+|00)(\\d{1,3})[\\s-]?)?(\\d{8,15})$";
        Pattern pattern = Pattern.compile(p);
        Matcher matcher = pattern.matcher(string);
        return matcher.matches();
    }
}