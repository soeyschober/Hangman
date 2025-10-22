import javax.swing.*;
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

    public HangmanUI() {
        setTitle("Hangman");
        setLayout(new BorderLayout());
        addComponents();
        wireListeners();
        initGame();
    }

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

    private void wireListeners(){

        einstellungenBtn.addActionListener(e -> openSettingsDialog());

        neuesSpielBtn.addActionListener(e -> {
            resetButtons();
            initGame();
        });

    }

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

    private String[] getActiveWordPool() {
        switch (form.getSelectedMinLength()) {
            case 2: return longWords;
            case 1: return defaultWords;
            case 0: return shortWords;
            default: return defaultWords;
        }
    }

    private void checkLetter(char letter, JButton btn) {
        btn.setEnabled(false);
        boolean found = false;
        for (int i = 0; i < selectedWord.length(); i++) {
            if (selectedWord.charAt(i) == letter) {
                displayWord[i] = letter;
                found = true;
            }
        }
        updateText();

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
    }

    private void resetButtons() {
        for (Component c : keyboardPl.getComponents()) {
            if (c instanceof JButton) {
                c.setEnabled(true);
                c.setBackground(Color.decode("#E0E0E0"));
            }
        }
    }

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

    private static void centerText(JTextPane pane) {
        StyledDocument doc = pane.getStyledDocument();
        SimpleAttributeSet center = new SimpleAttributeSet();
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
        doc.setParagraphAttributes(0, doc.getLength(), center, false);
    }

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
