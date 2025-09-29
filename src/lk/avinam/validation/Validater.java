
package lk.avinam.validation;

import raven.toast.Notifications;

public class Validater {

    public static boolean isEmailValid(String value) {
        if (value.isBlank()) {
            Notifications.getInstance().show(Notifications.Type.WARNING, Notifications.Location.TOP_RIGHT, 
                "Email input can't be empty");
            return false;
        } else if (!value.matches(Validation.EMAIL.validate())) {
            Notifications.getInstance().show(Notifications.Type.WARNING, Notifications.Location.TOP_RIGHT, 
                "Enter valid Email Address");
            return false;
        }
        return true;
    }

    public static boolean isMobileValid(String value) {
        if (value.isBlank()) {
            Notifications.getInstance().show(Notifications.Type.WARNING, Notifications.Location.TOP_RIGHT, 
                "Mobile field can't be empty");
            return false;
        } else if (!value.matches(Validation.MOBILE.validate())) {
            Notifications.getInstance().show(Notifications.Type.WARNING, Notifications.Location.TOP_RIGHT, 
                "Enter a valid Mobile Number");
            return false;
        }
        return true;
    }

    public static boolean isPasswordValid(String value) {
        if (value.isBlank()) {
            Notifications.getInstance().show(Notifications.Type.WARNING, Notifications.Location.TOP_RIGHT, 
                "Password field can't be empty");
            return false;
        }
        else if (!value.matches(Validation.PASSWORD.validate())) {
            Notifications.getInstance().show(Notifications.Type.WARNING, Notifications.Location.TOP_RIGHT, 
                "Password must include: At least one lowercase, one uppercase, " +
                "a special character, and at least one digit. " +
                "The password must be 5-7 characters long");
            return false;
        }
        return true;
    }

    public static boolean isInputFieldValid(String value) {
        if (value.isBlank()) {
            Notifications.getInstance().show(Notifications.Type.WARNING, Notifications.Location.TOP_RIGHT, 
                "Input field can't be empty");
            return false;
        }
        return true;
    }

    public static boolean isSelectedItemValid(int value) {
        if (value == 0) {
            Notifications.getInstance().show(Notifications.Type.WARNING, Notifications.Location.TOP_RIGHT, 
                "Please select a valid option");
            return false;
        }
        return true;
    }
}
