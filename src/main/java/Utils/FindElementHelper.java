package Utils;

import org.openqa.selenium.WebDriver;
import utam.core.framework.consumer.UtamLoader;
import utam.flexipage.pageobjects.Component2;
import utam.flexipage.pageobjects.Tab2;
import utam.force.pageobjects.ListViewManagerPrimaryDisplayManager;
import utam.global.pageobjects.*;
import utam.lightning.pageobjects.Combobox;
import utam.lightning.pageobjects.Picklist;
import utam.lightning.pageobjects.TabBar;
import utam.lightning.pageobjects.Tabset;
import utam.lists.pageobjects.*;
import utam.navex.pageobjects.DesktopLayoutContainer;
import utam.records.pageobjects.BaseRecordForm;
import utam.records.pageobjects.LwcDetailPanel;
import utam.records.pageobjects.LwcRecordLayout;
import utam.records.pageobjects.RecordLayoutItem;

import java.util.List;

public class FindElementHelper {
    private final UtamLoader loader;
    private final WebDriver driver;
    public List<RecordLayoutItem> items;
    public RecordActionWrapper recordFormModal;
    public BaseRecordForm recordForm;
    public LwcRecordLayout recordLayout;

    public FindElementHelper(UtamLoader Loader, WebDriver Driver) {
        this.loader = Loader;
        this.driver = Driver;
    }

    public void NavigateTo(String tab, String ApiName) {
        DesktopLayoutContainer dlc = loader.load(DesktopLayoutContainer.class);
        dlc.waitForVisible();
        AppNav appNav = dlc.getAppNav();
        AppNavBar appNavBar = appNav.getAppNavBar();
        AppNavBarItemRoot appNavBarMenuItem = appNavBar.getNavItem(tab);
        appNavBarMenuItem.clickAndWaitForUrl("lightning/o/" + ApiName);
        //appNavBarMenuItem.getRoot().click();
    }

    public void OpenApp(String AppName) {
        DesktopLayoutContainer dlc = loader.load(DesktopLayoutContainer.class);
        AppNav appNav = dlc.getAppNav();
        if (!(appNav.getAppName().getText()).equalsIgnoreCase(AppName)) {
            appNav.getAppLauncherHeader().getButton().click();
            AppLauncherMenu appLauncherMenu = loader.load(AppLauncherMenu.class);
            appLauncherMenu.getSearchBar().getLwcInput().setText(AppName);
            appLauncherMenu.getItems().get(0).getRoot().click();
        }
    }

    public void SelectPickListValue(Picklist Field, String Value) {
        Combobox combobox = Field.getComboBox();
        combobox.getBase().getRoot().scrollToCenter();
        combobox.getBase().expand();
        combobox.getBase().getItemByValue(Value).clickItem();
    }

    public void GetRelatedList(RecordHomeFlexipage2 recordhome, String object) {
        Component2 Related = recordhome.getRecordHomeTemplateDesktopWithSubheader().getComponent2("flexipage_tabset2");
        Tabset RelatedTabset = Related.getTabset2().getTabset();
        Tab2 RelatedList = RelatedTabset.getActiveTabContent(Tab2.class);
        RelatedListContainer relatedListContainer = RelatedList.getRelatedListContainer();
        RelatedListSingleContainer relatedListSingleContainer = relatedListContainer.getRelatedList("Therapy Cycles");
        relatedListSingleContainer.getRoot().scrollToCenter();

        RelatedListSingleAppBuilderMapper relatedListSingleAppBuilderMapper = relatedListSingleContainer.getAppBuilderList();
        RelatedListViewManager relatedListViewManager = relatedListSingleAppBuilderMapper.getRelatedListViewManager();
        //RelatedListViewManager.ViewAllElement viewAllElement = relatedListViewManager.getViewAll();
        relatedListViewManager.getCommonListInternal().getHeader().getTitle().click();

    }

    public LwcRecordLayout GetRecordLayout() {
        recordFormModal = loader.load(RecordActionWrapper.class);
        recordForm = recordFormModal.getRecordForm();
        recordLayout = recordForm.getRecordLayout();
        return recordLayout;
    }

    public void GetLayoutItems() {
        items = recordLayout.getAllItems();
    }

    public RecordLayoutItem GetElement(String Label) {
        return items.stream().filter((ri) -> ri.getRoot().getText().contains(Label)).findFirst().get();
    }

    public void SelectItemFromList(ConsoleObjectHome objectHome, String item) {
        ListViewManagerPrimaryDisplayManager grid = objectHome.getListView().getListViewContainer(ListViewManagerPrimaryDisplayManager.class);
        grid.getRecordLayout().getRecordLink(item).click();
    }

    public void OpenRelatedList(RecordHomeFlexipage2 recordhome) {
        TabBar.TabByLabelElement Related = recordhome.getRecordHomeTemplateDesktop2().getTabset2().getTabset().getTabBar().getTabByLabel("Related");
        Related.click();
    }

    public void GetDetails(RecordHomeFlexipage2 recordhome) {

        LwcDetailPanel detailPanel = recordhome.getTabset().getActiveTabContent(Tab2.class).getDetailPanel();
        items = detailPanel.getBaseRecordForm().getRecordLayout().getAllItems();
    }

    public RecordLayoutItem GetItemText(String Label) {
        return items.stream().filter((ri) -> ri.getLabel().getText().contains(Label)).findFirst().get();
    }

    public void GetDetailsTab(RecordHomeFlexipage2 recordhome) {
        Tabset tabset = recordhome.getTabset();
        TabBar tabBar = tabset.getTabBar();
        String activeTabName = tabBar.getActiveTabText();
        if (!"Details".equalsIgnoreCase(activeTabName)) {
            tabBar.clickTab("Details");
        }
    }

    public void SelectListView(String listViewName) {
        String List = driver.getCurrentUrl().replace("Recent", listViewName);
        driver.get(List);
    }

}
