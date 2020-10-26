package no.haakon.jotepad.controller;

import no.haakon.jotepad.view.JotepadFrame;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class JotepadFrameWindowListener implements WindowListener {

    private final JotepadController controller;
    private final JotepadFrame frame;

    public JotepadFrameWindowListener(JotepadController controller, JotepadFrame frame) {
        this.controller = controller;
        this.frame = frame;
        frame.addWindowListener(this);
    }

    @Override
    public void windowOpened(WindowEvent e) {
        // We just log it for now. May not do anything in the future.
        controller.getLogger().log("Window opened");
    }

    @Override
    public void windowClosing(WindowEvent e) {
        // Closing time, open all the doors and let you all into the world...
        controller.getLogger().log("Closing window %s", frame);
        controller.frameIsClosing(frame);
        frame.dispose();
    }

    @Override
    public void windowClosed(WindowEvent e) {
        controller.getLogger().log("Window %d has closed", frame.getId());
    }

    @Override
    public void windowIconified(WindowEvent e) {

    }

    @Override
    public void windowDeiconified(WindowEvent e) {

    }

    @Override
    public void windowActivated(WindowEvent e) {

    }

    @Override
    public void windowDeactivated(WindowEvent e) {

    }
}
