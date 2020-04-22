package no.haakon.jotepad.gui.components;

import no.haakon.jotepad.model.buffer.Buffer;

import javax.swing.*;
import java.io.File;
import java.util.Collection;
import java.util.regex.Pattern;

public class BufferFinnBufferVindu extends NarrowingInputList<Buffer> {
    public BufferFinnBufferVindu(Collection<Buffer> elementerTilListen, ApplicationFrame frame) {
        super("Finn buffer", elementerTilListen, frame);
    }


    @Override
    protected void gjørValg() {
        ListModel<ListRepresentation<Buffer>> liste = nåværendeUtvalg.getModel();

        if(liste.getSize() == 0) {
            return;
        }

        final int index = Integer.max(nåværendeUtvalg.getSelectedIndex(), 0);
        Buffer valgtElement = liste.getElementAt(0).getElement();

        frame.visBuffer(valgtElement);
        lukkVindu();
    }

    @Override
    protected boolean matchMotMønster(Pattern mønster, Buffer element) {
        String navn = element.getFil().map(File::getName).orElse("");
        return mønster.matcher(navn).find();
    }

    @Override
    protected String stringRepresentasjon(Buffer element) {
        return element.getBufferNavn();
    }
}
