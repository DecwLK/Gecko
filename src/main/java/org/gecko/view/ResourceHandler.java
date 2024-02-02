package org.gecko.view;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import lombok.Getter;
import lombok.Setter;

public class ResourceHandler {
    @Getter
    @Setter
    private static Locale currentLocale = Locale.getDefault();

    public static String getString(String bundle, String key) {
        return getBundle(bundle).getString(key);
    }

    private static ResourceBundle getBundle(String bundleName) {
        String bundlePath = "lang/";
        ResourceBundle bundle;
        try {
            bundle = ResourceBundle.getBundle(bundlePath + bundleName, currentLocale);
        } catch (MissingResourceException e) {
            bundle = ResourceBundle.getBundle(bundlePath + bundleName, Locale.US);
        }
        return bundle;
    }
}
