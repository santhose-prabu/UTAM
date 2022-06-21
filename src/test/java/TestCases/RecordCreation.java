package TestCases;

import Base.ApplicationLogin;
import Utils.TestEnvironment;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import utam.force.pageobjects.ListViewManagerHeader;
import utam.global.pageobjects.*;
import utam.lightning.pageobjects.Combobox;
import utam.lightning.pageobjects.Picklist;
import utam.navex.pageobjects.DesktopLayoutContainer;
import utam.records.pageobjects.BaseRecordForm;
import utam.records.pageobjects.LwcRecordLayout;
import utam.records.pageobjects.RecordLayoutItem;

public class RecordCreation extends ApplicationLogin {

    private final TestEnvironment testEnvironment = getTestEnvironment("sandbox");

    @BeforeTest
    public void setup() {
        setupChrome();
        login(testEnvironment, "home");
    }

    /**
     * navigate to object home via Navigation bar and click New
     */
    private void openRecordModal() {

    /*log("Navigate to an Object Home for " + recordType.name());
    getDriver().get(recordType.getObjectHomeUrl(testEnvironment.getRedirectUrl()));*/

        getDriver().get(testEnvironment.getRedirectUrl());

        DesktopLayoutContainer dlc = from(DesktopLayoutContainer.class);
        AppNav appNav = dlc.getAppNav();
        AppNavBar appNavBar = appNav.getAppNavBar();
        AppNavBarItemRoot appNavBarMenuItem = appNavBar.getNavItem("Accounts");
        appNavBarMenuItem.clickAndWaitForUrl("lightning/o/Account");

        log("Load Accounts Object Home page");
        ConsoleObjectHome objectHome = from(ConsoleObjectHome.class);
        ListViewManagerHeader listViewHeader = objectHome.getListView().getHeader();

        log("List view header: click button 'New'");
        listViewHeader.waitForAction("New").click();

        log("Load Record Form Modal");
        RecordActionWrapper recordFormModal = from(RecordActionWrapper.class);
        Assert.assertTrue(recordFormModal.isPresent(), "record creation modal did not appear");
    }

    @Test
    public void testAccountRecordCreation() {
        openRecordModal();

        // todo - depending on org setup, modal might not present, then comment next lines
   /* log("Load Change Record Type Modal");
    RecordActionWrapper recordTypeModal = from(RecordActionWrapper.class);
    log("Change Record Type Modal: click button 'New'");
    recordTypeModal.waitForChangeRecordFooter().clickButton("Next");*/

        log("Load Record Form Modal");
        RecordActionWrapper recordFormModal = from(RecordActionWrapper.class);
        BaseRecordForm recordForm = recordFormModal.getRecordForm();
        LwcRecordLayout recordLayout = recordForm.getRecordLayout();

        log("Access record form item by index");
        RecordLayoutItem item = recordLayout.getItem(1, 2, 1);

    /*List<RecordLayoutItem> allitems=recordLayout.getAllItems();
    System.out.println(allitems);*/

        log("Enter account name");
        final String accountName = "SeasonTest";
        item.getTextInput().setText(accountName);


        log("Enter PhoneNumber");
        RecordLayoutItem phone = recordLayout.getItem(1, 2, 2);
        phone.getInput().setText("4313427453");

        log("Enter Rating");
        Picklist Rating = recordLayout.getItem(1, 1, 2).getPicklist();
        Combobox combobox = Rating.getComboBox();
        combobox.getBase().expand();
        combobox.getBase().getItemByValue("Warm");

        /*log("Enter Season");
        Picklist Season = recordLayout.getItem(3, 1, 1).getPicklist();
        Combobox combobox = Season.getComboBox();
        combobox.getBase().expand();
        combobox.getBase().getItemByValue("High");*/

        log("Save new record");
        recordForm.clickFooterButton("Save");
        recordFormModal.waitForAbsence();

        log("Load Accounts Record Home page");
        from(RecordHomeFlexipage2.class);
    }

    @AfterTest
    public final void tearDown() {
        quitDriver();
    }

}