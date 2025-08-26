package com.lms.customid;

import org.hibernate.engine.spi.SharedSessionContractImplementor; 
import org.hibernate.id.IdentifierGenerator;

import com.lms.service.SessionManager;

import java.io.Serializable;

public class CustomIdUserinfo implements IdentifierGenerator {


	private static final long serialVersionUID = 1L;
	    private static final int DESIRED_LENGTH = 6;
   
    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) {
        synchronized (CustomIdUserinfo.class) {
            
        	  Long generatedId = SessionManager.sessionId;
        	
            String value =  String.format("%0" + DESIRED_LENGTH + "d", generatedId);
            return Long.parseLong(value);
        }
    }
}
