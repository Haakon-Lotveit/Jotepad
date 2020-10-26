package no.haakon.jotepad.view;

import no.haakon.jotepad.controller.JotepadController;
import no.haakon.jotepad.controller.JotepadLogger;

import java.awt.Component;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class CommandCollector implements KeyListener {

    private final JotepadController controller;
    private final Buffer buffer;

    public CommandCollector(JotepadController controller, Buffer connectedBuffer) {
        this.controller = controller;
        this.buffer = connectedBuffer;
        // This might be better without directly using the component, but that's for later.
        connectedBuffer.getComponent().addKeyListener(this);
    }

    boolean appendingCharacters = false;
    StringBuffer commandCollector = new StringBuffer();
    Component eventSource = null;

    private void startCollecting(KeyEvent e) {
        eventSource = e.getComponent();
        commandCollector.delete(0, commandCollector.length());
        appendingCharacters = true;
    }

    private String endCollecting() {
        String result = commandCollector.toString();
        commandCollector.delete(0, commandCollector.length());
        eventSource = null;
        appendingCharacters = false;
        return result;
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        // If we're getting commands from a new source, we need to start over.
        if(e.getComponent() != eventSource) {
            endCollecting(); // If the user has switched focus, we are not going to run any commands.
        }
        if(e.isAltDown() && !appendingCharacters) {
            controller.getLogger().log("ALT IS DOWN. WE ARE COLLETING.");
            startCollecting(e);
        }

        if(appendingCharacters) {
            if(e.isControlDown() || e.isShiftDown()) {
                // Pressing down one or both of Ctrl and Shift cancels the command.
                endCollecting();
            } else {
                char c = e.getKeyChar();
                controller.getLogger().log("Got character %c (%d)", c, (int) c);

                if (Character.isLetterOrDigit(c)) {
                    commandCollector.append(c);

                }
            }
        }
    }
    @Override
    public void keyReleased(KeyEvent e) {
        if(!e.isAltDown() && appendingCharacters) { ;
            appendingCharacters = false;
            String command = commandCollector.toString();
            commandCollector.delete(0, commandCollector.length());
            controller.getLogger().log("Collected command: %s", command);
            controller.receiveCommand(command, buffer);
        }
    }

}
