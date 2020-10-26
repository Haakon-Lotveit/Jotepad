package no.haakon.jotepad.old.kommando.global.lagrefil;

import no.haakon.jotepad.old.gui.components.ApplicationFrame;
import no.haakon.jotepad.old.kommando.AbstractKommando;
import no.haakon.jotepad.old.model.buffer.Buffer;

import javax.swing.*;
import java.util.LinkedHashMap;

public class LagreFilSom extends AbstractKommando {

    private final ApplicationFrame frame;

    public LagreFilSom(ApplicationFrame frame ) {
        this.frame = frame;
    }

    @Override
    public String navn() {
        return "lagre-fil-som";
    }

    @Override
    public void kjør(Buffer buffer, LinkedHashMap<String, Object> argumenter) {
        SwingUtilities.invokeLater(() -> VelgFilListe.forHjemmemappe(frame, buffer));
    }

    @Override
    public LinkedHashMap<String, Object> lesArgumenter(Buffer buffer) {
        return new LinkedHashMap<>();
    }
}
