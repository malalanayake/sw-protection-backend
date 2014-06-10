package com.sw.protection.backend.encoder;

import java.util.List;

import com.sw.protection.backend.common.exception.EncodingException;
import com.sw.protection.backend.config.ObjectType;

/**
 * This is use to encode the objects in to XML, JSON any given pattern
 * 
 * @author dinuka
 * 
 */
public interface Encoder {

	/**
	 * Set the object by declaring the object type
	 * 
	 * @param objectType
	 *            - Entity class type provided by ObjectType enum
	 * @param object
	 *            - Entity object
	 */
	public String encodeObject(ObjectType objectType, Object object) throws ClassCastException,
			EncodingException;

	/**
	 * Set the list of objects by declaring the object type
	 * 
	 * @param objectType
	 *            - Entity class type provided by ObjectType enum
	 * @param objectList
	 *            - List of objects
	 */
	public String encodeObjectList(ObjectType objectType, List<?> objectList)
			throws ClassCastException, EncodingException;

}
