import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import javax.swing.text.*;

public class HangmanUI extends JFrame {
    private JButton aBtn;
    private JButton bBtn;
    private JButton cBtn;
    private JButton dBtn;
    private JButton eBtn;
    private JButton fBtn;
    private JButton gBtn;
    private JButton hBtn;
    private JButton iBtn;
    private JButton jBtn;
    private JButton kBtn;
    private JButton lBtn;
    private JButton mBtn;
    private JButton nBtn;
    private JButton oBtn;
    private JButton pBtn;
    private JButton qBtn;
    private JButton rBtn;
    private JButton sBtn;
    private JButton tBtn;
    private JButton vBtn;
    private JButton uBtn;
    private JButton wBtn;
    private JButton xBtn;
    private JButton yBtn;
    private JButton zBtn;
    private JPanel imgPl;
    private JPanel keyboardPl;
    private JPanel wordPl;
    private JLabel imgLbl;
    private JTextPane wordTp;
    private JTextPane fehlerTp;

    private final String[] words = {"JAVA", "CODE", "PROGRAMM", "ENTWICKLER"};
    private String selectedWord;
    private char[] displayWord;
    private int errorCount = 0;
    private static final int MAX_ERRORS = 10;

    public HangmanUI() {
        setTitle("Hangman");
        setLayout(new BorderLayout());

        wordTp = new JTextPane();
        wordTp.setFont(new Font("Arial Monospaced", Font.BOLD, 24));
        wordTp.setEditable(false);
        add(wordTp, BorderLayout.NORTH);
        centerText(wordTp);

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

        add(keyboardPl, BorderLayout.CENTER);
        initGame();
    }

    private void initGame() {
        selectedWord = words[new Random().nextInt(words.length)];
        displayWord = new char[selectedWord.length()];
        for (int i = 0; i < displayWord.length; i++) displayWord[i] = '_';
        updateText();
        errorCount = 0;
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
            if (errorCount >= MAX_ERRORS) {
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
    }

    private static void centerText(JTextPane pane) {
        StyledDocument doc = pane.getStyledDocument();
        SimpleAttributeSet center = new SimpleAttributeSet();
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
        doc.setParagraphAttributes(0, doc.getLength(), center, false);
    }
}
