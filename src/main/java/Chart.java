import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.labels.StandardCategoryToolTipGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;

public class Chart extends Container {

    private View view;
    private org.jfree.chart.ChartPanel chartRefference;
    private FilterPanel filterPanel;
    private JComboBox optionsGranularityList;
    private JComboBox optionsList;
    private JPanel container;
    private String period = "%Y-%m-%d"; //time period for which to show data

    public void resetChart(String conditions) {
        this.function(optionsList.getSelectedIndex(), optionsGranularityList.getSelectedIndex(), conditions, container);
    }

    private void function(int selectedIndex, int granularityIndex, String conditions, Container container) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                container.remove(chartRefference);
                JLabel label = new JLabel("Loading...");
                label.setAlignmentX(Component.CENTER_ALIGNMENT);
                container.add(label);

                optionsGranularityList.setEnabled(false);
                optionsList.setEnabled(false);

                revalidate();
                repaint();

                switch (granularityIndex) {
                    case 0: {
                        period = "%Y-%m-%d"; break;
                    }
                    case 1: {
                        period = "Week %W of %Y"; break;
                    }
                    case 2: {
                        period = "%Y-%m"; break;
                    }
                    case 3: {
                        period = "%Y"; break;
                    }

                    default : {
                        period = "%Y-%m-%d"; break;
                    }
                }

                switch (selectedIndex) {
                    case 0: {

                        //container.remove(chartRefference);
                        org.jfree.chart.ChartPanel chart;
                        if(conditions.equals(""))
                            chart = generateChart("Number of Impressions", view.getChartData("ImpressionLog", "SELECT COUNT (*) FROM ImpressionLog WHERE", period));
                        else
                            chart = generateChart("Number of Impressions", view.getChartData("ImpressionLog", "SELECT COUNT (*) FROM ImpressionLog i WHERE " + conditions + " AND ", period));
                        container.remove(label);
                        container.add(chart);
                        chartRefference = chart;

                        revalidate();
                        repaint();
                        break;
                    }
                    case 1: {
                        //container.remove(chartRefference);
                        org.jfree.chart.ChartPanel chart;
                        if(conditions.equals(""))
                            chart = generateChart("Number of Clicks", view.getChartData("ClickLog", "SELECT COUNT (*) FROM ClickLog i WHERE", period));
                        else
                            chart = generateChart("Number of Clicks", view.getChartData("ClickLog", "SELECT COUNT (*) FROM CLickLogTemp i WHERE " + conditions + " AND ", period));
                        container.remove(label);
                        container.add(chart);
                        chartRefference = chart;

                        revalidate();
                        repaint();
                        break;
                    }
                    case 2: {
                        //container.remove(chartRefference);
                        org.jfree.chart.ChartPanel chart;
                        if(conditions.equals(""))
                            chart = generateChart("Number of Uniques", view.getChartData("ClickLog", "SELECT COUNT (DISTINCT ID) FROM ClickLog i WHERE", period));
                        else
                            chart = generateChart("Number of Uniques", view.getChartData("ClickLog", "SELECT COUNT (DISTINCT i.ID) FROM ClickLogTemp i WHERE " + conditions + " AND ", period));
                        container.remove(label);
                        container.add(chart);
                        chartRefference = chart;

                        revalidate();
                        repaint();
                        break;
                    }

                    case 3: {
                        //container.remove(chartRefference);
                        org.jfree.chart.ChartPanel chart;
                        if(conditions.equals(""))
                            chart = generateChart("Number of Bounces", view.getChartData("ServerLog", "SELECT count (*) FROM ServerLog i WHERE i.Pages_Viewed <= 1 AND ", period));
                        else
                            chart = generateChart("Number of Bounces", view.getChartData("ServerLog", "SELECT COUNT (*) FROM ServerLogTemp i WHERE i.Pages_Viewed <= 1 AND " + conditions + " AND ", period));
                        container.remove(label);
                        container.add(chart);
                        chartRefference = chart;

                        revalidate();
                        repaint();
                        break;
                    }

                    case 4: {
                        //container.remove(chartRefference);
                        org.jfree.chart.ChartPanel chart;
                        if(conditions.equals(""))
                            chart = generateChart("Number of Conversions", view.getChartData("ServerLog", "SELECT COUNT (*) FROM ServerLog i WHERE i.Conversion = 'Yes' AND", period));
                        else
                            chart = generateChart("Number of Conversions", view.getChartData("ServerLog", "SELECT COUNT (*) FROM ServerLogTemp i WHERE i.Conversion = 'Yes' AND " + conditions + " AND ", period));

                        container.remove(label);
                        container.add(chart);
                        chartRefference = chart;

                        revalidate();
                        repaint();
                        break;
                    }

                    case 5: {
                        org.jfree.chart.ChartPanel chart = generateChart("Total Cost", view.getDataChartTotalCost(period, conditions));

                        container.remove(label);
                        container.add(chart);
                        chartRefference = chart;

                        revalidate();
                        repaint();
                        break;
                    }

                    case 6: {
                        //container.remove(chartRefference);
                        org.jfree.chart.ChartPanel chart = generateChart("Click-through-rate", view.getDataChartCTR(period, conditions));

                        container.remove(label);
                        container.add(chart);
                        chartRefference = chart;

                        revalidate();
                        repaint();
                        break;
                    }

                    case 7: {
                        //container.remove(chartRefference);
                        org.jfree.chart.ChartPanel chart = generateChart("Cost-per-acquisition", view.getDataChartCPA(period, conditions));

                        container.remove(label);
                        container.add(chart);
                        chartRefference = chart;

                        revalidate();
                        repaint();
                        break;
                    }

                    case 8: {
                        //container.remove(chartRefference);
                        org.jfree.chart.ChartPanel chart = generateChart("Cost-per-click", view.getDataChartCPC(period, conditions));

                        container.remove(label);
                        container.add(chart);
                        chartRefference = chart;

                        revalidate();
                        repaint();
                        break;
                    }

                    case 9: {
                        //container.remove(chartRefference);
                        org.jfree.chart.ChartPanel chart = generateChart("Cost-per-thousand impressions", view.getDataChartCPM(period, conditions));

                        container.remove(label);
                        container.add(chart);
                        chartRefference = chart;

                        revalidate();
                        repaint();
                        break;
                    }

                    case 10: {
                        //container.remove(chartRefference);
                        org.jfree.chart.ChartPanel chart = generateChart("Bounce rate", view.getDataChartBounceRate(period, conditions));

                        container.remove(label);
                        container.add(chart);
                        chartRefference = chart;

                        revalidate();
                        repaint();
                        break;
                    }


                    default: {
                        //container.remove(chartRefference);
                        org.jfree.chart.ChartPanel chart = generateChart("Number of CLicks", view.getChartData("ClickLog", "SELECT COUNT (*) FROM ClickLog WHERE", period));

                        container.remove(label);
                        container.add(chart);
                        chartRefference = chart;


                        revalidate();
                        repaint();
                        break;
                    }
                }
                filterPanel.enableFiltering();
                optionsGranularityList.setEnabled(true);
                optionsList.setEnabled(true);
            }
        });
        thread.start();
    }

    private org.jfree.chart.ChartPanel generateChart(String YAxis, DefaultCategoryDataset dataset) {
        JFreeChart lineChart = ChartFactory.createLineChart(
                "",
                "Date",YAxis,
                dataset,
                //createDataset(),
                PlotOrientation.VERTICAL,
                false,true,false);

        org.jfree.chart.ChartPanel chartPanel = new org.jfree.chart.ChartPanel(lineChart);

        chartPanel.setAutoscrolls(true);
        chartPanel.setFillZoomRectangle(true);
        CategoryPlot plot = lineChart.getCategoryPlot();

        CategoryAxis domainAxis = plot.getDomainAxis();
        domainAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);

        final CategoryItemRenderer renderer = new LineAndShapeRenderer(true, true);

        renderer.setBaseToolTipGenerator(new StandardCategoryToolTipGenerator("{2}", NumberFormat.getInstance()));
        plot.setRenderer(renderer);

        lineChart.setBackgroundPaint(new Color(229,229,229));

        return chartPanel;
    }

    public void changeRefference (org.jfree.chart.ChartPanel chart) {
        chartRefference = chart;
    }

    public void init(View view, FilterPanel filterPanel) {
        this.view = view;
        this.filterPanel = filterPanel;

        this.setLayout(new BorderLayout());
        Container wrapper = new Container();
        wrapper.setLayout(new BorderLayout());

        JPanel headerWrapper = new JPanel();
        headerWrapper.setLayout(new GridLayout(2,2));

        JLabel dataText = new JLabel("<html><strong>Change the data displayed in the chart:</strong></html>");
        dataText.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        dataText.setAlignmentX(Component.LEFT_ALIGNMENT);

        String[] optionList = { "Number of Impressions", "Number of Clicks", "Number of Uniques", "Number of Bounces", "Number of Conversions", "Total Cost", "Click-through-rate", "Cost-per-acquisition", "Cost-per-click", "Cost-per-thousand impressions", "Bounce rate" };
        optionsList = new JComboBox(optionList);
        optionsList.setSelectedIndex(1);

        JLabel granularityText = new JLabel("<html><strong>Change the time granularity:</strong></html>");
        granularityText.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        granularityText.setAlignmentX(Component.LEFT_ALIGNMENT);

        String[] optionGranularityList = { "Daily", "Weekly", "Monthly", "Yearly"};
        optionsGranularityList = new JComboBox(optionGranularityList);
        optionsGranularityList.setSelectedIndex(0);


        final org.jfree.chart.ChartPanel chart = generateChart("Number of Clicks", view.getChartData("ClickLog", "SELECT COUNT (*) FROM ClickLog i WHERE", period));

        headerWrapper.add(dataText);
        headerWrapper.add(granularityText);
        headerWrapper.add(optionsList);
        headerWrapper.add(optionsGranularityList);
        headerWrapper.setOpaque(false);

        wrapper.add(headerWrapper, BorderLayout.NORTH);

        chartRefference = chart;
        container = new JPanel();
        container.setLayout(new BorderLayout());
        container.add(chartRefference, BorderLayout.CENTER);
        container.setOpaque(false);


        optionsGranularityList.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                function(optionsList.getSelectedIndex(),optionsGranularityList.getSelectedIndex(),view.getFilterConditions(),container);
                filterPanel.disableFiltering(1);
            }
        });

        optionsList.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                function(optionsList.getSelectedIndex(),optionsGranularityList.getSelectedIndex(),view.getFilterConditions(),container);
                filterPanel.disableFiltering(1);
            }
        });

        wrapper.add(container, BorderLayout.CENTER);

        this.add(wrapper, BorderLayout.CENTER);
    }
}
