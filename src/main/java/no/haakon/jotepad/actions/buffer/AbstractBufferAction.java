package no.haakon.jotepad.actions.buffer;

import no.haakon.jotepad.actions.AbstractJotepadAction;
import no.haakon.jotepad.gui.components.ApplicationFrame;

public abstract class AbstractBufferAction extends AbstractJotepadAction {
    protected AbstractBufferAction(String commandRoot, ApplicationFrame frame) {
        super(commandRoot, frame);
    }
}
