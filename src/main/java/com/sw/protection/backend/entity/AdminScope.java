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
 * AdminScope entity is going to store the scope details of admin users in the
 * system and this would be used to authorization process.
 * 
 * @author dinuka
 */
@Entity
@NamedQueries({
	@NamedQuery(name = "findAdminScopeByUserName", query = "SELECT u FROM AdminScope u where u.admin.user_name=:userName"),
	@NamedQuery(name = "findAdminScopeByUserNameAndAPIName", query = "select u from AdminScope u where u.admin.user_name=:userName and u.api_name=:apiName"), })
public class AdminScope implements Serializable {

    /**
     * interface provides the name queries and parameters
     */
    public static interface Constants {
	public static final String NAME_QUERY_FIND_BY_USER_NAME = "findAdminScopeByUserName";
	public static final String NAME_QUERY_FIND_BY_USER_NAME_AND_API_NAME = "findAdminScopeByUserNameAndAPIName";

	public static final String PARAM_USER_NAME = "userName";
	public static final String PARAM_API_NAME = "apiName";
    }

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(unique = true, nullable = false)
    private Long id;
    private String api_name;
    private boolean get;
    private boolean post;
    private boolean put;
    private boolean del;
    @Column(nullable = false)
    private String last_modified;
    @ManyToOne(fetch = FetchType.EAGER)
    private Admin admin;

    public String getLast_modified() {
	return last_modified;
    }

    public void setLast_modified(String last_modified) {
	this.last_modified = last_modified;
    }

    public Admin getAdmin() {
	return admin;
    }

    public void setAdmin(Admin admin) {
	this.admin = admin;
    }

    public String getApi_name() {
	return api_name;
    }

    public void setApi_name(String api_name) {
	this.api_name = api_name;
    }

    public boolean isGet() {
	return get;
    }

    public void setGet(boolean get) {
	this.get = get;
    }

    public boolean isPost() {
	return post;
    }

    public void setPost(boolean post) {
	this.post = post;
    }

    public boolean isPut() {
	return put;
    }

    public void setPut(boolean put) {
	this.put = put;
    }

    public boolean isDel() {
	return del;
    }

    public void setDel(boolean del) {
	this.del = del;
    }

    public Long getId() {
	return id;
    }

    public void setId(Long id) {
	this.id = id;
    }

    @Override
    public boolean equals(Object o) {
	return super.equals(o);
    }

    @Override
    public int hashCode() {
	return super.hashCode();
    }

    @Override
    public String toString() {
	return "ID:" + id + ", Api Name:" + api_name + ", GET:" + get + ", POST:" + post + ", PUT:" + put + ", DELETE:"
		+ del + ", Admin ID:" + admin.getId();
    }

}
