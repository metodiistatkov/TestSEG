import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYBarPainter;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.data.statistics.HistogramDataset;
import org.jfree.data.statistics.HistogramType;

import javax.swing.*;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


public class HistogramOfClickCostsPanel extends Container {

    private View view;
    private FilterPanel filterPanel;
    private org.jfree.chart.ChartPanel chartRefference;
    private Container container;

    public void updateHistrogram(String conditions) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                container.remove(chartRefference);
                JLabel label = new JLabel("Loading...");
                label.setAlignmentX(Component.CENTER_ALIGNMENT);
                container.add(label, BorderLayout.CENTER);
                revalidate();
                repaint();

                org.jfree.chart.ChartPanel chart = generateChart(conditions);
                container.remove(label);
                container.add(chart, BorderLayout.CENTER);
                chartRefference = chart;

                revalidate();
                repaint();
                filterPanel.enableFiltering();
            }
        }); thread.start();
    }

    private org.jfree.chart.ChartPanel generateChart(String conditions) {
        HistogramDataset dataset = new HistogramDataset();
        dataset.setType(HistogramType.RELATIVE_FREQUENCY);

        ResultSet data = view.getHistogramData(conditions);
        ArrayList<Double> values = new ArrayList<Double>();
        double[] valuesForDataset;

        try {
            while (data.next()) {
                try {
                    Double value = data.getDouble(1);
                    //System.out.print(value + " ");
                    values.add(value);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        valuesForDataset = new double[values.size()];

        int i = 0;
        for(Double value : values) {
            valuesForDataset[i] = value;
            i++;
        }

        //int bins = (int) Math.cbrt(values.size())*2;
        int bins = (int) (1 + 3.322*Math.log10(values.size()));
        //System.out.print(bins);
        if(!(values.size() == 0))
            dataset.addSeries("Histogram", valuesForDataset,bins);

        JFreeChart lineChart = ChartFactory.createHistogram(
                "",
                "Click Costs Intervals","Frequency",
                dataset,
                PlotOrientation.VERTICAL,
                false,true,false);

        org.jfree.chart.ChartPanel chartPanel = new org.jfree.chart.ChartPanel(lineChart);

        chartPanel.setAutoscrolls(true);
        chartPanel.setFillZoomRectangle(true);

        lineChart.setBackgroundPaint(new Color(229,229,229));
        XYPlot plot = lineChart.getXYPlot();
        XYBarRenderer renderer = (XYBarRenderer) plot.getRenderer();
        renderer.setBarPainter(new StandardXYBarPainter());

        return chartPanel;
    }

    public void init(View view, FilterPanel filterPanel) {
        this.view = view;
        this.filterPanel = filterPanel;

        this.setLayout(new BorderLayout());
        Container wrapper = new Container();
        container = wrapper;
        wrapper.setLayout(new BorderLayout());

        Container headerWrapper = new Container();
        headerWrapper.setLayout(new BorderLayout());

        JLabel text = new JLabel("<html><h2>Histogram of Click Costs</h2></html>");
        text.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        text.setAlignmentX(Component.LEFT_ALIGNMENT);

        headerWrapper.add(text, BorderLayout.CENTER);
        wrapper.add(headerWrapper, BorderLayout.NORTH);

        ChartPanel chart = generateChart("");
        chartRefference = chart;
        wrapper.add(chart, BorderLayout.CENTER);

        this.add(wrapper, BorderLayout.CENTER);
    }
}
