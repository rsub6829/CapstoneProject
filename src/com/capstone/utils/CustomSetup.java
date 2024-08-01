package com.capstone.utils;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.lang.reflect.Method;
import java.net.MalformedURLException;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.Browser;
import org.openqa.selenium.remote.SessionId;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
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
