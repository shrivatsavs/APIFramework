package com.api.helpers;

import static com.api.helpers.ReadWriteCSV.writeCSV;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Random;
import java.util.TimeZone;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;


public class Util {

	static Util instance;
	private static Logger logger = Logger.getLogger(Util.class);

	public static Util getInstance() {
		if (instance == null) {
			instance = new Util();
		}
		return instance;
	}

	public static void setInstance(Util instance) {
		Util.instance = instance;
	}

	public static <T> T deserializeToClass(String jString, Class<T> arg)
			throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		T mapStringToClass = mapper.readValue(jString, arg);

		logger.info("deserialized to :- " + arg);

		return mapStringToClass;
	}

	public static String serializeToStr(Object obj) throws JsonParseException,
			JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		String jsonStr = mapper.writeValueAsString(obj);
		logger.info("serialized string :- " + jsonStr);
		return jsonStr;
	}
	
	// to get file data as a string
	public static String readFileAsString(String filePath) throws IOException {

		String actualFilepath = Thread.currentThread().getContextClassLoader()
				.getResource(filePath).getPath();
		File file = new File(actualFilepath);
		String fileContents = FileUtils.readFileToString(file);

		return fileContents;
	}

	/** returns only date in format YYYY-MM-DD */
	public static Date getTodayDate() {
		java.sql.Date dt1 = new java.sql.Date(System.currentTimeMillis());
		return dt1;
	}

	/** returns current date and time in format MMM-dd-yyyy HH:mm:ss */
	public static String getCurrentDateTime() {
		DateFormat dateFormat = new SimpleDateFormat("MMM-dd-yyyy HH:mm:ss");
		Date date = new Date();
		String dateAsString = dateFormat.format(date);
		return dateAsString;
	}

	public static int getRandomTryCount(){
		Random r = new Random();
		int low = 0;
		int high = 4;
		int result = r.nextInt(high-low) + low;
		return result;
	}

	public static void waitTillPastDue() throws InterruptedException{
		Thread.sleep(90 * 10 * 1000);
	}

	public static void waitForSixMinute() throws InterruptedException{
		Thread.sleep(12 * 3 * 10000);
	}

	public static void waitForThirtySeconds() throws InterruptedException{
		Thread.sleep(30 * 1000);
	}
	public static void waitForFiveSeconds() throws InterruptedException{
		Thread.sleep(05 * 1000);
	}

	public static void waitTenSecond() throws InterruptedException {
		Thread.sleep(10*1000);
	}
	public static String convertToDecimal(double number) {
		DecimalFormat d = new DecimalFormat("#.#");
		String decNumber = d.format(number);
		return decNumber;
	}

	public static String getAvailableDateTime() {
		Date date = new Date();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		df.setTimeZone(TimeZone.getTimeZone("GMT"));
		return df.format(date.toInstant().plusSeconds(0).toEpochMilli());
	}
	public static String getPastDueDateTime() {
		Date date = new Date();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		df.setTimeZone(TimeZone.getTimeZone("GMT"));
		return df.format(date.toInstant().plusSeconds(780).toEpochMilli());
	}

	public static void getAssignPastDue(String dueDate) throws ParseException, InterruptedException{
		Date date = new Date();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		df.setTimeZone(TimeZone.getTimeZone("GMT"));
		String dateNow = df.format(date.toInstant().toEpochMilli());
		long milliSeconds =(df.parse(dueDate).getTime() - df.parse(dateNow).getTime());
		logger.info("Waiting "+(milliSeconds+(120*1000))/1000+" seconds for assignment past due");
		Thread.sleep(milliSeconds+(120*1000));
	}

	public static void ansBeforePastDue(String dueDate) throws ParseException, InterruptedException{
		Date date = new Date();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		df.setTimeZone(TimeZone.getTimeZone("GMT"));
		String dateNow = df.format(date.toInstant().toEpochMilli());
		long milliSeconds =(df.parse(dueDate).getTime() - df.parse(dateNow).getTime());
		logger.info("Waiting "+(milliSeconds-(75*1000))/1000+" seconds for assignment past due");
		Thread.sleep(milliSeconds-(75*1000));
	}

	public static void writeTime(String dueDate) {
		Date date = new Date();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		df.setTimeZone(TimeZone.getTimeZone("GMT"));
		String dateNow = df.format(date.toInstant().toEpochMilli());
	}

	public static int getRandomWithExclusion(int exclude) {
		int start = 0;
		int end = 4;
		Random rnd = new Random();
		int random;
		do{
			random = start + rnd.nextInt(end - start);
		}while(random == exclude);
		return random;
	}

	/** current time in milliseconds as string */
	public static String getDateTimeInMilisec(int currentDatePlus) {
		Date date = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE, currentDatePlus);
		date = cal.getTime();

		String dueDate = String.valueOf(date.getTime());

		return dueDate;
	}

	/** current time in milliseconds as string */
	public static long getDateTimeInMilisecLong(int currentDatePlus) {
		Date date = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE, currentDatePlus);
		date = cal.getTime();

		long dueDate = date.getTime();

		return dueDate;
	}

	public static Date convertMilisecToDate(long miliSec) {
		Date date = new Date(miliSec);
		logger.info("converted date : " + date);
		return date;
	}

	/** returns 7 digit random number */
	public static int getRandomNo() {
		int num = 0;
		num = (int) ((Math.random() * 9000000) + 1000000);
		return num;
	}

	/** returns only date in format YYYY-MM-DD */
	public static String getDate(long date)
	{
		java.sql.Date dt1 = new java.sql.Date(date);
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
		String dateAsString = dateFormat.format(dt1);
		return dateAsString;
	}

	public static long getTime(long time)
	{

		long dueTime=0;
		if(Calendar.getInstance().get(Calendar.AM_PM)==1)
		{
			long hour = (Calendar.getInstance().get(Calendar.HOUR)+12)*(3600000);
			int x = Calendar.getInstance().get(Calendar.MINUTE);
			double y = x*0.016666;

			long min = (long) (y*(3600000));
			long currentTime = hour+min;

			dueTime = currentTime + time;
			logger.info("Expected DueTime is"+dueTime);

		}

		else if((Calendar.getInstance().get(Calendar.AM_PM)==0))
		{
			long hour = (Calendar.getInstance().get(Calendar.HOUR))*(3600000);
			int x = Calendar.getInstance().get(Calendar.MINUTE);
			double y = x*0.016666;

			long min = (long) (y*(3600000));
			long currentTime = hour+min;

			dueTime = currentTime + time;
			logger.info("Expected DueTime is"+dueTime);
		}
		return dueTime;
	} public static long getTime()
	{

		long dueTime=0;
		if(Calendar.getInstance().get(Calendar.AM_PM)==1)
		{
			long hour = (Calendar.getInstance().get(Calendar.HOUR)+12)*(3600000);
			int x = Calendar.getInstance().get(Calendar.MINUTE);
			double y = x*0.016666;

			long min = (long) (y*(3600000));
			long currentTime = hour+min;

			dueTime = currentTime + 900000;
			logger.info("Expected DueTime is"+dueTime);

		}

		else if((Calendar.getInstance().get(Calendar.AM_PM)==0))
		{
			long hour = (Calendar.getInstance().get(Calendar.HOUR))*(3600000);
			int x = Calendar.getInstance().get(Calendar.MINUTE);
			double y = x*0.016666;

			long min = (long) (y*(3600000));
			long currentTime = hour+min;

			dueTime = currentTime + 900000;
			logger.info("Expected DueTime is"+dueTime);
		}
		return dueTime;
	}

	/** current time+next 2 milliseconds in milliseconds as long */
	public static String getDateTimeInMilisecLong() {
		Date date = new Date();
		//This method returns the time in millis
		long timeMilli = date.getTime()+120000;
		String dueDate = Long.toString(timeMilli);
		return dueDate;
	}

	/**Created to support FLA Time for PLP */
	public static String getCurrentFLATime(){
		String time = OffsetDateTime.now( ZoneOffset.UTC ).truncatedTo(ChronoUnit.SECONDS).toString();
		return time;
	}

	public static String getDueDate(long days){
		OffsetDateTime o = OffsetDateTime.now(ZoneOffset.UTC ).truncatedTo(ChronoUnit.SECONDS);
		OffsetDateTime d =  o.plusDays(days);
		return d.toString();
	}

	public static LinkedHashMap<String, String> getListOfStudents() throws FileNotFoundException {
		List<String> list = ReadWriteCSV.readCSV(System.getProperty("user.dir") + "/InputExcelSheets/25_PPE_Students.csv");
		LinkedHashMap<String,String> map = new LinkedHashMap<>();
		list.stream().forEach(line -> {
			String[] str = line.split(",");
			map.put(str[0],str[1]);
		});
		return map;
	}
	public static void writeStudentDataInCSV(String studentName,String instructorScore, String studentScore) throws IOException {
		String path=System.getProperty("user.dir")+File.separator+"result.csv";
		StringBuilder sb = new StringBuilder();
		sb.append(studentName);
		sb.append(",");
		sb.append(instructorScore);
		sb.append(",");
		sb.append(studentScore);
		writeCSV(sb.toString(),path);
	}
	/** returns date and time in format MMM-dd-yyyy HH:mm:ss ,with month number */
	public static String getDateTime(long millis) {
		java.sql.Date dt1 = new java.sql.Date(millis);
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateAsString = dateFormat.format(dt1);
		return dateAsString;
	}
	/** get time difference between two number in sec */
	public static String timeDifference(String time1,String time2) throws ParseException {

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date1 = dateFormat.parse(time1);
		Date date2 = dateFormat.parse(time2);
		String difference = String.valueOf((date1.getTime() - date2.getTime())/1000);
		return difference;
	}

	/**Add minute to the time and return the new time
	 * @throws ParseException */
	public static String getNewTime(String date,int time) throws ParseException {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date d = df.parse(date);
		Calendar cal = Calendar.getInstance();
		cal.setTime(d);
		cal.add(Calendar.MINUTE, time);
		String newTime = df.format(cal.getTime());
		return newTime;
	}

	/**get the date based on number of days added to the current date */
	public static String getDueDateTime(int noOfDays) {

		DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DAY_OF_MONTH,noOfDays);
		String newDate = df.format(c.getTime());
		return newDate;
	}
	public static String generateRandomString(int count) {
		StringBuilder sb = new StringBuilder(count);
		String randomString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "abcdefghijklmnopqrstuvxyz";
		for (int i = 0; i < count; i++) {
			int index = (int) (randomString.length() * Math.random());
			sb.append(randomString.charAt(index));
		}
		return sb.toString();
	}
	public static String getFutureTimeStampInUtcZone(String type, int value) {
		String timeStamp = "";
		switch (type) {
			case "millis":
				timeStamp = DateTime.now().withZone(DateTimeZone.UTC)
						.plusMillis(value).toString();
				break;
			case "seconds":
				timeStamp = DateTime.now().withZone(DateTimeZone.UTC)
						.plusMinutes(value).toString();
				break;
			case "minutes":
				timeStamp = DateTime.now().withZone(DateTimeZone.UTC)
						.plusMinutes(value).toString();
				break;
			case "days":
				timeStamp = DateTime.now().withZone(DateTimeZone.UTC)
						.plusDays(value).toString();
				break;
		}
		return timeStamp;
	}

	public static boolean getformattedDate(String editdateString,String dueDays) {


		LocalDateTime olddate = LocalDateTime.parse(editdateString,DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"));
		DateTimeFormatter newPattern = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
		String output = olddate.format(newPattern);
		LocalDateTime editednewDate = LocalDateTime.parse(output,newPattern);


		LocalDateTime olddate1 = LocalDateTime.parse(dueDays,DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'"));
		DateTimeFormatter newPattern1 = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
		String output1 = olddate1.format(newPattern1);
		LocalDateTime dueDate = LocalDateTime.parse(output1,newPattern1);

		if (dueDate.equals(editednewDate)) {
			return true;
		}
          else{
	         return  false;
          }
	}
	
	private static String relativePath;

	/**
	 * Function to get the absolute path of the framework (to be used as a relative
	 * path)
	 * 
	 * @return The absolute path of the framework
	 */
	public static String getRelativePath() {

		if (relativePath.equals("") || relativePath == null) {
			return new File(System.getProperty("user.dir")).getAbsolutePath();
		}
		else {
			return relativePath;
		}

	}

	/**
	 * Function to set the absolute path of the framework (to be used as a relative
	 * path)
	 * 
	 * @param relativePath The absolute path of the framework
	 */
	public static void setRelativePath(String relativePath) {

		Util.relativePath = relativePath;
	}

	/**
	 * Function to get the separator string to be used for directories and files
	 * based on the current OS
	 * 
	 * @return The file separator string
	 */
	public static String getFileSeparator() {
		return System.getProperty("file.separator");
	}

	/**
	 * Function to return the current time
	 * 
	 * @return The current time
	 * @see #getCurrentFormattedTime(String)
	 */
	public static Date getCurrentTime() {
		final Calendar calendar = Calendar.getInstance();
		return calendar.getTime();
	}

	/**
	 * Function to return the current time, formatted as per the DateFormatString
	 * setting
	 * 
	 * @param dateFormatString The date format string to be applied
	 * @return The current time, formatted as per the date format string specified
	 * @see #getCurrentTime()
	 * @see #getFormattedTime(Date, String)
	 */
	public static String getCurrentFormattedTime(String dateFormatString) {
		DateFormat dateFormat = new SimpleDateFormat(dateFormatString);
		Calendar calendar = Calendar.getInstance();
		return dateFormat.format(calendar.getTime());
	}

	/**
	 * Function to format the given time variable as specified by the
	 * DateFormatString setting
	 * 
	 * @param time             The date/time variable to be formatted
	 * @param dateFormatString The date format string to be applied
	 * @return The specified date/time, formatted as per the date format string
	 *         specified
	 * @see #getCurrentFormattedTime(String)
	 */
	public static String getFormattedTime(Date time, String dateFormatString) {
		DateFormat dateFormat = new SimpleDateFormat(dateFormatString);
		return dateFormat.format(time);
	}

}


   
