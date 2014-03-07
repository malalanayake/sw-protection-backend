package com.sw.protection.backend.dao.impl;

import static org.testng.Assert.assertEquals;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.sw.protection.backend.common.Formatters;
import com.sw.protection.backend.config.APINames;
import com.sw.protection.backend.config.APIOperations;
import com.sw.protection.backend.config.HibernateUtil;
import com.sw.protection.backend.config.SharedInMemoryData;
import com.sw.protection.backend.config.Types;
import com.sw.protection.backend.config.test.DBTestProperties;
import com.sw.protection.backend.dao.TraceDAO;
import com.sw.protection.backend.entity.Trace;

@Test(groups = { "TraceDAOImplTest" }, dependsOnGroups = { "CompanySWCopyDAOImplTest" })
public class TraceDAOImplTest {
    public static final Logger log = Logger.getLogger(TraceDAOImplTest.class.getName());

    @BeforeClass
    public static void setUpClass() throws Exception {
	HibernateUtil.setHost(DBTestProperties.HOST);
	HibernateUtil.setPort(DBTestProperties.PORT);
	HibernateUtil.setUsername(DBTestProperties.USER);
	HibernateUtil.setPassword(DBTestProperties.PW);
	HibernateUtil.setDbname(DBTestProperties.DBNAME);
	HibernateUtil.init();
	SharedInMemoryData.getInstance();
    }

    @Test
    public void saveTrace() {
	TraceDAO traceDAO = new TraceDAOImpl();
	Trace trace1 = new Trace();
	trace1.setApi_name(APINames.ADMIN);
	trace1.setOperation(APIOperations.PUT);
	trace1.setDate_time(Formatters.formatDate(new Date()));
	trace1.setType(Types.SUPER_ADMIN);
	trace1.setType_user_name("dinuka");
	trace1.setAffected_type(Types.ADMIN);
	trace1.setAffected_user_name("malinda");
	trace1.setBefore_data("Admin operation GET,POST");
	trace1.setAfter_data("Admin operation GET,POST,DELETE,PUT");
	traceDAO.saveTrace(trace1);

	Trace trace2 = new Trace();
	trace2.setApi_name(APINames.ADMIN);
	trace2.setOperation(APIOperations.POST);
	trace2.setDate_time(Formatters.formatDate(new Date()));
	trace2.setType(Types.SUPER_ADMIN);
	trace2.setType_user_name("dinuka");
	trace2.setAffected_type(Types.ADMIN);
	trace2.setAffected_user_name("kasuni");
	trace2.setBefore_data("Not Applicable");
	trace2.setAfter_data("Add new admin Admin with operation GET,POST,DELETE,PUT");
	traceDAO.saveTrace(trace2);

	Trace trace3 = new Trace();
	trace3.setApi_name(APINames.COMPANY);
	trace3.setOperation(APIOperations.GET);
	trace3.setDate_time(Formatters.formatDate(new Date()));
	trace3.setType(Types.ADMIN);
	trace3.setType_user_name("kasuni");
	trace3.setAffected_type(Types.COMPANY_USER);
	trace3.setAffected_user_name("dinuka");
	trace3.setBefore_data("All dinuka's data");
	trace3.setAfter_data("No changes");
	traceDAO.saveTrace(trace3);

	Trace trace4 = new Trace();
	trace4.setApi_name(APINames.SOFTWARE);
	trace4.setOperation(APIOperations.DELETE);
	trace4.setDate_time(Formatters.formatDate(new Date()));
	trace4.setType(Types.COMPANY);
	trace4.setType_user_name("sysensor");
	trace4.setAffected_type(Types.SOFTWARE);
	trace4.setAffected_user_name("payroll");
	trace4.setBefore_data("Payroll details");
	trace4.setAfter_data("Not Applicable");
	traceDAO.saveTrace(trace4);

    }

    @Test(dependsOnMethods = { "saveTrace" })
    public void getAllTraceByAPIName() {
	TraceDAO traceDAO = new TraceDAOImpl();
	List<Trace> traceList = traceDAO.getAllTraceByAPIName(APINames.ADMIN);
	assertEquals(traceList.size(), 2);
	List<Trace> traceList1 = traceDAO.getAllTraceByAPIName(APINames.SOFTWARE);
	assertEquals(traceList1.size(), 1);
    }

    @Test(dependsOnMethods = { "getAllTraceByAPIName" })
    public void getAllTraceByAffectedTypeAndUserName() {
	TraceDAO traceDAO = new TraceDAOImpl();
	List<Trace> traceList = traceDAO.getAllTraceByAffectedTypeAndUserName(Types.SOFTWARE, "payroll");
	assertEquals(traceList.size(), 1);
	List<Trace> traceList1 = traceDAO.getAllTraceByAffectedTypeAndUserName(Types.SOFTWARE, "adverising");
	assertEquals(traceList1, null);

    }

    @Test(dependsOnMethods = { "getAllTraceByAffectedTypeAndUserName" })
    public void getAllTraceByTypeAndUserName() {
	TraceDAO traceDAO = new TraceDAOImpl();
	List<Trace> traceList = traceDAO.getAllTraceByTypeAndUserName(Types.ADMIN, "kasuni");
	assertEquals(traceList.size(), 1);
	List<Trace> traceList1 = traceDAO.getAllTraceByTypeAndUserName(Types.CLIENT, "malinda");
	assertEquals(traceList1, null);
    }

    @Test(dependsOnMethods = { "getAllTraceByTypeAndUserName" })
    public void deleteTrace() {
	TraceDAO traceDAO = new TraceDAOImpl();
	List<Trace> traceList = traceDAO.getAllTraceByAPIName(APINames.ADMIN);
	assertEquals(traceList.size(), 2);
	for (Trace trace : traceList) {
	    try {
		traceDAO.deleteTrace(trace);
	    } catch (Exception ex) {

	    }
	}
	traceList = traceDAO.getAllTraceByAPIName(APINames.ADMIN);
	assertEquals(traceList, null);

	List<Trace> traceList1 = traceDAO.getAllTraceByAPIName(APINames.SOFTWARE);
	assertEquals(traceList1.size(), 1);
	for (Trace trace : traceList1) {
	    try {
		traceDAO.deleteTrace(trace);
	    } catch (Exception ex) {

	    }
	}
	traceList1 = traceDAO.getAllTraceByAPIName(APINames.SOFTWARE);
	assertEquals(traceList1, null);

	List<Trace> traceList2 = traceDAO.getAllTraceByAPIName(APINames.COMPANY);
	assertEquals(traceList2.size(), 1);
	for (Trace trace : traceList2) {
	    try {
		traceDAO.deleteTrace(trace);
	    } catch (Exception ex) {

	    }
	}
	traceList2 = traceDAO.getAllTraceByAPIName(APINames.COMPANY);
	assertEquals(traceList2, null);
    }

}
