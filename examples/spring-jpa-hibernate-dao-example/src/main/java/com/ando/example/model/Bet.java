package com.ando.example.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Bet entity mapping bet table.
 * 
 * @author Zied ANDOLSI
 * 
 */
@Entity
@Table(name = "bet")
public class Bet implements Serializable {

	/**
	 * serial version of class.
	 */
	private static final long serialVersionUID = -524835163916821122L;

	private Long id;
	private String team1;
	private String team2;
	private String score;
	private Date betDate;

	@Id
	@Column(name = "id")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "team1")
	public String getTeam1() {
		return team1;
	}

	public void setTeam1(String team1) {
		this.team1 = team1;
	}

	@Column(name = "team2")
	public String getTeam2() {
		return team2;
	}

	public void setTeam2(String team2) {
		this.team2 = team2;
	}

	@Column(name = "score")
	public String getScore() {
		return score;
	}

	public void setScore(String score) {
		this.score = score;
	}

	@Column(name = "betdate")
	public Date getBetDate() {
		return betDate;
	}

	public void setBetDate(Date betDate) {
		this.betDate = betDate;
	}

}
