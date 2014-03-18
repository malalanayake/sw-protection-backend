package com.sw.sample.data;

import java.util.UUID;

import com.sw.protection.backend.common.exception.DuplicateRecordException;
import com.sw.protection.backend.common.exception.OperationRollBackException;
import com.sw.protection.backend.config.AppContext;
import com.sw.protection.backend.config.SharedInMemoryData;
import com.sw.protection.backend.dao.AdminDAO;
import com.sw.protection.backend.entity.Admin;

public class PopulateData {

    String dataBaseName = "sw";

    public PopulateData() {
	SharedInMemoryData.getInstance();
    }

    public static void main(String[] args) {
	PopulateData populateData = new PopulateData();
	populateData.addAdminData();
    }

    public void addAdminData() {
	AdminDAO adminDAO = AppContext.getInstance().getBean(AdminDAO.class);
	for (int i = 0; i < 20; i++) {
	    Admin admin = new Admin();
	    admin.setName("AdminUser " + i);
	    admin.setUser_name("admin" + i);
	    admin.setPass_word("admin" + i);
	    admin.setEmail("admin" + i + "@test.com");
	    admin.setApi_key(UUID.randomUUID().toString());
	    try {
		adminDAO.saveAdmin(admin);
	    } catch (DuplicateRecordException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    } catch (OperationRollBackException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }
	}

    }

}
