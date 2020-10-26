package no.haakon.jotepad.start;

import no.haakon.jotepad.old.gui.components.ApplicationFrame;
import no.haakon.jotepad.old.kommando.BufferType;
import no.haakon.jotepad.old.kommando.global.ListBuffere;
import no.haakon.jotepad.old.kommando.NyRadEtter;
import no.haakon.jotepad.old.kommando.global.PopupMessage;
import no.haakon.jotepad.old.kommando.global.finnfil.FinnFil;
import no.haakon.jotepad.old.kommando.global.lagrefil.LagreFil;
import no.haakon.jotepad.old.kommando.global.lagrefil.LagreFilSom;
import no.haakon.jotepad.old.model.buffer.Buffer;
import no.haakon.jotepad.old.model.buffer.tabell.TabellBuffer;

import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Properties;
import java.util.stream.Stream;

/**
 * no.haakon.jotepad.start.Jotepad er en Notepad-klon skrevet i Java.
 * Dette er ikke et komplisert program, og det har ikke noen spesielle egenskaper.
 * I beste fall er det en illustrasjon av hvordan en kan lage et enkelt program med Swing i Java.
 */
@Deprecated // dette er den gamle starteren, ApplicationStarter er den nye. Den nye er ikke ferdig enda.
public class Jotepad {

    private static String hjemmemappe;
    private static String jotepadFolder;

    private static void useSystemLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("Kunne ikke bruke systemets innebygde look and feel");
        }
    }

    public static void main(String[] args) {
        setupFolders();
        Properties properties = loadProperties();
        //setFileAssociations(properties);
        System.out.println("Tilgjengelige utseender:");
        Arrays.stream(UIManager.getInstalledLookAndFeels()).forEach(info -> System.out.println(info.getClassName()));
        setLookAndFeel();
        ApplicationFrame applicationFrame = new ApplicationFrame(hjemmemappe);

        registrerGlobaleKommandoer(applicationFrame);
        registrerBufferKommandoer();

        applicationFrame.pack();
        applicationFrame.sentrerPåSkjerm();
        applicationFrame.setVisible(true);
    }

    private static void registrerGlobaleKommandoer(ApplicationFrame applicationFrame) {
        applicationFrame.registrerGlobalKommando(new PopupMessage());
        applicationFrame.registrerGlobalKommando(new BufferType());
        applicationFrame.registrerGlobalKommando(new FinnFil(applicationFrame));
        applicationFrame.registrerGlobalKommando(new ListBuffere(applicationFrame));
        applicationFrame.registrerGlobalKommando(new LagreFil(applicationFrame));
        applicationFrame.registrerGlobalKommando(new LagreFilSom(applicationFrame));
    }

    private static void registrerBufferKommandoer() {
        Buffer.registrerKommando(TabellBuffer.TYPE_NAVN, new NyRadEtter());
    }

    private static void setLookAndFeel() {
        if (System.getenv().containsKey("LOOK_AND_FEEL")) {
            System.out.println("Fant systemvariabel LOOK_AND_FEEL. Bruker denne til å sette utseende.");
            forsøkLookAndFeel(System.getenv("LOOK_AND_FEEL"));

        } else if (System.getProperties().containsKey("look.and.feel")) {
            System.out.println("Fant property look.and.feel. Bruker denne til å sette utseende.");
            forsøkLookAndFeel(System.getProperty("look.and.feel"));

        } else {
            useSystemLookAndFeel();
        }
    }


    private static Properties loadProperties() {
        File propertyFile = getPropertyFile();
        Properties out = new Properties();
        if(propertyFile == null) {
            System.err.println("Ingen .properties er satt.\nForventet å finne en i mappen: " + propertyFile.getAbsolutePath());
            return out;
        }

        if(!propertyFile.exists()) {
            System.err.println("Propertiesfil " + propertyFile.getAbsolutePath() + " finnes ikke.");
            return out;
        }
        try(InputStream is = new FileInputStream(propertyFile)) {
            out.load(is);
        } catch(IOException ioe) {
            System.err.println("Kunne ikke laste inn " + propertyFile.getAbsolutePath() + ".\nNerdete feilmelding:\n" + ioe);
        }

        return out;
    }

    private static void setupFolders() {
        final String os = System.getProperty("os.name");
        final String format = String.format("OS rapportert som %s, hvilket er tolket som %%s.%n", os);

        String hjemmemappe = null;
        String jotepadFolder = null;
        if(os.toLowerCase().contains("windows")) {
            System.out.printf(format, "Microsoft Windows");
            hjemmemappe = System.getenv("LOCALAPPDATA");
            jotepadFolder = "jotepad";

            // En eller annen nixbox, forhåpentligvis POSIX.
        } else if(Stream.of("aix", "nux", "nix", "osx", "sunos", "mac").anyMatch(s -> os.toLowerCase().contains(s))) {
            hjemmemappe = System.getProperty("user.home");
            jotepadFolder = ".jotepad";
            System.out.printf(format, "et UNIX-aktig os");
            // SOMETHING ELSE. All bets are off, but we'll give it a go.
        } else {
            hjemmemappe = System.getProperty("user.home");
            jotepadFolder = "jotepad";
            System.out.printf(format, "noe vi ikke har peiling på hva er, men vi skal prøve uansett.");
            System.out.println("Consider setting the property jotepad.config to the folder you want to have the configuration in.");
        }
        Jotepad.hjemmemappe = hjemmemappe;
        Jotepad.jotepadFolder = jotepadFolder;
    }

    private static File getPropertyFile() {
        final String os = System.getProperty("os.name");
        final String format = String.format("OS rapportert som %s, hvilket er tolket som %%s.%n", os);

        String confDir;
        if(System.getProperties().containsKey("jotepad.config")) {
            confDir = System.getProperty("jotepad.config");
            System.out.println("Egendefinert konfigurasjon i property jotepad.config. Bruker fil: '" + confDir + "'");
            // Microsoft Windows
        } else {
            confDir = hjemmemappe + File.separator + jotepadFolder;
        }

        File configurationFile = new File(confDir + File.separator + "jotepad.properties");
        System.out.println("The configuration directory is set to:" + configurationFile.getAbsolutePath());

        return configurationFile;
    }

    private static void forsøkLookAndFeel(String lookAndFeelClassname) {
        try {
            UIManager.setLookAndFeel(lookAndFeelClassname);
        } catch (Exception e) {
            System.err.println("Fant ikke look-and-feel med navn:" + lookAndFeelClassname);
            System.err.println("Bruker innebygget istedet");
            useSystemLookAndFeel();
        }
    }
}
