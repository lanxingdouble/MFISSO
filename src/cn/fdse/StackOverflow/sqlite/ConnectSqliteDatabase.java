package cn.fdse.StackOverflow.sqlite;
import java.sql.*;


import org.sqlite.JDBC;

import cn.fdse.StackOverflow.searchModule.util.Global;

public class ConnectSqliteDatabase
{
	private Statement stat;
	private PreparedStatement preparedStatement=null;
	private Connection connection;
	public void Set_stat()
	{
		this.stat=null;
	}
	public Statement Get_stat()
	{
		return this.stat;
	}
	public PreparedStatement Get_pstat()
	{
		return this.preparedStatement;
	}
	public void Set_connection()
	{
		this.connection=null;
	}
	
	public Connection getConnection()
	{
		return this.connection;
	}
	public void setAutoCommit(boolean b)
	{
		try {
			this.connection.setAutoCommit(b);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	 public ConnectSqliteDatabase() 
	{
		this.Set_stat();
		this.Set_connection();
		try {
    		//����sqlite��JDBC	
				Class.forName("org.sqlite.JDBC");
			//	connection=DriverManager.getConnection("jdbc:sqlite:"+Global.string+".db");
				//connection=DriverManager.getConnection("jdbc:sqlite://C:/Users/jqt/Desktop/eclipse/workspace/RevisionPatternGrub/Revision.db");
//				connection=DriverManager.getConnection("jdbc:sqlite://F:/changepatternminer/NewRevisionPatternGrub/Revision.db");

//				connection=DriverManager.getConnection("jdbc:sqlite://E:/data/swt-3-7-swt-3-7()/Revision.db");
				connection=DriverManager.getConnection("jdbc:sqlite://"+Global.syspath+"/StackOverflow.db");
//				connection=DriverManager.getConnection("jdbc:sqlite://"+path);
//				System.out.println(path);
//				connection=DriverManager.getConnection("jdbc:sqlite://E:/ChangeMine/Revision.db");
    		//
    		
    		//����һ����ݿ���AST.db�����������ھ��ڵ�ǰĿ¼�´��� 
    		 //
    		 stat=connection.createStatement();
             
    	}
    	catch (Exception e) {
    		e.printStackTrace();
			// TODO: handle exception
		}
    		
	}
}
