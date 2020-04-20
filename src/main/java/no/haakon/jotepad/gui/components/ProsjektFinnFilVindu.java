package no.haakon.jotepad.gui.components;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * Lar det opprette og behandle et interaktivt søk.
 */
public class ProsjektFinnFilVindu extends NarrowingInputList<File> {

    public ProsjektFinnFilVindu(Collection<File> elementerTilListen, Editor assosiertEditor) {
        super("Finn fil i prosjektet", elementerTilListen, assosiertEditor);
    }

    @Override
    protected void gjørValg() {
        final int index = Integer.max(nåværendeUtvalg.getSelectedIndex(), 0);
        Optional.ofNullable(this.nåværendeUtvalg.getModel().getElementAt(index)).map(ListRepresentation::getElement).ifPresent(f -> {
                    åpneFil(f);
                    this.lukkVindu();
                });
    }

    public void åpneFil(File fil) {
        assosiertEditor.getParentFrame().nyEditorForFil(fil, StandardCharsets.UTF_8);
    }

    @Override
    protected boolean matchMotMønster(Pattern mønster, File fil) {
        return mønster.matcher(fil.getName()).find();
    }

    @Override
    protected String stringRepresentasjon(File element) {
        return element.getName();
    }

}
