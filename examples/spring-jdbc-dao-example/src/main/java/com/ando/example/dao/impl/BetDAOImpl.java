package com.ando.example.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.ando.architecture.dao.impl.SpringJdbcDAO;
import com.ando.architecture.exception.DataBaseException;
import com.ando.example.dao.BetDAO;
import com.ando.example.model.Bet;

/**
 * This class provides data access operations on database for bet entity.
 * 
 * @author Zied ANDOLSI
 * 
 */
public class BetDAOImpl extends SpringJdbcDAO<Bet, Long> implements BetDAO {

	/**
	 * {@link BetDAOImpl}'s default logger.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(BetDAOImpl.class);

	/**
	 * Entity column names.
	 */
	private static final String[] columnNames = { Bet.COLUMN_TEAM1_NAME, Bet.COLUMN_TEAM2_NAME, Bet.COLUMN_SCORE_NAME,
			Bet.COLUMN_DATE_NAME };

	/**
	 * Default constructor.
	 */
	public BetDAOImpl() {
		super(Bet.class, Bet.TABLE_NAME, Bet.COLUMN_ID_NAME);
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
		return new Object[]{ entity.getTeam1(), entity.getTeam2(), entity.getScore(), entity.getBetDate() };
	}

	/**
	 * {@inheritDoc}
	 */
	public String[] getColumnNames() {
		return columnNames;
	}

	/**
	 * {@inheritDoc}
	 */
	public RowMapper<Bet> getRowMapper() {
		return new RowMapper<Bet>() {

			@Override
			public Bet mapRow(ResultSet rs, int rowNum) throws SQLException {
				Bet bet = new Bet();
				bet.setId(rs.getLong(Bet.COLUMN_ID_NAME));
				bet.setScore(rs.getString(Bet.COLUMN_SCORE_NAME));
				bet.setTeam1(rs.getString(Bet.COLUMN_TEAM1_NAME));
				bet.setTeam2(rs.getString(Bet.COLUMN_TEAM2_NAME));
				bet.setBetDate(rs.getDate(Bet.COLUMN_DATE_NAME));
				return bet;
			}
		};
	}

	/**
	 * {@inheritDoc}
	 */
	public Bet findBetByTeamsAndDate(String team1, String team2, Date date) throws DataBaseException {
		LOGGER.debug("Starting findBetByBetByTeamsAndDate method...");
		String query = "select * from Bet b where b.team1 = ? and b.team2 = ? and b.betdate = ?";
		return executeSingleResult(query, team1, team2, date);
	}

	/**
	 * {@inheritDoc}
	 */
	public List<Bet> findBetByTeams(String team1, String team2) throws DataBaseException {
		LOGGER.debug("Starting findBetByBetByTeamsAndDate method...");
		String query = "select * from Bet b where b.team1 = ? and b.team2 = ?";
		return executeResultList(query, team1, team2);
	}

}
