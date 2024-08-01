package com.capstone.utils;


import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import com.capstone.utils.loggers.LoggerUtil;

public class Properties {

    private final Class caller;
    private final String userDirPath = System.getProperty("user.dir") + File.separator + "target" + File.separator + "classes";
    private final String envPropsDirPath;
    private java.util.Properties envProperties = null;
    private java.util.Properties commonProperties = null;
    private Map<String, java.util.Properties> secretProperties = new HashMap<>();

    public Properties() {
        caller = Properties.class;
        envPropsDirPath = "";
    }

    /**
     *
     * @param caller class reference creating this Properties instance
     * @param envPropertiesDirectoryPath path to environment property files directory (relative path to main/resources dir)
     */
    public Properties(Class caller, String envPropertiesDirectoryPath) {
        this.caller = caller;
        envPropertiesDirectoryPath = envPropertiesDirectoryPath.trim();
        if (!envPropertiesDirectoryPath.isEmpty() && !envPropertiesDirectoryPath.startsWith(File.separator))
            envPropertiesDirectoryPath = File.separator + envPropertiesDirectoryPath;
        envPropsDirPath = envPropertiesDirectoryPath;
    }

    private void loadEnvProperties() {
        envProperties = new java.util.Properties();
        try {
            boolean isPropertyFileFound = false;
            String env = getCommonProperty("test.env");

            // First look inside jar (if the caller is a jar)
            if (caller.getProtectionDomain().getCodeSource().getLocation().getFile().endsWith(".jar")) {
                InputStream inputStream = findResourceInputStream(env, envPropsDirPath);
                if (inputStream != null) {
                    envProperties.load(inputStream);
                    isPropertyFileFound = true;
                } else {
                    LoggerUtil.logWARNING("Property file not found in lib for environment: " + env, null);
                }
            }

            // Now look at user.dir for overrides
            File file2 = findPropertyFile(env , envPropsDirPath);
            if(file2 != null) {
                try (InputStream input = new FileInputStream(file2)) {
                    envProperties.load(input);
                    isPropertyFileFound = true;
                    LoggerUtil.logINFO("Property file found: " + file2.getPath());
                }
            }
            else {
                LoggerUtil.logWARNING("Property file not found in " + userDirPath + envPropsDirPath + " for environment: " + env, null);
            }

            if (!isPropertyFileFound)
                throw new RuntimeException("Property file not found for environment: " + env);
        } catch (IOException e) {
            LoggerUtil.logERROR(e.getMessage(), e);
        }
    }

    private void loadCommonProperties() {
        commonProperties = new java.util.Properties();
        // common properties are always taken from user.dir
        File file = findPropertyFile("common", "");
        if (file != null) {
            try (InputStream inputStream = new FileInputStream(file)) {
                commonProperties.load(inputStream);
                LoggerUtil.logINFO("Property file found: " + file.getPath());
            } catch (IOException e) {
                LoggerUtil.logERROR(e.getMessage(), e);
            }
        } else {
            throw new RuntimeException("common properties file not found in " + userDirPath);
        }
    }

    /**
     *
     * @param appId Application ID in SSM API Gateway (https://sre-tools.cx-shop-nonprod.sysco-go.com)
     */
//    private void loadSecretProperties(String appId) {
//        java.util.Properties properties = new java.util.Properties();
//        String testEnv = getCommonProperty("test.env");
//        SSMAPIClient ssmapiClient = new SSMAPIClient(appId, testEnv);
//
//        String readerResponse = ssmapiClient.getProperties();
//        if (readerResponse == null) {
//            LoggerUtil.logERROR("Could not read properties from AWS SSM for AppId: " + appId + " and Env: " + testEnv);
//            return;
//        }
//
//        try (Reader stringReader = new StringReader(readerResponse)) {
//            properties.load(stringReader);
//            secretProperties.put(appId, properties);
//            LoggerUtil.logINFO("Loading secret properties was successful...");
//        } catch (IOException e) {
//            LoggerUtil.logERROR(e.getMessage(), e);
//        }
//    }

    /**
     * Set as System properties if not present already
     * @param properties properties to set in System
     */
    private void setSystemProperties(java.util.Properties properties) {
        for (Map.Entry<Object, Object> property: properties.entrySet()) {
            if (System.getProperty(property.getKey().toString()) == null)
                System.setProperty(property.getKey().toString(), property.getValue().toString());
        }
    }

    /**
     * Get values from the given environment property file
     *
     * @param key property key
     * @return Value of the given property (java.lang.String)
     */
    public String getEnvProperty(String key) {
        if (envProperties == null)
            loadEnvProperties();

        String p = System.getProperty(key);
        return p != null ? p : envProperties.getProperty(key);
    }

    /**
     * Get values from the common property file
     *
     * @param key property key
     * @return Value of the given property (java.lang.String)
     */
    public String getCommonProperty(String key) {
        if (commonProperties == null) {
            loadCommonProperties();
            // Set common properties as System properties
            // This is needed when E2E code is used in some other project as a dependency
            setSystemProperties(commonProperties);
        }

        String p = System.getProperty(key);
        return p != null ? p : commonProperties.getProperty(key);
    }

    /**
     * Get values for the secret property from AWS SSM
     *
     * @param appId Application ID in SSM API Gateway (https://sre-tools.cx-shop-nonprod.sysco-go.com)
     * @param key property key
     * @return Value of the given property (java.lang.String)
     */
//    public String getSecretProperty(String appId, String key) {
//        if (secretProperties.get(appId) == null)
//            loadSecretProperties(appId);
//
//        return secretProperties.get(appId) == null ? null : getNormalizedSecretPropertyValue(secretProperties.get(appId).getProperty(key));
//    }

    /**
     * SSM parameter store returns every value enclosed within quotes (eg: bar returns as "bar")
     * @param rawValue value returned from SSM (quotes inclusive)
     * @return value without quotes
     */
    private String getNormalizedSecretPropertyValue(String rawValue) {
        if (rawValue == null || rawValue.trim().isEmpty())
            return null;

        String normalizedValue = rawValue.trim();
        return normalizedValue.substring(1, normalizedValue.length() - 1);
    }

    /**
     * Look for a properties file with {@code resourceNamePattern} in {@link #caller}'s Resources list
     *
     * @param resourceNamePattern Name pattern to match
     * @param envPropsDirPath Sub directory for specific environment
     * @return the {@link InputStream} if a matching Resource is found; {@code null} otherwise
     */
    private InputStream findResourceInputStream(String resourceNamePattern, String envPropsDirPath) {
        InputStream inputStream = filterResourceInputStream("_" + resourceNamePattern + "_", envPropsDirPath);
        if (inputStream == null) {
            inputStream = filterResourceInputStream("_" + resourceNamePattern + ".", envPropsDirPath);
            if (inputStream == null) {
                inputStream = filterResourceInputStream(resourceNamePattern, envPropsDirPath);
            }
        }
        return inputStream;
    }

    private InputStream filterResourceInputStream(String resourceNamePattern, String envPropsDirPath) {
        String dirPath = envPropsDirPath.trim();
        String fileSeparator = "/"; // This is constant for all platforms including Windows

        // If envPropsDirPath is not empty
        if (!dirPath.isEmpty()) {
            // Make sure it does not start with "/"
            if (dirPath.startsWith(fileSeparator))
                dirPath = dirPath.substring(1);

            // Make sure it ends with "/"
            if (!dirPath.endsWith(fileSeparator))
                dirPath = dirPath + fileSeparator;
        }

        try (ZipInputStream zip = new ZipInputStream(caller.getProtectionDomain().getCodeSource().getLocation().openStream())) {
            while(true) {
                ZipEntry e = zip.getNextEntry();
                if (e == null)
                    break;
                String name = e.getName();
                if (name.startsWith(dirPath) && name.contains(resourceNamePattern) && name.endsWith(".properties")) {
                    LoggerUtil.logINFO("Property file found in lib: " + name);
                    return caller.getClassLoader().getResourceAsStream(name);
                }
            }
        } catch (IOException e) {
            LoggerUtil.logERROR(e.getMessage(), e);
        }

        return null;
    }

    /**
     * Look for a properties file with {@code fileNamePattern} in {@code ${user.dir}/target/classes/${envPropsDirPath}/}
     *
     * @param fileNamePattern Name pattern to match
     * @param envPropsDirPath Sub directory for specific environment
     * @return the {@link File} if a matching file is found; {@code null} otherwise
     */
    private File findPropertyFile(String fileNamePattern, String envPropsDirPath) {
        final Properties cxProperties = new Properties();
        File file= fileNamePattern.equals("qa") ? this.filterPropertyFile("_" + fileNamePattern + "_" + cxProperties.getCommonProperty("test.region") + "_", envPropsDirPath) : this.filterPropertyFile("_" + fileNamePattern + "_", envPropsDirPath);
        if (file == null) {
            file = this.filterPropertyFile("_" + fileNamePattern + ".", envPropsDirPath);
            if (file == null) {
                file = this.filterPropertyFile(fileNamePattern, envPropsDirPath);
            }
        }
        return file;
    }

    private File filterPropertyFile(String fileNamePattern, String envPropsDirPath) {
        File pDir = new File(userDirPath + envPropsDirPath);
        File[] files = pDir.listFiles((dir, name) -> name.contains(fileNamePattern) && name.endsWith(".properties"));
        if(files != null && files.length > 0) {
            return files[0];
        }
        return null;
    }

}
