package com.ando.example.dao;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.unitils.UnitilsJUnit4TestClassRunner;
import org.unitils.dbunit.annotation.DataSet;

import com.ando.architecture.exception.DataBaseException;
import com.ando.example.dao.impl.BetDAOImpl;
import com.ando.example.model.Bet;

@RunWith(UnitilsJUnit4TestClassRunner.class)
@DataSet(value = { "/dao-dataset.xml" })
public class BetDAOTest {

	/**
	 * {@link BetDAOTest}'s default logger.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(BetDAOTest.class);

	/**
	 * betDAO attribute.
	 */
	private BetDAO betDAO;

	/**
	 * Default constructor.
	 */
	public BetDAOTest() {
		betDAO = new BetDAOImpl();
	}

	/**
	 * Method testing findByOne behavior for :
	 * 
	 * <pre>
	 * - a bet's existing identifier.
	 * - a bet's not existing identifier
	 * - a null identifier.
	 */
	@Test
	public void findOne() {
		LOGGER.debug("starting findOne method...");

		// search with existing bet's identifier
		try {
			Bet bet = betDAO.findOne(Long.valueOf("1"));
			assertNotNull(bet);
		} catch (DataBaseException e) {
			LOGGER.error("error occurred when trying to get bet entity with identifier [ " + Long.valueOf("1")
					+ " ] : " + e.getLocalizedMessage());
			fail("error occurred when trying to get bet entity with identifier [ " + Long.valueOf("1") + " ] : "
					+ e.getLocalizedMessage());
		}
		LOGGER.debug("bet with identifier [ " + Long.valueOf("1") + " ] found.");

		// search with not existing bet's identifier
		try {
			Bet bet = betDAO.findOne(Long.valueOf("10000"));
			assertNull(bet);
		} catch (DataBaseException e) {
			LOGGER.error("error occurred when trying to get bet entity with identifier [ " + Long.valueOf("10000")
					+ " ] : " + e.getLocalizedMessage());
			fail("error occurred when trying to get bet entity with identifier [ " + Long.valueOf("10000") + " ] : "
					+ e.getLocalizedMessage());
		}
		LOGGER.debug("bet with identifier [ " + Long.valueOf("10000") + " ] not found.");

		// search with null identifier
		try {
			betDAO.findOne(null);
			fail("must throw exception before this line");
		} catch (DataBaseException e) {
			LOGGER.error("error occurred when trying to get bet entity with null identifier : "
					+ e.getLocalizedMessage());
			fail("error occurred when trying to get bet entity with null identifier : " + e.getLocalizedMessage());
		} catch (IllegalArgumentException e) {
			LOGGER.debug("Identifier must not have null value");
			assertTrue("Identifier must not have null value", true);
		}

		LOGGER.debug("testing findOne method ends.");
	}

	/**
	 * Method testing save behavior for :
	 * 
	 * <pre>
	 * - a bet's existing entity.
	 * - a bet's not existing entity
	 * - a null entity.
	 */
	@Test
	public void save() {
		LOGGER.debug("starting save method...");

		// save an existing bet's entity
		try {
			Bet bet = new Bet();
			bet.setId(Long.valueOf("1"));
			betDAO.save(bet);
			fail("must throw exception before this line");
		} catch (DataBaseException e) {
			LOGGER.info("bet entity with identifier [ " + Long.valueOf("1") + " ] already exists.");
			assertTrue("bet entity with identifier [ " + Long.valueOf("1") + " ] already exists.", true);
		}
		LOGGER.debug("bet with identifier [ " + Long.valueOf("1") + " ] already exists.");

		// save a not existing bet's entity
		try {
			Bet bet = new Bet();
			bet.setId(Long.valueOf("1000"));
			bet.setBetDate(new Date());
			bet = betDAO.save(bet);
			LOGGER.debug("bet with identifier [ " + bet.getId() + " ] saved.");
		} catch (DataBaseException e) {
			LOGGER.error("error occurred when trying to save bet entity : " + e.getLocalizedMessage());
			fail("error occurred when trying to save bet entity " + e.getLocalizedMessage());
		}

		// save null entity
		try {
			betDAO.save(null);
		} catch (DataBaseException e) {
			LOGGER.error("error occurred when trying to save null entity : " + e.getLocalizedMessage());
			fail("error occurred when trying to save null entity : " + e.getLocalizedMessage());
		} catch (IllegalArgumentException e) {
			LOGGER.debug("Entity must not be null");
			assertTrue("Entity must not be null", true);
		}

		LOGGER.debug("testing save method ends.");
	}

	/**
	 * Method testing update behavior for :
	 * 
	 * <pre>
	 * - a bet's existing entity.
	 * - a bet's not existing entity
	 * - a null entity.
	 */
	@Test
	public void update() {
		LOGGER.debug("starting update method...");

		// update an existing bet's entity
		try {
			Bet bet = betDAO.findOne(Long.valueOf("1"));
			assertNotNull(bet);
			bet.setBetDate(new Date());
			betDAO.update(bet);
		} catch (DataBaseException e) {
			LOGGER.error("error occurred when trying to update bet entity : " + e.getLocalizedMessage());
			fail("error occurred when trying to update bet entity " + e.getLocalizedMessage());
		}
		LOGGER.debug("bet with identifier [ " + Long.valueOf("1") + " ] updated.");

		// update a not existing bet's entity
		try {
			Bet bet = new Bet();
			bet.setId(Long.valueOf("10000"));
			bet.setBetDate(new Date());
			betDAO.update(bet);
			fail("must throw exception before this line");
		} catch (DataBaseException e) {
			LOGGER.info("bet entity with identifier [ " + Long.valueOf("10000") + " ] doesn't exist.");
			assertTrue("bet entity with identifier [ " + Long.valueOf("10000") + " ] doesn't exist.", true);
		}

		// update null entity
		try {
			betDAO.update(null);
			fail("must throw exception before this line");
		} catch (DataBaseException e) {
			LOGGER.error("error occurred when trying to update null entity : " + e.getLocalizedMessage());
			fail("error occurred when trying to update null entity : " + e.getLocalizedMessage());
		} catch (IllegalArgumentException e) {
			LOGGER.debug("Entity must not be null");
			assertTrue("Entity must not be null", true);
		}

		LOGGER.debug("testing update method ends.");
	}

	/**
	 * Method testing delete behavior for :
	 * 
	 * <pre>
	 * - a bet's existing entity.
	 * - a null entity.
	 */
	@Test
	public void delete() {
		LOGGER.debug("starting delete method...");

		// delete an existing bet's entity
		try {
			Bet bet = new Bet();
			bet.setId(Long.valueOf("1"));
			betDAO.delete(bet);
			List<Bet> bets = betDAO.getAll();
			assertNotNull(bets);
			assertTrue("list size must be 2 and not " + bets.size(), bets.size() == 2);
		} catch (DataBaseException e) {
			LOGGER.error("error occurred when trying to delete bet entity : " + e.getLocalizedMessage());
			fail("error occurred when trying to delete bet entity : " + e.getLocalizedMessage());
		}
		LOGGER.debug("bet with identifier [ " + Long.valueOf("1") + " ] deleted.");

		// update null entity
		try {
			betDAO.delete(null);
			fail("must throw exception before this line");
		} catch (DataBaseException e) {
			LOGGER.error("error occurred when trying to delete null entity : " + e.getLocalizedMessage());
			fail("error occurred when trying to delete null entity : " + e.getLocalizedMessage());
		} catch (IllegalArgumentException e) {
			LOGGER.debug("Entity must not be null");
			assertTrue("Entity must not be null", true);
		}

		LOGGER.debug("testing delete method ends.");
	}

	/**
	 * Method testing getAll behavior.
	 */
	@Test
	public void getAll() {
		LOGGER.debug("starting getAll method...");

		// getting all entities
		try {
			List<Bet> bets = betDAO.getAll();
			assertNotNull(bets);
			assertTrue("list size must be 3 and not " + bets.size(), bets.size() == 3);
		} catch (DataBaseException e) {
			LOGGER.error("error occurred when trying to get all bet entities : " + e.getLocalizedMessage());
			fail("error occurred when trying to get all bet entities : " + e.getLocalizedMessage());
		}

		LOGGER.debug("testing getAll method ends.");
	}

	/**
	 * Method testing findBetByTeamsAndDate behavior for :
	 * 
	 * <pre>
	 * - a unique result.
	 * - a non unique result.
	 * - a null result.
	 */
	@Test
	public void findBetByTeamsAndDate() {
		LOGGER.debug("Starting findBetByTeamsAndDate method...");

		Calendar date = Calendar.getInstance();

		// getting unique result
		LOGGER.debug("Getting unique result");
		try {
			date.set(2013, 11, 18, 12, 0, 0);
			date.set(Calendar.MILLISECOND, 0);
			Bet bet = betDAO.findBetByTeamsAndDate("team1", "team2", date.getTime());
			assertNotNull(bet);
		} catch (DataBaseException e) {
			LOGGER.error("error occurred when trying to find bet by teams and date : " + e.getLocalizedMessage());
			fail("error occurred when trying to find bet by teams and date : " + e.getLocalizedMessage());
		}

		// getting non unique result
		LOGGER.debug("Getting non unique result");
		try {
			date.set(2013, 11, 19, 12, 0, 0);
			date.set(Calendar.MILLISECOND, 0);
			betDAO.findBetByTeamsAndDate("team1", "team2", date.getTime());
			fail("must throw exception before this line");
		} catch (DataBaseException e) {
			LOGGER.debug("error occurred when trying to find bet by teams and date : " + e.getLocalizedMessage());
			assertTrue("error occurred when trying to find bet by teams and date : " + e.getLocalizedMessage(), true);
		}

		// getting null result
		LOGGER.debug("Getting null result");
		try {
			date.set(2015, 11, 19, 12, 0, 0);
			date.set(Calendar.MILLISECOND, 0);
			Bet bet = betDAO.findBetByTeamsAndDate("team1", "team2", date.getTime());
			assertNull(bet);
		} catch (DataBaseException e) {
			LOGGER.error("error occurred when trying to find bet by teams and date : " + e.getLocalizedMessage());
			fail("error occurred when trying to find bet by teams and date : " + e.getLocalizedMessage());
		}

		LOGGER.debug("testing findBetByTeamsAndDate method ends.");
	}

	/**
	 * Method testing findBetByTeams.
	 */
	@Test
	public void findBetByTeams() {
		LOGGER.debug("Starting findBetByTeams method...");

		// getting bets by teams
		try {
			List<Bet> bets = betDAO.findBetByTeams("team1", "team2");
			assertNotNull(bets);
			assertTrue("list size must be 3 and not " + bets.size(), bets.size() == 3);
		} catch (DataBaseException e) {
			LOGGER.error("error occurred when trying to find bet by teams : " + e.getLocalizedMessage());
			fail("error occurred when trying to find bet by teams : " + e.getLocalizedMessage());
		}

		LOGGER.debug("testing findBetByTeams method ends.");
	}
}
