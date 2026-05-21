package com.freshcart.qa;

import com.applitools.eyes.BatchInfo;
import com.applitools.eyes.RectangleSize;
import com.applitools.eyes.MatchLevel;
import com.applitools.eyes.TestResultsSummary;
import com.applitools.eyes.selenium.BrowserType;
import com.applitools.eyes.selenium.Configuration;
import com.applitools.eyes.selenium.Eyes;
import com.applitools.eyes.selenium.fluent.Target;
import com.applitools.eyes.visualgrid.model.DeviceName;
import com.applitools.eyes.visualgrid.services.VisualGridRunner;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import org.testng.annotations.Test;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;

public class LoginPageTest {
    private WebDriver driver;
    private Eyes eyes;
    private VisualGridRunner runner;

    private static final String BRANCH = System.getenv("APPLITOOLS_BRANCH") != null
            ? System.getenv("APPLITOOLS_BRANCH") : "local";
    private static final BatchInfo BATCH = new BatchInfo("FreshCart - " + BRANCH);

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
        if (System.getenv("GITHUB_RUN_ID") != null) {
            BATCH.setId(System.getenv("GITHUB_RUN_ID"));
        }
        config.setBranchName(System.getenv("APPLITOOLS_BRANCH"));
        config.setParentBranchName("main");
        config.addBrowser(1200, 800, BrowserType.CHROME);
        config.addBrowser(1200, 800, BrowserType.FIREFOX);
        config.addBrowser(1200, 800, BrowserType.SAFARI);
        config.addDeviceEmulation(DeviceName.Pixel_2);
        config.addDeviceEmulation(DeviceName.Nexus_10);
        eyes.setConfiguration(config);
    }

    @Test
    public void testLoginPageVisual() {
        eyes.open(driver, "FreshCart", "Login Page", new RectangleSize(1200, 800));
        driver.get("https://deepak-gurejani.github.io/Eyes-testng-lab/index.html");
        eyes.check("Login Page - Strict", Target.window().fully().matchLevel(MatchLevel.STRICT));
        eyes.check("Login Page - Layout", Target.window().fully().matchLevel(MatchLevel.LAYOUT));
        eyes.check("Login Page - Content", Target.window().fully().matchLevel(MatchLevel.CONTENT));
        eyes.check("Login Page - Exact", Target.window().fully().matchLevel(MatchLevel.EXACT));
        eyes.closeAsync();
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) driver.quit();
        if (eyes != null) eyes.abortIfNotClosed();
        if (runner != null) {
            TestResultsSummary allTestResults = runner.getAllTestResults(false);
            System.out.println("UFG Results: " + allTestResults);
        }
    }
}