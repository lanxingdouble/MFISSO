package cn.fdse.StackOverflow.sqlite;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


public class SYDatabase {
	private Statement stat = null;
	private PreparedStatement stat2 = null;
	ConnectSqliteDatabase connectDatabase;
	public static SYDatabase syd = new SYDatabase();
	
	public static SYDatabase getIns()
	{
		return syd;
	}

	public SYDatabase() {
		connectDatabase = new ConnectSqliteDatabase();
		this.stat = connectDatabase.Get_stat();
		// stat2 =
	}
	
	
	public void close()
	{
		try {
			this.stat.close();
			connectDatabase.getConnection().close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	public void execute(String s) {
	
		try {
			System.out.println(s);
			stat.executeUpdate(s);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	StringBuffer sBuffer = new StringBuffer();

	public void writedatabase(String s) {
		this.execute(s);

	}

	
	public ResultSet queryCopy(String s) {
		return executeQuery(s);
	}


	public ResultSet executeQuery(String s) {
		try {
			ResultSet rSet = stat.executeQuery(s);
			return rSet;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public void executeBatch() {
		try {
			connectDatabase.setAutoCommit(false);
			stat.executeBatch();
			connectDatabase.setAutoCommit(true);
		} catch (SQLException e) {
			e.printStackTrace();
			
		}

	}
}
