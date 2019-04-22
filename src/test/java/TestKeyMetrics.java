
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class TestKeyMetrics {

    //we have tested for both defect (using methods from classes) and validation (connecting and querying our database) errors

    private double noOfImpressions = 10;
    private double noOfClicks = 20;
    private double noOfUniques = 10;
    private double noOfBounces = 60;
    private double noOfConversions = 10;
    private double totalCost = 100;

    private double CTR = 2; //= noOfClicks / noOfImpressions
    private double CPA = 10; //= totalCost / noOfConversions
    private double CPC = 5; //= totalCost / noOfClicks
    private double CPM = 10000; //= totalCost * 1000 / noOfImpressions
    private double bounceRate = 3; //= noOfBounces / noOfClicks

    private KeyMetrics keyMetrics = new KeyMetrics(noOfImpressions, noOfClicks, noOfUniques, noOfBounces, noOfConversions, totalCost);

    private static Controller controller;
    private static String[] paths;
    private static Model model;

//    @org.junit.jupiter.api.BeforeAll
//    public static void makeConnection(){
//        model = new Model();
//        paths = new String[3];
//        paths[1]="C:\\Users\\Friksos\\IdeaProjects\\SEG\\2_week_campaign_2\\click_log.csv";
//        paths[0]="C:\\Users\\Friksos\\IdeaProjects\\SEG\\2_week_campaign_2\\impression_log.csv";
//        paths[2]="C:\\Users\\Friksos\\IdeaProjects\\SEG\\2_week_campaign_2\\server_log.csv";
//        model.init(paths);
//        System.out.println("Partition testing performed on all the key dashboard metrics");
//    }

    @Test
    void getNoOfImpressions() {
        assertEquals(10, keyMetrics.getNoOfImpressions());

    }

  @Test
    void getNoOfClicks() {
        assertEquals(20, keyMetrics.getNoOfClicks());
    }

    @Test
    void getNoOfUniques() {
        assertEquals(10, keyMetrics.getNoOfUniques());
    }

    @Test
    void getNoOfBounces() {
        assertEquals(60, keyMetrics.getNoOfBounces());
    }

    @Test
    void getNoOfConversions() {
        assertEquals(10, keyMetrics.getNoOfConversions());
    }

    @Test
    void getTotalCost() {
        assertEquals(100, keyMetrics.getTotalCost());
    }

    @Test
    void getCTR() {
        assertEquals(CTR, keyMetrics.getCTR());
    }

    @Test
    void getCPA() {
        assertEquals(CPA, keyMetrics.getCPA());
    }

    @Test
    void getCPC() {
        assertEquals(CPC, keyMetrics.getCPC());
    }

    @Test
    void getCPM() {
        assertEquals(CPM, keyMetrics.getCPM());
    }

    @Test
    void getBounceRate() {
        assertEquals(bounceRate, keyMetrics.getBounceRate());
    }


//
//    @Test
//    void getNoOfImpressions1() {
//        assertEquals(486104, model.executeQuery("SELECT COUNT (*) FROM ImpressionLog;"));
//
//    }
//
//    @org.junit.jupiter.api.Test
//    void getNoOfImpressionsFilter0() {
//        assertEquals(0, model.executeQuery("SELECT COUNT (*) FROM ImpressionLog WHERE "+" Gender = 'Male' AND Gender='Female' "+";"));
//    }
//
//    @org.junit.jupiter.api.Test
//    void getNoOfImpressionsFilter1() {
//        assertEquals(486104, model.executeQuery("SELECT COUNT (*) FROM ImpressionLog WHERE "+" Gender = 'Male' OR Gender='Female' "+";"),"Edge Case 2: When 'Male' or 'Female' are applied as a filter to the impressions it means to show count all the impressions (of all distinct users)");
//        System.out.println("getNoOfImpressionsFilter1(): \n Edge Case 2: When 'Male' or 'Female' are applied as a filter to the impressions it means to show count all the impressions (of all distinct users)");
//    }
//
//    @org.junit.jupiter.api.Test
//    void getNoOfImpressionsFilter2() {
//        assertEquals(388998, model.executeQuery("SELECT COUNT (*) FROM ImpressionLog WHERE "+" Income='Medium' OR Income='Low' "+";"));
//    }
//
//   @org.junit.jupiter.api.Test
//    void getNoOfImpressionsFilter3() {
//        assertEquals(328082, model.executeQuery("SELECT COUNT (*) FROM ImpressionLog WHERE Age BETWEEN 25 AND 54"+";"));
//    }
//
//  @org.junit.jupiter.api.Test
//    void getNoOfImpressionsChartDayFilter() {
//        assertEquals(1, model.executeQuery("SELECT COUNT (*) FROM ImpressionLog WHERE Entry_Date='2015-01-01 12:20:51' AND Gender='Male'"+";"));
//    }
//
//    @org.junit.jupiter.api.Test
//    void getNoOfImpressionsChartDayFilter0() {
//        assertEquals(4, model.executeQuery("SELECT COUNT (*) FROM ImpressionLog WHERE Entry_Date='2015-01-01 12:20:51'"+";"));
//    }
//
//    @org.junit.jupiter.api.Test
//    void getNoOfClicks1() {
//        assertEquals(23923, model.executeQuery("SELECT COUNT (*) FROM ClickLog;"));
//    }
//
//    @org.junit.jupiter.api.Test
//    void getNoOfUniques1() {
//        assertEquals(23806, model.executeQuery("SELECT COUNT (DISTINCT ID) FROM ClickLog;"));
//    }
//
//    @org.junit.jupiter.api.Test
//    void getNoOfBounces1() {
//        assertEquals(8665, model.executeQuery("SELECT COUNT (*) FROM ServerLog WHERE Pages_Viewed <= 1;"));
//    }
//
//    @org.junit.jupiter.api.Test
//    void getNoOfConversions1() {
//        assertEquals(2026, model.executeQuery("SELECT COUNT (*) FROM ServerLog WHERE Conversion = 'Yes';"));
//    }
//
//    @org.junit.jupiter.api.Test
//    void getTotalCost1() {
//        assertEquals(118097, model.executeQuery("SELECT SUM (Impression_Cost) FROM ImpressionLog;") + model.executeQuery("SELECT SUM (Click_Cost) FROM ClickLog;"));
//    }
//
//    @org.junit.jupiter.api.Test
//    void getImpressionsNoFilter5() {
//        assertEquals(0,model.executeQuery("SELECT COUNT (*) FROM ImpressionLog i WHERE " + "Context='travel'" + ";"),"No record (not 1 impression) in our dataset was logged with context being travel");
//        System.out.println("getImpressionsNoFilter5(): \n Edge Case1 : No record (not 1 impression) in our dataset was logged with context being travel");
//    }
//
//
//    @org.junit.jupiter.api.Test
//    void getCTR1() {
//        assertEquals(CTR, keyMetrics.getCTR());
//    }
//
//   @org.junit.jupiter.api.Test
//    void getCPA1() {
//        assertEquals(58.0, Math.round((model.executeQuery("SELECT SUM (Impression_Cost) FROM ImpressionLog;") + model.executeQuery("SELECT SUM (Click_Cost) FROM ClickLog;")) / model.executeQuery("SELECT COUNT (*) FROM ServerLog WHERE Conversion = 'Yes';")));
//    }
//
//   /* @org.junit.jupiter.api.Test
//    void getNoOfClicksFilter1() {
//        assertEquals(23923, model.executeQuery("SELECT COUNT (*) FROM ImpressionLog INNERJOIN ClickLog WHERE Age BETWEEN 25 AND 54"+";"));
//    }*/
//
//
//    @org.junit.jupiter.api.Test
//    void getCPC1() {
//        assertEquals(5.0, Math.round((model.executeQuery("SELECT SUM (Impression_Cost) FROM ImpressionLog;") + model.executeQuery("SELECT SUM (Click_Cost) FROM ClickLog;")) / model.executeQuery("SELECT COUNT (*) FROM ClickLog;")));
//    }
//
//    @org.junit.jupiter.api.Test
//    void getCPM1() {
//        assertEquals(243.0, Math.round(((model.executeQuery("SELECT SUM (Impression_Cost) FROM ImpressionLog;") + model.executeQuery("SELECT SUM (Click_Cost) FROM ClickLog;")) *1000)/( model.executeQuery("SELECT COUNT (*) FROM ImpressionLog"))));
//    }

}