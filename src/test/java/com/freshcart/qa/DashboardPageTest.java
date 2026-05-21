package com.freshcart.qa;

import com.applitools.eyes.BatchInfo;
import com.applitools.eyes.RectangleSize;
import com.applitools.eyes.TestResultsSummary;
import com.applitools.eyes.selenium.BrowserType;
import com.applitools.eyes.selenium.Configuration;
import com.applitools.eyes.selenium.Eyes;
import com.applitools.eyes.selenium.fluent.Target;
import com.applitools.eyes.visualgrid.services.VisualGridRunner;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import org.testng.annotations.Test;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;

public class DashboardPageTest {
    private WebDriver driver;
    private Eyes eyes;
    private VisualGridRunner runner;

    private static final String BRANCH = System.getenv("APPLITOOLS_BRANCH") != null
            ? System.getenv("APPLITOOLS_BRANCH") : "local";
    private static final String BATCH_ID = System.getenv("APPLITOOLS_BATCH_ID") != null
            ? System.getenv("APPLITOOLS_BATCH_ID")
            : (System.getenv("GITHUB_RUN_ID") != null
            ? System.getenv("GITHUB_RUN_ID")
            : "local-" + System.currentTimeMillis() / 60000);
    private static final BatchInfo BATCH = createBatch();

    private static BatchInfo createBatch() {
        BatchInfo batch = new BatchInfo("FreshCart - " + BRANCH);
        batch.setId(BATCH_ID);
        return batch;
    }

    @BeforeClass
    public void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new");
        driver = new ChromeDriver(options);

        runner = new VisualGridRunner(5);
        eyes = new Eyes(runner);

        Configuration config = eyes.getConfiguration();
        config.setApiKey(System.getenv("APPLITOOLS_API_KEY"));
        config.setBatch(BATCH);
        config.setBranchName(System.getenv("APPLITOOLS_BRANCH"));
        config.setParentBranchName("main");
        config.addBrowser(1200, 800, BrowserType.CHROME);
        eyes.setConfiguration(config);
    }

    @Test
    public void testDashboardWithIgnoreRegion() {
        eyes.open(driver, "FreshCart", "Dashboard", new RectangleSize(1200, 800));
        driver.get("https://deepak-gurejani.github.io/Applitools-FreshCart-demo/dashboard.html");

        eyes.check("Dashboard - Ignore Region (timestamp)",
                Target.window().fully()
                        .ignore(driver.findElement(By.id("liveTimestamp"))));

        eyes.closeAsync();
    }

    @Test
    public void testDashboardWithFloatingRegion() {
        eyes.open(driver, "FreshCart", "Dashboard", new RectangleSize(1200, 800));
        driver.get("https://deepak-gurejani.github.io/Applitools-FreshCart-demo/dashboard.html");

        eyes.check("Dashboard - Floating Region (timestamp)",
                Target.window().fully()
                        .floating(driver.findElement(By.id("liveTimestamp")), 10, 10, 10, 10));

        eyes.closeAsync();
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) driver.quit();
        if (eyes != null) eyes.abortIfNotClosed();
        if (runner != null) {
            TestResultsSummary allTestResults = runner.getAllTestResults(false);
            System.out.println("Dashboard UFG Results: " + allTestResults);
        }
    }
}