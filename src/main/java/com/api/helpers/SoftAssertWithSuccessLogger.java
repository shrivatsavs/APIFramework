package com.api.helpers;

import com.relevantcodes.extentreports.LogStatus;
import org.testng.asserts.IAssert;
import org.testng.asserts.SoftAssert;

/**
 * Created By: Subhodip Ghosh
 * Date:- 23-Oct-2020
 */
public class SoftAssertWithSuccessLogger extends SoftAssert {
    @Override
    public void onAssertSuccess(IAssert<?> assertCommand) {
        StringBuilder sb = new StringBuilder();
        try {
            if (!assertCommand.getMessage().equals(null))
                sb.append("<span style='color:black;font-weight:bold;'>Validation: </span>" + assertCommand.getMessage());
        }catch (Exception e){

        }
        sb.append(" <span style='color:black;font-weight:bold;'>Actual: "+assertCommand.getActual()+"</span>");
        sb.append(" <span style='color:black;font-weight:bold;'>Expected: "+assertCommand.getExpected()+"</span>");
        sb.append(" Assertion "+ "<span style='color:green;font-weight:bold;'>STATUS : "+LogStatus.PASS+"</span>");
        ExtentReporterNG.log(sb.toString());
    }
}
