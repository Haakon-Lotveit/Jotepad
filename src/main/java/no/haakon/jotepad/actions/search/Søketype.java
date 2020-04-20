package no.haakon.jotepad.actions.search;

import no.haakon.jotepad.gui.components.Editor;

import java.util.function.Function;

public enum Søketype {
    INGEN(NoneSearcher::new), ENKELT(SimpleSearcher::new), REGEX(RegexSearcher::new);

    private final Function<Editor, Searcher> searcherSupplier;

    Søketype(Function<Editor, Searcher> searcherSupplier) {
        this.searcherSupplier = searcherSupplier;
    }

    public Searcher søker(Editor editor) {
        return searcherSupplier.apply(editor);
    }
}
