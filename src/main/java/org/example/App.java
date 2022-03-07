package org.example;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.LoadState;
import com.microsoft.playwright.options.WaitUntilState;
import org.junit.Assert;

import java.util.ArrayList;
import java.util.List;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        List<String> list=new ArrayList<String>();
        list.add("--start-maximized");
        try(Playwright playwright = Playwright.create()){
            Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false).setSlowMo(100).setArgs(list));
            BrowserContext browserContext = browser.newContext();
            Page page = browserContext.newPage();
            page.setDefaultNavigationTimeout(60000);
            page.setDefaultTimeout(60000);
            page.navigate("https://intragate.development.ec.europa.eu/decide-drafting/ui", new Page.NavigateOptions().setWaitUntil(WaitUntilState.NETWORKIDLE));
            page.fill("#username", "dasatya");
            page.click("[name='whoamiSubmit']");
            page.fill("#password", "Netherland1234");
            page.waitForNavigation(() -> {
                page.click("[value='Sign in']");
            });
            page.waitForSelector(".leos-header-breadcrumb-caption");
            String pageName = page.innerText(".leos-header-breadcrumb-caption");
            Assert.assertEquals(pageName,"Repository Browser");
            page.fill("//input[@placeholder='Search by title']", "dragandroptesting");
            page.waitForLoadState();
            page.waitForNavigation(() -> {
                page.click("//*[text()='Open'][1]");
            });
            pageName = page.innerText(".leos-header-breadcrumb-caption");
            Assert.assertEquals(pageName,"Proposal Viewer");
            page.waitForNavigation(() -> {
                page.click("(//*[text()='Legal Act']//ancestor::div[contains(@class,'ui-block ')]//child::div[contains(@class,'v-slot-ui-block-content')])[1]//*[text()='Open']");
            });
            pageName = page.innerText(".leos-header-breadcrumb-caption");
            Assert.assertEquals(pageName,"Legal Act");
            page.click("//img[contains(@src,'toc-edit.png')]");
            page.dragAndDrop(".v-sliderpanel-content .v-gridlayout-slot:nth-child(8) div", "table[role='treegrid'] tbody tr:nth-child(4) td div.gwt-HTML");
            page.click("//*[contains(@src,'toc-save-close')]//ancestor::div[@role='button']");
            page.waitForLoadState();
            page.waitForSelector("//*[text()='Document structure saved.']");
            page.click("//*[text()='Document structure saved.']");
            page.waitForNavigation(() -> {
                page.click(".v-align-right div.leos-document-layout");
            });
            pageName = page.innerText(".leos-header-breadcrumb-caption");
            Assert.assertEquals(pageName,"Proposal Viewer");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
