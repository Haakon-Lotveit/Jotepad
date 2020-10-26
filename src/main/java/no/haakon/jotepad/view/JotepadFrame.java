package no.haakon.jotepad.view;

import no.haakon.jotepad.controller.JotepadController;
import no.haakon.jotepad.controller.JotepadFrameWindowListener;
import no.haakon.jotepad.controller.JotepadLogger;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;

public class JotepadFrame extends JFrame {
    private final JotepadLogger logger;
    private final JPanel bufferpanel;
    private final long id;
    private final JotepadController controller;
    private final CommunicationField communicationField;
    private volatile Buffer currentBuffer;
    /**
     * A JotepadFrame is a frame of the Jotepad editor. There can be several frames, all talking to one controller,
     * sharing the same buffers. Therefore there is logic in this class to ensure that closing a frame is communicated
     * to the controller.
     *
     * @param controller
     * @param id
     */
    public JotepadFrame(JotepadController controller, long id, String focusedBufferName) {
        super();

        this.id = id;
        this.controller = controller;
        this.logger = controller.getLogger();

        // This should be part of the controller, so that all frames know about the same buffers.
        CardLayout cardLayout = new CardLayout();
        this.bufferpanel = new JPanel(cardLayout);
        this.add(bufferpanel);

        setBuffer(controller.getBuffer(focusedBufferName).orElse(controller.getMessageBuffer()));

        this.communicationField = new CommunicationField();
        this.add(communicationField, BorderLayout.PAGE_END);

        // the constructor sets up everything so you don't need to do anything else but to new this one up.
        // I don't really like it, it feels too magical.
        new JotepadFrameWindowListener(controller, this);

        setTitle("Jotepad Neoframe");
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.setPreferredSize(new Dimension(1366, 740));
        this.pack();
    }

    public void setBuffer(Buffer buffer) {
        if(buffer == null) {
            logger.log("Frame %d asked to set its current buffer to null", this.getId());
            return;
        }
        if(currentBuffer != null) {
            bufferpanel.remove(currentBuffer.getComponent());
        }
        currentBuffer = buffer;
        bufferpanel.add(buffer.getComponent());
        validate();
    }


    public long getId() {
        return id;
    }
}
