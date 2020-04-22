package no.haakon.jotepad.actions.search;

import no.haakon.jotepad.model.buffer.AbstractTekstBuffer;

import java.util.function.Function;

public enum Søketype {
    INGEN(NoneSearcher::new), ENKELT(SimpleSearcher::new), REGEX(RegexSearcher::new);

    private final Function<AbstractTekstBuffer, Searcher> searcherSupplier;

    Søketype(Function<AbstractTekstBuffer, Searcher> searcherSupplier) {
        this.searcherSupplier = searcherSupplier;
    }

    public Searcher søker(AbstractTekstBuffer editor) {
        return searcherSupplier.apply(editor);
    }
}
