// Instructions:
// 1) Connect to "ssh dbsrv1.cdf.toronto.edu" using your cdf username and password
// 2) Download the JDBC driver (version 9.1-903 JDBC 4) from http://jdbc.postgresql.org/download.html and 
//    copy jdbc jar file (using sftp) to dbsrv1 server.
// 3) On line 60 connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/csc343h-username", "username", "");
//    -leave as is the host and port number ("localhost:5432"); 
//    -replace "username" with your cdf username, where the fields "csc343h-username" is the database name and "username" is the username that will be used to login into the database
//    - you may need to set the password field. The default one is set to empty (""); 
// 4) Compile the code:
//         javac JDBCExample.java
// 5) Run the code:
//         java -cp /*****path-to-jdbc-directory*****/postgresql-9.1-903.jdbc4.jar:. JDBCExample   
//    where postgresql-9.1-903.jdbc4.jar is jdbc jar file downloaded in step 2




import java.sql.*;
import java.lang.*;

public class Assignment2 {
    
  // A connection to the database  
  Connection connection;
  
  // Statement to run queries
  Statement sql;
  
  // Prepared Statement
  PreparedStatement ps;
  
  // Resultset for the query
  ResultSet rs;


   public static void main(String[] argv) {
        boolean testConnect;
        boolean flag;
        int count=1;
        Assignment2 A2 = new Assignment2(argv);
        //checking that arguments are valid
        if (argv.length != 3) {
            throw new IllegalArgumentException("3 arguments are required:"
                    + " URL, username and password.");
        }
        //creating the actual connection
        testConnect = A2.connectDB(argv[0], argv[1], argv[2]);
        if (testConnect == false) {
            System.out.println("There is no connection to database.");
            return;
        }
        count= A2.getCountriesNextToOceanCount(1);
        System.out.print("get countries near ocean 1: "+count);
        flag = A2.insertCountry (10, "Singapore", 50, 100);
        System.out.println("insert a country:"+flag);
        flag = A2.deleteNeighbour(1, 2);
        System.out.println("delete a country and neighbour tuple:"+flag);
    }
  //CONSTRUCTOR
  Assignment2(String[] argv){
  		System.out.println("-------- PostgreSQL JDBC Connection Testing ------------");
 		try {
		
 			// Load JDBC driver
			Class.forName("org.postgresql.Driver");
 
		} catch (ClassNotFoundException e) {
 
			System.out.println("Where is your PostgreSQL JDBC Driver? Include in your library path!");
			e.printStackTrace();
			return;
 
		}
  }
  
  //Using the input parameters, establish a connection to be used for this session. Returns true if connection is sucessful
  public boolean connectDB(String URL, String username, String password){

  		try {
			
 			//Make the connection to the database, ****** but replace "username" with your username ******
			//System.out.println("*** Please make sure to replace 'username' with your cdf username in the jdbc connection string!!!");
 			//connection = DriverManager.getConnection("jdbc:postgresql://127.0.0.1:5432/csc343h-c4wangru", "c4wangru", "LEO123--qwedsa");
 			connection = DriverManager.getConnection(URL, username, password);
 			
		} catch (SQLException e) {
 
			System.out.println("Connection Failed! Check output console");
			e.printStackTrace();
			return false;
 
		}
      return true;
  }
  
  //Closes the connection. Returns true if closure was sucessful
  public boolean disconnectDB(){
  	try {
  		connection.close();
  		return true;
  	} catch(SQLException e) {
  		e.printStackTrace();
  		return false;
  	}   
  }

  public boolean insertCountry (int cid, String name, int height, int population) {
  	try{
  		sql = connection.createStatement(); 

			//---------------------------------------------------------------------------------------
			//Create jdbc_demo table
		String sqlText;
		sqlText = ""
                    	+ "SELECT cid "
                    	+ "FROM country "
                    	+ "WHERE cid =" + cid + ";";
		//System.out.println("Executing this command: \n" + sqlText.replaceAll("\\s+", " ") + "\n");
		
    		rs=sql.executeQuery(sqlText);
    		if(rs==null)
    		{
			sqlText = ""
                   		+ "INSERT INTO country "
                    		+ "VALUES     ('" + cid + "', '" + name + "', '" + height + "','" + population + "')";
           		 //System.out.println("Executing this command: \n" + sqlText.replaceAll("\\s+", " ") + "\n");
            		sql.executeUpdate(sqlText);
           		rs.close();    		
    		}
    		else
    			return false;
    			
		return true;
		
  	}catch(SQLException e){
  		e.printStackTrace();
  		return false;
  	}
   
  }
  
  public int getCountriesNextToOceanCount(int oid) {
  	int returnvalue=-1;
  	String temp;
  	try{
	  	sql = connection.createStatement(); 

				//---------------------------------------------------------------------------------------
				//Create jdbc_demo table
		String sqlText;
		sqlText = ""
		        + "SELECT count(cid) AS count"
		        + "FROM oceanAccess "
		        + "WHERE oid =" + oid+ ";";
			//System.out.println("Executing this command: \n" + sqlText.replaceAll("\\s+", " ") + "\n");
		
	    	rs=sql.executeQuery(sqlText);
	  	if (rs==null)
	  	{
	  		return -1;
	  	}
	  	else
	  	{
	  		while(rs.next())
	  		{
				returnvalue=rs.getInt("count");

	  		}
	  		rs.close();
			return returnvalue;
	  	}
	  }catch (SQLException e) {
            System.out.println("getCountriesNextToOceanCount Exception");
            e.printStackTrace();
            return -1;
        }
  }
   
  public String getOceanInfo(int oid){
  	String returnvalue="";
  	try{
	  	sql = connection.createStatement(); 

				//---------------------------------------------------------------------------------------
				//Create jdbc_demo table
		String sqlText;
		sqlText = ""
		        + "SELECT oid, oname, depth"
		        + "FROM ocean "
		        + "WHERE oid =" + oid+ ";";
			//System.out.println("Executing this command: \n" + sqlText.replaceAll("\\s+", " ") + "\n");
		
	    	rs=sql.executeQuery(sqlText);
	  	if (rs==null)
	  	{
	  		return "";
	  	}
	  	else
	  	{
	  		while(rs.next())
	  		{
				returnvalue=rs.getString("oid")+":"+rs.getString("oname")+":"+rs.getString("depth");

	  		}
	  		rs.close();
			return returnvalue;
	  	}
	  }catch (SQLException e) {
            System.out.println("getOceanInfo Exception");
            e.printStackTrace();
            return "";
        }
  }

  public boolean chgHDI(int cid, int year, float newHDI){
 	String returnvalue="";
  	try{
	  	sql = connection.createStatement(); 

				//---------------------------------------------------------------------------------------
				//Create jdbc_demo table
			String sqlText;
 			 sqlText = "UPDATE hdi      " 
                                 + "   SET hdi_score ="+ newHDI
                                 + " WHERE  cid = " + cid
                                 + " and year=" + year; 
			//System.out.println("Executing this command: \n" + sqlText.replaceAll("\\s+", " ") + "\n");
			sql.executeUpdate(sqlText);
			System.out.println (sql.getUpdateCount() + " rows were update by this statement.\n");
			return true;
	}catch(SQLException e) {
            System.out.println("getOceanInfo Exception");
            e.printStackTrace();
            return false;
        }
  }

  public boolean deleteNeighbour(int c1id, int c2id){
  try{
	  	sql = connection.createStatement(); 

				//---------------------------------------------------------------------------------------
				//Create jdbc_demo table
		String sqlText;
 		sqlText = "DELETE from neighbour      " 
                        + " WHERE  country = " + c1id
                        + " and neighbor=" + c2id; 
			//System.out.println("Executing this command: \n" + sqlText.replaceAll("\\s+", " ") + "\n");
		sql.executeUpdate(sqlText);
		String sqlText2;
 		sqlText2 = "DELETE from neighbour      " 
                        + " WHERE  country = " + c2id
                        + " and neighbor=" + c1id; 
			//System.out.println("Executing this command: \n" + sqlText.replaceAll("\\s+", " ") + "\n");
		sql.executeUpdate(sqlText2);
		return true;
	}catch(SQLException e) {
            System.out.println("deleteNeighbour Exception");
            e.printStackTrace();
            return false;
        }       
  }
  
  public String listCountryLanguages(int cid){
	return "";
  }
  
  public boolean updateHeight(int cid, int decrH){
  try{
	  	sql = connection.createStatement(); 

				//---------------------------------------------------------------------------------------
				//Create jdbc_demo table
		String sqlText;
 		sqlText = "UPDATE country     " 
                        + "   SET height ="+ decrH
                        + " WHERE  cid = " + cid;
			//System.out.println("Executing this command: \n" + sqlText.replaceAll("\\s+", " ") + "\n");
		sql.executeUpdate(sqlText);
		System.out.println (sql.getUpdateCount() + " rows were update by this statement.\n");
		return true;
	}catch(SQLException e) {
            System.out.println("updateHeight Exception");
            e.printStackTrace();
            return false;
        } 
  }
    
  public boolean updateDB(){
	return false;    
  }
  
}
