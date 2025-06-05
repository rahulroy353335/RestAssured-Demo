package com.roy.base;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.roy.config.ConfigReader;
import com.roy.listeners.TestListener;
import com.roy.reporting.ExtentManager;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.lang.reflect.Method;

@Listeners(TestListener.class)
public class BaseTest {
    protected static final Logger logger = LogManager.getLogger(BaseTest.class);
    protected static ExtentReports extent = ExtentManager.createInstance();
    protected ExtentTest test;

    @BeforeSuite
    public void beforeSuite() {
        RestAssured.baseURI = ConfigReader.get("baseUrl");
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
    }

    @BeforeMethod
    public void beforeMethod(Method method, ITestContext context) {
        String testName = method.getName();
        test = extent.createTest(testName);
        context.setAttribute("test", test);
        logger.info("Starting test: " + testName);
    }

    @AfterMethod
    public void afterMethod(ITestResult result) {
        if (result.getStatus() == ITestResult.FAILURE) {
            test.log(Status.FAIL, "Test Failed: " + result.getThrowable());
            logger.error("Test Failed: " + result.getThrowable());
        } else if (result.getStatus() == ITestResult.SKIP) {
            test.log(Status.SKIP, "Test Skipped: " + result.getThrowable());
            logger.warn("Test Skipped: " + result.getThrowable());
        } else {
            test.log(Status.PASS, "Test Passed");
            logger.info("Test Passed");
        }
    }

    @AfterSuite
    public void afterSuite() {
        extent.flush();
    }
}
