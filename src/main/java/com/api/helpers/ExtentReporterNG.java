package com.api.helpers;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.testng.IReporter;
import org.testng.IResultMap;
import org.testng.ISuite;
import org.testng.ISuiteResult;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.xml.XmlSuite;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

public class ExtentReporterNG implements IReporter {
    private ExtentReports extent;
    public String fileseparator  = File.separator;
    String build_number = (System.getProperty("build_number") == null) ? "local" : System.getProperty("build_number");//Specify the build number,Replace "System.getProperty" with your property file location and make sure that build_number is specified in the property file
    String groups = (PropertyFiles.getProperty("env") == null) ? "Default" : PropertyFiles.getProperty("env");//Specify the testing environment, Replace "RevelProperties.getProperty" with your property file location and make sure that env is specified in the property file
    static Multimap<String, String> logMap = ArrayListMultimap.create();
    

    @Override
    public void generateReport(List<XmlSuite> xmlSuites, List<ISuite> suites, String outputDirectory) {
    	File fileDir = new File(System.getProperty("user.dir")+"/ExtentReports");
    	if (!fileDir.exists()) 
       	 	fileDir.mkdir();
    	String filepath  = fileDir+fileseparator+"API_AUTOMATION_REPORT_"+build_number+".html";//Specify the report name
    	
	   	   
    	if (build_number.equalsIgnoreCase("local"))
    	{
  		   filepath = fileseparator+filepath;
  		   File file = new File(filepath);
  		   
  		   if (!new File(fileseparator+"ExtentReports").exists()) {
  			   new File(fileseparator+"ExtentReports").mkdir();
  			fileDir.mkdir();
  		   }   
  		   
  		   if (file.exists()){
  			   String now = new SimpleDateFormat("dd-MMM-yyyy-HH-mm-ss").format(Calendar.getInstance().getTime());
  			   String newfile = file.getAbsolutePath().replace(build_number, now);
  			   file.renameTo(new File(newfile));
          	} 
      	}   
     	
    	extent = new ExtentReports(filepath, true);
    	
    	extent.config().documentTitle("API AUTOMATION REPORT")//Specify the document name
        .reportName(groups.toUpperCase()).reportHeadline("Automation Suite".toUpperCase());
    	
    	extent.addSystemInfo("Environment", groups.toUpperCase());
    	
    	

        for (ISuite suite : suites) {
            Map<String, ISuiteResult> result = suite.getResults();
           
            for (ISuiteResult r : result.values()) {
                ITestContext context = r.getTestContext();
                          
                buildTestNodes(context.getFailedTests(), LogStatus.FAIL);
                buildTestNodes(context.getSkippedTests(), LogStatus.SKIP);
                buildTestNodes(context.getPassedTests(), LogStatus.PASS);
            }
        }

        extent.flush();
        extent.close();
    }
    
    private void buildTestNodes(IResultMap tests, LogStatus status) {
        ExtentTest test;
  
        if (tests.size() > 0) {
            for (ITestResult result : tests.getAllResults()) {
             String message = "";  
             Throwable exception =  result.getThrowable();
             String testname = result.getName();
                         
             test = extent.startTest(testname);
  
                test.getTest().startedTime = getTime(result.getStartMillis());
                test.getTest().endedTime = getTime(result.getEndMillis());                
                              
                for (String log : logMap.get(result.getTestClass().getRealClass().getPackage().getName()+result.getTestClass().getRealClass().getSimpleName()+testname+result.getAttribute("iteration"))){
                	test.log(LogStatus.INFO, log);                  
                }       
                
                for (String group : result.getMethod().getGroups())
                    test.assignCategory(group);                  
  
                if (exception != null){     
                	message = "<span style='color:blue;font-weight:bold;'>REASON : </span>"+getStackTrace(exception);
                	
                }
                else{
                	message = "<span style='color:green;font-weight:bold;'>STATUS : "+status+"</span>"; 
                	
                }        
                test.log(status, message);        
                extent.endTest(test); 
            }
        }
    }
    public static void log(String log) {    
    	logMap.put(Reporter.getCurrentTestResult().getTestClass().getRealClass().getPackage().getName()+Reporter.getCurrentTestResult().getTestClass().getRealClass().getSimpleName()+Reporter.getCurrentTestResult().getName()+Reporter.getCurrentTestResult().getAttribute("iteration"), log);   
    }
    
    private Date getTime(long millis) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);
        return calendar.getTime();
    }
    
    public static String getStackTrace(Throwable throwable) {
        if (throwable == null){
         return "Exception is Null";
        }
        StringBuilder exception = new StringBuilder();
        exception.append(throwable.getMessage());
        
        StackTraceElement[] trace = throwable.getStackTrace();
        
        for (int i=0; i<=10; i++){
         exception.append(trace[i]);   
        }
        return exception.toString();
     }
}
