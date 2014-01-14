package com.ando.example.util;

import javax.sql.DataSource;

import org.dbunit.database.DatabaseDataSourceConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.TestExecutionListener;
import org.springframework.util.StringUtils;

public class CleanInsertTestExecutionListener implements TestExecutionListener {

	/**
	 * {@link CleanInsertTestExecutionListener}'s Logger.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(CleanInsertTestExecutionListener.class);

	public void beforeTestMethod(TestContext testContext) throws Exception {
		// location of the data set
		String dataSetResourcePath = null;

		// first, the annotation on the test class
		DataSetLocation dsLocation = testContext.getTestInstance().getClass().getAnnotation(DataSetLocation.class);
		if (dsLocation != null) {
			// found the annotation
			dataSetResourcePath = dsLocation.value();
			LOGGER.debug("annotated test, using data set [ " + dataSetResourcePath + " ]");
		} else {
			// no annotation, let's try with the name of the test
			String tempDsRes = testContext.getTestInstance().getClass().getName();
			tempDsRes = StringUtils.replace(tempDsRes, ".", "/");
			tempDsRes = "/" + tempDsRes + "-dataset.xml";
			if (getClass().getResourceAsStream(tempDsRes) != null) {
				LOGGER.debug("detected default dataset [ " + tempDsRes + " ]");
				dataSetResourcePath = tempDsRes;
			} else {
				LOGGER.info("no default dataset");
			}
		}

		if (dataSetResourcePath != null) {
			Resource dataSetResource = testContext.getApplicationContext().getResource(dataSetResourcePath);
			IDataSet dataSet = new FlatXmlDataSetBuilder().build(dataSetResource.getInputStream());
			IDatabaseConnection dbConn =
					new DatabaseDataSourceConnection(testContext.getApplicationContext().getBean(DataSource.class));
			DatabaseOperation.CLEAN_INSERT.execute(dbConn, dataSet);
		} else {
			LOGGER.info("[ " + testContext.getClass().getName() + " ] does not have any data set, no data injection");
		}

	}

	public void beforeTestClass(TestContext testContext) throws Exception {
		// TODO Auto-generated method stub

	}

	public void prepareTestInstance(TestContext testContext) throws Exception {
		// TODO Auto-generated method stub

	}

	public void afterTestMethod(TestContext testContext) throws Exception {
		// TODO Auto-generated method stub

	}

	public void afterTestClass(TestContext testContext) throws Exception {
		// TODO Auto-generated method stub

	}

}
