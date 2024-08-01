package pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.capstone.utils.CustomWebDriver;


public class MenuPage extends BasePage{
	private CustomWebDriver driver;
	
	@FindBy(linkText="All Items")
	private WebElement mnuAllItems;
	
	@FindBy(linkText="About")
	private WebElement mnuAbout;
	
	public MenuPage(CustomWebDriver customDriver)
	{
		this.driver=customDriver;
		PageFactory.initElements(customDriver.getDriver(),this);
	}
	
	public void selectAllItems()
	{
		driver.click(mnuAllItems);
	}
	
	public void selectAbout()
	{
		driver.click(mnuAbout);
	}
}
