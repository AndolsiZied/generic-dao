package com.ando.example.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Bet entity mapping bet table.
 * 
 * @author Zied ANDOLSI
 * 
 */
public class Bet implements Serializable {

	/**
	 * serial version of class.
	 */
	private static final long serialVersionUID = -524835163916821122L;

	public static final String TABLE_NAME = "bet";

	public static final String COLUMN_ID_NAME = "id";

	public static final String COLUMN_TEAM1_NAME = "team1";

	public static final String COLUMN_TEAM2_NAME = "team2";

	public static final String COLUMN_SCORE_NAME = "score";

	public static final String COLUMN_DATE_NAME = "betdate";

	private Long id;
	private String team1;
	private String team2;
	private String score;
	private Date betDate;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTeam1() {
		return team1;
	}

	public void setTeam1(String team1) {
		this.team1 = team1;
	}

	public String getTeam2() {
		return team2;
	}

	public void setTeam2(String team2) {
		this.team2 = team2;
	}

	public String getScore() {
		return score;
	}

	public void setScore(String score) {
		this.score = score;
	}

	public Date getBetDate() {
		return betDate;
	}

	public void setBetDate(Date betDate) {
		this.betDate = betDate;
	}

}
