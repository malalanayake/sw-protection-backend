package com.sw.protection.backend.dao;

import java.util.List;

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
    public CompanySWCopy getCompanySWCopy(String clientUserName, String softwareName, String motherBoard, String hd,
	    String mac);

    /**
     * Update software copy
     * 
     * @param companySWCopy
     *            - software copy going to be updated
     */
    public void updateCompanySWCopy(CompanySWCopy companySWCopy);

    /**
     * Delete specific software copy
     * 
     * @param companySWCopy
     *            - software copy going to be deleted
     */
    public void deleteCompanySWCopy(CompanySWCopy companySWCopy);

    /**
     * Save software copy object
     * 
     * @param companySWCopy
     *            - software copy going to be saved
     */
    public void saveCompanySWCopy(CompanySWCopy companySWCopy);

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
    public boolean isCompanySWCopyExist(String clientUserName, String softwareName, String motherBoard, String hd,
	    String mac);
}
