/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sw.protection.backend.config.test;

/**
 * This class contain test database configuration. To run the tests on developer
 * environment, developers need to setup the mysql database according to the
 * following parameters.
 * 
 * @author dinuka
 */
public interface DBTestProperties {
    public static final String HOST = "localhost";
    public static final String PORT = "3306";
    public static final String USER = "root";
    public static final String PW = "root123";
    public static final String DBNAME = "sw";
}
