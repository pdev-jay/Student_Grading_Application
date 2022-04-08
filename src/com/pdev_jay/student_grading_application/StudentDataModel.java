package com.pdev_jay.student_grading_application;

import java.io.Serializable;
import java.util.Objects;

public class StudentDataModel implements Comparable<Object>, Serializable{
	//과목 수
	final int SUBJECTS = 3;
	
	private String name;
	private int studentNumber;
	private String gender;
	private int kor;
	private int eng;
	private int math;
	private int sum;
	private double avr;
	private char grade;
	
	//사용자가 입력할 때 쓰이는 생성자
	public StudentDataModel(String name, int studentNumber, String gender, int kor, int eng, int math) {
		super();
		this.name = name;
		this.studentNumber = studentNumber;
		this.gender = gender;
		this.kor = kor;
		this.eng = eng;
		this.math = math;
		calGrade();
	}
	
	//데이터베이스에서 불러올 때 쓰이는 생성자
	public StudentDataModel(String name, int studentNumber, String gender, int kor, int eng, int math, int sum, double avr,
			char grade) {
		super();
		this.name = name;
		this.studentNumber = studentNumber;
		this.gender = gender;
		this.kor = kor;
		this.eng = eng;
		this.math = math;
		this.sum = sum;
		this.avr = avr;
		this.grade = grade;
	}


	//사용자가 입력할 때, 점수를 변경할 때 총점, 평균, 등급 계산
	public void calGrade() {
		this.sum = this.kor + this.eng + this.math;
		
		//소수점 이하 두번째 자리까지 표현
		this.avr = (double)Math.round((this.sum / (double)SUBJECTS)*100) / 100;
		
		switch((int)avr / 10) {
		case 10:
		case 9:
			this.grade = 'A';
			break;
		case 8:
			this.grade = 'B';
			break;
		case 7:
			this.grade = 'C';
			break;
		case 6:
			this.grade = 'D';
			break;
		default:
			this.grade = 'F';
			break;
		}
	}

	@Override
	public boolean equals(Object obj) {
		
		if (!(obj instanceof StudentDataModel)) {
			throw new IllegalArgumentException("Not inheritance relationship");
		}
		
		StudentDataModel studentDataModel = (StudentDataModel)obj;
		
		return this.studentNumber == studentDataModel.studentNumber;
	}
	
	@Override
	public int compareTo(Object obj) {
		int value = 0;
		
		if (!(obj instanceof StudentDataModel)) {
			throw new IllegalArgumentException("Not inheritance relationship");
		}
		
		StudentDataModel studentDataModel = (StudentDataModel)obj;
		
		if (this.sum > studentDataModel.sum) {
			value = 1;
		} else if (this.sum < studentDataModel.sum) {
			value = -1;
		}
		
		return value;
	}


	@Override
	public int hashCode() {
		return Objects.hashCode(this.studentNumber);
	}

	@Override
	public String toString() {
		return name + "\t" + studentNumber + "\t" + gender + "\t" + kor + "\t" + eng
				+ "\t" + math + "\t" + sum + "\t" + String.format("%.2f", avr) + "\t" + grade;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getStudentNumber() {
		return studentNumber;
	}

	public void setStudentNumber(int studentNumber) {
		this.studentNumber = studentNumber;
	}

	public String getGender() {
		return gender;
	}
	
	public void setGender(String gender) {
		this.gender = gender;
	}
	
	public int getKor() {
		return kor;
	}

	public void setKor(int kor) {
		this.kor = kor;
	}

	public int getEng() {
		return eng;
	}

	public void setEng(int eng) {
		this.eng = eng;
	}

	public int getMath() {
		return math;
	}

	public void setMath(int math) {
		this.math = math;
	}

	public int getSum() {
		return sum;
	}

	public void setSum(int sum) {
		this.sum = sum;
	}

	public double getAvr() {
		return avr;
	}

	public void setAvr(double avr) {
		this.avr = avr;
	}

	public char getGrade() {
		return grade;
	}

	public void setGrade(char grade) {
		this.grade = grade;
	}
}
