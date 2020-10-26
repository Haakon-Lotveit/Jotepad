package no.haakon.jotepad.controller;

import javax.swing.*;
import java.util.Properties;

/**
 * JotepadConfiguration is responsible for almost all the aspects of communications that are read out through
 * property files. Some people like YAML, but I like the built-in nature of properties. It's true that YAML has some
 * niceties like lists, indentation etc. and I do appreciate those.
 *
 * However, properties-files are much simpler to deal with. They're easier to write, they're easier to read, and they're
 * easier to parse. If you need advanced properties, you can use an XML-form, which is kind of nifty. I don't think
 * I've ever needed it, but it's good to know that advanced options are there if you need them.
 */
public class JotepadConfiguration {
    private final String lookAndFeel;
    private final String origin;
    private final String loggingTarget;
    private final String locale;
    private final Properties properties;
    public JotepadConfiguration(Properties properties) {
        lookAndFeel = properties.getProperty("look-and-feel", UIManager.getSystemLookAndFeelClassName());
        origin = properties.getProperty("origin", "unknown origin");
        loggingTarget = properties.getProperty("logfile", "standard-out");

        locale = properties.getProperty("locale", "nb-NO"); // This is a Norwegian program, after all.
        // PS: This means that if you want to have say, a Swedish locale, you can set it in the properties file.

        this.properties = properties;
    }

    public String getLoggingTarget() {
        return loggingTarget;
    }

    public String getLookAndFeel() {
        return lookAndFeel;
    }

    public String getOrigin() {
        return origin;
    }

    public String getLocale() {
        return locale;
    }

    // Properties are read only. This might change in the future, but for now it doesn't.
    public String getProperty(String name, String defaultValue) {
        return properties.getProperty(name, defaultValue);
    }
}
