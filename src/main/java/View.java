import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.awt.*;
import java.sql.ResultSet;

public class View {

    private Controller controller;
    private DashboardFrame dash;
    private SplashScreen splashScreen;

    public String getFilterConditions() {
        return dash.filterPanel.generateFiltersString();
    }

    public void setController(Controller controller) {
        this.controller = controller;
        splashScreen = new SplashScreen("Welcome!");
        splashScreen.init(this);
    }

    public void init() {
        dash = new DashboardFrame("DashBoard");
        dash.setView(this);
    }

    public void changeStyle(int size, String fontName, Color color) {
        LoadingFrame loadingFrame = new LoadingFrame();
        loadingFrame.init();
        new Thread(new Runnable() {
            @Override
            public void run() {
                dash.setVisible(false);
                dash.changeStyle(new JLabel(), size, fontName, color);
                loadingFrame.setVisible(false);
            }
        }).start();
    }

    public KeyMetrics getKeyMetrics() {
        return controller.getKeyMetrics();
    }

    public KeyMetrics getKeyMetrics(String conditions) {
        return controller.getKeyMetrics(conditions);
    }

    public void reset(String[] filePaths) {
        dash.setVisible(false);
        splashScreen.setVisible(false);
        controller.reset(filePaths);
    }

    public DashboardFrame getDash() {
        return this.dash;
    }

    public DefaultCategoryDataset getChartData(String tableName, String query, String period) {
        return controller.getChartData(tableName, query, period);
    }

    public DefaultCategoryDataset getDataChartTotalCost(String period, String conditions) {
        return controller.getDataChartTotalCost(period, conditions);
    }

    public DefaultCategoryDataset getDataChartCTR(String period, String conditions) {
        return controller.getDataChartCTR(period, conditions);
    }

    public DefaultCategoryDataset getDataChartCPA(String period, String conditions) {
        return controller.getDataChartCPA(period, conditions);
    }

    public DefaultCategoryDataset getDataChartCPC(String period, String conditions) {
        return controller.getDataChartCPC(period, conditions);
    }

    public DefaultCategoryDataset getDataChartCPM(String period, String conditions) {
        return controller.getDataChartCPM(period, conditions);
    }

    public DefaultCategoryDataset getDataChartBounceRate(String period, String conditions) {
        return controller.getDataChartBounceRate(period, conditions);
    }

    public String getMinDate() {
        return controller.getMinDate();
    }

    public String getMaxDate() {
        return controller.getMaxDate();
    }

    public ResultSet getHistogramData(String conditions) {
        return controller.getHistogramData(conditions);
    }

    public double getHistogramBins() { return controller.getHistogramsBins(); }

    public void setDateRestriction(String value) {
        controller.setDateRestriction(value);
    }
}
