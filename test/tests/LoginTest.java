package tests;

import org.testng.annotations.Test;

import com.capstone.utils.CustomSetup;

import pages.LoginPage;


public class LoginTest extends CustomSetup{

		
	@Test
	public void validateLogin()
	{
		 LoginPage loginPage= new LoginPage(this.driver);
		 
		 loginPage.login("standard_user", "secret_sauce");
		
	}
	
	@Test
	public void validateLockOutUser()
	{
		
		 LoginPage loginPage= new LoginPage(this.driver);
		 loginPage.login("locked_out_user", "secret_sauce","Epic sadface: Sorry, this user has been locked out.");
		
	}
	@Test
	public void validateInvalidPassword()
	{
		
		 LoginPage loginPage= new LoginPage(this.driver);
		 loginPage.login("locked_out_user", "secret_sauce1","Epic sadface: Username and password do not match any user in this service");
	}
	@Test
	public void validateInvalidUserName()
	{
		
		 LoginPage loginPage= new LoginPage(this.driver);
		 loginPage.login("locked_out_user1", "secret_sauce","Epic sadface: Username and password do not match any user in this service");
	}
}
