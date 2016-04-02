package pt.tecnico.myDrive.domain;

import java.math.BigInteger;
import java.util.Random;

import org.joda.time.DateTime;

public class Login extends Login_Base {
    
    public Login(String username, String password) {
        // TODO validade account
    	// TODO remove inactive sessions
    	
    	long token = new BigInteger(64, new Random()).longValue();
    	// TODO verify if token already exists
    	this.setToken(token);
    	this.setLastActivity(new DateTime());
    	this.setManager(Manager.getInstance());
    	this.setCurrentUser(Manager.getInstance().getUserByUsername(username));
    	this.setCurrentDir(Manager.getInstance().getUserByUsername(username).getHome());
    }
    
}
