package com.emerson.bpm.engine;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.emerson.bpm.api.MessageServiceProvider;

public class MessageServiceProviderImpl implements MessageServiceProvider {

	Properties props = new Properties();
	public MessageServiceProviderImpl() {
			
		try {
				InputStream is = Thread.currentThread().getContextClassLoader()
					.getResourceAsStream("I18N/Messages.properties");
						
				props.load(is);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	@Override
	public String get(String key) {
		return props.getProperty(key);
	}

}
