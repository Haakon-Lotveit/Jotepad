package no.haakon.jotepad.old.kommando.global.lagrefil;

import no.haakon.jotepad.old.gui.components.ApplicationFrame;
import no.haakon.jotepad.old.kommando.AbstractKommando;
import no.haakon.jotepad.old.model.buffer.Buffer;

import javax.swing.*;
import java.util.LinkedHashMap;

public class LagreFil extends AbstractKommando {

    private final ApplicationFrame frame;

    public LagreFil(ApplicationFrame frame) {
        this.frame = frame;
    }

    @Override
    public String navn() {
        return "lagre-fil";
    }

    @Override
    public void kjør(Buffer buffer, LinkedHashMap<String, Object> argumenter) {
            if(buffer.getFil().isEmpty()) {
                SwingUtilities.invokeLater(() -> VelgFilListe.forHjemmemappe(frame, buffer));
            } else {
                buffer.getEditor().lagre();
            }
    }

    @Override
    public LinkedHashMap<String, Object> lesArgumenter(Buffer buffer) {
        return new LinkedHashMap<>();
    }
}
