package com.sw.protection.backend.dao;

import java.util.List;

import com.sw.protection.backend.common.exception.DuplicateRecordException;
import com.sw.protection.backend.common.exception.OperationRollBackException;
import com.sw.protection.backend.common.exception.RecordAlreadyModifiedException;
import com.sw.protection.backend.entity.CompanySWCopy;

/**
 * Interface which is provide the Company Software Copy operations.
 * 
 * @author dinuka
 */
public interface CompanySWCopyDAO {
	/**
	 * Get all software copy objects
	 * 
	 * @return - List of all software copies
	 */
	public List<CompanySWCopy> getAllCompanySWCopies();

	/**
	 * Get all CompanySWCopies
	 * 
	 * @param page
	 *            - page number
	 * @param recordePerPage
	 *            - maximum record per page
	 * @return
	 */
	public List<CompanySWCopy> getAllCompanySWCopyWithPagination(int page, int recordePerPage);

	/**
	 * Get specific company software copy
	 * 
	 * @param clientUserName
	 *            - client user name
	 * @param softwareName
	 *            - software name
	 * @param motherBoard
	 *            - mother board id
	 * @param hd
	 *            - hard drive id
	 * @param mac
	 *            - mac address
	 * @return - specific software copy object
	 */
	public CompanySWCopy getCompanySWCopy(String clientUserName, String softwareName,
			String motherBoard, String hd, String mac);

	/**
	 * Update software copy
	 * 
	 * @param companySWCopy
	 *            - software copy going to be updated
	 */
	public CompanySWCopy updateCompanySWCopy(CompanySWCopy companySWCopy)
			throws RecordAlreadyModifiedException, OperationRollBackException;

	/**
	 * Delete specific software copy
	 * 
	 * @param companySWCopy
	 *            - software copy going to be deleted
	 */
	public CompanySWCopy deleteCompanySWCopy(CompanySWCopy companySWCopy)
			throws RecordAlreadyModifiedException, OperationRollBackException;

	/**
	 * Save software copy object
	 * 
	 * @param companySWCopy
	 *            - software copy going to be saved
	 */
	public CompanySWCopy saveCompanySWCopy(CompanySWCopy companySWCopy)
			throws DuplicateRecordException, OperationRollBackException;

	/**
	 * Check whether the company software copy is already exist
	 * 
	 * @param clientUserName
	 *            - client user name
	 * @param softwareName
	 *            - software name
	 * @param motherBoard
	 *            - mother board id
	 * @param hd
	 *            - hard drive id
	 * @param mac
	 *            - mac address
	 * @return
	 */
	public boolean isCompanySWCopyExist(String clientUserName, String softwareName,
			String motherBoard, String hd, String mac);

	/**
	 * This method is used to validate the given CompanySWCopy. This is going to
	 * use in service class before saving the object
	 * 
	 * @param companySWCopy
	 *            - given CompanySWCopy object
	 * @return
	 */
	public boolean validateCompanySWCopyforSave(CompanySWCopy companySWCopy);

	/**
	 * This method is used to validate the given CompanySWCopy. This is going to
	 * use in service class before update or delete the object
	 * 
	 * @param companySWCopy
	 *            - given CompanySWCopy object
	 * @return
	 */
	public boolean validateCompanySWCopyforUpdateandDelete(CompanySWCopy companySWCopy);
}
