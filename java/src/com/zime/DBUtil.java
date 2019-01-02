package com.zime;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.sql.Statement;
import java.util.logging.Logger;

public class DBUtil {
    // JDBC 驱动名及数据库 URL
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
    static final String DB_URL = "jdbc:mysql://localhost:3306/wsn";
 
    // 数据库的用户名与密码，需要根据自己的设置
    static final String USER = "root";
    static final String PASS = "123456";
    
	private Connection conn;
	
	private static DBUtil s_instance = null;
	
	private DBUtil() {
		
	}
	
	public static DBUtil getInstance() {
		if( s_instance == null ) {
			s_instance = new DBUtil();
		}
		
		return s_instance;
	}
	
	public boolean connect() {
		if( conn != null ) {
			return true;
		}
		
		try {
			Class.forName(JDBC_DRIVER);
			conn = DriverManager.getConnection(DB_URL, USER, PASS);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		return conn != null;
	}
	
	public void close() {
		if( conn != null ) {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public ResultSet query(String sql) {
		if( conn == null ) {
			connect();
		}
		
		try {
			Statement stmt = conn.createStatement();
			return stmt.executeQuery(sql);
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
		
		return null;
	}
	
	public int insert(String sql) {
		return update(sql);
	}
	
	public int delete(String sql) {
		return update(sql);
	}
	
	public int update(String sql) {
		if( conn == null ) {
			connect();
		}
		
		try {
			Statement stmt = conn.createStatement();
			return stmt.executeUpdate(sql);
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
		
		return -1;
	}

	public static void main(String[] args) {
		DBUtil util = DBUtil.getInstance();
		util.connect();
		ResultSet rs = util.query("SELECT * FROM user");
		try {
			if( rs.next() ) {
				String userName = rs.getString("username");
				String password = rs.getString("password");
				
				Logger.getLogger(DBUtil.class.getCanonicalName()).info(userName + "," + password);
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

}
