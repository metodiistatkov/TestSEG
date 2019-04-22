import javax.swing.*;
import java.awt.*;

public class SplashScreen extends JFrame {

    private int height;
    private int width;

    public SplashScreen(String frameTitle) {
        super(frameTitle);
    }

    public void initScreenSize() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.width = (int) screenSize.getWidth();
        this.height = (int) screenSize.getHeight();
    }

    public void init(View view) {
        this.initScreenSize();

        this.setLayout(new BorderLayout());
        JLabel text = new JLabel("<html><h2>Welcome!</h2></html>");
        text.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        text.setAlignmentX(Component.LEFT_ALIGNMENT);

        this.add(text, BorderLayout.NORTH);

        FileSelectPanel fileSelectPanel = new FileSelectPanel();
        fileSelectPanel.init(view);
        this.add(fileSelectPanel, BorderLayout.CENTER);

        this.pack();
        this.setSize(this.getWidth() + 200, this.getHeight());
        this.setMinimumSize(new Dimension(this.getWidth(),this.getHeight()));
        this.setLocation(this.width/2-this.getSize().width/2, this.height/2-this.getSize().height/2);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
