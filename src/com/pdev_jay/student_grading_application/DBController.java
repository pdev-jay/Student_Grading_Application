package com.pdev_jay.student_grading_application;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class DBController {

	public static int studentDataInsert(StudentDataModel studentDataModel) {
		int returnValue = 0;
		String insertQuery = null;
		PreparedStatement preparedStatement = null;
		
		Connection conn = DBUtility.getConnection();

		if(conn == null) {
			System.out.println("MySQL DB 연결 실패");
			return 0;
		}

		insertQuery = "insert into StudentDB.StudentDataTBL values (?, ?, ?, ?, ?, ?, ?, ?, ?)";

		
		try {
			//입력할 값을 query문에 연결
			preparedStatement = (PreparedStatement) conn.prepareStatement(insertQuery);
			preparedStatement.setString(1, studentDataModel.getName());
			preparedStatement.setInt(2, studentDataModel.getStudentNumber());
			preparedStatement.setString(3, studentDataModel.getGender());
			preparedStatement.setInt(4, studentDataModel.getKor());
			preparedStatement.setInt(5, studentDataModel.getEng());
			preparedStatement.setInt(6, studentDataModel.getMath());
			preparedStatement.setInt(7, studentDataModel.getSum());
			preparedStatement.setDouble(8, studentDataModel.getAvr());
			preparedStatement.setString(9, String.valueOf(studentDataModel.getGrade()));

			//삽입한 데이터의 갯수 리턴
			returnValue = preparedStatement.executeUpdate(); 
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			//preparedStatement와 connection 닫기
			try {
				if(preparedStatement != null && !preparedStatement.isClosed()) {
					preparedStatement.close();
				}
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return returnValue;
	}


	public static List<StudentDataModel> studentDataSelect() {
		List<StudentDataModel> list = new ArrayList<>();
		String selectQuery = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		
		Connection conn = DBUtility.getConnection();

		if(conn == null) {
			System.out.println("MySQL DB 연결 실패");
			return null;
		}

		selectQuery = "select * from StudentDB.StudentDataTBL";

		try {
			preparedStatement = (PreparedStatement) conn.prepareStatement(selectQuery);

			//executeQuery는 resultSet을 리턴
			resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				String name = resultSet.getString(1);
				int studentNumber = resultSet.getInt(2);
				String gender = resultSet.getString(3);
				int kor = resultSet.getInt(4);
				int eng = resultSet.getInt(5);
				int math = resultSet.getInt(6);
				int sum = resultSet.getInt(7);
				double avr = resultSet.getDouble(8);
				String grade = resultSet.getString(9);
				//문자열 타입을 문자형으로 변환
				char grade_char = grade.charAt(0);
				
				StudentDataModel studentDataModel = new StudentDataModel(name, studentNumber, gender, kor, eng, math, sum, avr, grade_char);
				list.add(studentDataModel);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if(preparedStatement != null && !preparedStatement.isClosed()) {
					preparedStatement.close();
				}
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}		
		return list;
	}


	public static List<StudentDataModel> studentDataSearch(String data, SearchType searchType) {
		List<StudentDataModel> list = new ArrayList<>();
		String searchQuery = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		Connection conn = DBUtility.getConnection();

		if(conn == null) {
			System.out.println("MySQL DB 연결 실패");
			return null;
		}

		try {
			//검색 옵션에 따른 query문 변경
			switch (searchType) {
			case NAME:
				searchQuery = "select * from StudentDB.StudentDataTBL where name like ?";
				preparedStatement = (PreparedStatement) conn.prepareStatement(searchQuery);
				data = "%" + data + "%";
				preparedStatement.setString(1, data);
				break;
				
			case STUDENT_NUMBER:
				searchQuery = "select * from StudentDB.StudentDataTBL where studentNumber like ?";
				preparedStatement = (PreparedStatement) conn.prepareStatement(searchQuery);
				preparedStatement.setInt(1, Integer.parseInt(data));
				break;
			}
			
			resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				String name = resultSet.getString(1);
				int studentNumber = resultSet.getInt(2);
				String gender = resultSet.getString(3);
				int kor = resultSet.getInt(4);
				int eng = resultSet.getInt(5);
				int math = resultSet.getInt(6);
				int sum = resultSet.getInt(7);
				double avr = resultSet.getDouble(8);
				String grade = resultSet.getString(9);
				//문자열 타입을 문자형으로 변환
				char grade_char = grade.charAt(0);
				
				StudentDataModel studentDataModel = new StudentDataModel(name, studentNumber, gender, kor, eng, math, sum, avr, grade_char);
				list.add(studentDataModel);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if(preparedStatement != null && !preparedStatement.isClosed()) {
					preparedStatement.close();
				}
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}		
		return list;
	}

	public static int studentDataDelete(int studentNumber) {
		int result = 0;
		String deleteQuery = null;
		
		Connection con = DBUtility.getConnection();

		if (con == null) {
			System.out.println("MySQL DB 연결 실패");
			return result;
		}

		deleteQuery = "delete from StudentDB.StudentDataTBL where studentNumber like ?";
		PreparedStatement preparedStatement = null;

		try {
			preparedStatement = (PreparedStatement) con.prepareStatement(deleteQuery);
			preparedStatement.setInt(1, studentNumber);
			result = preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if(preparedStatement != null && !preparedStatement.isClosed()) {
					preparedStatement.close();
				}
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	//학생 이름을 변경할 경우
	public static int studentDataUpdate(int studentNumber, String data) {
		int result = 0;
		String updateQuery = null;
		
		Connection con = DBUtility.getConnection();

		if (con == null) {
			System.out.println("MySQL DB 연결 실패");
			return result;
		}

		updateQuery = "update StudentDB.StudentDataTBL set name = ? where studentNumber like ?";
		PreparedStatement preparedStatement = null;

		try {
			preparedStatement = (PreparedStatement) con.prepareStatement(updateQuery);
			preparedStatement.setString(1, data);
			preparedStatement.setInt(2, studentNumber);
			result = preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if(preparedStatement != null && !preparedStatement.isClosed()) {
					preparedStatement.close();
				}
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	//학생의 과목 점수를 변경할 경우
	public static int studentDataUpdate(int studentNumber, int data, UpdateOption updateOption) {
		int result = 0;
		String updateQuery = null;
		PreparedStatement preparedStatement = null;

		Connection con = DBUtility.getConnection();

		if (con == null) {
			System.out.println("MySQL DB 연결 실패");
			return result;
		}

		//studentDataSearch 메소드를 이용하여 해당 학생번호의 정보 불러오기 -> 객체생성
		List<StudentDataModel> list = studentDataSearch(String.valueOf(studentNumber), SearchType.STUDENT_NUMBER);
		StudentDataModel studentDataModel = list.get(0);

		//수정할 과목에 따른 데이터와 query문 변경
		switch(updateOption) {
		case KOR:
			studentDataModel.setKor(data);
			updateQuery = "update StudentDB.StudentDataTBL set kor = ?, sum = ?, avr = ?, grade = ? where studentNumber = ?";
			break;
		case ENG:
			studentDataModel.setEng(data);
			updateQuery = "update StudentDB.StudentDataTBL set eng = ?, sum = ?, avr = ?, grade = ? where studentNumber = ?";
			break;
		case MATH:
			studentDataModel.setMath(data);
			updateQuery = "update StudentDB.StudentDataTBL set math = ?, sum = ?, avr = ?, grade = ? where studentNumber = ?";
			break;
		}
		//변경된 점수를 반영하여 총점, 평균, 등급 재계산
		studentDataModel.calGrade();

		//변경된 데이터를 가진 객체를 이용하여 데이터베이스에 업데이트
		try {
			preparedStatement = (PreparedStatement) con.prepareStatement(updateQuery);
			preparedStatement.setInt(1, data);
			preparedStatement.setInt(2, studentDataModel.getSum());
			preparedStatement.setDouble(3, studentDataModel.getAvr());
			preparedStatement.setString(4, String.valueOf(studentDataModel.getGrade()));
			preparedStatement.setInt(5, studentNumber);
			
			result = preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if(preparedStatement != null && !preparedStatement.isClosed()) {
					preparedStatement.close();
				}
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}


	public static List<StudentDataModel> studentDataSort(int no) {
		final int ASC = 1, DESC = 2;
		List<StudentDataModel> list = new ArrayList<>();
		String sortQuery = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		
		Connection con = DBUtility.getConnection();

		if(con == null) {
			System.out.println("MySQL DB 연결 실패");
			return null;
		}

		//오름차순, 내림차순 선택에 따른 query문 변경
		switch (no) {
		case ASC:
			sortQuery = "select * from StudentDB.StudentDataTBL order by sum asc";
			break;
		case DESC:
			sortQuery = "select * from StudentDB.StudentDataTBL order by sum desc";
			break;
		}
		
		try {
			preparedStatement = (PreparedStatement) con.prepareStatement(sortQuery);

			resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				String name = resultSet.getString(1);
				int studentNumber = resultSet.getInt(2);
				String gender = resultSet.getString(3);
				int kor = resultSet.getInt(4);
				int eng = resultSet.getInt(5);
				int math = resultSet.getInt(6);
				int sum = resultSet.getInt(7);
				double avr = resultSet.getDouble(8);
				String grade = resultSet.getString(9);
				//문자열 타입을 문자형으로 변환
				char grade_char = grade.charAt(0);
				
				StudentDataModel studentDataModel = new StudentDataModel(name, studentNumber, gender, kor, eng, math, sum, avr, grade_char);
				list.add(studentDataModel);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if(preparedStatement != null && !preparedStatement.isClosed()) {
					preparedStatement.close();
				}
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return list;
	}
}
