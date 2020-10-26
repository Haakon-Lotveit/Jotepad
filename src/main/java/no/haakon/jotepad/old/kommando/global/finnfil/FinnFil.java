package no.haakon.jotepad.old.kommando.global.finnfil;

import no.haakon.jotepad.old.gui.components.ApplicationFrame;
import no.haakon.jotepad.old.kommando.AbstractKommando;
import no.haakon.jotepad.old.model.buffer.Buffer;

import javax.swing.*;
import java.io.File;
import java.util.LinkedHashMap;

public final class FinnFil extends AbstractKommando {

    private final ApplicationFrame frame;

    public FinnFil(ApplicationFrame frame) {
        this.frame = frame;
    }

    @Override
    public String navn() {
        return "finn-fil";
    }

    @Override
    public void kjør(Buffer buffer, LinkedHashMap<String, Object> argumenter) {
        File startpunkt = buffer.getFil()
                .orElse(new File((String) frame.bindelser().getProperty("hjemmemappe")));
            SwingUtilities.invokeLater(() -> FilInputList.forFolder(startpunkt, frame));
    }

    @Override
    public LinkedHashMap<String, Object> lesArgumenter(Buffer buffer) {
        return new LinkedHashMap<>();
    }


    @Override
    public String toString() {
        return navn();
    }
}
