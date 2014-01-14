package com.ando.example.dao.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ando.architecture.dao.impl.MyBatisDAO;
import com.ando.architecture.exception.DataBaseException;
import com.ando.example.dao.BetDAO;
import com.ando.example.model.Bet;

/**
 * This class provides data access operations on database for bet entity.
 * 
 * @author Zied ANDOLSI
 * 
 */
public class BetDAOImpl extends MyBatisDAO<Bet, Long> implements BetDAO {

	/**
	 * {@link SpringBetDAO}'s default logger.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(BetDAOImpl.class);

	/**
	 * Entity column names.
	 */
	private static final String[] COLUMN_NAMES = { Bet.COLUMN_ID_NAME, Bet.COLUMN_TEAM1_NAME, Bet.COLUMN_TEAM2_NAME,
			Bet.COLUMN_SCORE_NAME, Bet.COLUMN_DATE_NAME };

	/**
	 * Default constructor.
	 */
	public BetDAOImpl() {
		super(Bet.class, Bet.COLUMN_ID_NAME);
	}

	/**
	 * {@inheritDoc}
	 */
	public Long getIdentifier(Bet entity) {
		if (entity == null) {
			LOGGER.error("The entity [ " + this.typeClass.getSimpleName() + " ] cannot be null.");
			throw new IllegalArgumentException("The entity [ " + this.typeClass.getSimpleName() + " ] cannot be null.");
		}
		return entity.getId();
	}

	/**
	 * {@inheritDoc}
	 */
	public void setIdentifier(Bet entity, Long id) {
		if (entity == null) {
			LOGGER.error("The entity [ " + this.typeClass.getSimpleName() + " ] cannot be null.");
			throw new IllegalArgumentException("The entity [ " + this.typeClass.getSimpleName() + " ] cannot be null.");
		}
		entity.setId(id);
	}

	/**
	 * {@inheritDoc}
	 */
	public Object[] getColumnValues(Bet entity) {
		if (entity == null) {
			LOGGER.error("The entity [ " + this.typeClass.getSimpleName() + " ] cannot be null.");
			throw new IllegalArgumentException("The entity [ " + this.typeClass.getSimpleName() + " ] cannot be null.");
		}
		return new Object[]{ entity.getId(), entity.getTeam1(), entity.getTeam2(), entity.getScore(),
				entity.getBetDate() };
	}

	/**
	 * {@inheritDoc}
	 */
	public String[] getColumnNames() {
		return COLUMN_NAMES;
	}

	/**
	 * {@inheritDoc}
	 */
	public Bet findBetByTeamsAndDate(String team1, String team2, Date date) throws DataBaseException {
		LOGGER.debug("Starting findBetByBetByTeamsAndDate method...");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(Bet.COLUMN_TEAM1_NAME, team1);
		params.put(Bet.COLUMN_TEAM2_NAME, team2);
		params.put(Bet.COLUMN_DATE_NAME, date);
		return executeSingleResult(params);
	}

	/**
	 * {@inheritDoc}
	 */
	public List<Bet> findBetByTeams(String team1, String team2) throws DataBaseException {
		LOGGER.debug("Starting findBetByBetByTeamsAndDate method...");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(Bet.COLUMN_TEAM1_NAME, team1);
		params.put(Bet.COLUMN_TEAM2_NAME, team2);
		return executeResultList(team1, team2);
	}

}
