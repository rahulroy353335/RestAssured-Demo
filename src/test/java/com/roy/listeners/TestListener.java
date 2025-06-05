package com.roy.listeners;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.roy.reporting.ExtentManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class TestListener implements ITestListener {
    private static final Logger logger = LogManager.getLogger(TestListener.class);
    private static ExtentReports extent = ExtentManager.createInstance();
    private static ThreadLocal<ExtentTest> test = new ThreadLocal<>();

    @Override
    public void onTestStart(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        ExtentTest extentTest = extent.createTest(testName);
        test.set(extentTest);

        // Log test start information
        logger.info("===================================================================");
        logger.info("Starting Test: " + testName);
        logger.info("Test Description: " + result.getMethod().getDescription());
        logger.info("Test Parameters: " + getParameters(result));
        logger.info("===================================================================");
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        test.get().log(Status.PASS, MarkupHelper.createLabel(testName + " PASSED", ExtentColor.GREEN));
        logger.info("Test PASSED: " + testName);
    }

    @Override
    public void onTestFailure(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        Throwable throwable = result.getThrowable();

        // Log the failure in ExtentReport
        test.get().log(Status.FAIL, MarkupHelper.createLabel(testName + " FAILED", ExtentColor.RED));
        test.get().log(Status.FAIL, throwable);

        // Log the failure in log file
        logger.error("Test FAILED: " + testName, throwable);

        // Add screenshot if available (you can extend this for API tests if needed)
        if (result.getAttribute("screenshot") != null) {
            test.get().addScreenCaptureFromPath(result.getAttribute("screenshot").toString());
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        Throwable throwable = result.getThrowable();

        test.get().log(Status.SKIP, MarkupHelper.createLabel(testName + " SKIPPED", ExtentColor.ORANGE));
        if (throwable != null) {
            test.get().skip(throwable);
            logger.warn("Test SKIPPED: " + testName, throwable);
        } else {
            logger.warn("Test SKIPPED: " + testName);
        }
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        // This method is for tests that fail but are within success percentage
        String testName = result.getMethod().getMethodName();
        test.get().log(Status.WARNING,
                MarkupHelper.createLabel(testName + " passed within success percentage", ExtentColor.YELLOW));
        logger.warn("Test passed within success percentage: " + testName);
    }

    @Override
    public void onStart(ITestContext context) {
        logger.info("=========== STARTING TEST SUITE: " + context.getName() + " ===========");
    }

    @Override
    public void onFinish(ITestContext context) {
        extent.flush();
        logger.info("=========== FINISHED TEST SUITE: " + context.getName() + " ===========");
        logger.info("Passed Tests: " + context.getPassedTests().size());
        logger.info("Failed Tests: " + context.getFailedTests().size());
        logger.info("Skipped Tests: " + context.getSkippedTests().size());
    }

    private String getParameters(ITestResult result) {
        Object[] parameters = result.getParameters();
        if (parameters == null || parameters.length == 0) {
            return "No parameters";
        }

        StringBuilder sb = new StringBuilder();
        for (Object param : parameters) {
            sb.append(param != null ? param.toString() : "null").append(", ");
        }
        return sb.substring(0, sb.length() - 2);
    }

    public static ExtentTest getTest() {
        return test.get();
    }

}
