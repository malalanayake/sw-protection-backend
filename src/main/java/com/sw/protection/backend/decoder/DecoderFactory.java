package com.sw.protection.backend.decoder;

import com.sw.protection.backend.config.EncoderDecoderType;

/**
 * Interface which is used to get the proper Decoder
 * 
 * @author dinuka
 * 
 */
public interface DecoderFactory {

	/**
	 * Get the relevant decoder
	 * 
	 * @param encoderDecoderType
	 *            - type of decoder XML, JSON
	 * @return
	 */
	public Decoder getDecoder(EncoderDecoderType encoderDecoderType);
}
