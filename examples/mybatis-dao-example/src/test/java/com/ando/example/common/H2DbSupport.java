package com.ando.example.common;

import java.util.Set;

import org.unitils.core.dbsupport.DbSupport;

/**
 * Implementation of {@link org.unitils.core.dbsupport.DbSupport} for a H2 database
 * 
 * @author Zied ANDOLSI
 */
public class H2DbSupport extends DbSupport {

	/**
	 * Default constructor.
	 */
	public H2DbSupport() {
		super("h2");
	}

	/**
	 * {@inheritDoc}.
	 */
	public Set<String> getColumnNames(String tableName) {
		return getSQLHandler().getItemsAsStringSet(
				"select COLUMN_NAME from INFORMATION_SCHEMA.COLUMNS where TABLE_NAME = '" + tableName
						+ "' AND TABLE_SCHEMA = '" + getSchemaName() + "'");
	}

	/**
	 * {@inheritDoc}.
	 */
	public Set<String> getTableNames() {
		return getSQLHandler().getItemsAsStringSet(
				"select TABLE_NAME from INFORMATION_SCHEMA.TABLES where TABLE_TYPE = 'TABLE' AND TABLE_SCHEMA = '"
						+ getSchemaName() + "'");
	}

	/**
	 * {@inheritDoc}.
	 */
	public Set<String> getViewNames() {
		return getSQLHandler().getItemsAsStringSet(
				"select TABLE_NAME from INFORMATION_SCHEMA.TABLES where TABLE_TYPE = 'VIEW' AND TABLE_SCHEMA = '"
						+ getSchemaName() + "'");
	}

	/**
	 * {@inheritDoc}.
	 */
	public Set<String> getSequenceNames() {
		return getSQLHandler().getItemsAsStringSet(
				"select SEQUENCE_NAME from INFORMATION_SCHEMA.SEQUENCES where SEQUENCE_SCHEMA = '" + getSchemaName()
						+ "'");
	}

	/**
	 * {@inheritDoc}.
	 */
	public Set<String> getTriggerNames() {
		return getSQLHandler()
				.getItemsAsStringSet(
						"select TRIGGER_NAME from INFORMATION_SCHEMA.TRIGGERS where TRIGGER_SCHEMA = '"
								+ getSchemaName() + "'");
	}

	/**
	 * {@inheritDoc}.
	 */
	public long getSequenceValue(String sequenceName) {
		return getSQLHandler().getItemAsLong(
				"select START_WITH from INFORMATION_SCHEMA.SEQUENCES where SEQUENCE_SCHEMA = '" + getSchemaName()
						+ "' and SEQUENCE_NAME = '" + sequenceName + "'");
	}

	/**
	 * {@inheritDoc}.
	 */
	public boolean supportsSequences() {
		return true;
	}

	/**
	 * {@inheritDoc}.
	 */
	public boolean supportsTriggers() {
		return true;
	}

	/**
	 * {@inheritDoc}.
	 */
	public boolean supportsIdentityColumns() {
		return true;
	}

	/**
	 * {@inheritDoc}.
	 */
	public void incrementSequenceToValue(String sequenceName, long newSequenceValue) {
		getSQLHandler()
				.executeUpdate("alter sequence " + qualified(sequenceName) + " restart with " + newSequenceValue);
	}

	/**
	 * {@inheritDoc}.
	 */
	public void incrementIdentityColumnToValue(String tableName, String identityColumnName, long identityValue) {
		getSQLHandler().executeUpdate(
				"alter table " + qualified(tableName) + " alter column " + quoted(identityColumnName)
						+ " RESTART WITH " + identityValue);
	}

	/**
	 * {@inheritDoc}.
	 */
	public void disableReferentialConstraints() {
		Set<String> tableNames = getTableNames();
		for (String tableName : tableNames) {
			disableReferentialConstraints(tableName);
		}
	}

	/**
	 * {@inheritDoc}.
	 */
	public void disableValueConstraints() {
		Set<String> tableNames = getTableNames();
		for (String tableName : tableNames) {
			disableValueConstraints(tableName);
		}
	}

	private void disableReferentialConstraints(String tableName) {
		Set<String> constraintNames = this.getForeignKeyConstraintNames(tableName);
		for (String constraintName : constraintNames) {
			this.removeForeignKeyConstraint(tableName, constraintName);
		}
	}

	private void disableValueConstraints(String tableName) {
		Set<String> primaryKeyColumnNames = this.getPrimaryKeyColumnNames(tableName);

		Set<String> notNullColumnNames = this.getNotNullColummnNames(tableName);
		for (String notNullColumnName : notNullColumnNames) {
			if (primaryKeyColumnNames.contains(notNullColumnName)) {
				continue;
			}
			this.removeNotNullConstraint(tableName, notNullColumnName);
		}
	}

	private Set<String> getPrimaryKeyColumnNames(String tableName) {
		return getSQLHandler().getItemsAsStringSet(
				"select COLUMN_NAME from INFORMATION_SCHEMA.INDEXES where PRIMARY_KEY=TRUE AND TABLE_NAME = '"
						+ tableName + "' AND TABLE_SCHEMA = '" + getSchemaName() + "'");
	}

	private Set<String> getNotNullColummnNames(String tableName) {
		return getSQLHandler().getItemsAsStringSet(
				"select COLUMN_NAME from INFORMATION_SCHEMA.COLUMNS where IS_NULLABLE = 'NO' AND TABLE_NAME = '"
						+ tableName + "' AND TABLE_SCHEMA = '" + getSchemaName() + "'");
	}

	private Set<String> getForeignKeyConstraintNames(String tableName) {
		return getSQLHandler().getItemsAsStringSet(
				"select CONSTRAINT_NAME from INFORMATION_SCHEMA.CONSTRAINTS "
						+ "where CONSTRAINT_TYPE = 'REFERENTIAL' AND TABLE_NAME = '" + tableName
						+ "' AND CONSTRAINT_SCHEMA = '" + getSchemaName() + "'");
	}

	private void removeForeignKeyConstraint(String tableName, String constraintName) {
		getSQLHandler().executeUpdate(
				"alter table " + qualified(tableName) + " drop constraint " + quoted(constraintName));
	}

	private void removeNotNullConstraint(String tableName, String columnName) {
		getSQLHandler().executeUpdate(
				"alter table " + qualified(tableName) + " alter column " + quoted(columnName) + " set null");
	}
}