package com.sw.protection.backend.config;

import java.util.UUID;

/**
 * This class will use to generate the api-keys
 * 
 * @author dinuka
 * 
 */
public class APIKeyGenerator {

    public static String generateAPIKey() {
	return UUID.randomUUID().toString();
    }
}
