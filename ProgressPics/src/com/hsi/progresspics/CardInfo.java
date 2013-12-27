package com.hsi.progresspics;

import java.util.Date;

public class CardInfo {
	String filename;
	float weight;
	Date date;
	String comments;

	public CardInfo(String filename, float weight, Date date, String comments) {
		this.filename = filename;
		this.weight = weight;
		this.date = date;
		this.comments = comments;
	}

	public CardInfo() {

	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public void setWeight(float weight) {
		this.weight = weight;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getFilename() {
		return filename;
	}

	public float getWeight() {
		return weight;
	}

	public Date getDate() {
		return date;
	}

	public String getComments() {
		return comments;
	}
}
