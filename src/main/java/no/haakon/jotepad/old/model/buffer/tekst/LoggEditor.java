package no.haakon.jotepad.old.model.buffer.tekst;

import no.haakon.jotepad.old.model.buffer.Buffer;
import no.haakon.jotepad.old.model.editor.Editor;

public class LoggEditor extends Editor {
    private final LoggBuffer buffer;

    public LoggEditor(LoggBuffer buffer) {
        this.buffer = buffer;
    }

    @Override
    protected Buffer getBuffer() {
        return buffer;
    }

    @Override
    public void undo() {
        // En logglinje er for alltid!
    }

    /**
     * Dette er en metode for å logge formatterte meldinger til loggen.
     * Merk at alt som blir logget er for alltid.
     * Merk at dette skal være en brukervennlig logg: Dersom du logger noe, bør det være noe en bruker kan forventes å kunne ha et forhold til.
     * "Jotepad har ikke lov til å skrive til fil 'C:/Windows/System32/supervirus.cmd'" er OK, men ting som stacktracer og lignende er ikke OK.
     * <p>
     * Dette er ikke en vanlig programlogg som en ser i enterpriseprogrammer der en kan logge ut error, info osv. Det blir noe helt annet.
     * Denne metoden lar deg sende meldinger til loggen på samme måte som PrintStream sin printf metode. (System.out.printf til dømes)
     *
     * @param format Formatet som skal brukes. Se String#format for detaljer om hva som er lov.
     * @param args   argumentene som skal formatteres.
     */
    public void formattertMelding(String format, Object... args) {
        try {
            String formattertTekst = String.format(format, args);
            melding(formattertTekst);
        } catch (Exception e) {
            melding("Prøvde å formattere en melding med format " + format + ", men det gikk ikke.");
        }

    }

    /**
     * Du kan sende en melding til loggen vha. denne metoden.
     * Merk at alt som blir logget er for alltid.
     * Merk at dette skal være en brukervennlig logg: Dersom du logger noe, bør det være noe en bruker kan forventes å kunne ha et forhold til.
     * "Jotepad har ikke lov til å skrive til fil 'C:/Windows/System32/supervirus.cmd'" er OK, men ting som stacktracer og lignende er ikke OK.
     * <p>
     * Dette er ikke en vanlig programlogg som en ser i enterpriseprogrammer der en kan logge ut error, info osv. Det blir noe helt annet.
     *
     * @param melding En streng som skal logges. Dersom den ikke ender på et linjeskift, vil et linjeskift bli lagt på automatisk.
     */
    public void melding(String melding) {
        if (melding == null || melding.isBlank()) {
            return;
        }
        melding = melding.endsWith("\n") ? melding : melding + "\n";
        buffer.getKomponent().append(melding);
    }

}
