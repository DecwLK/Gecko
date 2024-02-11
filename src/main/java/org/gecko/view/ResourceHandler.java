package org.gecko.view;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import lombok.Getter;
import lombok.Setter;

/** Provides methods for getting {@link String}s that are dependent on a given key and the Language currently used in the view. */
public class ResourceHandler {
    @Getter
    @Setter
    private static Locale currentLocale = Locale.getDefault();

    /**
     * Returns the localized string for the given key from the given bundle.
     *
     * @param bundle the name of the bundle
     * @param key    the key of the string
     * @return the localized string
     */
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
