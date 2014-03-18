package com.sw.protection.backend.decoder.impl;

import org.springframework.stereotype.Service;

import com.sw.protection.backend.config.EncoderDecoderType;
import com.sw.protection.backend.decoder.Decoder;
import com.sw.protection.backend.decoder.DecoderFactory;

/**
 * This class is provide the proper decoding type object according to the type
 * specified in runtime
 * 
 * @author dinuka
 * 
 */
@Service
public class DecoderFactoryImpl implements DecoderFactory {

    @Override
    public Decoder getDecoder(EncoderDecoderType encoderDecoderType) {
	Decoder decoder = null;
	switch (encoderDecoderType) {
	case JSON:
	    decoder = new JSONDecoder();
	    break;
	case XML:
	    decoder = null;
	    break;
	}
	return decoder;
    }

}
