import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            HangmanUI ui = new HangmanUI();
            ui.pack();
            ui.setResizable(false);
            ui.setLocationRelativeTo(null);
            ui.setVisible(true);
        });
    }
}
