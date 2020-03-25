package no.haakon.jotepad.actions.search;

import no.haakon.jotepad.gui.components.Editor;

import java.util.Optional;

public class SimpleSearcher implements Searcher {

    private final Editor editor;

    public SimpleSearcher(Editor editor) {
        this.editor = editor;
    }

    @Override
    public void searchForward(String søkEtter) {
        final int posisjon = editor.getCaretPosition();
        final String tekst = editor.getText();
        final int fra = tekst.indexOf(søkEtter, posisjon);
        if(fra < 0) {
            finnerIkkePopup();
            editor.setCaretPosition(posisjon);
            return;
        }
        final int til = fra + søkEtter.length();

        editor.setSelectionStart(fra);
        editor.setSelectionEnd(til);
    }

    @Override
    public void searchBackwards(String term) {
        final int posisjon = editor.getCaretPosition();

        // Hvis en tekstbit er valgt, så søker vi FØR den valgte tekstbiten.
        int effektivPosisjon = posisjon - Optional.ofNullable(editor.getSelectedText()).orElse("").length();

        final String tekst = editor.getText().substring(0, effektivPosisjon);
        final int fra = tekst.lastIndexOf(term);
        final int til = fra + term.length();

        if(fra < 0) {
            finnerIkkePopup();
            editor.setCaretPosition(effektivPosisjon);
            return;
        }

        editor.setSelectionStart(fra);
        editor.setSelectionEnd(til);

    }


    private void finnerIkkePopup() {
        editor.popupInfo("Finner ikke", "Kan ikke finne teksten din");
    }

}
