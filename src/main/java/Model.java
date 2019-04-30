import java.sql.ResultSet;

public class Model {

    private Controller controller;
    private String pathImpression;
    private String pathClick;
    private String pathServer;
    private QueryDB queryDB;

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public void init(String[] filePaths) {
        CreateDB csvDatabase = new CreateDB();
        csvDatabase.createConnection();
        queryDB = new QueryDB(csvDatabase.getMakeConnection());

        //controller.updateLoading(1);

        pathImpression = filePaths[0];
        String typesImpression = "Entry_Date INTEGER, ID INTEGER, Gender TEXT, AGE Text, Income TEXT, Context TEXT, Impression_Cost REAL";
        String colNamesImpression ="Entry_Date,ID,Gender,AGE,Income, Context, Impression_Cost";
        csvDatabase.readCSV(pathImpression, "ImpressionLog", typesImpression, colNamesImpression);

        //controller.updateLoading(2);

        pathClick = filePaths[1];
        String typesClick = "Entry_Date INTEGER, ID INTEGER, Click_Cost REAL";
        String colNamesClick ="Entry_Date,ID,Click_Cost";
        csvDatabase.readCSV(pathClick, "ClickLog", typesClick, colNamesClick);
        Thread clickLogThread = new Thread(new Runnable() {
            @Override
            public void run() {
                queryDB.computeQueryWithoutOutput("CREATE TABLE ClickLogTemp AS SELECT * FROM ClickLog INNER JOIN (SELECT DISTINCT ID, Gender, AGE, Income, Context From ImpressionLog) i ON ClickLog.ID = i.ID;");
            }
        });

        clickLogThread.start();

      //  controller.updateLoading(3);

        pathServer = filePaths[2];
        String typesServer = "Entry_Date INTEGER, ID INTEGER, Exit_Date TEXT, Pages_Viewed INTEGER, Conversion BLOB";
        String colNamesServer ="Entry_Date,ID,Exit_Date,Pages_Viewed,Conversion";
        csvDatabase.readCSV(pathServer, "ServerLog", typesServer, colNamesServer);


        queryDB.computeQueryWithoutOutput("CREATE TABLE ServerLogTemp AS SELECT * FROM ServerLog INNER JOIN (SELECT DISTINCT ID, Gender, AGE, Income, Context From ImpressionLog) i ON ServerLog.ID = i.ID;");
        try {
            clickLogThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
      //  controller.updateLoading(4);
    }

    public double executeQuery(String query) {
        return queryDB.computeQuery(query);
    }

    public ResultSet executeChartQuery (String query) {
        return queryDB.computeChartQuery(query);
    }
}
