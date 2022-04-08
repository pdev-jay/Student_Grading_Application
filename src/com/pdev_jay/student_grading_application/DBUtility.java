package com.pdev_jay.student_grading_application;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBUtility {

	public static Connection getConnection() {

		Connection con = null;
		FileReader fr = null;

		try {
			fr = new FileReader("src/com/pdev_jay/student_grading_application/db.properties");
			Properties properties = new Properties();
			properties.load(fr);

			String DRIVER = properties.getProperty("DRIVER");
			String URL = properties.getProperty("URL");
			String userID = properties.getProperty("userID");
			String userPassword = properties.getProperty("userPassword");

			//DRIVER 적재
			Class.forName(DRIVER);
			//데이터베이스 연결
			con = (Connection) DriverManager.getConnection(URL, userID, userPassword);

		} catch (ClassNotFoundException e) {
			System.out.println("mysql database connection fail");
			e.printStackTrace();
		} catch (SQLException e) {
			System.out.println("mysql database connection fail");
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			System.out.println("File not found db.properties");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("File db.properties connection fail");
			e.printStackTrace();
		} finally {
			try {
				fr.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return con;
	}
}
