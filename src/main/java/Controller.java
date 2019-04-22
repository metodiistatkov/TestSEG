import org.jfree.data.category.DefaultCategoryDataset;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Controller {

    private Model model;
    private View view;
    private LoadingFrame loadingFrame;
    private Thread thread;
    private double noOfImpressions;
    private double noOfClicks;
    private double noOfUniques;
    private double noOfBounces;
    private double noOfConversions;
    private double totalCost1;
    private double totalCost2;
    private String dateRestriction;

    public void setDateRestriction(String value) {
        dateRestriction = value;
    }

    public Controller() {
        //loadingFrame = new LoadingFrame();
        //loadingFrame.init();
        //loadingFrame.setVisible(false);

        view = new View();
        view.setController(this);
        view.init();
    }

    public void init(String[] filePaths) {
        model.init(filePaths);
    }

    public void reset(String[] filePaths) {
        model = new Model();
        model.setController(this);

        loadingFrame = new LoadingFrame();
        loadingFrame.init();

        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                init(filePaths);
                loadingFrame.setVisible(false);
                view.getDash().resetPanel();
            }
        });

        thread.start();
        //view.getDash().resetPanel();
    }

    public KeyMetrics getKeyMetrics() {
        return new KeyMetrics(model.executeQuery("SELECT COUNT (*) FROM ImpressionLog;"),
                model.executeQuery("SELECT COUNT (*) FROM ClickLog;"), model.executeQuery("SELECT COUNT (DISTINCT ID) FROM ClickLog;"),
                model.executeQuery("SELECT count (*) FROM ServerLog WHERE Pages_Viewed <= 1;"),
                model.executeQuery("SELECT COUNT (*) FROM ServerLog WHERE Conversion = 'Yes';"),
                model.executeQuery("SELECT SUM (Impression_Cost) FROM ImpressionLog;") + model.executeQuery("SELECT SUM (Click_Cost) FROM ClickLog;"));
    }

    public KeyMetrics getKeyMetrics(String conditions) {

        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                noOfImpressions = model.executeQuery("SELECT COUNT (*) FROM ImpressionLog i WHERE " + conditions + ";");
            }
        });
        thread1.start();


        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                noOfClicks = model.executeQuery("SELECT COUNT (*) FROM ClickLogTemp i WHERE " + conditions + ";");
            }
        });
        thread2.start();

        Thread thread3 = new Thread(new Runnable() {
            @Override
            public void run() {
                noOfUniques = model.executeQuery("SELECT COUNT (DISTINCT i.ID) FROM ClickLogTemp i WHERE " + conditions + ";");
            }
        });
        thread3.start();

        Thread thread4 = new Thread(new Runnable() {
            @Override
            public void run() {
                noOfBounces = model.executeQuery("SELECT COUNT (*) FROM ServerLogTemp i WHERE i.Pages_Viewed <= 1 AND " + conditions + ";");
            }
        });
        thread4.start();

        Thread thread5 = new Thread(new Runnable() {
            @Override
            public void run() {
                noOfConversions = model.executeQuery("SELECT COUNT (*) FROM ServerLogTemp i WHERE i.Conversion = 'Yes' AND " + conditions + ";");
            }
        });
        thread5.start();

        Thread thread6 = new Thread(new Runnable() {
            @Override
            public void run() {
                totalCost1 = model.executeQuery("SELECT SUM (Impression_Cost) FROM ImpressionLog i WHERE " + conditions + ";");
            }
        });
        thread6.start();

        Thread thread7 = new Thread(new Runnable() {
            @Override
            public void run() {
                totalCost2 = model.executeQuery("SELECT SUM (Click_Cost) FROM ClickLogTemp i WHERE " + conditions + ";");
            }
        });
        thread7.start();


        try {
            thread1.join();
            thread2.join();
            thread3.join();
            thread4.join();
            thread5.join();
            thread6.join();
            thread7.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return new KeyMetrics(noOfImpressions, noOfClicks, noOfUniques, noOfBounces, noOfConversions, totalCost1 + totalCost2);
    }

    public void updateLoading(int fileNumber) {
        loadingFrame.setProgress(fileNumber);
    }

    public DefaultCategoryDataset getDataChartTotalCost(String period, String conditions) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        ResultSet days = model.executeChartQuery("SELECT DISTINCT strftime('"+ period +"', Entry_Date) FROM ClickLog i WHERE (" + dateRestriction + ");");
        ArrayList<String> stringDays = new ArrayList<>();
        try {
            while (days.next()) {
                try {
                    String row = days.getString(1);
                    stringDays.add(row);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        for(String day : stringDays) {
            if(conditions.equals(""))
                dataset.addValue(model.executeQuery("SELECT SUM (Impression_Cost) FROM ImpressionLog WHERE strftime('"+ period +"', Entry_Date) = \"" + day + "\";") + model.executeQuery("SELECT SUM (Click_Cost) FROM ClickLog WHERE strftime('"+ period +"', Entry_Date) = \"" + day + "\";"), "day", day);
            else
                dataset.addValue(model.executeQuery("SELECT SUM (Impression_Cost) FROM ImpressionLog i WHERE " + conditions + " AND strftime('"+ period +"', Entry_Date) = \"" + day + "\";") + model.executeQuery("SELECT SUM (Click_Cost) FROM ClickLogTemp i WHERE " + conditions + " AND strftime('"+ period +"', Entry_Date) = \"" + day + "\";"), "day", day);
        }
        return dataset;
    }

    public DefaultCategoryDataset getDataChartCTR(String period, String conditions) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        ResultSet days = model.executeChartQuery("SELECT DISTINCT strftime('"+ period +"', Entry_Date) FROM ClickLog i WHERE (" + dateRestriction + ");");
        ArrayList<String> stringDays = new ArrayList<>();
        try {
            while (days.next()) {
                try {
                    String row = days.getString(1);
                    stringDays.add(row);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (String day : stringDays) {
            if(conditions.equals(""))
                dataset.addValue(model.executeQuery("SELECT COUNT (*) FROM ClickLog WHERE strftime('" + period + "', Entry_Date) = \"" + day + "\";") / model.executeQuery("SELECT COUNT (*) FROM ImpressionLog WHERE strftime('" + period + "', Entry_Date) = \"" + day + "\";"), "day", day);
            else
                dataset.addValue(model.executeQuery("SELECT COUNT (*) FROM ClickLogTemp i WHERE " + conditions + " AND strftime('" + period + "', i.Entry_Date) = \"" + day + "\";") / model.executeQuery("SELECT COUNT (*) FROM ImpressionLog i WHERE " + conditions + " AND strftime('" + period + "', i.Entry_Date) = \"" + day + "\";"), "day", day);

        }

        return dataset;
    }

    public DefaultCategoryDataset getDataChartCPA(String period, String conditions) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        ResultSet days = model.executeChartQuery("SELECT DISTINCT strftime('"+ period +"', Entry_Date) FROM ClickLog i WHERE (" + dateRestriction + ");");
        ArrayList<String> stringDays = new ArrayList<>();
        try {
            while (days.next()) {
                try {
                    String row = days.getString(1);
                    stringDays.add(row);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        for(String day : stringDays) {
            if(conditions.equals(""))
                dataset.addValue((model.executeQuery("SELECT SUM (Impression_Cost) FROM ImpressionLog WHERE strftime('"+ period +"', Entry_Date) = \"" + day + "\";") + model.executeQuery("SELECT SUM (Click_Cost) FROM ClickLog WHERE strftime('"+ period +"', Entry_Date) = \"" + day + "\";")) / model.executeQuery("SELECT COUNT (*) FROM ServerLog WHERE Conversion = 'Yes' AND strftime('"+ period +"', Entry_Date) = \"" + day + "\";"), "day", day);
            else
                dataset.addValue((model.executeQuery("SELECT SUM (i.Impression_Cost) FROM ImpressionLog i WHERE "+ conditions +" AND strftime('"+ period +"', i.Entry_Date) = \"" + day + "\";") + model.executeQuery("SELECT SUM (Click_Cost) FROM ClickLogTemp i WHERE " + conditions + " AND strftime('"+ period +"', i.Entry_Date) = \"" + day + "\";")) / model.executeQuery("SELECT COUNT (*) FROM ServerLogTemp i WHERE i.Conversion = 'Yes' AND " + conditions + " AND strftime('"+ period +"', i.Entry_Date) = \"" + day + "\";"), "day", day);
        }

        return dataset;
    }

    public DefaultCategoryDataset getDataChartCPC(String period, String conditions) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        ResultSet days = model.executeChartQuery("SELECT DISTINCT strftime('"+ period +"', Entry_Date) FROM ClickLog i WHERE (" + dateRestriction + ");");
        ArrayList<String> stringDays = new ArrayList<>();
        try {
            while (days.next()) {
                try {
                    String row = days.getString(1);
                    stringDays.add(row);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        for(String day : stringDays) {
            if(conditions.equals(""))
                dataset.addValue((model.executeQuery("SELECT SUM (Impression_Cost) FROM ImpressionLog i WHERE strftime('"+ period +"', i.Entry_Date) = \"" + day + "\";") + model.executeQuery("SELECT SUM (Click_Cost) FROM ClickLog WHERE strftime('"+ period +"', Entry_Date) = \"" + day + "\";")) / model.executeQuery("SELECT COUNT (*) FROM ClickLog WHERE strftime('"+ period +"', Entry_Date) = \"" + day + "\";"), "day", day);
            else
                dataset.addValue((model.executeQuery("SELECT SUM (i.Impression_Cost) FROM ImpressionLog i WHERE "+ conditions +" AND strftime('"+ period +"', i.Entry_Date) = \"" + day + "\";") + model.executeQuery("SELECT SUM (Click_Cost) FROM ClickLogTemp i WHERE " + conditions + " AND strftime('"+ period +"', i.Entry_Date) = \"" + day + "\";")) / model.executeQuery("SELECT COUNT (*) FROM ClickLogTemp i WHERE " + conditions + " AND strftime('"+ period +"', i.Entry_Date) = \"" + day + "\";"), "day", day);
        }

        return dataset;
    }

    public DefaultCategoryDataset getDataChartCPM(String period, String conditions) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        ResultSet days = model.executeChartQuery("SELECT DISTINCT strftime('"+ period +"', Entry_Date) FROM ClickLog i WHERE (" + dateRestriction + ");");
        ArrayList<String> stringDays = new ArrayList<>();
        try {
            while (days.next()) {
                try {
                    String row = days.getString(1);
                    stringDays.add(row);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (String day : stringDays) {
            if(conditions.equals(""))
                dataset.addValue(1000*(model.executeQuery("SELECT SUM (Impression_Cost) FROM ImpressionLog WHERE strftime('"+ period +"', Entry_Date) = \"" + day + "\";") + model.executeQuery("SELECT SUM (Click_Cost) FROM ClickLog WHERE strftime('"+ period +"', Entry_Date) = \"" + day + "\";")) / model.executeQuery("SELECT COUNT (*) FROM ImpressionLog WHERE strftime('"+ period +"', Entry_Date) = \"" + day + "\";"), "day", day);
            else
                dataset.addValue(1000*(model.executeQuery("SELECT SUM (i.Impression_Cost) FROM ImpressionLog i WHERE " + conditions + " AND strftime('"+ period +"', i.Entry_Date) = \"" + day + "\";") + model.executeQuery("SELECT SUM (Click_Cost) FROM ClickLogTemp i WHERE " + conditions + " AND strftime('"+ period +"', i.Entry_Date) = \"" + day + "\";")) / model.executeQuery("SELECT COUNT (*) FROM ImpressionLog i WHERE " + conditions + " AND strftime('"+ period +"', i.Entry_Date) = \"" + day + "\";"), "day", day);
        }

        return dataset;
    }

    public DefaultCategoryDataset getDataChartBounceRate(String period, String conditions) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        ResultSet days = model.executeChartQuery("SELECT DISTINCT strftime('"+ period +"', Entry_Date) FROM ClickLog i WHERE (" + dateRestriction + ");");
        ArrayList<String> stringDays = new ArrayList<>();
        try {
            while (days.next()) {
                try {
                    String row = days.getString(1);
                    stringDays.add(row);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (String day : stringDays) {
            if(conditions.equals(""))
                dataset.addValue(model.executeQuery("SELECT count (*) FROM ServerLog WHERE Pages_Viewed <= 1 AND strftime('"+ period +"', Entry_Date) = \"" + day + "\";") / model.executeQuery("SELECT COUNT (*) FROM ClickLog WHERE strftime('"+ period +"', Entry_Date) = \"" + day + "\";"), "day", day);
            else
                dataset.addValue(model.executeQuery("SELECT count (*) FROM ServerLogTemp i WHERE i.Pages_Viewed <= 1 AND " + conditions + " AND strftime('"+ period +"', i.Entry_Date) = \"" + day + "\";") / model.executeQuery("SELECT COUNT (*) FROM ClickLogTemp i WHERE " + conditions + " AND strftime('"+ period +"', i.Entry_Date) = \"" + day + "\";"), "day", day);

        }

        return dataset;
    }

    public DefaultCategoryDataset getChartData(String tableName, String query, String period) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        ResultSet days = model.executeChartQuery("SELECT DISTINCT strftime('"+ period +"', Entry_Date) FROM " + tableName + " i WHERE (" + dateRestriction + ");");
        ArrayList<String> stringDays = new ArrayList<>();
        try {
            while (days.next()) {
                try {
                    String row = days.getString(1);
                    stringDays.add(row);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        for(String day : stringDays) {
            if(tableName.equals("ImpressionLog"))
                dataset.addValue(model.executeQuery("" + query +" strftime('" + period + "', Entry_Date) = \"" + day + "\";"), "day", day);
            else
                dataset.addValue(model.executeQuery("" + query +" strftime('" + period + "', i.Entry_Date) = \"" + day + "\";"), "day", day);
        }
        return dataset;
    }

    public ResultSet getHistogramData(String conditions) {
        if(conditions.equals(""))
            return model.executeChartQuery("SELECT Click_Cost FROM ClickLog;");
        else
            return model.executeChartQuery("SELECT Click_Cost FROM ClickLogTemp i WHERE "+conditions+";");

    }

    public double getHistogramsBins() {
        return model.executeQuery("SELECT COUNT (i) FROM (SELECT cast(Click_Cost as int) i FROM ClickLog GROUP BY cast(Click_Cost as int));") ;//- model.executeQuery("SELECT min (i) FROM (SELECT cast(Click_Cost as int) i FROM ClickLog GROUP BY cast(Click_Cost as int));") + 1;
    }

    public String getMinDate() {
        String value = "";
        try {
            value = model.executeChartQuery("SELECT strftime('%Y-%m-%d', min(Entry_Date)) FROM ImpressionLog;").getString(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return value;
    }

    public String getMaxDate() {
        String value = "";
        try {
            value = model.executeChartQuery("SELECT strftime('%Y-%m-%d', max(Entry_Date)) FROM ImpressionLog;").getString(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return value;
    }


}
