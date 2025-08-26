package com.lms.customid;

import org.hibernate.engine.spi.SharedSessionContractImplementor; 
import org.hibernate.id.IdentifierGenerator;

import com.lms.service.SessionManager;

import java.io.Serializable;

public class CustomIdAgent implements IdentifierGenerator {


	private static final long serialVersionUID = 1L;
	private static final long INITIAL_VALUE = 200001; // Initial value for the ID
    private static final int DESIRED_LENGTH = 6; // Desired length of the ID
    private long nextValue = INITIAL_VALUE;
  
    
    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) {
        synchronized (CustomIdAgent.class) {
            
        	  Long generatedId = nextValue+SessionManager.userCount++;
        	
            String value =  String.format("%0" + DESIRED_LENGTH + "d", generatedId);
            return Long.parseLong(value);
        }
    }
}
