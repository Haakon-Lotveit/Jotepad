package no.haakon.jotepad.kommando;

import no.haakon.jotepad.model.buffer.Buffer;

import java.util.LinkedHashMap;

public class PopupMessage implements Kommando {

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
}
