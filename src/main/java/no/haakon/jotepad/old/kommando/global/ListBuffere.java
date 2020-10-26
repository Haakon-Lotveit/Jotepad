package no.haakon.jotepad.old.kommando.global;

import no.haakon.jotepad.old.gui.components.ApplicationFrame;
import no.haakon.jotepad.old.gui.components.inputlister.BufferFinnBufferVindu;
import no.haakon.jotepad.old.kommando.AbstractKommando;
import no.haakon.jotepad.old.model.buffer.Buffer;

import javax.swing.*;
import java.util.LinkedHashMap;

public class ListBuffere extends AbstractKommando {

    private final ApplicationFrame frame;

    public ListBuffere(ApplicationFrame frame) {
        this.frame = frame;
    }

    @Override
    public String navn() {
        return "list-buffere";
    }

    @Override
    public void kjør(Buffer buffer, LinkedHashMap<String, Object> argumenter) {
        SwingUtilities.invokeLater(() -> new BufferFinnBufferVindu(frame.getBuffere(), frame));
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
