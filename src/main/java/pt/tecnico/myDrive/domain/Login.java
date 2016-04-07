package pt.tecnico.myDrive.domain;

import java.math.BigInteger;
import java.util.Random;

import org.joda.time.DateTime;

import pt.tecnico.myDrive.exception.AccessDeniedToGetTokenException;
import pt.tecnico.myDrive.exception.InvalidUsernameOrPasswordException;

public class Login extends Login_Base {
    
	
    public Login(String username, String password) {
        this.validateAccount(username, password);
    	// TODO remove inactive sessions
    	Long token = new BigInteger(64, new Random()).longValue();
    	// TODO verify if token already exists
    	this.setToken(token);
    	this.setLastActivity(new DateTime());
    	this.setManager(Manager.getInstance());
    	this.setCurrentUser(Manager.getInstance().getUserByUsername(username));
    	this.setCurrentDir(Manager.getInstance().getUserByUsername(username).getHome());
    }
    
    private void validateAccount(String username, String password) {
    	User user = Manager.getInstance().getUserByUsername(username);
    	if ( user == null || !user.validatePassword(password)) {
    		throw new InvalidUsernameOrPasswordException();
    	}
    }
    
    
    @Override 
    public Long getToken() {
		//TODO
    	//throw new AccessDeniedToGetTokenException();
		return 1L;
    }
    
    public boolean validateToken(Long token){
    	return super.getToken().equals(token);
    }
    
}
