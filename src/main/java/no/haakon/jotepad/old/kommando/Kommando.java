package no.haakon.jotepad.old.kommando;

import no.haakon.jotepad.old.model.buffer.Buffer;

import java.util.LinkedHashMap;

public interface Kommando {
    String navn();
    void kjør(Buffer buffer, LinkedHashMap<String, Object> argumenter);
    LinkedHashMap<String, Object> lesArgumenter(Buffer buffer);

    default void kjørInteraktivt(Buffer buffer) {
        kjør(buffer, lesArgumenter(buffer));
    }

}
