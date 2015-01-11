import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Struct;
import java.sql.ResultSetMetaData;

import javax.naming.spi.DirStateFactory.Result;
public class hw2 {
	@SuppressWarnings("deprecation")
	public static void main(String args[]) throws NumberFormatException, IOException, SQLException{
	String line;
		StringBuilder queryString = new StringBuilder();

		System.out.println("-------- Oracle JDBC Connection ------");
		
		try{
			Class.forName("oracle.jdbc.driver.OracleDriver");
		}
		catch(Exception e){
			System.out.println("Driver Failure");
			e.printStackTrace();
		}
		
		System.out.println("Driver Registered");
		
		Connection conn = null;
		Statement statement;
		
		try{
			conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl", "hr","database");
		}
		catch(SQLException e){
			System.out.println("Connection Failed! ");
			e.printStackTrace();
		}
		
		if(conn!=null)
			System.out.println("Connection Successful");
		else
			System.out.println("Failed to make connection!");
			if(args[0].equals("window")){
			  if(args[1].equals("building")){
				String lowerLeftXCoord = args[2];
				String lowerLeftYCoord = args[3];
				String topRightXCoord = args[4];
				String topRightYCoord = args[5];
			
				queryString = new StringBuilder();
				queryString.append("SELECT BUILDING_ID FROM building B").append(" WHERE SDO_INSIDE(B.shape, SDO_GEOMETRY(2003, NULL, NULL, SDO_ELEM_INFO_ARRAY(1, 1003, 3), SDO_ORDINATE_ARRAY(");
				queryString.append(Integer.parseInt(lowerLeftXCoord)).append(",");
				queryString.append(Integer.parseInt(lowerLeftYCoord)).append(",");
				queryString.append(Integer.parseInt(topRightXCoord)).append(",");
				queryString.append(Integer.parseInt(topRightYCoord)).append("))) = 'TRUE'");
				
			
				statement = conn.createStatement();
				ResultSet rs = statement.executeQuery(queryString.toString());
				System.out.println("BUILDING_ID");
				System.out.println("------");
				while(rs.next()){
					String output = rs.getString("BUILDING_ID");
					System.out.println(output);
				}
				statement.close();
			}
			if(args[1].equals("student")){
				String lowerLeftXCoord = args[2];
				String lowerLeftYCoord = args[3];
				String topRightXCoord = args[4];
				String topRightYCoord = args[5];
				
				queryString = new StringBuilder();
				queryString.append("SELECT student_id FROM students WHERE SDO_INSIDE(studentpos, SDO_GEOMETRY(2003, NULL, NULL, SDO_ELEM_INFO_ARRAY(1, 1003, 3), SDO_ORDINATE_ARRAY(");
				queryString.append(Integer.parseInt(lowerLeftXCoord)).append(",");
				queryString.append(Integer.parseInt(lowerLeftYCoord)).append(",");
				queryString.append(Integer.parseInt(topRightXCoord)).append(",");
				queryString.append(Integer.parseInt(topRightYCoord)).append("))) = 'TRUE'");
				
				
				statement = conn.createStatement();
				ResultSet rs = statement.executeQuery(queryString.toString());
				System.out.println("student_id");
				System.out.println("------");
				while(rs.next()){
					String output = rs.getString("student_id");
					System.out.println(output);
				}
				statement.close();
			}
			else if(args[1].equals("tramstop")){
				String lowerLeftXCoord = args[2];
				String lowerLeftYCoord = args[3];
				String topRightXCoord = args[4];
				String topRightYCoord = args[5];
				
				queryString = new StringBuilder();
				queryString.append("SELECT tramstation_id FROM tramstop WHERE SDO_INSIDE(studentposition, SDO_GEOMETRY(2001, NULL, NULL, SDO_ELEM_INFO_ARRAY(1, 1003, 3), SDO_ORDINATE_ARRAY(");
				queryString.append(Integer.parseInt(lowerLeftXCoord)).append(",");
				queryString.append(Integer.parseInt(lowerLeftYCoord)).append(",");
				queryString.append(Integer.parseInt(topRightXCoord)).append(",");
				queryString.append(Integer.parseInt(topRightYCoord)).append("))) = 'TRUE'");
				
				statement = conn.createStatement();
				ResultSet rs = statement.executeQuery(queryString.toString());
				System.out.println("tramstation_id");
				System.out.println("----");
				while(rs.next()){
					String output = rs.getString("tramstation_id");
					System.out.println(output);
				}
				statement.close();
				
			}
		}
		
		//Query type = 'within'
		if(args[0].equals("within")){String studentid = args[1];
				String distance = args[2];
				
				statement = conn.createStatement();
			
				queryString = new StringBuilder();
				
		String windowQ="SELECT BUILDING_ID FROM BUILDING WHERE SDO_WITHIN_DISTANCE(SHAPE,(SELECT S.STUDENTPOS FROM STUDENTS S WHERE S.STUDENT_ID='"+studentid+"'), 'distance="+distance+"')='TRUE' UNION ALL SELECT TRAMSTATION_ID FROM TRAMSTOP WHERE SDO_WITHIN_DISTANCE(STUDENTPOSITION,(SELECT S.STUDENTPOS FROM STUDENTS S WHERE S.STUDENT_ID='"+studentid+"'), 'distance="+distance+"')='TRUE'";
		
		ResultSet rs1 = statement.executeQuery(windowQ);
		
		System.out.println("BUILDING_ID     TRAMSTATION_ID");
				System.out.println("------");
				while(rs1.next())
				{
					System.out.println(rs1.getString("BUILDING_ID"));
					
				}	
				
				while(rs1.next())
				{
					
					System.out.println(rs1.getString("TRAMSTATION_ID"));
				}

				
			
				
		
		    statement.close();
		    

				
			} 
			 //Nearest Neighbor
			if(args[0].equals("nearest-neighbor")){
			
			if(args[1].equals("building")){
				String id = args[2];
				String number_of_neighbours = args[3];
				
				queryString = new StringBuilder();
				queryString.append("SELECT BUILDING_ID FROM (SELECT B.BUILDING_ID, MDSYS.SDO_NN_DISTANCE(1)  FROM building B WHERE B.BUILDING_ID!='");
				queryString.append(id).append("'");
				queryString.append("AND SDO_NN(B.shape, (SELECT D.shape FROM building D WHERE D.BUILDING_ID = '");
				queryString.append(id).append("'), 'SDO_BATCH_SIZE=0', 1) = 'TRUE'");
				queryString.append("ORDER BY MDSYS.SDO_NN_DISTANCE(1) ASC) WHERE ROWNUM<=").append(number_of_neighbours);
				
				statement = conn.createStatement();
				ResultSet rs = statement.executeQuery(queryString.toString());
				System.out.println("BUILDING_ID");
				System.out.println("----");
				
				while(rs.next()){
					String output = rs.getString("BUILDING_ID");
					System.out.println(output);
				}
				statement.close();
				
			}
				if(args[1].equals("student")){
				String id = args[2];
				String number_of_neighbours = args[3];
				
				queryString = new StringBuilder();
				queryString.append("SELECT student_id FROM (SELECT s.student_id, MDSYS.SDO_NN_DISTANCE(1)  FROM students s WHERE s.student_id!='");
				queryString.append(id).append("'");
				queryString.append("AND SDO_NN(s.studentpos, (SELECT D.studentpos FROM students D WHERE D.student_id = '");
				queryString.append(id).append("'), 'SDO_BATCH_SIZE=0', 1) = 'TRUE'");
				queryString.append("ORDER BY MDSYS.SDO_NN_DISTANCE(1) ASC) WHERE ROWNUM<=").append(number_of_neighbours);
				
				statement = conn.createStatement();
				ResultSet rs = statement.executeQuery(queryString.toString());
				System.out.println("student_id");
				System.out.println("----");
				
				while(rs.next()){
					String output = rs.getString("student_id");
					System.out.println(output);
				}
				statement.close();
				
			}
					if(args[1].equals("tramstop")){
				String id = args[2];
				String number_of_neighbours = args[3];
				
				queryString = new StringBuilder();
				queryString.append("SELECT tramstation_id FROM (SELECT t.tramstation_id, MDSYS.SDO_NN_DISTANCE(1)  FROM tramstop t WHERE t.tramstation_id!='");
				queryString.append(id).append("'");
				queryString.append("AND SDO_NN(t.studentposition, (SELECT D.studentposition FROM tramstop D WHERE D.tramstation_id = '");
				queryString.append(id).append("'), 'SDO_BATCH_SIZE=0', 1) = 'TRUE'");
				queryString.append("ORDER BY MDSYS.SDO_NN_DISTANCE(1) ASC) WHERE ROWNUM<=").append(number_of_neighbours);
				
				statement = conn.createStatement();
				ResultSet rs = statement.executeQuery(queryString.toString());
				System.out.println("student_id");
				System.out.println("----");
				
				while(rs.next()){
					String output = rs.getString("tramstation_id");
					System.out.println(output);
				}
				statement.close();
				
			}
			
			}
			if(args[0].equals("fixed")){
			if(args[1].equals("1")){
				queryString = new StringBuilder();
				
				
                String windowQ="(((SELECT S1.STUDENT_ID FROM STUDENTS S1 WHERE SDO_WITHIN_DISTANCE(S1.STUDENTPOS,(SELECT T1.STUDENTPOSITION FROM TRAMSTOP T1 WHERE T1.TRAMSTATION_ID='t2ohe'),'distance=70')='TRUE') INTERSECT (SELECT S2.STUDENT_ID FROM STUDENTS S2 WHERE SDO_WITHIN_DISTANCE(S2.STUDENTPOS,(SELECT T3.STUDENTPOSITION FROM TRAMSTOP T3 WHERE T3.TRAMSTATION_ID='t6ssl'),'distance=50')='TRUE')) UNION ((SELECT B1.BUILDING_ID FROM BUILDING B1 WHERE SDO_WITHIN_DISTANCE(B1.SHAPE,(SELECT T2.STUDENTPOSITION FROM TRAMSTOP T2 WHERE T2.TRAMSTATION_ID='t2ohe'),'distance=70')='TRUE') INTERSECT (SELECT B2.BUILDING_ID FROM BUILDING B2 WHERE SDO_WITHIN_DISTANCE(B2.SHAPE,(SELECT T4.STUDENTPOSITION FROM TRAMSTOP T4 WHERE T4.TRAMSTATION_ID='t6ssl'),'distance=50')='TRUE')))";				
				
				statement = conn.createStatement();
				ResultSet rs = statement.executeQuery(windowQ);
				System.out.println("Student ID	 Building_id");
				System.out.println("-----		");
				
				while(rs.next()){
					String outputB = rs.getString("STUDENT_ID");
					
					System.out.println(outputB);
				}
				statement.close();
			}
			else if(args[1].equals("2")){
				queryString = new StringBuilder();
				
				
				String windowQ="SELECT S.STUDENT_ID,T.TRAMSTATION_ID FROM TRAMSTOP T,STUDENTS S WHERE SDO_NN(T.STUDENTPOSITION,S.STUDENTPOS,'SDO_NUM_RES=2')='TRUE'";
				
				
				statement = conn.createStatement();
				ResultSet rs = statement.executeQuery(windowQ);
				System.out.println("Student ID		TRAMSTATION_ID");
				System.out.println("-----			-----");
				
				while(rs.next()){
					String outputB = rs.getString("STUDENT_ID");
					String outputF = rs.getString("TRAMSTATION_ID");
					System.out.println(outputB+"			"+outputF);
				}
				statement.close();
			}
			     else if(args[1].equals("3")){
				queryString = new StringBuilder();
				queryString.append("SELECT T.tramstation_id, COUNT(*) FROM tramstop T, building B WHERE SDO_WITHIN_DISTANCE(B.shape, T.studentposition, 'distance=250') = 'TRUE'");
				queryString.append(" GROUP BY(T.tramstation_id) HAVING COUNT(*) = ( SELECT MAX(COUNT(*)) FROM building B1, tramstop T1");
				queryString.append(" WHERE SDO_WITHIN_DISTANCE(B1.shape, T1.studentposition, 'distance=250') = 'TRUE' GROUP BY(T1.tramstation_id))");
				
				statement = conn.createStatement();
				ResultSet rs = statement.executeQuery(queryString.toString());
				System.out.println("TRAMSTATION_ID");
				System.out.println("----");
				
				while(rs.next()){
					String output = rs.getString("tramstation_id");
					System.out.println(output);
				}
				statement.close();
			}
				else if(args[1].equals("4")){
		String windowQ="SELECT STUDENT_ID,NUM_OF_REV_NN FROM (SELECT COUNT(S.STUDENT_ID) AS NUM_OF_REV_NN,S.STUDENT_ID FROM STUDENTS S,BUILDING B WHERE SDO_NN(S.STUDENTPOS,B.SHAPE,'SDO_NUM_RES=1')='TRUE' GROUP BY S.STUDENT_ID ORDER BY COUNT(S.STUDENT_ID) DESC) WHERE ROWNUM<=5";
		    statement = conn.createStatement();
			ResultSet rs = statement.executeQuery(windowQ);
				
				
				System.out.println("STUDENT_ID	COUNT");
				System.out.println("----	-------");
				
				while(rs.next()){
					String output = rs.getString("STUDENT_ID");
					String outputCount = rs.getString("NUM_OF_REV_NN");
					System.out.println(output+"		"+outputCount);
				}
				statement.close();
			}
			else if(args[1].equals("5")){
					
				queryString = new StringBuilder();
				queryString.append("SELECT maximum FROM (SELECT MAX(SDO_GEOM.SDO_MAX_MBR_ORDINATE(B.SHAPE, M.diminfo, 1)) maximum FROM BUILDING B, USER_SDO_GEOM_METADATA M ");
				queryString.append("WHERE M.table_name = 'BUILDING' AND M.column_name = 'SHAPE' AND B.BUILDING_NAME LIKE 'SS%')");
				
				
				
				statement = conn.createStatement();
				ResultSet rs = statement.executeQuery(queryString.toString());
				System.out.println("Upper X, Y Co-ordinate");
				System.out.println("----");
				
				
				while(rs.next()){
					
					String output = rs.getString("maximum");
					
					System.out.print(output + ", ");
				}
				
				queryString = new StringBuilder();
				queryString.append("SELECT MIN(SDO_GEOM.SDO_MIN_MBR_ORDINATE(B.shape, M.diminfo, 2)) AS MINX FROM building B, USER_SDO_GEOM_METADATA M ");
				queryString.append("WHERE M.table_name = 'BUILDING' AND M.column_name = 'SHAPE' AND B.BUILDING_NAME LIKE 'SS%'");
				
				
				statement = conn.createStatement();
				rs = statement.executeQuery(queryString.toString());
				
				
				while(rs.next()){
					
					String output = rs.getString("MINX");
					System.out.println(output);
					System.out.println();
				}
				
				queryString = new StringBuilder();
				queryString.append("SELECT MIN(SDO_GEOM.SDO_MIN_MBR_ORDINATE(B.shape, M.diminfo, 1)) AS MINX FROM building B, USER_SDO_GEOM_METADATA M ");
				queryString.append("WHERE M.table_name = 'BUILDING' AND column_name = 'SHAPE' AND B.BUILDING_NAME LIKE 'SS%'");
				
				statement = conn.createStatement();
				rs = statement.executeQuery(queryString.toString());
				System.out.println("Lower X, Y Co-ordinate");
				System.out.println("----");
				
				while(rs.next()){
					String output = rs.getString("MINX");
					System.out.print(output+ ", ");
				}
				
				queryString = new StringBuilder();
				queryString = new StringBuilder();
				queryString.append("SELECT MAX(SDO_GEOM.SDO_MAX_MBR_ORDINATE(B.shape, m.diminfo, 2)) AS MAXY FROM building B, user_sdo_geom_metadata M ");
				queryString.append("WHERE M.table_name = 'BUILDING' AND M.column_name = 'SHAPE' AND B.BUILDING_NAME LIKE 'SS%'");
				
				statement = conn.createStatement();
				rs = statement.executeQuery(queryString.toString());
				
				while(rs.next()){
					String output = rs.getString("MAXY");
					
					System.out.println(output);
				}
				
				statement.close();
			}
			}
			}
		    }

		
		
		