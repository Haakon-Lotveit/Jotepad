package no.haakon.jotepad.model.buffer.tabell;

import com.opencsv.ICSVWriter;
import no.haakon.jotepad.model.buffer.Buffer;
import no.haakon.jotepad.model.editor.Editor;
import no.haakon.jotepad.model.editor.TableEditor;

import javax.swing.*;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class TabellBuffer extends Buffer {

    public static final String TYPE_NAVN = "Tabell";

    private final File fil;
    private final Charset charset;
    private final TableEditor editor;
    private JTable component;
    private TabellModell modell;
    private CsvIoInnstillinger innstillinger;

    public TabellBuffer(File fil, Map<String, Object> args) {
        super(fil, args);
        this.fil = fil;
        this.charset = konverter(args.get("charset"), Charset.class).orElseGet( () -> {
            System.err.println("Intet tegnsett oppgitt, bruker default (" + Charset.defaultCharset().name() + ")");
            return Charset.defaultCharset();
        });
        this.editor = new TableEditor(this);
        this.component = new JTable();
        this.modell = new TabellModell();
        parsFil();
    }


    public static Collection<Pattern> støttedeFiler() {
        return List.of(Pattern.compile("\\.[Cc][Ss][Vv]$"));
    }

    public String lesFil() {
        try (InputStream is = new FileInputStream(fil)){
            return new String(is.readAllBytes(), charset);
        }
        catch (FileNotFoundException fnfe) {
            System.err.println("Fant ikke filen " + fil.getAbsolutePath());
            return "";
        }
        catch (IOException ioe) {
            System.err.println("Feil under lesing: " + ioe);
            return "";
        }
    }

    public void parsFil() {
        try(Reader reader = new StringReader("Header 1,Header 2,Header 3\r\nRow 1.1,Row 2.1,Row 3.1\r\nRow 1.2,Row 2.2,Row 3.2\r\nRow 1.3,Row 2.3,Row 3.3\r\n")) {
            modell.lastCsv(reader, CsvIoInnstillinger.builder().build());
        } catch(IOException nope) {
            // TODO: HANDLE THIS.
            System.err.println("Noe gikk galt, lol: " + nope);
        }
        ;

        component.setModel(modell);
    }

    public byte[] tilBytes(Charset charset) {
        try(Writer stringWriter = new StringWriter();
            ICSVWriter csvWriter = CsvIoInnstillinger.builder().build().lagWriter(stringWriter)) {
            for(String[] rad : this.getModell().tabelldata) {
                csvWriter.writeNext(rad);
            }

            return  stringWriter.toString().getBytes(charset);

        } catch (Exception e) {
            System.err.println("Kunne ikke serialisere ned filen. Returnerer ingen bytes");
            return new byte[0];
        }
    }

    @Override
    public String getTypeNavn() {
        return TYPE_NAVN;
    }

    @Override
    public void die() {
        this.component = new JTable();
    }

    @Override
    public JTable getComponent() {
        return component;
    }


    @Override
    public Editor getEditor() {
        return editor;
    }

    @Override
    public byte[] filInnhold() {
        return tilBytes(StandardCharsets.UTF_8);
    }

    // Spesifikk for TabellBuffer
    public TabellModell getModell() {
        return modell;
    }

    public CsvIoInnstillinger getCsvIoInnstillinger() {
        return innstillinger;
    }
}
