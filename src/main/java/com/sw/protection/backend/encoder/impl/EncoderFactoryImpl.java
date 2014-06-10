package com.sw.protection.backend.encoder.impl;

import org.springframework.stereotype.Service;

import com.sw.protection.backend.config.EncoderDecoderType;
import com.sw.protection.backend.encoder.Encoder;
import com.sw.protection.backend.encoder.EncoderFactory;

/**
 * This class is provide the proper encoding type object according to the type
 * specified in runtime
 * 
 * @author dinuka
 * 
 */
@Service
public class EncoderFactoryImpl implements EncoderFactory {

	@Override
	public Encoder getEncoder(EncoderDecoderType encoderDecoderType) {
		Encoder encoder = null;
		switch (encoderDecoderType) {
		case JSON:
			encoder = new JSONEncoder();
			break;
		case XML:
			encoder = null;
			break;
		}
		return encoder;
	}

}
