package no.haakon.jotepad.old.kommando;

import no.haakon.jotepad.old.model.buffer.Buffer;

import java.util.LinkedHashMap;

public class BufferType extends AbstractKommando {
    @Override
    public String navn() {
        return "melding-buffer-type";
    }

    @Override
    public void kjør(Buffer buffer, LinkedHashMap<String, Object> argumenter) {
        buffer.guiHelpers().popupInfo("Buffertype", "Buffertype for denne bufferen er '" + buffer.getTypeNavn() + "'");
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
