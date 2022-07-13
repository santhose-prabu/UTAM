package TestCases;

import Base.ApplicationLogin;
import Utils.FindElementHelper;
import Utils.TestEnvironment;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import utam.force.pageobjects.ChangeRecordType;
import utam.force.pageobjects.ListViewManagerHeader;
import utam.global.pageobjects.*;
import utam.lightning.pageobjects.Combobox;
import utam.lightning.pageobjects.Picklist;
import utam.navex.pageobjects.DesktopLayoutContainer;
import utam.records.pageobjects.BaseRecordForm;
import utam.records.pageobjects.LwcHighlightsPanel;
import utam.records.pageobjects.LwcRecordLayout;
import utam.records.pageobjects.RecordLayoutItem;

import java.util.List;

public class RecordCreation extends ApplicationLogin {

    private final TestEnvironment testEnvironment = getTestEnvironment("sandbox");
    FindElementHelper findElementHelper;

    @BeforeTest
    public void setup() {
        setupChrome();
        login(testEnvironment, "home");
        findElementHelper = new FindElementHelper(loader, driver);
    }

    /**
     * navigate to object home via Navigation bar and click New
     */
    private void openRecordModal() {

        getDriver().get(testEnvironment.getRedirectUrl());

        findElementHelper.OpenApp("Service");

        debug(3);

        DesktopLayoutContainer dlc = loader.load(DesktopLayoutContainer.class);
        AppNav appNav = dlc.getAppNav();
        AppNavBar appNavBar = appNav.getAppNavBar();
        AppNavBarItemRoot appNavBarMenuItem = appNavBar.getNavItem("Accounts");
        appNavBarMenuItem.clickAndWaitForUrl("lightning/o/Account");

        log("Load Accounts Object Home page");
        ConsoleObjectHome objectHome = loader.load(ConsoleObjectHome.class);
        ListViewManagerHeader listViewHeader = objectHome.getListView().getHeader();

        log("List view header: click button 'New'");
        listViewHeader.waitForAction("New").click();

        log("Load Record Form Modal");
        RecordActionWrapper recordFormModal = loader.load(RecordActionWrapper.class);
        Assert.assertTrue(recordFormModal.isPresent(), "record creation modal did not appear");
    }

    @Test
    public void testAccountRecordCreation() {
        openRecordModal();

        log("Load Record Form Modal");
        RecordActionWrapper recordFormModal = loader.load(RecordActionWrapper.class);
        BaseRecordForm recordForm = recordFormModal.getRecordForm();
        LwcRecordLayout recordLayout = recordForm.getRecordLayout();

        log("Get all Items and access record form items by field label");
        List<RecordLayoutItem> items = recordLayout.getAllItems();
        findElementHelper.items=items;

        log("Enter account name");
        final String accountName = "UTAM Demo";
        RecordLayoutItem AccountName = items.stream().filter((ri) -> ri.getRoot().getText().contains("Account Name")).findFirst().get();
        AccountName.getTextInput().setText(accountName);

        log("Enter PhoneNumber");
        //RecordLayoutItem phone = recordLayout.getItem(1, 2, 2);
        RecordLayoutItem phone = items.stream().filter((ri) -> ri.getRoot().getText().contains("Phone")).findFirst().get();
        phone.getInput().setText("4313427453");

        log("Enter Rating");
        //Picklist Rating = recordLayout.getItem(1, 1, 2).getPicklist();
        RecordLayoutItem item = findElementHelper.GetElement("Rating");
        Picklist Rating = item.getPicklist();
        Combobox combobox = Rating.getComboBox();
        combobox.getBase().expand();
        combobox.getBase().getItemByValue("Warm").clickItem();

        log("Enter Season");
        findElementHelper.SelectPickListValue(findElementHelper.GetElement("Season").getPicklist(), "Summer");

        log("Enter Description");
        RecordLayoutItem Description = findElementHelper.GetElement("Description");
        Description.getRecordTextArea().getTextArea().clearAndEnterText("LocateElement");

        log("Save new record");
        recordForm.clickFooterButton("Save");
        recordFormModal.waitForAbsence();

        log("Load Accounts Record Home page");
        from(RecordHomeFlexipage2.class);
    }

    @Test
    public void EditAccountRecord() {

        findElementHelper.OpenApp("Service");

        debug(3);

        log("Load Accounts tab");
        findElementHelper.NavigateTo("Accounts", "Account");

        debug(3);

        log("Select All from List view");
        findElementHelper.SelectListView("00B5i000006i1KuEAI");

        debug(3);

        log("Select Account List view");
        ConsoleObjectHome objectHome = from(ConsoleObjectHome.class);
        findElementHelper.SelectItemFromList(objectHome, "GenePoint");

        RecordHomeFlexipage2 recordHome = from(RecordHomeFlexipage2.class);

        findElementHelper.GetDetailsTab(recordHome);

        log("Access Record Highlights panel");
        LwcHighlightsPanel highlightsPanel = recordHome.getHighlights();

        log("Wait for button 'Edit' and click on it");
        highlightsPanel.getActions().getDropdownButton().clickButton();
        highlightsPanel.getActions().getActionRendererWithTitle("Edit").getRibbonMenuItem().clickLinkItem();

        log("Load Record Form Modal");
        findElementHelper.GetRecordLayout();

        log("Access record form item by field label");
        findElementHelper.GetLayoutItems();

        log("Edit Rating field");
        findElementHelper.SelectPickListValue(findElementHelper.GetElement("Rating").getPicklist(), "Warm");

        log("Edit Season field");
        findElementHelper.SelectPickListValue(findElementHelper.GetElement("Season").getPicklist(), "Summer");

        log("Save updated record");
        findElementHelper.recordForm.clickFooterButton("Save");
        findElementHelper.recordFormModal.waitForAbsence();

    }

    @Test
    public void CreatePersonAccount() {

        openRecordModal();

        debug(3);

        // todo - depending on org setup, modal might not present, then comment next lines
        log("Load Change Record Type Modal");
        RecordActionWrapper recordTypeModal = from(RecordActionWrapper.class);
        ChangeRecordType changeRecordType = from(ChangeRecordType.class);
        changeRecordType.getSelectableChangeRecordTypeRadioButtons().get(5).select();
        log("Change Record Type Modal: click button 'Next'");
        recordTypeModal.waitForChangeRecordFooter().clickButton("Next");

        debug(2);

        log("Load Record Form Modal");
        findElementHelper.GetRecordLayout();

        debug(3);

        log("Access record form item by field label");
        findElementHelper.GetLayoutItems();

        log("Enter Account Details");
        findElementHelper.SelectPickListValue(findElementHelper.GetElement("Patient Status").getPicklist(), "Active");
        findElementHelper.recordLayout.getInputName().getInputName().getSalutationPicklist().getComboBox().getBase().getRoot().click();
        findElementHelper.recordLayout.getInputName().getInputName().getSalutationPicklist().getComboBox().getBase().getItemByValue("Mr.").clickItem();
        findElementHelper.recordLayout.getInputName().getInputName().getFirstNameInput().setText("Test");
        findElementHelper.recordLayout.getInputName().getInputName().getLastNameInput().setText("UAT");


        log("Save new record");
        findElementHelper.recordForm.clickFooterButton("Save");
        findElementHelper.recordFormModal.waitForAbsence();

        log("Load Accounts Record Home page");
        from(RecordHomeFlexipage2.class);

    }

    @AfterTest
    public final void tearDown() {
        quitDriver();
    }

}
