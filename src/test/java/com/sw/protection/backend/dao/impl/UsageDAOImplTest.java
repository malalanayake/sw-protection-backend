package com.sw.protection.backend.dao.impl;

import static org.testng.Assert.assertEquals;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.sw.protection.backend.common.Formatters;
import com.sw.protection.backend.common.exception.DuplicateRecordException;
import com.sw.protection.backend.common.exception.RecordAlreadyModifiedException;
import com.sw.protection.backend.config.APINames;
import com.sw.protection.backend.config.APIOperations;
import com.sw.protection.backend.config.AppContext;
import com.sw.protection.backend.config.ObjectType;
import com.sw.protection.backend.config.SharedInMemoryData;
import com.sw.protection.backend.dao.UsageDAO;
import com.sw.protection.backend.entity.UsageData;

@Test(groups = { "UsageDAOImplTest" }, dependsOnGroups = { "TraceDAOImplTest" })
public class UsageDAOImplTest {
	public static final Logger log = Logger.getLogger(UsageDAOImplTest.class.getName());

	@BeforeClass
	public static void setUpClass() throws Exception {
		SharedInMemoryData.getInstance();
	}

	@Test
	public void saveUsage() {
		UsageDAO usageDAO = AppContext.getInstance().getBean(UsageDAO.class);
		UsageData usage = new UsageData();
		usage.setApi_name(APINames.ADMIN);
		usage.setOperation(APIOperations.GET);
		usage.setType(ObjectType.ADMIN);
		usage.setType_id(1l);
		usage.setDate_time(Formatters.formatDate(new Date()));
		usage.setLast_modified(Formatters.formatDate(new Date()));
		usage.setAccess_count("100");
		usage.setDecline_count("0");
		try {
			usageDAO.saveUsage(usage);
		} catch (Exception ex) {

		}

		// Check the DuplicateRecordException behavior
		String exceptionClass = "";
		try {
			usageDAO.saveUsage(usage);
		} catch (Exception ex) {
			exceptionClass = ex.getClass().toString();
		}
		assertEquals(exceptionClass, DuplicateRecordException.class.toString());

		UsageData usage2 = new UsageData();
		usage2.setApi_name(APINames.COMPANY_USER);
		usage2.setOperation(APIOperations.POST);
		usage2.setType(ObjectType.ADMIN);
		usage2.setType_id(1l);
		usage2.setDate_time(Formatters.formatDate(new Date()));
		usage2.setLast_modified(Formatters.formatDate(new Date()));
		usage2.setAccess_count("200");
		usage2.setDecline_count("0");
		try {
			usageDAO.saveUsage(usage2);
		} catch (Exception ex) {

		}

		UsageData usage3 = new UsageData();
		usage3.setApi_name(APINames.COMPANY_SW);
		usage3.setOperation(APIOperations.PUT);
		usage3.setType(ObjectType.COMPANY);
		usage3.setType_id(1l);
		usage3.setDate_time(Formatters.formatDate(new Date()));
		usage3.setLast_modified(Formatters.formatDate(new Date()));
		usage3.setAccess_count("0");
		usage3.setDecline_count("10");
		try {
			usageDAO.saveUsage(usage3);
		} catch (Exception ex) {

		}

		UsageData usage4 = new UsageData();
		usage4.setApi_name(APINames.COMPANY);
		usage4.setOperation(APIOperations.DELETE);
		usage4.setType(ObjectType.COMPANY_USER);
		usage4.setType_id(1l);
		usage4.setDate_time(Formatters.formatDate(new Date()));
		usage4.setLast_modified(Formatters.formatDate(new Date()));
		usage4.setAccess_count("200");
		usage4.setDecline_count("20");
		try {
			usageDAO.saveUsage(usage4);
		} catch (Exception ex) {

		}

	}

	@Test(dependsOnMethods = { "saveUsage" })
	public void getAllUsagesByAPIName() {
		UsageDAO usageDAO = AppContext.getInstance().getBean(UsageDAO.class);
		List<UsageData> usageList1 = usageDAO.getAllUsagesByAPIName(APINames.ADMIN);
		assertEquals(usageList1.size(), 1);
		List<UsageData> usageList2 = usageDAO.getAllUsagesByAPIName(APINames.COMPANY_USER);
		assertEquals(usageList2.size(), 1);
		List<UsageData> usageList3 = usageDAO.getAllUsagesByAPIName(APINames.COMPANY_SW);
		assertEquals(usageList3.size(), 1);
		List<UsageData> usageList4 = usageDAO.getAllUsagesByAPIName(APINames.COMPANY);
		assertEquals(usageList4.size(), 1);
	}

	@Test(dependsOnMethods = { "getAllUsagesByAPIName" })
	public void getAllUsagesByTypeAndID() {
		UsageDAO usageDAO = AppContext.getInstance().getBean(UsageDAO.class);
		List<UsageData> usageList1 = usageDAO.getAllUsagesByTypeAndID(ObjectType.ADMIN, 1l);
		assertEquals(usageList1.size(), 2);

		List<UsageData> usageList2 = usageDAO.getAllUsagesByTypeAndID(ObjectType.SUPER_ADMIN, 1l);
		assertEquals(usageList2, null);
	}

	@Test(dependsOnMethods = { "getAllUsagesByTypeAndID" })
	public void updateUsage() {
		UsageDAO usageDAO = AppContext.getInstance().getBean(UsageDAO.class);
		List<UsageData> usageList1 = usageDAO.getAllUsagesByAPIName(APINames.ADMIN);
		assertEquals(usageList1.size(), 1);
		UsageData usageData = usageList1.get(0);
		List<UsageData> usageListAlreadyUptodate = usageDAO.getAllUsagesByAPIName(APINames.ADMIN);
		UsageData usageDataAlreadyUptodate = usageListAlreadyUptodate.get(0);

		assertEquals(usageData.getApi_name(), APINames.ADMIN);
		assertEquals(usageData.getOperation(), APIOperations.GET);
		assertEquals(usageData.getType(), ObjectType.ADMIN);
		assertEquals(usageData.getAccess_count(), "100");
		assertEquals(usageData.getDecline_count(), "0");

		usageData.setAccess_count("200");
		usageData.setDecline_count("12");
		try {
			usageDAO.updateUsage(usageData);
		} catch (Exception ex) {

		}

		// Check the RecordAlreadyModifiedException behavior
		String exceptionClass = "";
		try {
			usageDAO.updateUsage(usageDataAlreadyUptodate);
		} catch (Exception ex) {
			exceptionClass = ex.getClass().toString();
		}
		assertEquals(exceptionClass, RecordAlreadyModifiedException.class.toString());

		List<UsageData> usageList2 = usageDAO.getAllUsagesByAPIName(APINames.ADMIN);
		assertEquals(usageList2.size(), 1);
		UsageData usageData2 = usageList2.get(0);
		assertEquals(usageData2.getApi_name(), APINames.ADMIN);
		assertEquals(usageData2.getOperation(), APIOperations.GET);
		assertEquals(usageData2.getType(), ObjectType.ADMIN);
		assertEquals(usageData2.getAccess_count(), "200");
		assertEquals(usageData2.getDecline_count(), "12");

	}

	@Test(dependsOnMethods = { "updateUsage" })
	public void deleteUsage() {
		UsageDAO usageDAO = AppContext.getInstance().getBean(UsageDAO.class);
		List<UsageData> usageList1 = usageDAO.getAllUsagesByAPIName(APINames.ADMIN);
		UsageData usageData = usageList1.get(0);
		List<UsageData> usageListAlreadyUptodate = usageDAO.getAllUsagesByAPIName(APINames.ADMIN);
		UsageData usageDataAlreadyUptodate = usageListAlreadyUptodate.get(0);
		try {
			usageDAO.updateUsage(usageDataAlreadyUptodate);
		} catch (Exception ex) {

		}

		// Check the RecordAlreadyModifiedException behavior
		String exceptionClass = "";
		try {
			usageDAO.deleteUsage(usageData);
		} catch (Exception ex) {
			exceptionClass = ex.getClass().toString();
		}
		assertEquals(exceptionClass, RecordAlreadyModifiedException.class.toString());

		usageList1 = usageDAO.getAllUsagesByAPIName(APINames.ADMIN);
		for (UsageData usage : usageList1) {
			try {
				usageDAO.deleteUsage(usage);
			} catch (Exception ex) {

			}
		}
		List<UsageData> usageList2 = usageDAO.getAllUsagesByAPIName(APINames.COMPANY_USER);
		for (UsageData usage : usageList2) {
			try {
				usageDAO.deleteUsage(usage);
			} catch (Exception ex) {

			}
		}
		List<UsageData> usageList3 = usageDAO.getAllUsagesByAPIName(APINames.COMPANY_SW);
		for (UsageData usage : usageList3) {
			try {
				usageDAO.deleteUsage(usage);
			} catch (Exception ex) {

			}
		}
		List<UsageData> usageList4 = usageDAO.getAllUsagesByAPIName(APINames.COMPANY);
		for (UsageData usage : usageList4) {
			try {
				usageDAO.deleteUsage(usage);
			} catch (Exception ex) {

			}
		}
	}
}
