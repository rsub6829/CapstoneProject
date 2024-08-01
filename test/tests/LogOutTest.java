package tests;

import org.testng.annotations.Test;

import com.capstone.utils.CustomSetup;

import pages.LoginPage;
import pages.LogoutPage;


public class LogOutTest extends CustomSetup{

		
	@Test
	public void validateLogout()
	{
		 LoginPage loginPage= new LoginPage(this.driver);
		 loginPage.login("standard_user", "secret_sauce");
		 LogoutPage logOutPage= new LogoutPage(this.driver);
		 logOutPage.logout();
		
	}
}
