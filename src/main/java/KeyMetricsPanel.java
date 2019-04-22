import javax.swing.*;
import java.awt.*;

public class KeyMetricsPanel extends Container {

    private View view;
    private JPanel keyMetricsContainer;
    private FilterPanel filterPanel;
    private Container keyWrapper;
    private KeyMetricContainer noOfImpression;
    private KeyMetricContainer noOfClicks;
    private KeyMetricContainer noOfUniques;
    private KeyMetricContainer noOfBounes;
    private KeyMetricContainer noOfConversion;
    private KeyMetricContainer tC;
    private KeyMetricContainer cTR;
    private KeyMetricContainer cPA;
    private KeyMetricContainer cPC;
    private KeyMetricContainer cPM;
    private KeyMetricContainer bounceRate;
    private Component refference;

    public void changeKeyMetrics (final String condition) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                keyWrapper.remove(refference);

                JLabel label = new JLabel("Loading...");
                label.setAlignmentX(Component.CENTER_ALIGNMENT);

                refference = label;
                keyWrapper.add(label, BorderLayout.SOUTH);
                revalidate();
                repaint();

                KeyMetrics keyMetrics;
                if (condition.equals("")) {
                    keyMetrics = view.getKeyMetrics();
                } else {
                    keyMetrics = view.getKeyMetrics(condition);
                }

                noOfImpression.setValue(keyMetrics.getNoOfImpressions());
                noOfClicks.setValue(keyMetrics.getNoOfClicks());
                noOfUniques.setValue(keyMetrics.getNoOfUniques());
                noOfBounes.setValue(keyMetrics.getNoOfBounces());
                noOfConversion.setValue(keyMetrics.getNoOfConversions());
                tC.setValue(keyMetrics.getTotalCost());
                cTR.setValue(keyMetrics.getCTR());
                cPA.setValue(keyMetrics.getCPA());
                cPC.setValue(keyMetrics.getCPC());
                cPM.setValue(keyMetrics.getCPM());
                bounceRate.setValue(keyMetrics.getBounceRate());

                keyWrapper.remove(refference);
                refference = keyMetricsContainer;
                keyWrapper.add(keyMetricsContainer, BorderLayout.SOUTH);

                revalidate();
                repaint();
                filterPanel.enableFiltering();
            }
        });
        thread.start();

    }

    public void init(View view, FilterPanel filterPanel) {
        this.view = view;
        this.filterPanel = filterPanel;

        this.setLayout(new BorderLayout());
        Container wrapper = new Container();
        wrapper.setLayout(new BorderLayout());

        Container headerWrapper = new Container();
        headerWrapper.setLayout(new BorderLayout());

        JLabel text = new JLabel("<html><h2>Key Metrics of the Campaign</h2></html>");
        text.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        text.setAlignmentX(Component.LEFT_ALIGNMENT);

        headerWrapper.add(text, BorderLayout.CENTER);

        Container contentWrapper = new Container();
        contentWrapper.setLayout(new BorderLayout());

        keyMetricsContainer = new JPanel();
        keyMetricsContainer.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        keyMetricsContainer.setOpaque(false);

        KeyMetrics keyMetrics = view.getKeyMetrics();
        keyMetricsContainer.setLayout(new GridLayout(6,2, 20, 20));

        noOfImpression = new KeyMetricContainer("Number of Impressions", keyMetrics.getNoOfImpressions());
        keyMetricsContainer.add(noOfImpression);

        noOfClicks = new KeyMetricContainer("Number of Clicks", keyMetrics.getNoOfClicks());
        keyMetricsContainer.add(noOfClicks);

        noOfUniques = new KeyMetricContainer("Number of Uniques", keyMetrics.getNoOfUniques());
        keyMetricsContainer.add(noOfUniques);

        noOfBounes = new KeyMetricContainer("Number of Bounces", keyMetrics.getNoOfBounces());
        keyMetricsContainer.add(noOfBounes);

        noOfConversion = new KeyMetricContainer("Number of Conversions", keyMetrics.getNoOfConversions());
        keyMetricsContainer.add(noOfConversion);

        tC = new KeyMetricContainer("Total Cost", keyMetrics.getTotalCost());
        keyMetricsContainer.add(tC);

        cTR = new KeyMetricContainer("Click-through-rate", keyMetrics.getCTR());
        keyMetricsContainer.add(cTR);

        cPA = new KeyMetricContainer("Cost-per-acquisition", keyMetrics.getCPA());
        keyMetricsContainer.add(cPA);

        cPC = new KeyMetricContainer("Cost-per-click", keyMetrics.getCPC());
        keyMetricsContainer.add(cPC);

        cPM = new KeyMetricContainer("Cost-per-thousand impressions", keyMetrics.getCPM());
        keyMetricsContainer.add(cPM);

        bounceRate = new KeyMetricContainer("Bounce Rate", keyMetrics.getBounceRate());
        keyMetricsContainer.add(bounceRate);


        //FilterPanel filterPanel = new FilterPanel();
        //filterPanel.init(this.view, this);

        //SwingUtilities swingUtilities = new SwingUtilities();
        //this.makeCompactGrid(contentWrapper, 1, 2, 3, 3, 3, 3); //xPad, yPad

        keyWrapper = new Container();
        keyWrapper.setLayout(new BorderLayout());
        refference = keyMetricsContainer;
        keyWrapper.add(keyMetricsContainer, BorderLayout.SOUTH);
        keyWrapper.add(headerWrapper, BorderLayout.NORTH);

        contentWrapper.add(keyWrapper, BorderLayout.NORTH);
        wrapper.add(contentWrapper, BorderLayout.CENTER);

        /*
        Container filterContainer = new Container();
        filterContainer.setLayout(new BorderLayout());
        JScrollPane pane = new JScrollPane(filterPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        filterPanel.setBackground(new Color(229,229,229));

        pane.setOpaque(false);
        pane.setBorder(BorderFactory.createMatteBorder(0,1,0,0, Color.DARK_GRAY));
        filterContainer.add(pane, BorderLayout.CENTER);

        wrapper.add(filterContainer, BorderLayout.EAST);*/

        this.add(wrapper, BorderLayout.CENTER);
    }

}
