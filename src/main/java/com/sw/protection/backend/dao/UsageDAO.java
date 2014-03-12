package com.sw.protection.backend.dao;

import java.util.List;

import com.sw.protection.backend.common.exception.DuplicateRecordException;
import com.sw.protection.backend.common.exception.OperationRollBackException;
import com.sw.protection.backend.common.exception.RecordAlreadyModifiedException;
import com.sw.protection.backend.config.ObjectType;
import com.sw.protection.backend.entity.UsageData;

/**
 * Interface which is provide the Usage operations.
 * 
 * @author dinuka
 * 
 */
public interface UsageDAO {
    /**
     * Get all usage data according to the user type and id as an example type:
     * CLIENT id:2
     * 
     * @param type
     *            - CLIENT,ADMIN,COMPANY,COMPANY_USER,SUPER_ADMIN
     * @param id
     * @return - List of Usages.
     */
    public List<UsageData> getAllUsagesByTypeAndID(ObjectType type, Long id);

    /**
     * Get all usages data according to the API Name
     * 
     * @param apiName
     *            - given API name
     * @return - List of Usages.
     */
    public List<UsageData> getAllUsagesByAPIName(String apiName);

    /**
     * Get usage by ID
     * 
     * @param ID
     *            - usage id
     * @return - specific usage object
     */
    public UsageData getUsage(Long ID);

    /**
     * Update usage
     * 
     * @param usage
     */
    public void updateUsage(UsageData usage) throws RecordAlreadyModifiedException, OperationRollBackException;

    /**
     * Delete usage data
     * 
     * @param usage
     */
    public void deleteUsage(UsageData usage) throws RecordAlreadyModifiedException, OperationRollBackException;

    /**
     * Save usage
     * 
     * @param usage
     */
    public UsageData saveUsage(UsageData usage) throws DuplicateRecordException, OperationRollBackException;

    /**
     * Check whether the usage data exist
     * 
     * @param usage
     * @return
     */
    public boolean isUsageDataExist(UsageData usage);
}
