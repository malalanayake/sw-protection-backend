package com.sw.protection.backend.encoder;

import com.sw.protection.backend.config.EncoderDecoderType;

/**
 * Enum which is used to create the proper Encoder
 * 
 * @author dinuka
 * 
 */
public interface EncoderFactory {
    /**
     * Get the encoder
     * 
     * @param encoderDecoderType
     *            - Specify the type of encoder ex/ JSON, XML
     * @return - encoder
     */
    public Encoder getEncoder(EncoderDecoderType encoderDecoderType);
}
