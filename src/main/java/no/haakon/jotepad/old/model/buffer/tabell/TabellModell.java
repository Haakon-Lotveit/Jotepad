package no.haakon.jotepad.old.model.buffer.tabell;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import java.io.IOException;
import java.io.Reader;
import java.util.*;
import java.util.function.Function;

public class TabellModell implements TableModel {
    int rader, kolonner;

    // Fra rad, til kolonne.
    String[][] tabelldata;
    final List<TableModelListener> lyttere;
    private String[] kolonneNavn;


    public TabellModell(Collection<TableModelListener> lyttere) {
        tabelldata = new String[0][0];
        this.lyttere = new ArrayList<>(lyttere);
    }

    public TabellModell() {
        this(Collections.emptyList());
    }

    private void settVerdiOgBokfør(String verdi, int rad, int kolonne) {
        verdi = verdi == null ? "" : verdi;
        if (verdi.equals(getValueAt(rad, kolonne))) {
            return; // ingen verdier endres.
        }

        // Vi må sjekke dette først, fordi oppdateringen under melder ikke fra om dette.
        boolean nyeRader = rad > rader;
        boolean nyeKolonner = kolonne > kolonner;

        bareSettVerdi(verdi, rad, kolonne);
        final TableModelEvent hendelse = new TableModelEvent(this, rad, rad, kolonne);
        lyttere.forEach(l -> l.tableChanged(hendelse));

        // Til sist må vi informere om antall rader og kolonner:
        if (nyeRader) {
            lagEventNyRad(rad);
        }
        if (nyeKolonner) {
            lagEventNyKolonne(kolonne);
        }
    }

    private void lagEventNyRad(int rad) {
        TableModelEvent event = new TableModelEvent(this);
        lyttere.forEach(l -> l.tableChanged(event));
    }

    private void lagEventNyKolonne(int kolonne) {
        final TableModelEvent nyKolonne = new TableModelEvent(this, 0, rader, kolonne, TableModelEvent.INSERT);
        lyttere.forEach(l -> l.tableChanged(nyKolonne));
    }

    private void lagEventStrukturendring() {
        TableModelEvent event = new TableModelEvent(this, TableModelEvent.HEADER_ROW);
        lyttere.forEach(l -> l.tableChanged(event));
    }

    // Merk at verdi skal ALDRI være null.
    private void bareSettVerdi(String verdi, int rad, int kolonne) {
        radOgKolonneKapasitet(rad, kolonne);
        tabelldata[rad][kolonne] = verdi;
    }

    public void radOgKolonneKapasitet(int minimumAntallRader, int minimumAntallKolonner) {
        kolonneKapasitet(minimumAntallKolonner);
        radKapasitet(minimumAntallRader);
    }

    public void kolonneKapasitet(int minimum) {
        int nyttAntallKolonner = Integer.max(minimum, kolonner);
        Function<String[], String[]> utvidRad = (String[] rad) -> rad == null? new String[nyttAntallKolonner] : Arrays.copyOf(rad, nyttAntallKolonner);
        for(int i = 0; i < tabelldata.length; ++i) {
            tabelldata[i] = utvidRad.apply(tabelldata[i]);
        }
        kolonner = nyttAntallKolonner;
    }

    public void radKapasitet(int minimum) {
        if(tabelldata.length >= minimum) {
            return; // Alt er ok, vi trenger ikke gjøre noe.
        }

        int nyttAntallRader = Integer.max(minimum, rader);
        tabelldata = Arrays.copyOf(tabelldata, nyttAntallRader);
        rader = nyttAntallRader;
        for(int nyRadIndeks = nyttAntallRader-1; tabelldata[nyRadIndeks] == null && nyRadIndeks > 0; --nyRadIndeks) {
            tabelldata[nyRadIndeks] = new String[kolonner];
        }
    }

    @Override
    public int getRowCount() {
        return rader;
    }

    @Override
    public int getColumnCount() {
        return kolonner;
    }

    @Override
    public String getColumnName(int columnIndex) {
        if(columnIndex >= 0 && columnIndex < kolonneNavn.length) {
            String out = kolonneNavn[columnIndex];
            return out == null? "":out;
        }
        return "";
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return String.class;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return true;
    }

    @Override
    public String getValueAt(int rowIndex, int columnIndex) {
    if(rowIndex > rader-1 || columnIndex > kolonner-1 || rowIndex < 0 || columnIndex < 0) {
        System.err.printf("Prøvde å aksessere en indeks som var utenfor gyldig rekkevide. Indeks: [%d, %d], rekkevidde: [0-%d, 0-%d] (inklusiv)%nRekkefølger er rader-kolonner%n",
        rowIndex, columnIndex, rader-1, kolonner-1);
        return "";
    }
        // Hvis vi er her vet vi at tabellen har verdien vi er ute etter.
        return tabelldata[rowIndex][columnIndex];
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        settVerdiOgBokfør(aValue.toString(), rowIndex, columnIndex);
    }

    @Override
    public void addTableModelListener(TableModelListener l) {
        lyttere.add(l);
    }

    @Override
    public void removeTableModelListener(TableModelListener l) {
        lyttere.remove(l);
    }

    public void lastCsv(Reader reader, CsvIoInnstillinger options) {


        List<String[]> rows;
        try(CSVReader csvReader = options.lagReader(reader)) {
            rows = csvReader.readAll();
        }
        catch(CsvException | IOException e) {
            System.err.println("noe gikk galt. TODO: fiks bedre tilbakemelding");
            return;
        }

        if(rows.isEmpty()) {
            return; // Hvis vi ikke har noe data, setter vi ikke noe.
        }

        // Først oppdaterer vi dataene.
        // Før vi setter inn dataene, oppretter vi kolonnenavnene. Det er første rad. Vi fjerner den raden fra listen vi får ut.
        this.kolonneNavn = rows.remove(0);
        int currentRow = 0;
        for (String[] row : rows) {
            radKapasitet(rows.size()); // Vi må ha minimum så mange rader.
            kolonner = Integer.max(kolonner, row.length);
            for (int kol = 0; kol < row.length; ++kol) {
                bareSettVerdi(row[kol], currentRow, kol);
            }
            ++currentRow;
        }
        rader = rows.size();

        // Så sier vi ifra om at vi er ferdige.
        lyttere.forEach(l -> l.tableChanged(new TableModelEvent(this)));
    }

    public String[] kolonneNavn() {
        return kolonneNavn.clone();
    }

    public void settInnKolonne(String navnKolonne, int kolonneposisjon) {
        if(kolonneposisjon < 0) {
            System.err.println("Kolonneposisjon kan ikke være negativ. Var: " + kolonneposisjon);
            return;
        }
        navnKolonne = navnKolonne == null? "":navnKolonne;


        // Oppdaterer kolonnenavnene.
        kolonneNavn = radMedNyttInnhold(kolonneNavn, kolonneposisjon, navnKolonne);
        String[] nyeKolonnenavn = new String[Integer.max(getColumnCount(), kolonneposisjon+1)];

        // Oppdaterer datamodellen med ny kolonne
        for(int i = 0; i < tabelldata.length; ++i) {
            tabelldata[i] = radMedNyttInnhold(tabelldata[i], kolonneposisjon, "");
        }

        ++kolonner;
        lagEventStrukturendring();

    }

    private String[] radMedNyttInnhold(String[] gammelRad, int indeksForNyRad, String nyttInnhold) {
        String[] nyRad = Arrays.copyOf(gammelRad, gammelRad.length + 1);
        for(int i = nyRad.length-1; i > indeksForNyRad; --i) {
            nyRad[i] = nyRad[i-1];
        }
        nyRad[indeksForNyRad] = nyttInnhold;
        return nyRad;
    }

    public void settInnRad(int radPosisjon) {
        String[][] nyTabelldata = Arrays.copyOf(tabelldata, tabelldata.length+1);
        for(int i = nyTabelldata.length-1; i > radPosisjon; --i) {
            nyTabelldata[i] = nyTabelldata[i-1];
        }
        nyTabelldata[radPosisjon] = new String[kolonner];

        tabelldata = nyTabelldata;
        ++rader;
        lagEventStrukturendring();
    }

}
