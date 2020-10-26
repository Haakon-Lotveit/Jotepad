package no.haakon.jotepad.view;

import no.haakon.jotepad.controller.JotepadController;
import no.haakon.jotepad.controller.JotepadLogger;

import javax.swing.JComponent;
import javax.swing.JTextArea;
import java.io.File;
import java.util.Optional;

/**
 * Reimplementation of old class. This is written together with the buffer interface.
 */
public class MessageBuffer implements Buffer {
    private final JotepadLogger logger;
    private final JTextArea component;
    private final String defaultMatch = "DEFAULT";
    private final CommandCollector commandCollector;

    public MessageBuffer(JotepadController controller) {
        this.logger = controller.getLogger();
        this.component = new JTextArea();
        component.setEditable(false);

        this.commandCollector = new CommandCollector(controller, this);
    }

    @Override
    public String name() {
        return "*message-buffer*";
    }

    @Override
    public Optional<File> saveFile() {
        return Optional.empty();
    }

    @Override
    public void save() {
        logger.log("Saving the messagebuffer is not yet implemented");
    }

    @Override
    public JComponent getComponent() {
        return component;
    }

    @Override
    public void getFocus(JotepadFrame focusedFrame) {
        return;
    }

    @Override
    public void loseFocus(JotepadFrame focusedFrame) {
        return;
    }

    /**
     * Takes a message and adds it.
     * @param msg the message you want added.
     */
    public void message(String msg) {
        component.append(msg + "\n");
    }
}
