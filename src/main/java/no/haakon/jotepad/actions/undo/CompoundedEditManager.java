package no.haakon.jotepad.actions.undo;

import javax.swing.undo.UndoableEdit;
import java.util.ArrayList;

public class CompoundedEditManager {
    private long aldersgrense = 700_000_000L; // Hvor gammel skal en sammensatt edit være før vi begynner på en ny en?

    private CompoundedEdit building = CompoundedEdit.createSentinelCompoundedEdit(); // Dette lager en ny CompoundedEdit med maksimal alder.
    private ArrayList<CompoundedEdit> compoundedEdits;

    public CompoundedEditManager() {
        compoundedEdits = new ArrayList<>();
    }

    public void receiveUndoableEdit(UndoableEdit edit) {
        System.out.println("periode: " + building.periode());
        if(building.periode() > aldersgrense) {
            System.out.println("ny edit");
            compoundedEdits.add(building);
            building = new CompoundedEdit();
        }
        building.acceptEdit(new TimestampedEdit(edit));
    }

    /**
     * Denne vil ikke kaste exceptions, men bare stilltiende og bittert feile...
     */
    public void undo() {
        building.undoAll();
        // redos.add(building);
        building = compoundedEdits.isEmpty()? CompoundedEdit.createSentinelCompoundedEdit() : compoundedEdits.remove(compoundedEdits.size()-1);
    }

}
