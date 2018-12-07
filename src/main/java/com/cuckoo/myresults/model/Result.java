package com.cuckoo.myresults.model;

import java.io.Serializable;

public class Result implements Serializable{
	
	private String enrollmentId;
	private String exam;
	private String subject1;
	private String subject2;
	private String subject3;
	private String result;
	

	public String getEnrollmentId() {
		return enrollmentId;
	}
	public void setEnrollmentId(String enrollmentId) {
		this.enrollmentId = enrollmentId;
	}
	public String getExam() {
		return exam;
	}
	public void setExam(String exam) {
		this.exam = exam;
	}
	public String getSubject1() {
		return subject1;
	}
	public void setSubject1(String subject1) {
		this.subject1 = subject1;
	}
	public String getSubject2() {
		return subject2;
	}
	public void setSubject2(String subject2) {
		this.subject2 = subject2;
	}
	public String getSubject3() {
		return subject3;
	}
	public void setSubject3(String subject3) {
		this.subject3 = subject3;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	@Override
	public String toString() {
		return "Result [enrollmentId=" + enrollmentId + ", exam=" + exam + ", subject1=" + subject1 + ", subject2="
				+ subject2 + ", subject3=" + subject3 + ", result=" + result + "]";
	}


}
