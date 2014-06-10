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
import com.sw.protection.backend.config.ObjectType;

/**
 * This is an Entity which is holding the usage data of API's
 * 
 * @author dinuka
 */
@Entity
@NamedQueries({
		@NamedQuery(name = "findAllUsagesByTypeAndID", query = "SELECT u FROM UsageData u where u.type=:typeName and u.type_id=:typeID"),
		@NamedQuery(name = "findAllUsagesByAPIName", query = "SELECT u FROM UsageData u where u.api_name=:apiName"),
		@NamedQuery(name = "findUsagesIsAlreadyExist", query = "SELECT u FROM UsageData u where u.type=:typeName and u.type_id=:typeID and u.api_name=:apiName and u.operation=:apiOperation") })
public class UsageData implements Serializable {

	/**
	 * interface provides the name queries and parameters
	 */
	public static interface Constants {

		public static final String NAME_QUERY_FIND_ALL_BY_TYPE_AND_ID = "findAllUsagesByTypeAndID";
		public static final String NAME_QUERY_FIND_ALL_BY_API_NAME = "findAllUsagesByAPIName";
		public static final String NAME_QUERY_IS_RECORD_ALREADY_EXIST = "findUsagesIsAlreadyExist";
		public static final String PARAM_USAGE_TYPE_NAME = "typeName";
		public static final String PARAM_USAGE_TYPE_ID = "typeID";
		public static final String PARAM_USAGE_API_NAME = "apiName";
		public static final String PARAM_USAGE_API_OPERATION = "apiOperation";
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
	private Long type_id;
	@Column(nullable = false)
	private ObjectType type;
	@Column(nullable = false)
	private String date_time;
	@Column(nullable = false)
	private String last_modified;
	@Column(nullable = false)
	private String access_count;
	@Column(nullable = false)
	private String decline_count;

	public APIOperations getOperation() {
		return operation;
	}

	public ObjectType getType() {
		return type;
	}

	public void setOperation(APIOperations operation) {
		this.operation = operation;
	}

	public String getAccess_count() {
		return access_count;
	}

	public void setAccess_count(String access_count) {
		this.access_count = access_count;
	}

	public String getDecline_count() {
		return decline_count;
	}

	public void setDecline_count(String decline_count) {
		this.decline_count = decline_count;
	}

	public void setType(ObjectType type) {
		this.type = type;
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
		String output = "Usage - ID:" + id + ", API Name:" + api_name + ", Operation:" + operation
				+ ", Type:" + type + ", TypeID:" + type_id + ", AccessCount:" + access_count
				+ ", DeclineCount:" + decline_count + ", Date:" + date_time + ", LM:"
				+ last_modified;
		return output;
	}

}
