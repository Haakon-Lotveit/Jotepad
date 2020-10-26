package no.haakon.jotepad.old.kommando.global;

import no.haakon.jotepad.old.kommando.AbstractKommando;
import no.haakon.jotepad.old.model.buffer.Buffer;

import java.util.LinkedHashMap;

public class PopupMessage extends AbstractKommando {

    public static final String MELDING_ARG = "melding";

    @Override
    public String navn() {
        return "popup-message";
    }

    @Override
    public void kjør(Buffer buffer, LinkedHashMap<String, Object> argumenter) {
        buffer.guiHelpers().popupInfo("Melding", argumenter.get(MELDING_ARG).toString());
    }

    @Override
    public LinkedHashMap<String, Object> lesArgumenter(Buffer buffer) {
        LinkedHashMap<String, Object> argumenter = new LinkedHashMap<>();
        String melding = buffer.guiHelpers().inputBox("INPUT", "Skriv melding:").orElse("");
        argumenter.put(MELDING_ARG, melding);

        return argumenter;
    }

    @Override
    public String toString() {
        return navn();
    }
}
