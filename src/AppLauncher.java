import javax.swing.*;
import java.util.HashSet;
import java.util.Set;

public class AppLauncher {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
            new AppGUI().setVisible(true);

            }
        });
    }

}


