package com.capstone.settings.drivers;

import java.net.MalformedURLException;
import java.net.URI;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.capstone.settings.IDriverInterface;
import com.epam.healenium.SelfHealingDriver;

public class RemoteDriver implements IDriverInterface {

    private SelfHealingDriver driver;

    @Override
    public WebDriver setDriver(Object capabilities) throws MalformedURLException {
        RemoteWebDriver delegate = new RemoteWebDriver(
                URI.create("http://" + SELENOID + ":4444/wd/hub").toURL(),
                (Capabilities) capabilities
        );
        this.driver = SelfHealingDriver.create(delegate);

        return this.driver;
    }

    @Override
    public Object useChrome() {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("browserName", "chrome");
        capabilities.setCapability("enableVNC", true);

        return capabilities;
    }

    @Override
    public Object useFirefox() {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("browserName", "firefox");
        capabilities.setCapability("enableVNC", true);

        return capabilities;
    }

    @Override
    public Object useEdge() {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("browserName", "edge");
        capabilities.setCapability("enableVNC", true);

        return capabilities;
    }

    @Override
    public Object useSafari() {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("browserName", "safari");
        capabilities.setCapability("enableVNC", true);

        return capabilities;
    }
}