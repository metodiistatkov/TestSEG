import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class CreateDB {

    private MakeConnection makeConnection = new MakeConnection();

    public MakeConnection getMakeConnection() {
        return makeConnection;
    }

    public void createConnection() {
        makeConnection.getConnection();
    }


    public void readCSV(String path, String table, String types, String colNames){
        String csvFile = path;
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ",";

        try {

            br = new BufferedReader(new FileReader(csvFile));

            //Create table upon reading the first line
            String firstLine = br.readLine();
            createSQL(table, types);

            //Insert data
            ArrayList<String[]> array1 = new ArrayList<>();
            while ((line = br.readLine()) != null) {

                // use comma as separator
                String[] array = line.split(cvsSplitBy);
                array1.add(array);
                //insertSQL(table, types, colNames, array);



            }
            insertSQLAll(table, types, colNames, array1);

            Statement stm = makeConnection.getCon().createStatement();
            //stm.executeUpdate("backup to backup.db");
            stm.close();
            //makeConnection.getCon().close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void createSQL(String table, String types){

        try {
            Connection con = makeConnection.getCon();
            Statement stmt = con.createStatement();
            String create = "DROP TABLE IF EXISTS " + table;
            String create2 = "CREATE TABLE " + table + "(" + types + ");";
            stmt.executeUpdate(create);
            stmt.executeUpdate(create2);
            stmt.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private void insertSQL(String table, String types, String colNames, String[] array) throws SQLException{

        Connection con = makeConnection.getCon();
        String sql =  "INSERT INTO " + table + " (" + colNames + ") VALUES(";
        for(int i = 0; i < array.length; i++) {
            if(i == array.length - 1) {
                sql += "?);";
            }
            else {
                sql += "?,";
            }
        }

        con.setAutoCommit(false);
        PreparedStatement stmt=con.prepareStatement(sql);
        //System.err.println(Arrays.toString(array));
        for(int i=0; i<array.length; i++){
            if(isInteger(array[i])){
                stmt.setInt(i+1,Integer.parseInt(array[i]));
            } else {
                stmt.setString(i+1,array[i]);
            }

        }

        stmt.addBatch();
        stmt.executeBatch();
        con.commit();

        stmt.close();

    }

    private void insertSQLAll(String table, String types, String colNames, ArrayList<String[]> array) throws SQLException{

        Connection con = makeConnection.getCon();
        con.setAutoCommit(false);

        String sql =  "INSERT INTO " + table + " (" + colNames + ") VALUES(";


        for (int i = 0; i < array.get(0).length; i++) {
            if (i == array.get(0).length - 1) {
                sql += "?);";
            } else {
                sql += "?,";
            }
        }

        PreparedStatement stmt=con.prepareStatement(sql);

        for(String[] array1 : array) {
            //System.err.println(Arrays.toString(array));
            for (int i = 0; i < array1.length; i++) {
                if (isInteger(array1[i])) {
                    stmt.setInt(i + 1, Integer.parseInt(array1[i]));
                } else {
                    stmt.setString(i + 1, array1[i]);
                }

            }

            stmt.addBatch();
        }
        stmt.executeBatch();
        con.commit();

        stmt.close();

    }

    private boolean isInteger( String input )
    {
        try
        {
            Integer.parseInt( input );
            return true;
        }
        catch( Exception e)
        {
            return false;
        }
    }
}
