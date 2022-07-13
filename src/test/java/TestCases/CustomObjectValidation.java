package TestCases;

import Base.ApplicationLogin;
import Utils.FindElementHelper;
import Utils.TestEnvironment;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import utam.force.pageobjects.ListViewManagerHeader;
import utam.global.pageobjects.*;
import utam.records.pageobjects.BaseRecordForm;
import utam.records.pageobjects.LwcHighlightsPanel;
import utam.records.pageobjects.LwcRecordLayout;
import utam.records.pageobjects.RecordLayoutItem;


public class CustomObjectValidation extends ApplicationLogin {

    private final TestEnvironment testEnvironment = getTestEnvironment("sandbox");
    FindElementHelper findElementHelper;

    @BeforeTest
    public void setup() {
        setupChrome();
        login(testEnvironment, "home");
        findElementHelper = new FindElementHelper(loader,driver);
    }

    private void openApp() {

        getDriver().get(testEnvironment.getRedirectUrl());

        log("Load DreamHouseApp");
        findElementHelper.OpenApp("DreamHouse");

        debug(3);

    }

    @Test
    public void CreateProperty() {
        openApp();

        log("Load Properties tab");
        findElementHelper.NavigateTo("Properties", "Property__c");

        log("Load Properties Object Home page");
        ConsoleObjectHome objectHome = from(ConsoleObjectHome.class);
        ListViewManagerHeader listViewHeader = objectHome.getListView().getHeader();

        log("List view header: click button 'New'");
        listViewHeader.waitForAction("New").click();

        log("Load Record Form Modal");
        findElementHelper.GetRecordLayout();

        log("Access record form item by field label");
        findElementHelper.GetLayoutItems();

        log("Enter Property details");
        final String PropertyName = "Test Last Property";
        findElementHelper.GetElement("Property Name").getTextInput().setText(PropertyName);
        findElementHelper.SelectPickListValue(findElementHelper.GetElement("Status").getPicklist(), "Pre Market");
        RecordLayoutItem date = findElementHelper.GetElement("Date Listed");
        date.getRoot().scrollToCenter();
        date.getDatepicker().setDateText("6/30/2022");

        log("Save new record");
        findElementHelper.recordForm.clickFooterButton("Save");
        findElementHelper.recordFormModal.waitForAbsence();

        log("Load Accounts Record Home page");
        from(RecordHomeFlexipage2.class);
    }

    @Test
    public void EditProperty() {

        openApp();

        log("Load Properties tab");
        findElementHelper.NavigateTo("Properties", "Property__c");

        debug(3);

        log("Select All from List view");
        findElementHelper.SelectListView("00B5i000001XsrdEAC");
        /*final String AllProperties = "00B5i000001XsrdEAC";
        String List = getDriver().getCurrentUrl().replace("Recent", AllProperties);
        getDriver().get(List);*/

        debug(3);

        log("Select Property List view");
        ConsoleObjectHome objectHome = from(ConsoleObjectHome.class);
        findElementHelper.SelectItemFromList(objectHome,"211 Charles Street");

        RecordHomeFlexipage2 recordHome = from(RecordHomeFlexipage2.class);

        log("Access Record Highlights panel");
        LwcHighlightsPanel highlightsPanel = recordHome.getHighlights();

        log("Wait for button 'Edit' and click on it");
        highlightsPanel.getActions().getActionRendererWithTitle("Edit").clickButton();

        log("Load Record Form Modal");
        findElementHelper.GetRecordLayout();
       /* RecordActionWrapper recordFormModal = from(RecordActionWrapper.class);
        BaseRecordForm recordForm = recordFormModal.getRecordForm();
        LwcRecordLayout recordLayout = recordForm.getRecordLayout();*/

        log("Access record form item by field label");
        findElementHelper.GetLayoutItems();

        log("Edit Price field");
        findElementHelper.GetElement("Price").getTextInput().setText("1150000");

        log("Edit Date Closed");
        RecordLayoutItem date = findElementHelper.GetElement("Date Listed");
        date.getRoot().scrollToCenter();
        date.getDatepicker().clearDateText();
        date.getDatepicker().setDateText("6/30/2022");

        log("Save updated record");
        findElementHelper.recordForm.clickFooterButton("Save");
        findElementHelper.recordFormModal.waitForAbsence();

    }


}