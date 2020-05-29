package no.haakon.jotepad.gui.components;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import groovy.lang.MissingPropertyException;
import no.haakon.jotepad.gui.menubar.JotepadMenubar;
import no.haakon.jotepad.kommando.Kommando;
import no.haakon.jotepad.model.buffer.Buffer;
import no.haakon.jotepad.model.buffer.BufferFactory;
import no.haakon.jotepad.model.buffer.tekst.LoggBuffer;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This is a simple frame that sets up no.haakon.jotepad.start.Jotepad.
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

    public ApplicationFrame() {
        super();
        layout = new CardLayout();
        panel = new JPanel(layout);
        this.add(panel);

        this.bufferFactory = new BufferFactory(this);

        logg = new LoggBuffer(Map.of(Buffer.APPLICATION_FRAME, this));
        addBuffer(logg);
        String velkomstMelding = "" +
                "Velkommen til Jotepad! Dette er loggen, der meldinger fra systemet blir lagt!\n" +
                "Akkurat nå er det mye klabb og babb i systemet, og ingenting er ferdig.\n" +
                "Snart blir denne loggen en mer ordentlig logg, og brukergrensesnittet blir mer ordentlig av seg.\n" +
                "Hvis du vil prøve editoren kan du åpne en fil, men pass på! Verktøylinjen vil med tiden forsvinne.\n" +
                "For å kjøre kommandoer som er definerte kan du bruke Alt + X, for å kjøre en vilkårlig snutt med groovy-kode, bruk Alt + .";
        logg.getEditor().melding(velkomstMelding);


        // Oppretter groovy-tingene:
        this.groovyBindelser = new Binding();
        this.shell = new GroovyShell(groovyBindelser);

        opprettGroovyFunksjoner();

        // Når groovy vet om skallet, er det på tide å legge til grunnleggende variabler
        groovyBindelser.setVariable("frame", this);

        setJMenuBar(new JotepadMenubar(this));

        oppdaterTittel();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setPreferredSize(new Dimension(1366, 740));
        this.pack();
    }

    private void opprettGroovyFunksjoner() {
        groovyBindelser.setVariable("loggbuffer", logg);
        groovyBindelser.setVariable("message-logger", logg.getEditor());
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
        lukkBuffer(synligBuffer());
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

    public GroovyShell lagShell() {
        return shell;
    }
}
