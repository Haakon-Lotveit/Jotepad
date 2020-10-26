package no.haakon.jotepad.controller;

import no.haakon.jotepad.view.MessageBuffer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Locale;
import java.util.Objects;

/**
 * Logging and messaging in Jotepad is not the same as enterprise logging that you might be familiar with.
 * There's no formats specified in XML, and there are no log levels. There are however, two terms that you should
 * know about. To message, means to tell the user something. This goes in its own buffer that the user can read things
 * in. These messages also gets logged, meaning they get stored to disk.
 * To log means to put a message in the log file. This is useful for people who want logs, and can be useful for people
 * making stuff for Jotepad. Note that by standard the log is only written to standard output.
 * Therefore, be sensible in which one you use:
 * <ul>
 *     <li><b>Messages</b> are when you need to tell the user something. These are not technical messages, but user focused ones.</li>
 *     <li><b>Logs</b> are for debugging and similar. They are not by default stored on disk. They are not user focused as such.</li>
 * </ul>
 */
public class JotepadLogger implements AutoCloseable {

    private final PrintStream target;
    private final MessageBuffer messageBuffer;
    private Locale locale;

    /**
     * Creates a new JotepadLogger based on the configuration given.
     * it uses two standard fields to set itself up. It asks where to write the log, and what locale to use.
     * Locales are useful because different locales format things differently.
     * @param configuration
     */
    public JotepadLogger(JotepadConfiguration configuration, JotepadController controller) {
        Objects.requireNonNull(configuration, "Configuration must be a real non-null object");
        this.messageBuffer = new MessageBuffer(controller);
        if(configuration.getLoggingTarget().equalsIgnoreCase("standard-out")) {
            target = System.out;
        } else if(configuration.getLoggingTarget().equalsIgnoreCase("no-op")) {
            target = new PrintStream(OutputStream.nullOutputStream());
        } else {
            String logfilePath = configuration.getLoggingTarget();
            PrintStream ps;
            try {
                ps = new PrintStream(new File(logfilePath));
            } catch (FileNotFoundException e) {
                /*
                 * Do note that FNFE is not just thrown if the file is not found. If the file does not exist,
                 * but it can be created, FNFE is not thrown. Check the docs for the PrintStream-constructor,
                 * and see for yourself.
                 */
                ps = System.out;
                ps.printf("Couldn't use logfile '%s', using System.out instead. This log will NOT be stored in the file%n", logfilePath);
                ps.println("Nerdy stuff begins:");
                ps.println(e);
                ps.println("Nerdy stuff is now over.");
            }
            target = ps;
        }

        // Sets the locale. Locale can be changed during runtime.
        String localeSpec = configuration.getLocale();
        setLocale(Locale.forLanguageTag(localeSpec));
    }

    public void setLocale(Locale newLocale) {
        this.locale = newLocale;
    }

    public MessageBuffer getMessageBuffer() {
        return this.messageBuffer;
    }

    public String message(String format, Object...args) {
        return message(String.format(locale, format, args));
    }

    public String message(String msg) {
        messageBuffer.message(msg);
        log("Message: %s", msg);
        return msg;
    }

    public String log(String format, Object... args) {
        return log(String.format(locale, format, args));
    }

    public String log(String message) {
        target.println(message);
        return message;
    }


    /**
     * We're mostly providing the autocloseable interface to make testing just a little bit simpler.
     * This way you can provide an overriding implementation that changes just the things you need,
     * and you won't have to close anything yourself. If you want to test and you just want to discard
     * the logs, you can set the "logfile" property to "no-op". "standard-out" is also supported.
     * You can also ask for a temporary file and set that file's path if you want that.
     * Or you can use a normal file. It's all up to you and your needs.
     * @throws Exception if we can't close the stream. However, since this is invoked at shutdown or during testing,
     *                   this is not really a big concern. I'd suggest logging it, but... Printing it will do. ;)
     */
    @Override
    public void close() throws Exception {
        if(target == System.out || target == System.err) {
            return; // We don't close standard out and standard error. That would be silly.
        }

        target.close();
    }
}
