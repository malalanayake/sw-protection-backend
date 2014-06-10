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
 * This is an Entity which is going to hold the company client data
 * 
 * @author dinuka
 */
@Entity
@NamedQueries({
		@NamedQuery(name = "findCompanyClientAll", query = "SELECT u FROM CompanyClient u"),
		@NamedQuery(name = "findByNameCompanyClient", query = "select u from CompanyClient u where u.name like:companyClientName"),
		@NamedQuery(name = "findByCompanyClientUserName", query = "select u from CompanyClient u where u.user_name=:companyUserName") })
public class CompanyClient implements Serializable {

	/**
	 * interface provides the name queries and parameters
	 */
	public static interface Constants {

		public static final String NAME_QUERY_FIND_COMPANY_CLIENT_ALL = "findCompanyClientAll";
		public static final String NAME_QUERY_FIND_BY_COMPANY_CLIENT_NAME = "findByNameCompanyClient";
		public static final String PARAM_COMPANY_CLIENT_NAME = "companyClientName";
		public static final String NAME_QUERY_FIND_BY_COMPANY_CLIENT_USER_NAME = "findByCompanyClientUserName";
		public static final String PARAM_COMPANY_CLIENT_USER_NAME = "companyUserName";
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
	private String email;
	@Column(nullable = false)
	private String date_time;
	@Column(nullable = false)
	private String last_modified;

	@ManyToOne(fetch = FetchType.EAGER)
	private Company company;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "company_client")
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
		String output = "Client ID:" + id + ", Name:" + name + ", UserName:" + user_name
				+ ", Email:" + email + ", Date:" + date_time + ", LM:" + last_modified;
		return output;
	}

}
