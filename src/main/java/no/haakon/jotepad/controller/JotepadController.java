package no.haakon.jotepad.controller;

import no.haakon.jotepad.view.Buffer;
import no.haakon.jotepad.view.JotepadFrame;
import no.haakon.jotepad.view.MessageBuffer;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * JotepadController is a development of the ApplicationFrame-class and the idea behind it, which grew into a ball of mud.
 * An object of this class is in charge of the system.
 * This is the locus point of the application, and while it probably have too many responsibilities,
 * in order to clearly see where this controller can be split up, and where it can delegate, and so on,
 * a first step must be taken. (This is after all a personal project, not a professional one, so don't be too angry
 * that I don't refactor everything in one majestic go.)
 * <p>
 * The list of responsibilites this class holds is as follows:
 * <ul>
 *     <li>Holding the state of the application</li>
 *     <li>Having the script engine</li>
 *     <li>Knowing about all the buffers in the application</li>
 *     <li>knowing about all the Frames in this application</li>
 *     <li>facilitating communication between the various layers.</li>
 * </ul>
 * <p>
 * So it's not a massive ball of mud yet. And since it is the locus of communication, it does need to know about
 * almost every aspect of the system. This also provides a theoretical modder with an entry point into the system.
 */
public class JotepadController {
    private final JotepadLogger logger;
    private final MessageBuffer messageBuffer;

    /** Given that several controllers could exist, which would normally be a bad idea,
     *  the frameid is not by itself a primary key candidate */
    private final AtomicLong frameIds = new AtomicLong(0L);
    private final Map<Long, JotepadFrame> frames = new HashMap<>();

    private final Map<String, Buffer> buffers;

    public JotepadController(JotepadConfiguration config) {
        this.logger = new JotepadLogger(config, this);
        this.messageBuffer = this.logger.getMessageBuffer();

        logger.log("Logger set successfully. :)");
        try {
            UIManager.setLookAndFeel(config.getLookAndFeel());
            logger.log("Look and feel set to %s successfully", config.getLookAndFeel());
        } catch (Exception e) {
            logger.log("Could not set look and feel to %s.%nUsing the standard look and feel instead.",
                    config.getLookAndFeel());
            logger.log("Error setting look and feel: %s", e);
        }
        logger.log("Sending the startup messages to the message buffer");
        logger.message("This is the message buffer. It's where Jotepad stores messages it's sent you");
        logger.message("Below is a text area. It's there for Jotepad to tell you things.");
        logger.message("It will later be used as an input field as well");
        logger.log("Setting up the buffers");
        buffers = new ConcurrentHashMap<>();
        addBuffer(messageBuffer);

        logger.log("Buffers set up. Starting up with the following buffers: %s", buffers.keySet());
        logger.log("Starting application");
        startApplication();
    }

    private void addBuffer(Buffer buffer) { ;
        buffers.put(buffer.name(), buffer);
    }

    public void changeBufferName(String oldName) {
        if(oldName == null) {
            logger.message("Error renaming buffer. Old name was a null-value");
        }
        Buffer buffer = removeBufferWithName(oldName);

        if(buffer == null) {
            logger.message("No buffer with name %s was found, nothing will be renamed", oldName);
        } else {
            addBuffer(buffer);
        }
    }

    private Buffer removeBuffer(Buffer buffer) {
        return buffer == null? null : removeBufferWithName(buffer.name());
    }

    private Buffer removeBufferWithName(String oldName) {
        Buffer buffer = buffers.remove(oldName);
        return buffer;
    }

    public void startApplication() {
        logger.log("Adding the first frame.");
        addFrame();
    }

    public void addFrame() {
        SwingUtilities.invokeLater(() -> {
            long id = frameIds.getAndIncrement();
            JotepadFrame frame = new JotepadFrame(this, id, messageBuffer.name());
            frames.put(frame.getId(), frame);
            frame.setVisible(true);
            logger.log("Added frame %d", id);
        });
    }

    public JotepadLogger getLogger() {
        return logger;
    }

    public MessageBuffer getMessageBuffer() {
        return messageBuffer;
    }


    /**
     * Notifies the controller that the frame is closing down.
     * @param frame the frame that's closing down.
     */
    public void frameIsClosing(JotepadFrame frame) {
        frames.remove(frame.getId());
        logger.message("Closed down frame %d", frame.getId());
        logger.message("%d windows left", frames.size());
    }

    public Optional<Buffer> getBuffer(String bufferName) {
        return Optional.ofNullable(buffers.get(bufferName));
    }

    /**
     * This allows a command to be called from some buffer.
     * Commands are essentially functions that take a buffer as an argument.
     * Currently completely unimplemented.
     * @param command
     * @param buffer
     */
    public void receiveCommand(String command, Buffer buffer) {

    }

    public void setCommunicationFieldForBuffer(Buffer buffer, String text) {
    }
}
