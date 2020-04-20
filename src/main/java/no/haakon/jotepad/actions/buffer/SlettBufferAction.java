package no.haakon.jotepad.actions.buffer;

        import no.haakon.jotepad.gui.components.ApplicationFrame;
        import no.haakon.jotepad.gui.components.JaNeiValg;

        import java.awt.event.ActionEvent;

public class SlettBufferAction extends AbstractBufferAction {

    public static final String COMMAND_ROOT = "BUFFER_SLETT";

    public SlettBufferAction(ApplicationFrame frame) {
        super(COMMAND_ROOT, frame);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        frame.lukkSynligBuffer();
    }
}
