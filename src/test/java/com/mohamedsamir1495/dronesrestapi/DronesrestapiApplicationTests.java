package com.mohamedsamir1495.dronesrestapi;

import static org.junit.Assert.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import com.mohamedsamir1495.dronesrestapi.config.SpringIntegrationTest;

class DronesrestapiApplicationTests extends SpringIntegrationTest{
	
	@Autowired
	ApplicationContext context;

	@Test
	void contextLoads() {
		assertNotNull(context);
	}

}
