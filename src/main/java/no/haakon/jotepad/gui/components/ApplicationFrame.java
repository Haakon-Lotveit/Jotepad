package no.haakon.jotepad.gui.components;

import javax.swing.*;
import java.awt.*;

/**
 * This is a simple frame that sets up no.haakon.jotepad.start.Jotepad.
 */
public class ApplicationFrame extends JFrame {

    public ApplicationFrame() {
        super();
        this.setTitle("Jotepad");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setPreferredSize(new Dimension(1366, 740));
        this.pack();

    }

    public void sentrerPåSkjerm() {
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
    }
}
