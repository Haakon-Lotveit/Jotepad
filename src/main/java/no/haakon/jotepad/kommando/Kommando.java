package no.haakon.jotepad.kommando;

import no.haakon.jotepad.model.buffer.Buffer;

import java.util.LinkedHashMap;

public interface Kommando {
    public String navn();
    public void kjør(Buffer buffer, LinkedHashMap<String, Object> argumenter);
    public LinkedHashMap<String, Object> lesArgumenter(Buffer buffer);
}
