package com.sw.protection.backend.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

/**
 * This is an Entity which is going to hold the data for company software copies
 * 
 * @author dinuka
 */
@Entity
@NamedQueries({
	@NamedQuery(name = "findCompanySWCopyAll", query = "SELECT u FROM CompanySWCopy u"),
	@NamedQuery(name = "findByCompanySoftwareNameAndClientUNwithProperties", query = "select u from CompanySWCopy u where u.company_sw.name=:companySWName and u.company_client.user_name=:companyClientUserName and u.mother_board=:mBoard and u.hd=:hDrive and u.mac=:macAddress") })
public class CompanySWCopy implements Serializable {
    /**
     * interface provides the name queries and parameters
     */
    public static interface Constants {

	public static final String NAME_QUERY_FIND_COMPANY_SW_COPY_ALL = "findCompanySWCopyAll";
	public static final String NAME_QUERY_FIND_BY_COMPANY_SW_NAME_AND_CLIENT_USER_NAME_WITH_PROPERTIES = "findByCompanySoftwareNameAndClientUNwithProperties";
	public static final String PARAM_COMPANY_SW_NAME = "companySWName";
	public static final String PARAM_CLIENT_USER_NAME = "companyClientUserName";
	public static final String PARAM_MOTHER_BOARD = "mBoard";
	public static final String PARAM_HARD_DRIVE = "hDrive";
	public static final String PARAM_MAC_ADDRESS = "macAddress";
    }

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(nullable = false)
    private String mother_board;
    @Column(nullable = false)
    private String hd;
    @Column(nullable = false)
    private String mac;
    @Column(nullable = false)
    private String expire_date;
    @Column(nullable = false)
    private String date_time;
    @Column(nullable = false)
    private String last_modified;

    @ManyToOne(fetch = FetchType.EAGER)
    private CompanySW company_sw;

    @ManyToOne(fetch = FetchType.EAGER)
    private CompanyClient company_client;

    public String getMother_board() {
	return mother_board;
    }

    public void setMother_board(String mother_board) {
	this.mother_board = mother_board;
    }

    public String getHd() {
	return hd;
    }

    public void setHd(String hd) {
	this.hd = hd;
    }

    public String getMac() {
	return mac;
    }

    public void setMac(String mac) {
	this.mac = mac;
    }

    public String getExpire_date() {
	return expire_date;
    }

    public void setExpire_date(String expire_date) {
	this.expire_date = expire_date;
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

    public CompanySW getCompany_sw() {
	return company_sw;
    }

    public void setCompany_sw(CompanySW company_sw) {
	this.company_sw = company_sw;
    }

    public CompanyClient getCompany_client() {
	return company_client;
    }

    public void setCompany_client(CompanyClient company_client) {
	this.company_client = company_client;
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
	return "com.sw.protection.backend.entity.CompanySWCopy[ id=" + id + " ]";
    }

}
