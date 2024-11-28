package com.capstone.utils;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.Browser;
import org.openqa.selenium.remote.SessionId;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.DataProvider;

import com.capstone.constants.BrowserType;
import com.capstone.constants.DriverType;
import com.capstone.settings.DriverContext;
import com.capstone.utils.loggers.LoggerUtil;
import com.epam.healenium.SelfHealingDriver;

import io.github.bonigarcia.wdm.WebDriverManager;

public class CustomSetup {
	public PropertyManager propertyManager=null;
	public CustomWebDriver driver=null;
	private Map<String,String> containerDetails=new HashMap();

	@DataProvider(name="dataProvider")
	public Object[][] fetchData(Method method)
	{
		
		return null;
		
	}
	@BeforeMethod(alwaysRun=true)
	public void setup()
	{
		//LoggerUtil.logINFO("In Custom Setup");
		//propertyManager=new PropertyManager();
		driver= new CustomWebDriver();
		//driver.setDriver(this.fetchDriver(propertyManager.getProperty("browser")));
		//driver.getDriver().get(propertyManager.getProperty("baseUrl"));
		driver.setDriver(this.fetchDriver("chrome"));
		driver.getDriver().get("https://www.saucedemo.com/");
	}
	
	@AfterMethod(alwaysRun=true)
	public void tearDown(){
		
		driver.getDriver().quit();
	}
	
	@BeforeSuite(alwaysRun=true)
	public void beforeSuiteSetup()
	{
		startDockers();
	}
	
	@AfterSuite(alwaysRun=true)
	public void afterSuiteTearDown()
	{
		stopDockers();
	}
	 private void stopDockers()
	    {
	    	for (Map.Entry<String, String> entry : containerDetails.entrySet()) {

	    		String containerID=entry.getKey();
	    		String containerName=entry.getValue();
	        	System.out.println("container Name: " + containerName);
	        	System.out.println("container ID: " + containerID);

	    		if(!containerName.contains("postgres"))
	    		{
	            	System.out.println("stop " + containerID);
	            	ProcessBuilder builder = new ProcessBuilder(
	    		            "cmd.exe", "/c", "docker stop " + containerID ,"/c", "docker rm " + containerID);

	            builder.redirectErrorStream(true);
	            Process p = null;
	    		try {
	    			p = builder.start();
	    		} catch (IOException e) {
	    			// TODO Auto-generated catch block
	    			e.printStackTrace();
	    		}
	            BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
	    		}
	    	}


	    }
	    private void startDockers()
	    {
	    	System.out.println("****** Start docker *****");
	    	ProcessBuilder pb = new ProcessBuilder("cmd.exe", "/C","docker-compose --env-file default.env up -d");
	    	//pb.command("/docker_start.bat");
	    	try {

	    		Process process = pb.start();
	    		Thread.sleep(60000);
	    		pb = new ProcessBuilder(
			            "cmd.exe", "/c", "docker ps");
	    		pb.redirectErrorStream(true);
	    		process = pb.start();
	    		StringBuilder output = new StringBuilder();

	    		BufferedReader reader = new BufferedReader(
	    				new InputStreamReader(process.getInputStream()));

	    		String line;

	    		while ((line = reader.readLine()) != null) {
	    			output.append(line + "\n");
	    			String[] o1=line.split("   ");
	    			System.out.println(o1[0]);
	    			System.out.println(o1[1]);

	    			if(!o1[0].equalsIgnoreCase("CONTAINER"))
	    			{
	    				
						containerDetails.put(o1[0].trim(),o1[1].trim());
	    			}
	    		}

	    		int exitVal = process.waitFor();
	    		if (exitVal == 0) {
	    			System.out.println("Success!");
	    			System.out.println(output);
	    		}
	    	}
	    catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	    }
	public SelfHealingDriver fetchDriver(String browserName)
	{
		SelfHealingDriver driver=null;
		RemoteWebDriver delegateDriver=null;
		//SessionId session=null;
		switch(browserName.toLowerCase())
		{
		case "chrome":
			WebDriverManager.chromedriver().setup();
			ChromeOptions chromeOptions=new ChromeOptions();
			 try {
				//WebDriver driver1 = new DriverContext(DriverType.LOCAL).getDriver(BrowserType.CHROME);
				 delegateDriver=new ChromeDriver(chromeOptions);
				driver=SelfHealingDriver.create(delegateDriver);
				//driver=SelfHealingDriver.create(driver1);
			 } catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			
			//session = ((ChromeDriver)driver).getSessionId();
			//System.out.println("Session id: " + session.toString());

			break;
		case "firefox":
			WebDriverManager.firefoxdriver().setup();
			FirefoxOptions fireFoxoptions=new FirefoxOptions();
				driver=SelfHealingDriver.create(new FirefoxDriver(fireFoxoptions));
				// session = ((FirefoxDriver)driver).getSessionId();
				//System.out.println("Session id: " + session.toString());
			break;
		case "edge":
			break;
		 default:
			break;
		}
		return driver;
		
	}
}
