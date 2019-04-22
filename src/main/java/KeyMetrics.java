public class KeyMetrics {

    private double noOfImpressions = -1;
    private double noOfClicks = -1;
    private double noOfUniques = -1;
    private double noOfBounces = -1;
    private double noOfConversions = -1;
    private double totalCost = -1;
    private double CTR = -1;
    private double CPA = -1;
    private double CPC = -1;
    private double CPM = -1;
    private double bounceRate = -1;

    public KeyMetrics(double noOfImpressions, double noOfClicks, double noOfUniques, double noOfBounces, double noOfConversions, double totalCost) {
        this.noOfImpressions = noOfImpressions;
        this.noOfClicks = noOfClicks;
        this.noOfUniques = noOfUniques;
        this.noOfBounces = noOfBounces;
        this.noOfConversions = noOfConversions;
        this.totalCost = totalCost;
        this.CTR = this.noOfClicks / this.noOfImpressions;
        this.CPA = this.totalCost / this.noOfConversions;
        this.CPC = this.totalCost / this.noOfClicks;
        this.CPM = this.totalCost * 1000 / this.noOfImpressions;
        this.bounceRate = this.noOfBounces / this.noOfClicks;
        //check up calculations online later
    }

    public double getNoOfImpressions() {
        return noOfImpressions;
    }

    public double getNoOfClicks() {
        return noOfClicks;
    }

    public double getNoOfUniques() {
        return noOfUniques;
    }

    public double getNoOfBounces() {
        return noOfBounces;
    }

    public double getNoOfConversions() {
        return noOfConversions;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public double getCTR() {
        return CTR;
    }

    public double getCPA() {
        return CPA;
    }

    public double getCPC() {
        return CPC;
    }

    public double getCPM() {
        return CPM;
    }

    public double getBounceRate() {
        return bounceRate;
    }
}
