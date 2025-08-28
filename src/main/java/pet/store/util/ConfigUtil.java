package pet.store.util;

import lombok.experimental.UtilityClass;
import pet.store.model.enums.Environment;

import java.util.ResourceBundle;

import static java.lang.String.format;

@UtilityClass
public class ConfigUtil {

    private static final String ENVIRONMENT_LOWER_CASE;
    private static final ResourceBundle BUNDLE;

    static {
        BUNDLE = ResourceBundle.getBundle("config");
        ENVIRONMENT_LOWER_CASE = getEnvironment()
                .name()
                .toLowerCase();
    }

    private static String getProperty(String property) {
        return BUNDLE.getString(property);
    }

    public static Environment getEnvironment() {
        return Environment.valueOf(getProperty("environment").toUpperCase());
    }

    public static String getPetStoreUrl() {
        return getProperty(format("pet.store.%s.url", ENVIRONMENT_LOWER_CASE));
    }

}
