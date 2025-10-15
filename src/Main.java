import javax.swing.SwingUtilities;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            HangmanUI ui = new HangmanUI();
            ui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // oder: WindowConstants.EXIT_ON_CLOSE
            ui.pack();
            ui.setResizable(false);
            ui.setLocationRelativeTo(null);
            ui.setVisible(true);
        });
    }
}
