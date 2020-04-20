package no.haakon.jotepad.actions.search;

import no.haakon.jotepad.gui.components.ApplicationFrame;

import java.awt.event.ActionEvent;

public class ShowSearchTermAction extends AbstractSearchAction {

    public static String COMMAND_ROOT = "VIS-SØKETERM";

    public ShowSearchTermAction(ApplicationFrame frame) {
        super(COMMAND_ROOT, frame);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String searchTerm = getSearchTerm();
        if(searchTerm == null) {
            frame.synligBuffer().popupInfo("Søketerm", "Ingen søketerm har blitt satt");
            return;
        }

        frame.synligBuffer().popupInfo("Søketerm", "Søketerm: " + searchTerm);
    }
}
