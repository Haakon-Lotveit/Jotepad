package no.haakon.jotepad.kommando;

import no.haakon.jotepad.model.buffer.Buffer;

import java.util.LinkedHashMap;

public class BufferType implements Kommando {
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
}
