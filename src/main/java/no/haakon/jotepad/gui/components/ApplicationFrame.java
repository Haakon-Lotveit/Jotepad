package no.haakon.jotepad.gui.components;

import javax.swing.*;

/**
 * This is a simple frame that sets up no.haakon.jotepad.start.Jotepad.
 */
public class ApplicationFrame extends JFrame {

    public ApplicationFrame() {
        super();
        this.setTitle("Jotepad");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.pack();
    }
}
