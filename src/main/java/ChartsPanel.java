import javax.swing.*;
import java.awt.*;

public class ChartsPanel extends Container {

    private View view;
    private Chart chart;

    public void resetChart(String conditions) {
        chart.resetChart(conditions);
    }

    public void init(View view, FilterPanel filterPanel) {

        this.setLayout(new BorderLayout());

        Container wrapper = new Container();
        wrapper.setLayout(new BorderLayout());

        JLabel text = new JLabel("<html><h2>Charts</h2></html>");
        text.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
        text.setAlignmentX(Component.LEFT_ALIGNMENT);

        wrapper.add(text, BorderLayout.NORTH);

        Container chartsWrapper = new Container();
        chartsWrapper.setLayout(new BoxLayout(chartsWrapper, BoxLayout.PAGE_AXIS));

        chart = new Chart();
        chart.init(view, filterPanel);
        chartsWrapper.add(chart);

        wrapper.add(chartsWrapper, BorderLayout.CENTER);
        this.add(wrapper, BorderLayout.CENTER);

        /*
        Container filterContainer = new Container();
        filterContainer.setLayout(new BorderLayout());
        JScrollPane pane = new JScrollPane(filterPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        filterPanel.setBackground(new Color(229,229,229));

        pane.setOpaque(false);
        pane.setBorder(BorderFactory.createMatteBorder(0,1,0,0, Color.DARK_GRAY));
        filterContainer.add(pane, BorderLayout.CENTER);

        this.add(filterContainer, BorderLayout.EAST);*/
    }


}
