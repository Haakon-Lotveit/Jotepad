package no.haakon.jotepad.old.model.buffer;

import no.haakon.jotepad.old.gui.components.ApplicationFrame;
import no.haakon.jotepad.old.model.buffer.bilde.BildeBuffer;
import no.haakon.jotepad.old.model.buffer.tabell.TabellBuffer;
import no.haakon.jotepad.old.model.buffer.tekst.BasicTextBuffer;

import java.io.File;
import java.util.*;
import java.util.function.BiFunction;
import java.util.regex.Pattern;

public class BufferFactory {
    private final ApplicationFrame frame;
    private BiFunction<File, Map<String, Object>, Buffer> standardBufferProdusent = BasicTextBuffer::new;

    // Dette er mappet som lar denne klassen opprette buffere av ymse typer.
    // En kan legge til mønstre vha registrerBuffer, også utenfor, som et inngangspunkt for utvidelser.
    private final Map<Pattern, BiFunction<File, Map<String, Object>, Buffer>> buffertyper = new TreeMap<>(Comparator.comparing(Pattern::pattern));

    public BufferFactory(ApplicationFrame frame) {
        this.frame = frame;
        // registrerer bildetyper.
        BildeBuffer.støttedeFiler().forEach(pattern -> buffertyper.put(pattern, BildeBuffer::new));
        TabellBuffer.støttedeFiler().forEach(pattern -> buffertyper.put(pattern, TabellBuffer::new));
    }

    /**
     * Dersom ingen produsent er registrert som matcher en gitt filtype, blir denne brukt.
     * @param standardBufferProdusent produsenten som skal brukes dersom ingen andre produsenter matcher.
     */
    public void setStandardBuffer(BiFunction<File, Map<String, Object>, Buffer> standardBufferProdusent) {
        this.standardBufferProdusent = standardBufferProdusent;
    }

    /**
     * Registrerer en ny buffertype.
     * Dette er en særdeles dynamisk måte å gjøre ting på, mer Groovy enn Java. Merk at ingen av argumentene kan være null.
     * Merk at Buffer.APPLICATION_FRAME er en reservert nøkkel: Du kan ikke bruke den nøkkelen til eget bruk.
     * @param mønster mønsteret vi skal matche mot. Dersom flere mønstre matcher en fil er det ingen garantier om hvilket mønster som vinner.
     * @param produsent en funksjon som tar en fil og et map av argumenter i et Map<String, Object>. Det betyr at konstruktørene som aksepterer disse argumentene selv må sjekke at alle argumenter
     *                  er tilstede og av riktig type. Overflødige argumenter anses <i>ikke</i> som en feil og skal ignoreres.
     */
    public static void registrerBuffer(Pattern mønster, BiFunction<File, Map<String, Object>, Buffer> produsent) {
        Objects.requireNonNull(mønster, "Mønsteret kan ikke være null");
        Objects.requireNonNull(produsent, "Produsenten kan ikke være null");

        System.out.println("Ikke implementert enda...");
    }

    /**
     * Går gjennom registrerte buffertyper for å finne en som matcher filen som er gitt inn.
     * Hvis ikke noen slik type er registrert, blir standardBufferProdusent benyttet.
     * Deretter blir en buffer forsøkt opprettet med fil og argumenter som oppgitt.
     * @param fil filen som skal åpnes. Kan ikke være null.
     * @param args argumenter som bufferens konstruktør trenger. {@link Buffer#APPLICATION_FRAME} vil bli overskrevet om den er brukt som nøkkel.
     * @return en Buffer om opprettelsen gikk bra, en tom optional ellers.
     */
    public Optional<Buffer> createBuffer(File fil, Map<String, Object> args) {
        HashMap<String, Object> copiedArgs = new HashMap<>();
        args.forEach(copiedArgs::put);
        copiedArgs.put(Buffer.APPLICATION_FRAME, frame);
        return Optional.ofNullable(buffertyper.entrySet().stream()
                .filter(entry -> entry.getKey().matcher(fil.getName()).find())
                .map(Map.Entry::getValue)
                .findAny()
                .orElse(standardBufferProdusent)
                .apply(fil, copiedArgs));
    }

    public Buffer tomBuffer() {
        return standardBufferProdusent.apply(null, Map.of(Buffer.APPLICATION_FRAME, frame));
    }
}
