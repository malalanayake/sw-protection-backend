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
 * 
 * @author dinuka
 */
@Entity
@NamedQueries({
	@NamedQuery(name = "findAllCompanies", query = "SELECT u FROM Company u"),
	@NamedQuery(name = "findByCompanyName", query = "select u from Company u where u.name like:companyName"),
	@NamedQuery(name = "findByCompanyUsersName", query = "select u from Company u where u.user_name=:companyUserName"),
	@NamedQuery(name = "findByCompanyAPIKey", query = "select u from Company u where u.api_key=:companyApiKey") })
public class Company implements Serializable {

    /**
     * interface provides the name queries and parameters
     */
    public static interface Constants {

	public static final String NAME_QUERY_FIND_COMPANY_ALL = "findAllCompanies";
	public static final String NAME_QUERY_FIND_COMPANY_BY_NAME = "findByCompanyName";
	public static final String PARAM_COMPANY_NAME = "like:companyName";
	public static final String NAME_QUERY_FIND_BY_COMPANY_USER_NAME = "findByCompanyUsersName";
	public static final String NAME_QUERY_FIND_BY_COMPANY_API_KEY = "findByCompanyAPIKey";
	public static final String PARAM_COMPANY_USER_NAME = "companyUserName";
	public static final String PARAM_COMPANY_API_KEY = "companyApiKey";
    }

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String name;
    @Column(unique = true, nullable = false)
    private String user_name;
    @Column(nullable = false)
    private String pass_word;
    @Column(nullable = false)
    private String api_key;
    @Column(nullable = false)
    private String email;
    @Column(nullable = false)
    private String date_time;
    @Column(nullable = false)
    private String last_modified;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "company")
    private Set<CompanyUser> companyUserSet = new HashSet<CompanyUser>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "company")
    private Set<CompanySW> companySWSet = new HashSet<CompanySW>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "company")
    private Set<CompanyClient> companyClientSet = new HashSet<CompanyClient>();

    public Set<CompanyUser> getCompanyUserSet() {
	return companyUserSet;
    }

    public void setCompanyUserSet(Set<CompanyUser> companyUserSet) {
	this.companyUserSet = companyUserSet;
    }

    public Set<CompanySW> getCompanySWSet() {
	return companySWSet;
    }

    public void setCompanySWSet(Set<CompanySW> companySWSet) {
	this.companySWSet = companySWSet;
    }

    public Set<CompanyClient> getCompanyClientSet() {
	return companyClientSet;
    }

    public void setCompanyClientSet(Set<CompanyClient> companyClientSet) {
	this.companyClientSet = companyClientSet;
    }

    public String getLast_modified() {
	return last_modified;
    }

    public void setLast_modified(String last_modified) {
	this.last_modified = last_modified;
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
	return super.equals(obj);
    }

    @Override
    public int hashCode() {
	return super.hashCode();
    }

    @Override
    public String toString() {
	String output = "id=" + id + ", name=" + name + ", user_name=" + user_name + ", api_key=" + api_key
		+ ", email=" + email + ", date_time=" + date_time;
	output = output + ", Admin ScopSet :";
	if (!companyUserSet.isEmpty()) {
	    for (CompanyUser user : companyUserSet) {
		output = output + user.toString();
	    }
	}
	return output;

    }

}
