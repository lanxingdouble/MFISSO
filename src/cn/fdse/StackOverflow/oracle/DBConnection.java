package cn.fdse.StackOverflow.oracle;

import java.sql.*;

public class DBConnection {
	private static final String DBDrive ="oracle.jdbc.driver.OracleDriver";
	
	//private static final String DBUrl="jdbc:mysql://localhost:3306/stackoverflow";
	private static final String DBUrl = "jdbc:oracle:thin:@MyDbComputerNameOrIP:1521:ORCL";// 
	private static final String DBUser="root";
	private static final String DBPassword="fdse";
	
//	public static Connection getConnectio()
//	{
//		Connection connection =null;
//		try {
//				Class.forName(DBDrive);
//
//				 connection=DriverManager.getConnection(DBUrl,DBUser,DBPassword);
////				connection=DriverManager.getConnection("jdbc:sqlite://E:/ChangeMine/Revision.db");
//	    		
//			}
//    	catch (Exception e) {
//    		e.printStackTrace();
//			// TODO: handle exception
//		}
//		return connection; 
//	}
	public static Connection getConnection() throws Exception
	{
		Connection conn=null;
		try{
			Class.forName(DBDrive);
		    conn = DriverManager.getConnection(DBUrl,DBUser, DBPassword);
		}catch(ClassNotFoundException e){
			e.printStackTrace();
		}catch(SQLException e){
			e.printStackTrace();
		}
		return conn;
	}
	
	public static void close(Connection conn){
		if(conn!=null){
			try{
				conn.close();
			}catch(SQLException e){
				e.printStackTrace();
			}
		}
	}
	
	public static void close(PreparedStatement pstmt){
		if(pstmt!=null){
			try{
				pstmt.close();
			}catch(SQLException e){
				e.printStackTrace();
			}
		}
	}
	
	public static void close(ResultSet rs){
		if(rs!=null){
			try{
				rs.close();
			}catch(SQLException e){
				e.printStackTrace();
			}
		}
	}
}
