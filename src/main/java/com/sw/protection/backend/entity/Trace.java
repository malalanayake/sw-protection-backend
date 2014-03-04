package com.sw.protection.backend.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import com.sw.protection.backend.config.APIOperations;
import com.sw.protection.backend.config.Types;

/**
 * This is an Entity which is holding the data of user tracing operations
 * 
 * @author dinuka
 */
@Entity
@NamedQueries({
	@NamedQuery(name = "findAllByTypeAndUserName", query = "SELECT u FROM Trace u where u.type=:typeName and u.type_user_name=:typeUserName"),
	@NamedQuery(name = "findAllByAffectedTypeAndUserName", query = "SELECT u FROM Trace u where u.affected_type=:affectedTypeName and u.affected_user_name=:affectedTypeUserName"),
	@NamedQuery(name = "findAllByAPIName", query = "SELECT u FROM Trace u where u.api_name=:apiName") })
public class Trace implements Serializable {

    /**
     * interface provides the name queries and parameters
     */
    public static interface Constants {

	public static final String NAME_QUERY_FIND_ALL_BY_TYPE_AND_USER_NAME = "findAllByTypeAndUserName";
	public static final String NAME_QUERY_FIND_ALL_BY_AFFECTED_TYPE_AND_USER_NAME = "findAllByAffectedTypeAndUserName";
	public static final String NAME_QUERY_FIND_ALL_BY_API_NAME = "findAllByAPIName";
	public static final String PARAM_TYPE_NAME = "typeName";
	public static final String PARAM_TYPE_USER_NAME = "typeUserName";
	public static final String PARAM_AFFECTED_TYPE_NAME = "affectedTypeName";
	public static final String PARAM_AFFECTED_TYPE_USER_NAME = "affectedTypeUserName";
	public static final String PARAM_API_NAME = "apiName";
    }

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(nullable = false)
    private APIOperations operation;
    @Column(nullable = false)
    private String api_name;
    @Column(nullable = false)
    private String type_user_name;
    @Column(nullable = false)
    private Types type;
    @Column(nullable = false)
    private String date_time;
    @Column(nullable = false)
    private Types affected_type;
    @Column(nullable = false)
    private String affected_user_name;
    @Column(nullable = false)
    private String before_data;
    @Column(nullable = false)
    private String after_data;

    public String getType_user_name() {
	return type_user_name;
    }

    public void setType_user_name(String type_user_name) {
	this.type_user_name = type_user_name;
    }

    public String getAffected_user_name() {
	return affected_user_name;
    }

    public void setAffected_user_name(String affected_user_name) {
	this.affected_user_name = affected_user_name;
    }

    public String getBefore_data() {
	return before_data;
    }

    public void setBefore_data(String before_data) {
	this.before_data = before_data;
    }

    public String getAfter_data() {
	return after_data;
    }

    public void setAfter_data(String after_data) {
	this.after_data = after_data;
    }

    public APIOperations getOperation() {
	return operation;
    }

    public void setOperation(APIOperations operation) {
	this.operation = operation;
    }

    public String getApi_name() {
	return api_name;
    }

    public void setApi_name(String api_name) {
	this.api_name = api_name;
    }

    public Types getType() {
	return type;
    }

    public void setType(Types type) {
	this.type = type;
    }

    public Types getAffected_type() {
	return affected_type;
    }

    public void setAffected_type(Types affected_type) {
	this.affected_type = affected_type;
    }

    public String getDate_time() {
	return date_time;
    }

    public void setDate_time(String date_time) {
	this.date_time = date_time;
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
	String output = "Trace - ID:" + id + ", APIName:" + api_name + ", Operation:" + operation + ", Type:" + type
		+ ", TypeUserName:" + type_user_name + ", Date:" + date_time + ", AffectedType:" + affected_type
		+ ", AffectedUserName:" + affected_user_name + ", BData:" + before_data + ", AData:" + after_data;
	return output;
    }
}
