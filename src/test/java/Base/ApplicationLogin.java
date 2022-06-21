package Base;

import Utils.TestEnvironment;
import utam.core.driver.Document;
import utam.helpers.pageobjects.Login;

public class ApplicationLogin extends UtamTestBase {
    /**
     * login to the environment based on url and credentials provided in env.properties file which
     * should be located in test resources root
     *
     * @param testEnvironment environment information
     * @param landingPagePartialUrl after login, this is partial url that we land in
     */
    protected final void login(TestEnvironment testEnvironment, String landingPagePartialUrl) {
        final String baseUrl = testEnvironment.getBaseUrl();
        final String userName = testEnvironment.getUserName();
        log("Navigate to login URL: " + baseUrl);
        getDriver().get(baseUrl);
        Login loginPage = from(Login.class);
        log(
                String.format(
                        "Enter username '%s' and password, wait for landing page Url containing '%s'",
                        userName, landingPagePartialUrl));
        loginPage.login(userName, testEnvironment.getPassword());
        //Document document = getDomDocument();
        //document.waitFor(() -> document.getUrl().contains(landingPagePartialUrl));
    }
}
