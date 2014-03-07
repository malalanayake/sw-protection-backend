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
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

/**
 * This is an entity which is going to use for store the company user data
 * 
 * @author dinuka
 */
@Entity
@NamedQueries({
	@NamedQuery(name = "findCompanyUserAll", query = "SELECT u FROM CompanyUser u"),
	@NamedQuery(name = "findByCompanyUserName", query = "select u from CompanyUser u where u.name like:adminName"),
	@NamedQuery(name = "findByCompanyUserUserName", query = "select u from CompanyUser u where u.user_name=:userName"),
	@NamedQuery(name = "findByCompanyUserAPIKey", query = "select u from CompanyUser u where u.api_key=:apiKey") })
public class CompanyUser implements Serializable {
    /**
     * interface provides the name queries and parameters
     */
    public static interface Constants {

	public static final String NAME_QUERY_FIND_COMPANY_USER_ALL = "findCompanyUserAll";
	public static final String NAME_QUERY_FIND_BY_NAME = "findByCompanyUserName";
	public static final String PARAM_NAME = "adminName";
	public static final String NAME_QUERY_FIND_BY_USER_NAME = "findByCompanyUserUserName";
	public static final String NAME_QUERY_FIND_BY_API_KEY = "findByCompanyUserAPIKey";
	public static final String PARAM_USER_NAME = "userName";
	public static final String PARAM_API_KEY = "apiKey";
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

    @ManyToOne(fetch = FetchType.EAGER)
    private Company company;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "companyUser")
    private Set<CompanyUserScope> userScopeSet = new HashSet<CompanyUserScope>();

    public Company getCompany() {
	return company;
    }

    public void setCompany(Company company) {
	this.company = company;
    }

    public Set<CompanyUserScope> getUserScopeSet() {
	return userScopeSet;
    }

    public void setUserScopeSet(Set<CompanyUserScope> userScopeSet) {
	this.userScopeSet = userScopeSet;
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
	return output;

    }

}
