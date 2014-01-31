/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sw.protection.backend.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

/**
 * Admin User Entity witch is going to store the admin user data in the system
 * 
 * @author dinuka
 */
@Entity
@NamedQueries( { @NamedQuery(name = "findAll", query = "SELECT u FROM Admin u"),
	@NamedQuery(name = "findByName", query = "select u from Admin u where u.name like:adminName"),
	@NamedQuery(name = "findByUserName", query = "select u from Admin u where u.user_name=:userName"),
	@NamedQuery(name = "findByAPIKey", query = "select u from Admin u where u.api_key=:apiKey") })
public class Admin implements Serializable {

    /**
     * interface provides the name queries and parameters
     */
    public static interface Constants {

	public static final String NAME_QUERY_FIND_ALL = "findAll";
	public static final String NAME_QUERY_FIND_BY_NAME = "findByName";
	public static final String PARAM_ADMIN_NAME = "adminName";
	public static final String NAME_QUERY_FIND_BY_USER_NAME = "findByUserName";
	public static final String NAME_QUERY_FIND_BY_API_KEY = "findByAPIKey";
	public static final String PARAM_USER_NAME = "userName";
	public static final String PARAM_API_KEY = "apiKey";
    }

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    @Column(unique = true, nullable = false)
    private String user_name;
    private String pass_word;
    private String api_key;
    private String email;
    private String date_time;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "admin")
    private Set<AdminScope> adminScopeSet = new HashSet<AdminScope>();

    public Set<AdminScope> getAdminScopeSet() {
	return adminScopeSet;
    }

    public void setAdminScopeSet(Set<AdminScope> adminScopeSet) {
	this.adminScopeSet = adminScopeSet;
    }

    public Long getId() {
	return id;
    }

    public void setId(Long id) {
	this.id = id;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public String getUser_name() {
	return user_name;
    }

    public void setUser_name(String user_name) {
	this.user_name = user_name;
    }

    public String getPass_word() {
	return pass_word;
    }

    public void setPass_word(String pass_word) {
	this.pass_word = pass_word;
    }

    public String getApi_key() {
	return api_key;
    }

    public void setApi_key(String api_key) {
	this.api_key = api_key;
    }

    public String getEmail() {
	return email;
    }

    public void setEmail(String email) {
	this.email = email;
    }

    public String getDate_time() {
	return date_time;
    }

    public void setDate_time(String date_time) {
	this.date_time = date_time;
    }

    @Override
    public boolean equals(Object obj) {
	return super.equals(obj); // To change body of generated methods, choose
	// Tools | Templates.
    }

    @Override
    public int hashCode() {
	return super.hashCode(); // To change body of generated methods, choose
	// Tools | Templates.
    }

    @Override
    public String toString() {
	String output = "id=" + id + ", name=" + name + ", user_name=" + user_name + ", api_key=" + api_key
		+ ", email=" + email + ", date_time=" + date_time;
	output = output + ", Admin ScopSet :";
	if (!adminScopeSet.isEmpty()) {
	    for (AdminScope scop : adminScopeSet) {
		output = output + scop.toString();
	    }
	}
	return output;

    }
}
