package com.sw.protection.backend.common;

import com.sw.protection.backend.dao.impl.AdminDAOImpl;

public class ClearLocks implements Runnable {
    AdminDAOImpl adminDaoImpl = new AdminDAOImpl();

    @Override
    public void run() {
	adminDaoImpl.clearLocks();
    }

}
