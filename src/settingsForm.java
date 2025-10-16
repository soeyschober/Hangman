import javax.swing.*;
import java.awt.*;

public class settingsForm {
    private JPanel settingsOverview;
    private JRadioButton fehlerBtn;
    private JSlider fehlerSlider;
    private JSlider laengeSlider;
    private JButton okBtn;
    private JButton exitBtn;

    public settingsForm() {

        okBtn.addActionListener(e -> {
            // missing logic
            Window w = SwingUtilities.getWindowAncestor(settingsOverview);
            if (w != null) w.dispose();
        });

        exitBtn.addActionListener(e -> {
            Window w = SwingUtilities.getWindowAncestor(settingsOverview);
            if (w != null) w.dispose();
        });
    }

    public JPanel getRoot() {
        return settingsOverview;
    }
}
