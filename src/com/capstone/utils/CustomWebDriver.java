package com.capstone.utils;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import com.epam.healenium.SelfHealingDriver;

public class CustomWebDriver {
//	 private CustomWebDriver() {
//	        // Private constructor
//	    }
//
//	    private static ThreadLocal<CustomWebDriver> _threadLocal = 
//	                new ThreadLocal<CustomWebDriver>() {
//	                    @Override
//	                    protected CustomWebDriver initialValue() {
//	                        return new CustomWebDriver();
//	                    }
//	                };
//
//	    /**
//	     * Returns the thread local singleton instance
//	     */
//	    public static CustomWebDriver getInstance() {
//	        return _threadLocal.get();
//	    }
	public CustomWebDriver()
	{
		
	}
	private SelfHealingDriver driver;
	
	public SelfHealingDriver getDriver()
	{
		return driver;
		
	}
	public void setDriver(SelfHealingDriver driver)
	{
		this.driver=driver;
	}
	public void click(WebElement element)
	{
		element.click();
	}
	public void validateText(WebElement element,String text)
	{
		
		Assert.assertEquals(element.getText().trim(), text);
	}
	public void setText(WebElement element,String text)
	{
		element.sendKeys(text);
	}
	public void wait(int seconds)
	{
		driver.getDelegate().manage().timeouts().implicitlyWait(seconds, TimeUnit.SECONDS);

	}
}
