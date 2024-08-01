package pages;


import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.capstone.utils.CustomWebDriver;
import com.epam.healenium.annotation.DisableHealing;


public class LoginPage extends BasePage {
	private CustomWebDriver driver;

	@FindBy(id="user-name")
	private WebElement txtUserName;

	@FindBy(id="password")
	private WebElement txtPassword;
	
	@FindBy(name="login-button")
	private WebElement btnLogin;
	
	@FindBy(xpath="//h3[@data-test='error']")
	private WebElement txtMsg;
	
	public LoginPage(CustomWebDriver customDriver)
	{
		this.driver=customDriver;
		PageFactory.initElements(customDriver.getDriver(),this);
	}
	
	
	public void login(String userName,String password)
	{
		driver.setText(txtUserName, userName);
		driver.setText(txtPassword, password);
		driver.click(btnLogin);
	}
	public void login(String userName,String password,String errorMessage)
	{
		login( userName, password);
		driver.validateText(txtMsg,errorMessage);
	}
	
}
