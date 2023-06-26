package com.api.helpers;

import org.testng.annotations.Listeners;


@Listeners({ ExtentReporterNG.class })  // For only Extent Reports
//@Listeners({ ATUReportsListener.class, ConfigurationListener.class, MethodListener.class }) // For only ATU Reports
//@Listeners({ ATUReportsListener.class, ConfigurationListener.class, MethodListener.class, ExtentReporterNG.class }) // For both ATU & Extent Reports
public class ReportBase extends TestBase{

}