import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import javax.swing.text.*;

public class HangmanUI extends JFrame {
    private JButton aBtn, bBtn, cBtn, dBtn, eBtn, fBtn, gBtn, hBtn, iBtn, jBtn, kBtn, lBtn, mBtn, nBtn, oBtn, pBtn, qBtn, rBtn, sBtn, tBtn, uBtn, vBtn, wBtn, xBtn, yBtn, zBtn;
    private JPanel imgPl;
    private JPanel keyboardPl;
    private JPanel wordPl;
    private JLabel imgLbl;
    private JTextPane wordTp;
    private JTextPane fehlerTp;
    private JButton neuesSpielBtn;
    private JButton einstellungenBtn;
    private JToolBar toolBar;
    private JPanel centerPnl;
    private JPanel headerPnl;

    private final String[] shortWords = {
            "JAVA", "CODE", "BYTE", "GIT", "TEST", "LOOP", "TASK", "HASH", "NODE", "PLAN", "API", "PIPE", "JSON", "ENUM", "LOG"
    };

    private final String[] defaultWords = {
            "KLASSE", "OBJECT", "THREAD", "MODULE", "LAMBDA", "SCRIPT", "BACKEND", "FRONTEND", "SERVICE", "BRANCH", "REQUEST", "RESPONSE", "ENTITIES", "PATTERN", "LIBRARY", "CLEANUP", "FEATURE", "HANDLER", "FACTORY", "PACKAGE"
    };

    private final String[] longWords = {
            "ENTWICKLER", "PROGRAMMIERER", "DATENBANKEN", "SCHNITTSTELLEN", "SERIALIZING", "KONFIGURATION", "MULTITHREADING", "AUTHENTICATION", "AUTORISIERUNG", "CONTINUOUS", "DESERIALIZING", "INFRASTRUKTUR", "ABHAENGIGKEITEN", "ANWENDUNGSDIENST", "VERIFICATION"
    };
    private String selectedWord;
    private char[] displayWord;
    private int errorCount = 0;

    private int maxErrors = 6;
    private int minWordLength = 3;

    settingsForm form = new settingsForm();

    /**
     * {@summary Initialisiert das UI-Fenster und startet ein neues Spiel.}
     * Setzt Titel und Layout, fügt Komponenten ein, verdrahtet Events und triggert initGame().
     */
    public HangmanUI() {
        setTitle("Hangman");
        setLayout(new BorderLayout());
        addComponents();
        wireListeners();
        initGame();
    }

    /**
     * {@summary Erzeugt und arrangiert alle UI-Komponenten.}
     * Baut Toolbar, Bildbereich, Wortanzeige, Fehleranzeige sowie das On-Screen-Keyboard auf
     * und fügt alles ins BorderLayout ein. Hängt Letter-Button-Handler an.
     */
    public void addComponents(){
        headerPnl = new JPanel();
        toolBar = new JToolBar();
        headerPnl.add(toolBar);
        toolBar.setFloatable(false);
        toolBar.add(einstellungenBtn);
        toolBar.add(Box.createHorizontalGlue());
        toolBar.add(neuesSpielBtn);
        add(toolBar, BorderLayout.NORTH);

        wordTp = new JTextPane();
        wordTp.setFont(new Font("Arial Monospaced", Font.BOLD, 24));
        centerText(wordTp);

        fehlerTp = new JTextPane();
        fehlerTp.setFont(new Font("Arial Monospaced", Font.BOLD, 12));

        imgPl = new JPanel(new BorderLayout());
        imgLbl = new JLabel("", SwingConstants.CENTER);
        imgPl.add(imgLbl, BorderLayout.CENTER);
        showHangman(0);

        centerPnl = new JPanel(new BorderLayout());
        centerPnl.add(imgPl, BorderLayout.NORTH);
        centerPnl.add(wordTp, BorderLayout.CENTER);
        centerPnl.add(fehlerTp, BorderLayout.SOUTH);
        add(centerPnl, BorderLayout.CENTER);

        if (keyboardPl == null) keyboardPl = new JPanel();

        if (keyboardPl.getComponentCount() == 0) {
            for (char ch = 'A'; ch <= 'Z'; ch++) {
                final char letter = ch;
                JButton btn = new JButton(String.valueOf(letter));
                btn.addActionListener(e -> checkLetter(letter, btn));
                keyboardPl.add(btn);
            }
        } else {
            for (Component c : keyboardPl.getComponents()) {
                if (c instanceof JButton) {
                    JButton btn = (JButton) c;
                    final char letter = Character.toUpperCase(btn.getText().charAt(0));
                    for (ActionListener al : btn.getActionListeners()) {
                        btn.removeActionListener(al);
                    }
                    btn.addActionListener(e -> checkLetter(letter, btn));
                }
            }
        }

        add(keyboardPl, BorderLayout.SOUTH);
    }

    /**
     * {@summary Verdrahtet Event-Listener für Toolbar-Aktionen.}
     * Öffnet den Einstellungsdialog und startet per Button ein neues Spiel inklusive Reset der Buttons.
     */
    private void wireListeners(){

        einstellungenBtn.addActionListener(e -> openSettingsDialog());

        neuesSpielBtn.addActionListener(e -> {
            resetButtons();
            initGame();
        });

    }

    /**
     * {@summary Öffnet den modalen Einstellungsdialog und übernimmt Werte.}
     * Zeigt settingsForm, liest maxErrors und minWordLength aus, fängt Fehler ab,
     * setzt UI zurück und startet ein neues Spiel.
     */
    private void openSettingsDialog() {
        JDialog dlg = new JDialog(this, "Einstellungen", true);
        dlg.setContentPane(form.getRoot());
        dlg.pack();
        dlg.setLocationRelativeTo(this);
        dlg.setVisible(true);

        try {
            this.maxErrors = form.getSelectedMaxErrors();
            this.minWordLength = form.getSelectedMinLength();
        } catch (Throwable ignored) {
        }
        resetButtons();
        initGame();
    }

    /**
     * {@summary Startet bzw. setzt eine Spielrunde zurück.}
     * Wählt anhand der Einstellungen einen Wortpool, zieht ein Zufallswort,
     * initialisiert displayWord, aktualisiert die Textanzeige und setzt Fehlerstand und Grafik.
     */
    private void initGame() {
        String[] poolArr = getActiveWordPool();

        if (poolArr == null || poolArr.length == 0) {
            poolArr = defaultWords;
        }

        // Random pick
        String pick = poolArr[new Random().nextInt(poolArr.length)];
        selectedWord = pick.toUpperCase();

        displayWord = new char[selectedWord.length()];
        for (int i = 0; i < displayWord.length; i++) displayWord[i] = '_';

        updateText();
        errorCount = 0;
        showHangman(0);
    }

    /**
     * {@summary Liefert den aktiven Wortpool entsprechend der Mindestlänge.}
     * Mapped die Auswahl der settingsForm auf shortWords, defaultWords oder longWords.
     */
    private String[] getActiveWordPool() {
        switch (form.getSelectedMinLength()) {
            case 2: return longWords;
            case 1: return defaultWords;
            case 0: return shortWords;
            default: return defaultWords;
        }
    }

    /**
     * {@summary Prüft einen geratenen Buchstaben und aktualisiert Spielzustand.}
     * Deaktiviert den Button, deckt Treffer im displayWord auf oder erhöht Fehler,
     * prüft Sieg/Niederlage, startet ggf. neue Runde und aktualisiert Anzeige.
     */
    private void checkLetter(char letter, JButton btn) {
        btn.setEnabled(false);
        boolean found = false;
        for (int i = 0; i < selectedWord.length(); i++) {
            if (selectedWord.charAt(i) == letter) {
                displayWord[i] = letter;
                found = true;
            }
        }

        if (!found) {
            errorCount++;
            showHangman(errorCount);
            if (errorCount >= maxErrors) {
                JOptionPane.showMessageDialog(this, "Game Over! Das Wort war: " + selectedWord);
                resetButtons();
                initGame();
                return;
            }
        }
        if (new String(displayWord).equals(selectedWord)) {
            JOptionPane.showMessageDialog(this, "Gewonnen! Das Wort war: " + selectedWord);
            resetButtons();
            initGame();
        }
        
        updateText();
    }

    /**
     * {@summary Setzt das On-Screen-Keyboard zurück.}
     * Aktiviert alle Buchstabenbuttons und stellt die Standardfarbe wieder her.
     */
    private void resetButtons() {
        for (Component c : keyboardPl.getComponents()) {
            if (c instanceof JButton) {
                c.setEnabled(true);
                c.setBackground(Color.decode("#E0E0E0"));
            }
        }
    }

    /**
     * {@summary Aktualisiert Wortanzeige und Fehlerzähler im UI.}
     * Formatiert displayWord mit Abständen, zentriert Text und zeigt Fehler nur bei aktivierter Option.
     */
    private void updateText() {
        StringBuilder sb = new StringBuilder();
        for (char c : displayWord) sb.append(c).append(' ');
        wordTp.setText(sb.toString());
        centerText(wordTp);

        if (form.isShowErrors()) {
            fehlerTp.setText("Fehler: " + errorCount + " / " + maxErrors);
            centerText(fehlerTp);
        } else {
            fehlerTp.setText("");
        }
    }

    /**
     * {@summary Zentriert den Text eines JTextPane.}
     * Setzt Absatz-Attribute des Dokuments auf zentrierte Ausrichtung.
     */
    private static void centerText(JTextPane pane) {
        StyledDocument doc = pane.getStyledDocument();
        SimpleAttributeSet center = new SimpleAttributeSet();
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
        doc.setParagraphAttributes(0, doc.getLength(), center, false);
    }

    /**
     * {@summary Aktualisiert die Galgenmännchen-Grafik je nach Fehlerzahl.}
     * Wählt die passende Bildserie anhand maxErrors, begrenzt den Index,
     * lädt das Bild aus dem Klassenpfad oder Dateisystem und setzt es im Label.
     */
    private void showHangman(int errors) {
        String prefix;
        int seriesMaxIndex;
        if (maxErrors <= 3) {
            prefix = "hangman3_";
            seriesMaxIndex = 3;
        } else if (maxErrors <= 6) {
            prefix = "hangman6_";
            seriesMaxIndex = 6;
        } else {
            prefix = "hangman_";
            seriesMaxIndex = 10;
        }

        int idx = Math.max(0, Math.min(errors, seriesMaxIndex));
        String cpPath = "/img/" + prefix + idx + ".png";

        java.net.URL url = getClass().getResource(cpPath);

        ImageIcon icon;
        if (url != null) {
            icon = new ImageIcon(url);
        } else {
            String fsPath = "img/" + cpPath.substring("/img/".length());
            icon = new ImageIcon(fsPath);
        }

        imgLbl.setIcon(icon);
        imgLbl.revalidate();
        imgLbl.repaint();
    }
}