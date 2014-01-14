package com.ando.example.dao.impl;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ando.architecture.dao.impl.JPAHibernateDAO;
import com.ando.architecture.exception.DataBaseException;
import com.ando.example.dao.BetDAO;
import com.ando.example.model.Bet;

/**
 * This class provides data access operations on database for bet entity.
 * 
 * @author Zied ANDOLSI
 * 
 */
public class BetDAOImpl extends JPAHibernateDAO<Bet, Long> implements BetDAO {

	/**
	 * {@link BetDAOImpl}'s default logger.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(BetDAOImpl.class);

	/**
	 * Default constructor.
	 */
	public BetDAOImpl() {
		super(Bet.class, "betPu");
	}

	/**
	 * {@inheritDoc}.
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
	public Bet findBetByTeamsAndDate(String team1, String team2, Date date) throws DataBaseException {
		LOGGER.debug("Starting findBetByBetByTeamsAndDate method...");
		String query = "from Bet b where b.team1 = ? and b.team2 = ? and b.betDate = ?";
		return executeSingleResult(query, team1, team2, date);
	}

	/**
	 * {@inheritDoc}
	 */
	public List<Bet> findBetByTeams(String team1, String team2) throws DataBaseException {
		LOGGER.debug("Starting findBetByBetByTeamsAndDate method...");
		String query = "from Bet b where b.team1 = ? and b.team2 = ?";
		return executeResultList(query, team1, team2);
	}

}
