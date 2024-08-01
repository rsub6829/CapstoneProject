package com.capstone.settings;



import org.openqa.selenium.WebDriver;

import com.capstone.constants.BrowserType;
import com.capstone.constants.DriverType;
import com.capstone.settings.drivers.LocalDriver;
import com.capstone.settings.drivers.ProxyDriver;
import com.capstone.settings.drivers.RemoteDriver;

import java.net.MalformedURLException;

public class DriverContext {

    private WebDriver driver;
    private IDriverInterface context;

    public DriverContext(DriverType local) {
        switch (local) {
            case LOCAL:
                this.context = new LocalDriver();
                break;
            case PROXY:
                this.context = new ProxyDriver();
                break;
            case REMOTE:
                this.context = new RemoteDriver();
                break;
        }

    }

    public WebDriver getDriver(BrowserType browser) throws MalformedURLException {
        switch (browser) {
            case CHROME:
                this.driver = context.setDriver(context.useChrome());
                break;
            case FIREFOX:
                this.driver = context.setDriver(context.useFirefox());
                break;
            case IE:
                this.driver = context.setDriver(context.useEdge());
                break;
        }
        return this.driver;
    }
}