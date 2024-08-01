package pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.capstone.utils.CustomWebDriver;


public class LogoutPage extends BasePage{
	
	private CustomWebDriver driver;
	
	@FindBy(linkText="Logout")
	private WebElement lnkLogOut;
	
	@FindBy(id="react-burger-menu-btn")
	private WebElement openMnu;
	
	public LogoutPage(CustomWebDriver customDriver)
	{
		this.driver=customDriver;
		PageFactory.initElements(customDriver.getDriver(),this);
	}
	
	public void logout()
	{
		driver.click(openMnu);
		driver.wait(3);
		driver.click(lnkLogOut);
	}
}
