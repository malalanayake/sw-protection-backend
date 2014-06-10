package com.sw.sample.data;

import java.util.Set;
import java.util.UUID;

import com.sw.protection.backend.common.exception.DuplicateRecordException;
import com.sw.protection.backend.common.exception.OperationRollBackException;
import com.sw.protection.backend.config.AppContext;
import com.sw.protection.backend.dao.AdminDAO;
import com.sw.protection.backend.entity.Admin;
import com.sw.protection.backend.entity.AdminScope;

public class Test {

	public static void main(String[] args) {
		AdminDAO adminDAO = AppContext.getInstance().getBean(AdminDAO.class);
		Admin admin = new Admin();
		admin.setUser_name("dinuka");
		admin.setPass_word("test");
		admin.setEmail("dinuak@123.com");
		admin.setName("Dinuka");
		admin.setApi_key(UUID.randomUUID().toString());

		AdminScope adminScope = new AdminScope();
		adminScope.setAdmin(admin);
		adminScope.setApi_name("app");
		adminScope.setDel(true);
		adminScope.setGet(true);
		adminScope.setPost(true);
		adminScope.setPut(true);

		Set<AdminScope> adminScopSet = admin.getAdminScopeSet();
		adminScopSet.add(adminScope);
		admin.setAdminScopeSet(adminScopSet);
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
