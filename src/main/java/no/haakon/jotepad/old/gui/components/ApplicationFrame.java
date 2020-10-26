package no.haakon.jotepad.old.gui.components;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import groovy.lang.MissingPropertyException;
import no.haakon.jotepad.old.gui.menubar.JotepadMenubar;
import no.haakon.jotepad.old.kommando.Kommando;
import no.haakon.jotepad.old.model.buffer.Buffer;
import no.haakon.jotepad.old.model.buffer.BufferFactory;
import no.haakon.jotepad.old.model.buffer.tekst.LoggBuffer;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Dette begynte som en enkel JFrame men er nå en ball av nyttig funksjonalitet skubbet ned halsen på en JFrrame.
 * Den holder på ting som Groovy-tolken, bufferene, kommandoene som er satt opp, brukerloggen, med mer.
 */
public class ApplicationFrame extends JFrame {

    public static final String TITLE = "Jotepad";
    private final Binding groovyBindelser;
    private final GroovyShell shell;
    private Buffer currentBuffer = null;
    private final CardLayout layout;
    private final JPanel panel;
    private BufferFactory bufferFactory;
    private List<Kommando> globaleKommandoer = new ArrayList<>();
    private final LoggBuffer logg;

    public ApplicationFrame(String hjemmemappe) {
        super();
        layout = new CardLayout();
        panel = new JPanel(layout);
        this.add(panel);

        this.bufferFactory = new BufferFactory(this);

        logg = new LoggBuffer(Map.of(Buffer.APPLICATION_FRAME, this));
        addBuffer(logg);
        String velkomstMelding = "" +
                "Velkommen til Jotepad! Dette er loggen, der meldinger fra systemet blir lagt.\n" +
                "Akkurat nå er det mye klabb og babb i systemet, og ingenting er ferdig.\n" +
                "Merk at ting kan og vil endre seg drastisk. Dette er bare et hobbyprosjekt tross alt.\n" +
                "For å gjøre en handling, som å åpne en fil, kan du trykke alt + x.";
        logg.getEditor().melding(velkomstMelding);


        // Oppretter groovy-tingene:
        this.groovyBindelser = new Binding();
        groovyBindelser.setVariable("hjemmemappe", hjemmemappe);
        this.shell = new GroovyShell(groovyBindelser);

        initialiserGroovyVariabler();

        // Når groovy vet om skallet, er det på tide å legge til grunnleggende variabler
        groovyBindelser.setVariable("frame", this);

        setJMenuBar(new JotepadMenubar(this));

        oppdaterTittel();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setPreferredSize(new Dimension(1366, 740));
        this.pack();
    }

    private void initialiserGroovyVariabler() {
        groovyBindelser.setVariable("loggbuffer", logg);
        groovyBindelser.setVariable("logger", logg.getEditor());
        groovyBindelser.setVariable("kommandoer", globaleKommandoer);
    }

    public void sentrerPåSkjerm() {
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2 - this.getSize().height / 2);
    }

    public void addBuffer(Buffer buffer) {
        panel.add(buffer.getSkroller(), buffer.getUnikId());
        visBuffer(buffer);
    }

    public void visBuffer(Buffer buffer) {
        System.out.printf("Prøver å vise buffer '%s', med unik id '%s'%n", buffer.getBufferNavn(), buffer.getUnikId());
        layout.show(panel, buffer.getUnikId()); // Merk at id her deler navn med JSCrollPane sitt navn...
        oppdaterTittel(buffer);
    }

    public void visForrigeBuffer() {
        layout.previous(panel);
        oppdaterTittel();
    }

    public void visNesteBuffer() {
        layout.next(panel);
        oppdaterTittel();
    }

    public void visFørsteBuffer() {
        layout.first(panel);
        oppdaterTittel();
    }

    public void visSisteBuffer() {
        layout.last(panel);
        oppdaterTittel();
    }

    public void visBufferMedNavn(final String navn) {
        layout.show(panel, navn);
    }

    public Collection<Buffer> getBuffere() {
        return Arrays.stream(panel.getComponents())
                .filter(c -> c instanceof BufferSkroller)
                .map(BufferSkroller.class::cast)
                .map(BufferSkroller::getBuffer)
                .collect(Collectors.toList());
        // Ugh.
    }

    public void åpneFil(File fil, Charset charset) {
        bufferFactory.createBuffer(fil, Map.of("charset", charset))
                .ifPresentOrElse(this::addBuffer,
                                 () -> System.err.println("Kunne ikke åpne fil " + fil.getAbsolutePath()));
    }

    private void oppdaterTittel(Buffer buffer) {
         oppdaterTittel(buffer.getBufferNavn());
    }

    private void oppdaterTittel() {
        String tittel = synligBuffer().getBufferNavn();
        oppdaterTittel(tittel);
    }

    public Buffer synligBuffer() {
        return Arrays.stream(panel.getComponents())
                .filter(Component::isVisible)
                .filter(c -> c instanceof BufferSkroller)
                .map(BufferSkroller.class::cast)
                .map(BufferSkroller::getBuffer).findAny()
                .get(); // Det skal ALLTID være en og bare en som dette er gyldig for.
    }

    private void oppdaterTittel(final String suffix) {
        setTitle(String.format("%s - %s", TITLE, suffix));
    }

    public void lukkBuffer(Buffer buffer) {
        if(getBuffere().size() <= 1) {
            // det er siste, så vi må være litt varsomme...
            nyTomBuffer();
        }
        else {
            visNesteBuffer();
        }
            panel.remove(buffer.getSkroller());
    }

    public void lukkSynligBuffer() {
        if(synligBuffer() == logg) {
            logg("Loggbuffer blir aldri lukket");
        } else {
            Buffer buffer = synligBuffer();
            loggFormattert("Lukker buffer %s", buffer.getBufferNavn());
            lukkBuffer(synligBuffer());
        }
    }

    public void nyTomBuffer() {
        addBuffer(bufferFactory.tomBuffer());
    }

    // Håndterer state som ymse kommandoer og lignende trenger å sette...


    public ApplicationFrame setValue(String key, Object value) {
        groovyBindelser.setVariable(key, value);
        return this;
    }

    public Optional<Object> getValue(String key) {
        try {
            return Optional.ofNullable(groovyBindelser.getVariable(key));
        } catch (MissingPropertyException mpe) {
            return Optional.empty();
        }
    }

    public void registrerGlobalKommando(Kommando kommando) {
        this.globaleKommandoer.add(kommando);
    }

    public Collection<Kommando> globaleKommandoer() {
        return Collections.unmodifiableCollection(globaleKommandoer);
    }


    public Binding bindelser() {
        return groovyBindelser;
    }

    public GroovyShell getShell() {
        return shell;
    }

    /**
     * Logger en melding til loggbufferen
     * @param melding Meldingen som skal logges. Argumenter som er null, tomme eller blanke blir ignorert.
     */
    public void logg(String melding) {
        if(melding == null || melding.isBlank()) {
            return;
        }
        logg.getEditor().melding(melding);
    }

    public void loggFormattert(String format, Object... args) {
        if(format == null || format.isBlank()) {
            return;
        }
        logg.getEditor().formattertMelding(format, args);
    }
}
