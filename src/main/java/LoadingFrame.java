import javax.swing.*;
import java.awt.*;

public class LoadingFrame extends JFrame {

    private JProgressBar progressBar;

    private int height;
    private int width;

    public LoadingFrame() {
        super("Loading...");
    }

    public void initScreenSize() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.width = (int) screenSize.getWidth();
        this.height = (int) screenSize.getHeight();
    }

    public void init() {
        this.initScreenSize();

        this.setLayout(new GridLayout(1,1));

        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        panel.setLayout(new GridLayout(2,1, 5,5));
        JLabel text = new JLabel("<html>Please wait while we finish to process your files...<br/>" +
                "This may take a while.</html>");
        progressBar = new JProgressBar(0, 4);
        panel.add(text);
        panel.add(progressBar);

        this.add(panel);

        this.pack();
        this.setSize(this.getWidth() + 200, this.getHeight());
        this.setResizable(false);
        this.setLocation(this.width/2-this.getSize().width/2, this.height/2-this.getSize().height/2);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }

    public void setProgress(int fileNumber) {
        progressBar.setValue(fileNumber);
        this.repaint();
    }
}
