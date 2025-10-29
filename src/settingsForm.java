import javax.swing.*;
import java.awt.*;

/**
 * {@summary Einstellungsdialog für das Hangman-Spiel.}
 * Kapselt Auswahl der maximalen Fehler, der Mindestwortlänge und die Option zur Fehleranzeige.
 * Bestätigt Werte über OK, verwirft/verlässt über Exit.
 */
public class settingsForm {
    private JPanel settingsOverview;
    private JRadioButton fehlerRBtn;
    private JSlider fehlerSlider;
    private JSlider laengeSlider;
    private JButton okBtn;
    private JButton exitBtn;

    private static final int[] ERROR_PRESETS = {3, 6, 10};
    private static final int[] LENGTH_MIN_PRESETS = {0, 1, 2};
    private static final String[] LENGTH_LABELS = {"kurz", "mittel", "lang"};

    private int selectedMaxErrors = ERROR_PRESETS[1];
    private int selectedMinLength = LENGTH_MIN_PRESETS[1];
    private boolean showErrors = false;

    /**
     * {@summary Initialisiert den Dialog und verdrahtet Button-Events.}
     * Überträgt Slider- und Radio-Button-Werte auf interne Felder bei OK und schließt das Fenster.
     * Exit schließt ohne Änderungen.
     */
    public settingsForm() {

        okBtn.addActionListener(e -> {

            selectedMaxErrors = ERROR_PRESETS[Math.max(0, Math.min(2, fehlerSlider.getValue()))];
            selectedMinLength = LENGTH_MIN_PRESETS[Math.max(0, Math.min(2, laengeSlider.getValue()))];
            showErrors = fehlerRBtn.isSelected();

            Window w = SwingUtilities.getWindowAncestor(settingsOverview);
            if (w != null) w.dispose();
        });

        exitBtn.addActionListener(e -> {
            Window w = SwingUtilities.getWindowAncestor(settingsOverview);
            if (w != null) w.dispose();
        });
    }

    //region Getter
    public JPanel getRoot() {
        return settingsOverview;
    }

    public int getSelectedMaxErrors() {
        return selectedMaxErrors;
    }

    public int getSelectedMinLength() {
        return selectedMinLength;
    }

    public boolean isShowErrors() {
        return showErrors;
    }
    //endregion
}