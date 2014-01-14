package com.ando.example.dao;

import java.util.Date;
import java.util.List;

import com.ando.architecture.dao.DAO;
import com.ando.architecture.exception.DataBaseException;
import com.ando.example.model.Bet;

/**
 * This interface defines methods required to manage bet entity in database.
 * 
 * @author Zied ANDOLSI
 * 
 */
public interface BetDAO extends DAO<Bet, Long> {

	/**
	 * The method {@link #findBetByTeamsAndDate(String, String, Date)} loads all bets for given teams and in a given
	 * date.
	 * 
	 * @param team1
	 *            first team
	 * @param team2
	 *            second team
	 * @param date
	 *            date
	 * @return list of bets
	 * @throws DataBaseException
	 *             thrown if an exception occurs when trying to load bets from the database.
	 */
	Bet findBetByTeamsAndDate(String team1, String team2, Date date) throws DataBaseException;

	/**
	 * The method {@link #findBetByTeams(String, String)} loads all bets for given teams.
	 * 
	 * @param team1
	 *            first team
	 * @param team2
	 *            second team
	 * @return list of bets
	 * @throws DataBaseException
	 *             thrown if an exception occurs when trying to load bets from the database.
	 */
	public List<Bet> findBetByTeams(String team1, String team2) throws DataBaseException;

}
