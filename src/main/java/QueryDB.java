import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class QueryDB {
    private MakeConnection makeConnection;

    public QueryDB(MakeConnection makeConnection){
        this.makeConnection = makeConnection;
    }

    public double computeQuery(String query) {
        Statement stm;

        try  {
            stm = makeConnection.getCon().createStatement();
            //System.out.println(query);
            ResultSet rs = stm.executeQuery(query);
            long num = rs.getLong(1);

            return num;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1;
    }

    public ResultSet computeChartQuery(String query) {
        Statement stm;

        try  {
            stm = makeConnection.getCon().createStatement();
            ResultSet rs = stm.executeQuery(query);

            return rs;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void computeQueryWithoutOutput(String query) {
        Statement stm;

        try  {
            stm = makeConnection.getCon().createStatement();
            stm.executeQuery(query);

        } catch (SQLException e) {
            //e.printStackTrace();
        }
    }

}
