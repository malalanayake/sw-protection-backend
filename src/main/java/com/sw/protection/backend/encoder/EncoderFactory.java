package com.sw.protection.backend.encoder;

import com.sw.protection.backend.config.EncoderDecoderType;

/**
 * Interface which is used to get the proper Encoder
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
