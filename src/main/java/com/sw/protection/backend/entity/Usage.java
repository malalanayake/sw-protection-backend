/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sw.protection.backend.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * 
 * @author dinuka
 */
@Entity
public class Usage implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(nullable = false)
    private String operation;
    @Column(nullable = false)
    private String api_name;
    @Column(nullable = false)
    private Long type_id;
    @Column(nullable = false)
    private String type;
    @Column(nullable = false)
    private String date_time;
    @Column(nullable = false)
    private String last_modified;

    public String getOperation() {
	return operation;
    }

    public void setOperation(String operation) {
	this.operation = operation;
    }

    public String getApi_name() {
	return api_name;
    }

    public void setApi_name(String api_name) {
	this.api_name = api_name;
    }

    public Long getType_id() {
	return type_id;
    }

    public void setType_id(Long type_id) {
	this.type_id = type_id;
    }

    public String getType() {
	return type;
    }

    public void setType(String type) {
	this.type = type;
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
