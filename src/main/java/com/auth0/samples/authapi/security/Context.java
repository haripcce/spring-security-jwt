package com.auth0.samples.authapi.security;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

@Service
public class Context implements ApplicationContextAware {

	private static ApplicationContext applicationContextInstance;
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		applicationContextInstance = applicationContext;
	}

	public static ApplicationContext getContext(){
		return applicationContextInstance;
	}
}
