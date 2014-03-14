package com.sw.sample.data;

import java.util.UUID;

import com.sw.protection.backend.common.exception.DuplicateRecordException;
import com.sw.protection.backend.common.exception.OperationRollBackException;
import com.sw.protection.backend.common.exception.RecordAlreadyModifiedException;
import com.sw.protection.backend.config.HibernateUtil;
import com.sw.protection.backend.config.SharedInMemoryData;
import com.sw.protection.backend.config.test.DBTestProperties;
import com.sw.protection.backend.dao.AdminDAO;
import com.sw.protection.backend.dao.impl.AdminDAOImpl;
import com.sw.protection.backend.entity.Admin;

public class RemoveData {

    String dataBaseName = "online";

    public RemoveData() {
	HibernateUtil.setHost(DBTestProperties.HOST);
	HibernateUtil.setPort(DBTestProperties.PORT);
	HibernateUtil.setUsername(DBTestProperties.USER);
	HibernateUtil.setPassword(DBTestProperties.PW);
	HibernateUtil.setDbname(dataBaseName);
	HibernateUtil.init();
	SharedInMemoryData.getInstance();
    }

    public static void main(String[] args) {
	RemoveData removeData = new RemoveData();
	removeData.deleteAdminData();
    }

    public void deleteAdminData() {
	AdminDAO adminDAO = new AdminDAOImpl();
	for (int i = 0; i < 20; i++) {
	    Admin admin = new Admin();
	    admin.setName("AdminUser " + i);
	    admin.setUser_name("admin" + i);
	    admin.setPass_word("admin" + i);
	    admin.setEmail("admin" + i + "@test.com");
	    admin.setApi_key(UUID.randomUUID().toString());
	    try {
		admin = adminDAO.getAdmin(admin.getUser_name());
		adminDAO.deleteAdmin(admin);
	    } catch (OperationRollBackException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    } catch (RecordAlreadyModifiedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }
	}

    }

}
