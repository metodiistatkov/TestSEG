import javax.swing.*;
import java.awt.*;

public class DashboardFrame extends JFrame {

    private int height;
    private int width;
    private View view;
    public FilterPanel filterPanel;

    public DashboardFrame(String frameTitle) {
        super(frameTitle);
    }

    public void initScreenSize() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.width = (int) screenSize.getWidth();
        this.height = (int) screenSize.getHeight();
    }

    public void resetPanel() {
        this.getContentPane().removeAll();
        this.init(view);
    }

    public void setView(View view) {
        this.view = view;
    }

    public void changeStyle(Component component, int fontSize, String fontName, Color color) {
        Font f = component.getFont();
        Font labelFont = new Font(fontName,f.getStyle(),fontSize);
        UIManager.put("Label.font", labelFont);
        UIManager.put("Button.font", labelFont);
        UIManager.put("CheckBox.font", labelFont);
        UIManager.put("ComboBox.font", labelFont);
        UIManager.put("Spinner.font", labelFont);
        UIManager.put("FormattedTextField.font", labelFont);
        UIManager.put("Label.foreground", color);
        UIManager.put("Button.foreground", color);
        UIManager.put("CheckBox.foreground", color);
        UIManager.put("ComboBox.foreground", color);
        UIManager.put("Spinner.foreground", color);
        UIManager.put("FormattedTextField.foreground", color);

        resetPanel();
    }

    public void init(View view) {

        this.initScreenSize();
        this.view = view;
        //this.setSize(1024, 728);

        JTabbedPane panel = new JTabbedPane(JTabbedPane.TOP);
        panel.setTabLayoutPolicy(JTabbedPane.WRAP_TAB_LAYOUT);
        panel.setBorder(BorderFactory.createEmptyBorder());

        this.setLayout(new BorderLayout());

        filterPanel = new FilterPanel();

        KeyMetricsPanel keyMetricsPanel = new KeyMetricsPanel();
        keyMetricsPanel.init(view, filterPanel);
        panel.addTab("Key Metrics", keyMetricsPanel);

        ChartsPanel chartsPanel = new ChartsPanel();
        chartsPanel.init(view, filterPanel);
        panel.addTab("Charts", chartsPanel);

        HistogramOfClickCostsPanel histogramOfClickCostsPanel = new HistogramOfClickCostsPanel();
        histogramOfClickCostsPanel.init(view, filterPanel);
        panel.addTab("Histogram Of Click Costs", histogramOfClickCostsPanel);

        SettingsPanel settingsPanel = new SettingsPanel();
        settingsPanel.init(view);
        panel.addTab("Settings", settingsPanel);

        JLabel text = new JLabel("<html><h1 style='font-size:1.73em; text-align: center;'>Ad Auction Dashboard</h1></html>");
        text.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        text.setAlignmentX(Component.CENTER_ALIGNMENT);

        this.add(panel, BorderLayout.CENTER);
        this.add(text, BorderLayout.NORTH);

        filterPanel.init(view, keyMetricsPanel, chartsPanel, histogramOfClickCostsPanel);

        this.add(filterPanel, BorderLayout.EAST);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        //this.setMinimumSize(new Dimension(this.getWidth(), 650));
        this.setSize(this.getWidth(), 700);

        this.setLocation(this.width/2-this.getSize().width/2, this.height/2-this.getSize().height/2);
        this.setVisible(true);

    }

}
