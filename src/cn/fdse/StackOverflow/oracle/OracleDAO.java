package cn.fdse.StackOverflow.oracle;

import java.sql.*;



public class OracleDAO{

	
	public void insertOperationProcess(String processData) {
		Connection conn = null;
		try {
			conn = DBConnection.getConnection();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		PreparedStatement pstmt = null;
        String psql = "insert into testpstmt values(?,?,?)";
        try {
			pstmt = conn.prepareStatement(psql);
			pstmt.executeBatch();
			conn.commit();
			pstmt.close();
			conn.close();


		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}
	




}
