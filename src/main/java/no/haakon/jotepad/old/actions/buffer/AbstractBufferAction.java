package no.haakon.jotepad.old.actions.buffer;

import no.haakon.jotepad.old.actions.AbstractJotepadAction;
import no.haakon.jotepad.old.gui.components.ApplicationFrame;

public abstract class AbstractBufferAction extends AbstractJotepadAction {
    protected AbstractBufferAction(String commandRoot, ApplicationFrame frame) {
        super(commandRoot, frame);
    }
}
