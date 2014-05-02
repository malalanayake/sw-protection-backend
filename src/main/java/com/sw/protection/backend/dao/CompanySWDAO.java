package com.sw.protection.backend.dao;

import java.util.List;

import com.sw.protection.backend.common.exception.DuplicateRecordException;
import com.sw.protection.backend.common.exception.OperationRollBackException;
import com.sw.protection.backend.common.exception.RecordAlreadyModifiedException;
import com.sw.protection.backend.entity.CompanySW;

/**
 * Interface which is provide the Company Software operations.
 * 
 * @author dinuka
 */
public interface CompanySWDAO {
    /**
     * Get all company softwares
     * 
     * @return - List of softwares
     */
    public List<CompanySW> getAllCompanySWs();

    /**
     * Get specific company software
     * 
     * @param companyUserName
     *            - company user name
     * @param softwareName
     *            - Software name
     * @return - Specific company software object
     */
    public CompanySW getCompanySW(String companyUserName, String softwareName);

    /**
     * Get all companySW according to the company user name
     * 
     * @param page
     *            - page number
     * @param recordePerPage
     *            - maximum record per page
     * @return
     */
    public List<CompanySW> getAllCompanySWWithPagination(int page, int recordePerPage);

    /**
     * Update company software
     * 
     * @param companySW
     *            - software going to be updated
     */
    public CompanySW updateCompanySW(CompanySW companySW) throws RecordAlreadyModifiedException,
	    OperationRollBackException;

    /**
     * Delete specific company software
     * 
     * @param companySW
     *            - software going to be deleted
     */
    public CompanySW deleteCompanySW(CompanySW companySW) throws RecordAlreadyModifiedException,
	    OperationRollBackException;

    /**
     * Save company software
     * 
     * @param companySW
     *            - software going to be saved
     */
    public CompanySW saveCompanySW(CompanySW companySW) throws DuplicateRecordException, OperationRollBackException;

    /**
     * Check whether the company software is exist
     * 
     * @param companyUserName
     *            - company user name
     * @param softwareName
     *            - software name
     * @return - TRUE/FALSE
     */
    public boolean isCompanySWExist(String companyUserName, String softwareName);

    /**
     * This method is used to validate the given CompanySW. This is going to use
     * in service class before saving the object
     * 
     * @param companySW
     *            - given CompanySW object
     * @return
     */
    public boolean validateCompanySWforSave(CompanySW companySW);

    /**
     * This method is used to validate the given CompanySW. This is going to use
     * in service class before update or delete the object
     * 
     * @param companySW
     *            - given companySW object
     * @return
     */
    public boolean validateCompanySWforUpdateandDelete(CompanySW companySW);
}
