package com.sw.protection.backend.decoder;

import java.util.List;

import com.sw.protection.backend.common.exception.DecodingException;
import com.sw.protection.backend.config.ObjectType;

/**
 * This is use to decode the XML, JSON to relevant java object
 * 
 * @author dinuka
 * 
 */
public interface Decoder {

	/**
	 * Decoding the string to java object
	 * 
	 * @param objectType
	 *            - the type that we going to decode
	 * @param encodedString
	 *            - Encoded string by proper format XML, JSON
	 * @return - relevant object
	 */
	public Object decodeObject(ObjectType objectType, String encodedString)
			throws DecodingException;

	/**
	 * Decoding the list of strings in to objects
	 * 
	 * @param objectType
	 *            - the type of object list
	 * @param encodedString
	 *            - Encoded list of objects by proper format XML, JSON
	 * @return - List of objects
	 */
	public List<?> decodeObjectList(ObjectType objectType, String encodedString)
			throws DecodingException;
}
