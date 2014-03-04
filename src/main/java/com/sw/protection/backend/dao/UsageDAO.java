package com.sw.protection.backend.dao;

import java.util.List;

import com.sw.protection.backend.config.Types;
import com.sw.protection.backend.entity.Trace;
import com.sw.protection.backend.entity.UsageData;

/**
 * Interface which is provide the Usage operations.
 * 
 * @author dinuka
 * 
 */
public interface UsageDAO {
    /**
     * Get all usage data according to the user type and id as an example type: CLIENT id:2
     * @param type - CLIENT,ADMIN,COMPANY,COMPANY_USER,SUPER_ADMIN
     * @param id 
     * @return - List of Usages.
     */
    public List<UsageData> getAllUsagesByTypeAndID(Types type, Long id);
    
    /**
     * Get all usages data according to the API Name
     * @param apiName - given API name
     * @return - List of Usages.
     */
    public List<UsageData> getAllUsagesByAPIName(String apiName);
   
    /**
     * Get usage by ID
     * @param ID - usage id
     * @return - specific usage object
     */
    public UsageData getUsage(Long ID);
    
    /**
     * Update usage
     * @param usage
     */
    public void updateUsage(UsageData usage);
    
    /**
     * Delete usage data
     * @param usage
     */
    public void deleteUsage(UsageData usage);
    
    /**
     * Save usage
     * @param usage
     */
    public void saveUsage(UsageData usage); 
}
