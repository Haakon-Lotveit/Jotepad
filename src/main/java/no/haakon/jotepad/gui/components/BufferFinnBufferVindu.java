package no.haakon.jotepad.gui.components;

import javax.swing.*;
import java.io.File;
import java.util.Collection;
import java.util.regex.Pattern;

public class BufferFinnBufferVindu extends NarrowingInputList<Editor> {
    public BufferFinnBufferVindu(Collection<Editor> elementerTilListen, Editor assosiertEditor) {
        super("Finn buffer", elementerTilListen, assosiertEditor);
    }


    @Override
    protected void gjørValg() {
        ListModel<ListRepresentation<Editor>> liste = nåværendeUtvalg.getModel();

        if(liste.getSize() == 0) {
            return;
        }

        final int index = Integer.max(nåværendeUtvalg.getSelectedIndex(), 0);
        Editor valgtElement = liste.getElementAt(0).getElement();

        assosiertEditor.getParentFrame().visEditor(valgtElement);
        lukkVindu();
    }

    @Override
    protected boolean matchMotMønster(Pattern mønster, Editor element) {
        String navn = element.getFile().map(File::getName).orElse("");
        return mønster.matcher(navn).find();
    }

    @Override
    protected String stringRepresentasjon(Editor element) {
        return element.getVennligNavn();
    }
}
