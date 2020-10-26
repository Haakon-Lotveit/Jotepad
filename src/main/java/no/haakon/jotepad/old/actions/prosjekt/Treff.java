package no.haakon.jotepad.old.actions.prosjekt;

import java.io.File;
import java.util.regex.MatchResult;

/* TODOS:
 * TODO: Sorteringer etter ymse standarder
 * TODO: toString
 */
public class Treff {
    private int start;
    private int slutt;
    public final File matchendeFil;
    public final String treff;

    public Treff(MatchResult matchResult, File fil, String innhold) {
        this.start = matchResult.start();
        this.slutt = matchResult.end();
        this.matchendeFil = fil;
        this.treff = finnSubStreng(innhold);
    }

    private String finnSubStreng(String innhold) {
        for(; start >= 0 && innhold.charAt(start) != '\n'; --start);
        ++start;

        for(; slutt <= innhold.length() && innhold.charAt(slutt) != '\n'; ++slutt);
        --slutt;

        System.out.printf("Substreng fra %d til %d fra streng med storleik %d%n", start, slutt, innhold.length());

        try {
            return innhold.substring(start, slutt);
        } catch(Exception e) {
            String feilmelding = String.format("Kunne ikke lage visning på fil '%s'", matchendeFil.getAbsolutePath());
            System.err.println(feilmelding);
            throw new IllegalStateException(feilmelding, e);
        }


    }

    public String toString() {
        return String.format("%d-%d i %s: %s", start, slutt, matchendeFil.getAbsolutePath(), treff);
    }

    public File getFil() {
        return matchendeFil;
    }
}