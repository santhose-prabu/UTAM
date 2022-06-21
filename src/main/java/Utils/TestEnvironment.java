package Utils;

import java.util.ResourceBundle;
import java.util.Set;

public class TestEnvironment {
    private static final String MISSING_PROPERTY_ERR = "Property '%s' is not set in env.properties";

    private final String envPrefix;
    private final String baseUrl;
    private final String redirectUrl;
    private final String userName;
    private final String password;

    public TestEnvironment(String envNamePrefix) {
        this.envPrefix = envNamePrefix;
        ResourceBundle resourceBundle = ResourceBundle.getBundle("env");
        Set<String> keys = resourceBundle.keySet();
        String urlKey = getBaseUrlKey();
        this.baseUrl = keys.contains(urlKey) ? wrapUrl(resourceBundle.getString(urlKey)) : "";
        String usernameKey = getUsernameKey();
        this.userName = keys.contains(usernameKey) ? resourceBundle.getString(usernameKey) : "";
        String passwordKey = getPasswordKey();
        this.password = keys.contains(passwordKey) ? resourceBundle.getString(passwordKey) : "";
        String redUrlKey = getRedirectUrlKey();
        this.redirectUrl = keys.contains(redUrlKey) ? wrapUrl(resourceBundle.getString(redUrlKey)) : "";
    }

    private static String wrapUrl(String url) {
        String transformed = url;
        // if url does not start from http or https - add
        if (!url.startsWith("http")) {
            transformed = "http://" + url;
        }
        // if url does not end with "/", add
        if (!url.endsWith("/")) {
            transformed = transformed.concat("/");
        }
        return transformed;
    }

    private String getBaseUrlKey() {
        return envPrefix + ".url";
    }

    private String getRedirectUrlKey() {
        return envPrefix + ".redirectUrl";
    }

    private String getUsernameKey() {
        return envPrefix + ".username";
    }

    private String getPasswordKey() {
        return envPrefix + ".password";
    }

    public String getBaseUrl() {
        if (baseUrl.isEmpty()) {
            throw new IllegalArgumentException(String.format(MISSING_PROPERTY_ERR, getBaseUrlKey()));
        }
        return baseUrl;
    }

    public String getUserName() {
        if (userName.isEmpty()) {
            throw new IllegalArgumentException(String.format(MISSING_PROPERTY_ERR, getUsernameKey()));
        }
        return userName;
    }

    public String getPassword() {
        if (password.isEmpty()) {
            throw new IllegalArgumentException(String.format(MISSING_PROPERTY_ERR, getPasswordKey()));
        }
        return password;
    }

     public String getRedirectUrl() {
        if (redirectUrl.isEmpty()) {
            return baseUrl;
        }
        return redirectUrl;
    }
}