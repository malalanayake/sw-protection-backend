/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sw.protection.backend.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 * AdminScope entity is going to store the scope details of admin users in the
 * system
 * 
 * @author dinuka
 */
@Entity
public class AdminScope implements Serializable {

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
    @ManyToOne(fetch = FetchType.LAZY)
    private Admin admin;

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
	return super.equals(o); // To change body of generated methods, choose
				// Tools | Templates.
    }

    @Override
    public int hashCode() {
	return super.hashCode(); // To change body of generated methods, choose
				 // Tools | Templates.
    }

    @Override
    public String toString() {
	return "com.sw.protection.backend.entity.AdminScope[ id=" + id + " ]";
    }

}
