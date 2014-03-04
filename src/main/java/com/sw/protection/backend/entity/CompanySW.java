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
 * This is an Entity which is going to hold the company software data
 * 
 * @author dinuka
 */
@Entity
@NamedQueries({
	@NamedQuery(name = "findCompanySWAll", query = "SELECT u FROM CompanySW u"),
	@NamedQuery(name = "findByNameOfCompanySWAndCompanyUserName", query = "select u from CompanySW u where u.name=:companySWName and u.company.user_name=:companyUserName"),
	@NamedQuery(name = "findByCompanySWName", query = "select u from CompanySW u where u.name=:companySWName") })
public class CompanySW implements Serializable {
    /**
     * interface provides the name queries and parameters
     */
    public static interface Constants {

	public static final String NAME_QUERY_FIND_COMPANY_SW_ALL = "findCompanySWAll";
	public static final String NAME_QUERY_FIND_BY_COMPANY_SW_NAME_AND_COMPANY_USER_NAME = "findByNameOfCompanySWAndCompanyUserName";
	public static final String PARAM_COMPANY_SW_NAME = "companySWName";
	public static final String NAME_QUERY_FIND_BY_COMPANY_SW_NAME = "findByCompanySWName";
	public static final String PARAM_COMPANY_USER_NAME = "companyUserName";
    }

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String description;
    @Column(nullable = false)
    private String date_time;
    @Column(nullable = false)
    private String last_modified;

    @ManyToOne(fetch = FetchType.EAGER)
    private Company company;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "company_sw")
    private Set<CompanySWCopy> companySWCopySet = new HashSet<CompanySWCopy>();

    public Set<CompanySWCopy> getCompanySWCopySet() {
	return companySWCopySet;
    }

    public void setCompanySWCopySet(Set<CompanySWCopy> companySWCopySet) {
	this.companySWCopySet = companySWCopySet;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public String getDescription() {
	return description;
    }

    public void setDescription(String description) {
	this.description = description;
    }

    public String getDate_time() {
	return date_time;
    }

    public void setDate_time(String date_time) {
	this.date_time = date_time;
    }

    public String getLast_modified() {
	return last_modified;
    }

    public void setLast_modified(String last_modified) {
	this.last_modified = last_modified;
    }

    public Company getCompany() {
	return company;
    }

    public void setCompany(Company company) {
	this.company = company;
    }

    public Long getId() {
	return id;
    }

    public void setId(Long id) {
	this.id = id;
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
	String output = "Software ID:" + id + ", Name:" + name + ", Description:" + description + ", Date:" + date_time
		+ ", LM:" + last_modified;
	return output;
    }

}
