package no.haakon.jotepad.gui.components.inputlister;

import no.haakon.jotepad.gui.components.ApplicationFrame;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import java.awt.*;
import java.awt.event.*;
import java.util.Collection;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import java.util.stream.Collectors;

import static java.lang.Integer.max;

public abstract class NarrowingInputList<T> {
    protected final Collection<T> listen;
    private final String opprinneligTittel;
    protected final ApplicationFrame frame;
    protected JList<ListRepresentation<T>> nåværendeUtvalg;
    protected JFrame vindu;

    // Lys, Kamera, ACTION!
    private final ActionListener reagerPåEnter = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            gjørValg();
        }
    };

    private final DocumentListener reagerPåEndringer = new DocumentListener() {
        @Override
        public void insertUpdate(DocumentEvent e) {
            react(e);
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            react(e);
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            react(e);
        }

        private void react(DocumentEvent e) {
            try {
                String tekst = e.getDocument().getText(0, e.getDocument().getLength());
                oppdaterUtvalg(tekst);
            } catch (BadLocationException ble) {
                System.err.println("Kunne ikke hente ut teksten fra søkefeltet, noe som aldri burde skje.");
            }
        }
    };

    // håndterer at vi klikker på listen.
    private final MouseListener håndterLister = new MouseListener() {
        @Override
        public void mouseClicked(MouseEvent e) {
            if(e.getClickCount() > 1) { // Hvis du dobbeltklikker eller mer. Merk at hva som er et dobbeltklikk er opp til OS-et.
                gjørValg();
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {
            return;
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            return;
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            return;
        }

        @Override
        public void mouseExited(MouseEvent e) {
            return;
        }
    };

    public NarrowingInputList(String tittel, Collection<T> elementerTilListen, ApplicationFrame frame) {
        this.listen = elementerTilListen;
        this.opprinneligTittel = tittel;
        this.frame = frame;
        init(frame);
    }

    /**
     * Når denne metoden blir kalt er en ferdig med å bruke listen. Et valg har blitt tatt, og basert på valget, skal noe skje.
     * Hva som skal skje er opp til implementasjonen.
     */
    protected abstract void gjørValg();

    /**
     * Sjekker om et element av type T matcher mot et regulært uttrykk.
     * Dvs. at en implementasjon av denne metoden vil motta et regulært uttrykk, og skal sjekke om
     * elementet matcher dette. Elementet må derfor mappes om til en stringverdi først.
     * Hvilken stringverdi som skal brukes, og hvordan denne skal genereres må avgjøres av implementasjonen.
     * @param mønster et regulært uttrykk. Vil bli generert basert på brukerens inndata. Vil alltid være et gyldig regulært uttrykk, og aldri null.
     * @param element elementet som skal testes mot. Elementet vil alltid være medlem av den opprinnelige samlingen som ble oppgitt til denne klassen.
     * @return true hviss elementets strengrepresentasjon matches av mønsteret, og false ellers.
     */
    protected abstract boolean matchMotMønster(Pattern mønster, T element);

    /**
     * Strengen her er den som blir vist i listen per element.
     * Dersom toString er greit nok, har du lov til å bvare returnere elementets toString-metode.
     * Dersom du ikke gir inn null-verdier i den initielle samlingen er dette garantert aldri null.
     * @param element elementet som skal ha en strengrepresentasjon til listen.
     * @return en brukervennlig tekstsnutt som representerer dette objektet. (Stien til en fil til dømes).
     */
    protected abstract String stringRepresentasjon(T element);


    protected void lukkVindu() {
        vindu.dispose();
    }

    public void oppdaterUtvalg(String tekst) {
        if(tekst.length() < oppdateringGrenseverdi()) {
            return;
        }

        Optional<Pattern> oppdaterMønster = mønsterFra(tekst);
        if (oppdaterMønster.isEmpty()) {
            return;
        }

        DefaultListModel<ListRepresentation<T>> nyListModel = oppdaterListeModell(tekst, oppdaterMønster);

        nåværendeUtvalg.setModel(nyListModel);
    }

    /**
     * Hvor mange tegn må der være før en kjører en oppdatering? På små lister er det ikke et meningsfylt spørsmål.
     * På større lister derimot, slik som for tekstsøk i flere filer, gi dette mening å spesifisere.
     * Som standard returneres 3, som var et tall som fungerte for tekstsøk. Men det er fritt fram for å overkjøre denne metoden,
     * for å gi en bedre verdi for ting.
     * @return 3, et tall som eksperimentalt ble satt som grense for fritt tekstsøk gjennom hele Jotepad-kildekoden.
     */
    protected int oppdateringGrenseverdi() {
        return 3;
    }

    /**
     * Dette er metoden som oppdaterer innholdet i listen. Kan være verdt å overkjøre om du har spesielle behov for matching,
     * som å ikke matche via regex. (I det tilfellet, er det lurt å overkjøre {@link NarrowingInputList#mønsterFra(String)} også,
     * så en ikke lager patterns en ikke trenger. (En kan for eksempel kompilere ".*" og gjenbruke det objektet konstant.)
     * En får med både teksten som brukeren har skrevet ned, samt mønsteret som ble bygget tidligere i {@link NarrowingInputList#oppdaterUtvalg(String)}.
     * @param tekst Teksten brukeren har skrevet i vinduet. Aldri null.
     * @param mønster Mønsteret som ble bygget tidligere. Med mindre {@link NarrowingInputList#oppdaterUtvalg(String)} er overkjørt vil denne aldri være null heller.
     * @return en DefaultListModel som inneholder alle matchende elementer i {@link NarrowingInputList#listen} som skal med videre.
     *         Det er ikke tillatt å redigere den listen.
     */
    protected DefaultListModel<ListRepresentation<T>> oppdaterListeModell(String tekst, Optional<Pattern> mønster) {
        DefaultListModel<ListRepresentation<T>> nyListModel = new DefaultListModel<>();
        listen.stream()
                .filter(element -> tekst.isEmpty() || matchMotMønster(mønster.get(), element))
                .map(this::listRepresentation)
                .sorted()
                .forEach(nyListModel::addElement);
        return nyListModel;
    }


    /**
     * Oppdaterer mønsteret som skal matches mot. Lovlig tekst er hva som helst, unntatt null.
     * Det er ikke gitt at en kan matche mot mønsteret, i hvilket tilfellet, en tom Optional blir returnert.
     * Dette er en metode som kan være behov for å overkjøre i subklasser, dersom du trenger en spesifikk type av regulære uttrykk,
     * trenger å gjøre noe behandling av teksten mm. Standard oppførsel er at dette mønsteret blir laget hver gang teksten i listen endrer seg.
     * @param tekst teksten som skal lages et mønster fra.
     * @return en Optional som kan være tom dersom det ikke går å lage et mønster. Ellers vil den inneholde et mønster basert på teksten.
     */
    protected Optional<Pattern> mønsterFra(String tekst) {
        try {
            return Optional.of(Pattern.compile(tekst));
        }
        catch(PatternSyntaxException ignored) {
            System.err.println("Syntaksfeil i regex, oppdaterer ikke liste for tekst '" + tekst + "'");
            return Optional.empty();
        }
    }


    protected void init(JFrame hovedVindu) {
        vindu = new JFrame(opprinneligTittel);
        JPanel panel = new JPanel();
        vindu.add(panel);
        BoxLayout manager = new BoxLayout(panel, BoxLayout.Y_AXIS);
        panel.setLayout(manager);

        JTextField søkefelt = new JTextField(40);


        DefaultListModel<ListRepresentation<T>> listModel = new DefaultListModel<>();
        listModel.addAll(listen.stream().map(this::listRepresentation).collect(Collectors.toList()));
        nåværendeUtvalg = new JList<>(listModel);
        JScrollPane skroller = new JScrollPane();
        skroller.setViewportView(nåværendeUtvalg);
        nåværendeUtvalg.setLayoutOrientation(JList.VERTICAL);

        // setter felles bredde
        final int commonWidth = max(søkefelt.getPreferredSize().width, nåværendeUtvalg.getPreferredSize().width);
        Dimension søkefeltStørrelse = søkefelt.getPreferredSize();
        søkefeltStørrelse.width = commonWidth;
        søkefelt.setMinimumSize(søkefeltStørrelse);

        Dimension størrelse = nåværendeUtvalg.getPreferredSize();
        størrelse.width = commonWidth;
        nåværendeUtvalg.setMinimumSize(størrelse);
        nåværendeUtvalg.setFixedCellWidth(commonWidth);

        panel.add(søkefelt);
        panel.add(skroller);

        panel.setSize(new Dimension(commonWidth, søkefeltStørrelse.height + størrelse.height));
        vindu.pack();
        vindu.setVisible(true);
        vindu.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        søkefelt.getDocument().addDocumentListener(reagerPåEndringer);
        søkefelt.addActionListener(reagerPåEnter);
        nåværendeUtvalg.addMouseListener(håndterLister);
        KeyStroke enter = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0);
        nåværendeUtvalg.getInputMap().put(enter, "ENTER");
        nåværendeUtvalg.getActionMap().put("ENTER", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gjørValg();
            }
        });

        søkefelt.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), "FOKUS_LISTE");
        søkefelt.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), "FOKUS_LISTE");
        søkefelt.getActionMap().put("FOKUS_LISTE", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Gir fokus til listen");
                if(nåværendeUtvalg.getSelectedIndex() < 0) {
                    nåværendeUtvalg.setSelectedIndex(0);
                }
                nåværendeUtvalg.requestFocus();
            }
        });


        // putter dette vinduet i sentrum av det andre vinduet.
        int xPosisjon = ((hovedVindu.getWidth() / 2) - (vindu.getWidth() / 2)) + hovedVindu.getLocationOnScreen().x; // Velkommen til gamle dager, da dette her med get og set var litt sånn nytt og vakkert...
        int yPosisjon = ((hovedVindu.getHeight() / 2) - (vindu.getHeight() / 2)) + hovedVindu.getLocationOnScreen().y;

        vindu.setLocation(xPosisjon, yPosisjon);
    }

    /**
     * Dersom et element i listen er valgt, eller det bare er et element igjen i listen, vil dette elementet bli returnert i en Optional.
     * Ellers vil en tom optional bli returnert. Dette er en hjelpemetode for å gjøre ting lettere.
     * @return en Optional, som hvis det finnes et logisk valgt element (enten eksplisitt, eller det bare er et element igjen) inneholder elementet.
     *         Hvis ikke returneres en tom optional.
     */
    public Optional<T> getValgtElement() {
        // Hvis det bare er et element igjen.
        if(nåværendeUtvalg.getModel().getSize() == 1) {
            return Optional.of(nåværendeUtvalg.getModel().getElementAt(0).getElement());
        }

        // Hvis ikke noen elementer er valgt.
        if(nåværendeUtvalg.getSelectedIndex() < 0) {
            return Optional.empty();
        }

        return Optional.of(nåværendeUtvalg.getSelectedValue().getElement());
    }

    /*
     * Dette er bare støttekode for å gjøre det lettere å vise ting i en liste på et fornuftig vis.
     * Siden toString ikke alltid er en god måte å gjøre ting på, lager vi en ny metode som holder på et objekt, og bruker stringRepresentasjon-metoden,
     * for å generere en streng som representerer elementet. Det er fullstendig lov å definere strengrepresentasjon som
     * return e.toString(), men det er ikke alltid passende.
     */
    private ListRepresentation<T> listRepresentation(T element) {
        return new ListRepresentation<>(element, stringRepresentasjon(element));
    }

    public class ListRepresentation<T> implements Comparable<ListRepresentation<T>> {
        public ListRepresentation(T element, String representasjon) {
            this.element = element;
            this.representasjon = representasjon;
        }

        public final T element;
        public final String representasjon;

        @Override
        public String toString() {
            return representasjon;
        }

        public T getElement() {
            return element;
        }

        @Override
        public int compareTo(ListRepresentation<T> o) {
            return this.toString().compareTo(o.toString());
        }
    }

}
