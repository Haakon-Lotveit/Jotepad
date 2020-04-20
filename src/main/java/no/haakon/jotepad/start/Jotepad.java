package no.haakon.jotepad.start;

import no.haakon.jotepad.gui.components.ApplicationFrame;
import no.haakon.jotepad.gui.components.Editor;
import no.haakon.jotepad.gui.menubar.JotepadMenubar;

import javax.swing.*;
import java.util.Arrays;

/**
 * no.haakon.jotepad.start.Jotepad er en Notepad-klon skrevet i Java.
 * Dette er ikke et komplisert program, og det har ikke noen spesielle egenskaper.
 * I beste fall er det en illustrasjon av hvordan en kan lage et enkelt program med Swing i Java.
 */
public class Jotepad {

    private static void useSystemLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("Kunne ikke bruke systemets innebygde look and feel");
        }
    }

    public static void main(String[] args) {

        System.out.println("Tilgjengelige utseender:");
        Arrays.stream(UIManager.getInstalledLookAndFeels()).forEach(info -> System.out.println(info.getClassName()));
        if (System.getenv().containsKey("LOOK_AND_FEEL")) {
            System.out.println("Fant systemvariabel LOOK_AND_FEEL. Bruker denne til å sette utseende.");
            forsøkLookAndFeel(System.getenv("LOOK_AND_FEEL"));

        } else if (System.getProperties().containsKey("look.and.feel")) {
            System.out.println("Fant property look.and.feel. Bruker denne til å sette utseende.");
            forsøkLookAndFeel(System.getProperty("look.and.feel"));

        } else {
            useSystemLookAndFeel();
        }
        ApplicationFrame applicationFrame = new ApplicationFrame();

        applicationFrame.pack();
        applicationFrame.sentrerPåSkjerm();
        applicationFrame.setVisible(true);


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
