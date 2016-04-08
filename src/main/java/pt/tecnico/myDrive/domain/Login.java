package pt.tecnico.myDrive.domain;

import java.math.BigInteger;
import java.util.Random;

import org.joda.time.DateTime;

import pt.tecnico.myDrive.exception.AccessDeniedToManipulateLoginException;
import pt.tecnico.myDrive.exception.InvalidUsernameOrPasswordException;

public class Login extends Login_Base {
    
	
    public Login(String username, String password) {
        this.validateAccount(username, password);
    	// TODO remove inactive sessions
    	Long token = new BigInteger(64, new Random()).longValue();
    	// TODO verify if token already exists
    	super.setToken(token);
    	this.setLastActivity(new DateTime());
    	super.setManager(Manager.getInstance());
    	super.setCurrentUser(Manager.getInstance().getUserByUsername(username));
    	this.setCurrentDir(Manager.getInstance().getUserByUsername(username).getHome());
    }
    
    private void validateAccount(String username, String password) {
    	User user = Manager.getInstance().getUserByUsername(username);
    	if ( user == null || !user.validatePassword(password)) {
    		throw new InvalidUsernameOrPasswordException();
    	}
    }
    
    
    public boolean validateToken(Long token){
    	return super.getToken().equals(token);
    }
    
    @Override
    public void setToken(Long token){
    	throw new AccessDeniedToManipulateLoginException();
    }
    
    @Override
    public void setManager(Manager manager){
    	throw new AccessDeniedToManipulateLoginException();
    }
    
    @Override
    public void setCurrentUser(User user){
    	throw new AccessDeniedToManipulateLoginException();
    }
    
}
