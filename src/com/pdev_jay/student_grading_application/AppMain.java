package com.pdev_jay.student_grading_application;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class AppMain {
	public static Scanner scan = new Scanner(System.in);
	public static final int INSERT = 1, SEARCH = 2, DELETE = 3, UPDATE = 4, PRINT = 5, SORT = 6, EXIT = 7;

	public static void main(String[] args) {
		boolean flag = false;

		while (!flag) {
			int no = selectMenu();
			switch (no) {
			case INSERT:
				studentDataInsert();
				break;
			case SEARCH:
				studentDataSearch();
				break;
			case DELETE:
				studentDataDelete();
				break;
			case UPDATE:
				studentDataUpdate();
				break;
			case PRINT:
				studentDataPrint();
				break;
			case SORT:
				studentDataSort();
				break;
			case EXIT:
				System.out.println("프로그램이 종료되었습니다.");
				flag = true;
				break;
			default: 
				System.out.println("다시 입력하세요.");
				break;
			}
		}
	}

	private static void studentDataInsert() {
		//입력받을 값
		List<StudentDataModel> list = null;
		String name = null;
		int studentNumber = 0;
		String gender = null;
		int kor = 0;
		int eng = 0;
		int math = 0;
		boolean flag = false;

		while(true) {
			String 	filter = "^[가-힣]{2,5}$";

			System.out.print("이름 입력 >>  ");
			name = scan.nextLine();

			Pattern pattern = Pattern.compile(filter);
			Matcher matcher = pattern.matcher(name);

			if (matcher.matches()) {
				break;
			} else {
				System.out.println("다시 입력하세요.");
			}
		}

		while(!flag) {
			studentNumber = inputIntData("학생번호 입력", 1000, 9999);
			//동일한 학번 입력 제한
			list = DBController.studentDataSearch(String.valueOf(studentNumber), SearchType.STUDENT_NUMBER);
			if (list.size() != 0) {
				System.out.println("이미 존재하는 학생번호입니다.");
			} else {
				flag = true;
			}
		}
		
		while(true) {

			System.out.print("성별 입력(남자, 여자) >>  ");
			gender = scan.nextLine();

			if (gender.equals("남자") || gender.equals("여자")) {
				break;
			} else {
				System.out.println("다시 입력하세요.");
			}
		}

		kor = inputIntData("국어 점수", 0, 100);
		eng = inputIntData("영어 점수", 0, 100);
		math = inputIntData("수학 점수", 0, 100);

		//DB에 삽입할 모델 객체 생성
		StudentDataModel studentDataModel = new StudentDataModel(name, studentNumber, gender, kor, eng, math);

		int returnValue = DBController.studentDataInsert(studentDataModel);

		if (returnValue != 0) {
			System.out.println("\n" + studentDataModel.getName() + "의 정보 저장 성공");
		} else {
			System.out.println("\n" + studentDataModel.getName() + "의 정보 저장 실패");
		}
	}

	private static int inputIntData(String data, int min, int max) {
		int value = 0;

		while(true) {
			System.out.print(data + "(" + min + " ~ " + max + ") >>  ");

			try {
				value = Integer.parseInt(scan.nextLine());
			} catch (InputMismatchException e) {
				System.out.println("숫자를 입력하세요.");
				continue;
			} catch(Exception e) {
				System.out.println("숫자를 입력하세요.");
				continue;
			}

			if (value >= min && value <= max) {
				break;
			} else {
				System.out.println("다시 입력하세요.");
			}
		}
		return value;
	}


	private static void studentDataSearch() {
		final int NAME = 1, STUDENT_NUMBER = 2, EXIT = 3;
		List<StudentDataModel> list = new ArrayList<>();

		String name = null;
		int studentNumber = 0;
		String data = null;
		SearchType searchType = null;

		int no = searchOption();

		switch(no) {

		case NAME:
			while(true) {
				String 	filter = "^[가-힣]{2,5}$";

				System.out.print("\n이름 입력 >>  ");
				name = scan.nextLine();

				Pattern pattern = Pattern.compile(filter);
				Matcher matcher = pattern.matcher(name);
				if (matcher.matches()) {
					data = name;
					break;
				} else {
					System.out.println("다시 입력하세요.");
				}
			}
			searchType = SearchType.NAME;
			break;

		case STUDENT_NUMBER:
			studentNumber = inputIntData("\n학생번호 입력", 1000, 9999);
			data = String.valueOf(studentNumber);
			searchType = SearchType.STUDENT_NUMBER;
			break;

		case EXIT:
			System.out.println("검색이 취소되었습니다.");
			return;
		}

		list = DBController.studentDataSearch(data, searchType);

		if (list.size() <= 0) {
			System.out.println("\n일치하는 데이터가 존재하지 않습니다.");
			return;
		} 

		System.out.println("\n-------------------------------------------------------------------");
		System.out.println("이름\t학생번호\t성별\t국어\t영어\t수학\t총점\t평균\t등급");
		System.out.println("-------------------------------------------------------------------");
		for (StudentDataModel studentDataModel : list) {
			System.out.println(studentDataModel);
		}
		System.out.println("-------------------------------------------------------------------\n");
	}

	private static int searchOption() {
		boolean flag = false;
		int no = 0;

		while (!flag) {
			System.out.println("\n--------------------------");
			System.out.println("1. 이름  2. 학생번호  3. 취소");
			System.out.println("--------------------------\n");
			System.out.print("번호 선택 >>  ");

			try {
				no = Integer.parseInt(scan.nextLine());
			} catch (InputMismatchException e) {
				System.out.println("숫자를 입력하세요.");
				continue;
			} catch(Exception e) {
				System.out.println("숫자를 입력하세요.");
				continue;
			}

			if(no >= 1 && no <= 3) {
				flag = true;
			} else {
				System.out.println("1 부터 3 사이의 숫자를 입력하세요.");
			}
		}
		return no;
	}

	private static void studentDataDelete() {
		StudentDataModel temp_data = null;
		int resultNumber = 0;
		boolean flag = false;
		int studentNumber = inputIntData("\n삭제할 학생번호 입력", 1000, 9999);

		List<StudentDataModel> list = DBController.studentDataSearch(String.valueOf(studentNumber), SearchType.STUDENT_NUMBER);

		if (list.size() <= 0) {
			System.out.println("\n해당 학생번호의 정보가 존재하지 않습니다.");
			return;
		}
		
		System.out.println("\n-------------------------------------------------------------------");
		System.out.println("이름\t학생번호\t성별\t국어\t영어\t수학\t총점\t평균\t등급");
		System.out.println("-------------------------------------------------------------------");
		for (StudentDataModel studentDataModel : list) {
			System.out.println(studentDataModel);
			temp_data = studentDataModel;
		}
		System.out.println("-------------------------------------------------------------------\n");

		while(!flag) {
			System.out.print(temp_data.getName() + "의 정보를 삭제하시겠습니까? (Y/N) >>  ");
			String check = scan.nextLine();
			if (check.equals("N")) {
				System.out.println("삭제가 취소되었습니다.\n");
				return;
			} else if (check.equals("Y")) {
				flag = true;
			} else {
				System.out.println("다시 입력해주세요.\n");
			}
		}

		resultNumber = DBController.studentDataDelete(studentNumber);

		if(resultNumber != 0) {
			System.out.println(temp_data.getName() + "의 정보를 삭제했습니다.");
		} else {
			System.out.println(temp_data.getName() + "의 정보를 삭제하지 못했습니다.");
		}
		return;
	}

	private static void studentDataUpdate() {
		final int NAME = 1, KOR = 2, ENG = 3, MATH = 4;

		List<StudentDataModel> list = new ArrayList<>();
		UpdateOption updateOption = null;

		String name = null;
		int kor = 0;
		int eng = 0;
		int math = 0;
		boolean flag = false;
		int studentNumber = 0;
		int menu = 0;
		int result = 0;

		while (!flag) {
			System.out.print("\n수정할 학생의 학생번호 입력 >>  ");

			try {
				studentNumber = Integer.parseInt(scan.nextLine());
			} catch (InputMismatchException e) {
				System.out.println("숫자를 입력하세요.");
				continue;
			} catch(Exception e) {
				System.out.println("숫자를 입력하세요.");
				continue;
			}

			if(studentNumber >= 1000 && studentNumber <= 9999) {
				flag = true;
			} else {
				System.out.println("1000 부터 9999 사이의 숫자를 입력하세요.");
			}
		}

		list = DBController.studentDataSearch(String.valueOf(studentNumber), SearchType.STUDENT_NUMBER);

		if(list.size() <= 0) {
			System.out.println("학생번호 " + studentNumber + "와 일치하는 검색결과가 없습니다.");
			return;
		}

		//수정 전 데이터를 보여주기 위한 임시객체
		StudentDataModel tempData = null;

		System.out.println("\n-------------------------------------------------------------------");
		System.out.println("이름\t학생번호\t성별\t국어\t영어\t수학\t총점\t평균\t등급");
		System.out.println("-------------------------------------------------------------------");
		for (StudentDataModel studentDataModel : list) {
			System.out.println(studentDataModel);
			tempData = studentDataModel;
		}
		System.out.println("-------------------------------------------------------------------\n");

		while(true) {
			System.out.println("1. 이름  2. 국어  3. 영어  4. 수학");
			System.out.print("\n수정할 항목 선택 >>  ");

			try {
				menu = Integer.parseInt(scan.nextLine());
			} catch (InputMismatchException e) {
				System.out.println("숫자를 입력하세요.");
				continue;
			} catch(Exception e) {
				System.out.println("숫자를 입력하세요.");
				continue;
			}

			if (menu >= 1 && menu <= 4) {
				break;
			} else {
				System.out.println("다시 입력하세요.");
			}
		}

		switch(menu) {

		case NAME:
			while(true) {
				String 	filter = "^[가-힣]{2,5}$";

				System.out.print("수정할 이름 입력 [수정 전 : " + tempData.getName() + "] >>  ");
				String nameToUpdate = scan.nextLine();

				Pattern pattern = Pattern.compile(filter);
				Matcher matcher = pattern.matcher(nameToUpdate);
				if (matcher.matches()) {
					name = nameToUpdate;
					break;
				} else {
					System.out.println("다시 입력하세요.");
				}
			}
			result = DBController.studentDataUpdate(studentNumber, name);
			break;

		case KOR:
			kor = inputIntData("수정할 국어 점수 [수정 전 : " + tempData.getKor() + "]", 0, 100);
			updateOption = UpdateOption.KOR;
			result = DBController.studentDataUpdate(studentNumber, kor, updateOption);
			break;

		case ENG:
			eng = inputIntData("수정할 영어 점수 [수정 전 : " + tempData.getEng() + "]", 0, 100);
			updateOption = UpdateOption.ENG;
			result = DBController.studentDataUpdate(studentNumber, eng, updateOption);
			break;

		case MATH:
			math = inputIntData("수정할 수학 점수 [수정 전 : " + tempData.getMath() + "]", 0, 100);
			updateOption = UpdateOption.MATH;
			result = DBController.studentDataUpdate(studentNumber, math, updateOption);
			break;
		}

		if (result != 0) {
			System.out.println(list.get(0).getName() + "의 정보를 수정하였습니다.");
		} else {
			System.out.println(list.get(0).getName() + "의 정보수정을 실패하였습니다.");
		}
	}

	private static void studentDataPrint() {
		List<StudentDataModel> list = new ArrayList<>();
		list = DBController.studentDataSelect();

		if (list.size() <= 0) {
			System.out.println("저장된 데이터가 존재하지 않습니다.");
			return;
		}

		System.out.println("\n-------------------------------------------------------------------");
		System.out.println("이름\t학생번호\t성별\t국어\t영어\t수학\t총점\t평균\t등급");
		System.out.println("-------------------------------------------------------------------");
		for (StudentDataModel studentDataModel : list) {
			System.out.println(studentDataModel);
		}
		System.out.println("-------------------------------------------------------------------\n");
	}

	private static void studentDataSort() {
		List<StudentDataModel> list = new ArrayList<StudentDataModel>();
		int no = 0;
		boolean flag = false;

		//오름차순, 내림차순 선택
		while(!flag) {

			System.out.println("\n1. 오름차순(총점)  2.내림차순(총점)");
			System.out.print("\n정렬 방식 선택 >>  ");

			try {
				no = Integer.parseInt(scan.nextLine());
			} catch (InputMismatchException e) {
				System.out.println("숫자를 입력하세요.");
				continue;
			} catch(Exception e) {
				System.out.println("숫자를 입력하세요.");
				continue;
			}

			if(no >= 1 && no <= 2) {
				flag = true;
			} else {
				System.out.println("1 또는 2를 입력하세요.");
			}
		}

		list = DBController.studentDataSort(no);

		if(list.size() <= 0) {
			System.out.println("\n정렬할 내용이 없습니다.");
			return;
		}

		System.out.println("\n-------------------------------------------------------------------");
		System.out.println("이름\t학생번호\t성별\t국어\t영어\t수학\t총점\t평균\t등급");
		System.out.println("-------------------------------------------------------------------");
		for (StudentDataModel studentDataModel : list) {
			System.out.println(studentDataModel);
		}
		System.out.println("-------------------------------------------------------------------\n");

		return;
	}

	private static int selectMenu() {
		boolean flag = false;
		int no = 0;

		while (!flag) {
			System.out.println("-----------------------------------------------------");
			System.out.println("1.입력\t2.조회\t3.삭제\t4.수정\t5.출력\t6.정렬\t7.종료");
			System.out.println("-----------------------------------------------------\n");
			System.out.print("번호 선택 >>  ");

			try {
				no = Integer.parseInt(scan.nextLine());
			} catch (InputMismatchException e) {
				System.out.println("숫자를 입력하세요.");
				continue;
			} catch(Exception e) {
				System.out.println("숫자를 입력하세요.");
				continue;
			}

			if(no >= 1 && no <= 7) {
				flag = true;
			} else {
				System.out.println("1 부터 7 사이의 숫자를 입력하세요.");
			}
		}
		return no;
	}
}
