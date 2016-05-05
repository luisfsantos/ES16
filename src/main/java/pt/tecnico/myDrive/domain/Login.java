package pt.tecnico.myDrive.domain;

import java.math.BigInteger;
import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;

import org.joda.time.DateTime;

import pt.tecnico.myDrive.exception.AccessDeniedToManipulateLoginException;
import pt.tecnico.myDrive.exception.InvalidEnvironmentVarNameException;
import pt.tecnico.myDrive.exception.InvalidEnvironmentVarValueException;
import pt.tecnico.myDrive.exception.InvalidUsernameOrPasswordException;

public class Login extends Login_Base {
    
	
    public Login(String username, String password) {
        this.validateAccount(username, password);
    	Manager.getInstance().removeInactiveLogins();
    	Long token = new BigInteger(64, new Random()).longValue();
    	while (Manager.getInstance().tokenAlreadyExist(token)){
    		token = new BigInteger(64, new Random()).longValue();
    	}
    	super.setToken(token);
    	super.setLastActivity(new DateTime());
    	super.setManager(Manager.getInstance());
    	super.setCurrentUser(Manager.getInstance().fetchUser(username, password));
    	this.setCurrentDir(Manager.getInstance().fetchUser(username, password).getHome());
    }
    

	private void validateAccount(String username, String password) {
    	User user = Manager.getInstance().fetchUser(username, password);
    	if ( user == null ) {
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
    
    @Override
	public void setLastActivity(DateTime lastActivity) {
    	if (lastActivity.isBefore(getLastActivity())) {
    		super.setLastActivity(lastActivity);
    	} else {
    		throw new AccessDeniedToManipulateLoginException();
    	}
	}


	public void addEnvironmentVariable(String name, String value) {
		if (name == null) {
			throw new InvalidEnvironmentVarNameException("null");
		}
		if (value == null) {
			throw new InvalidEnvironmentVarValueException("null");
		}
		EnvironmentVariable environmentVariable = new EnvironmentVariable(this, name, value);
		super.addEnvironmentVariable(environmentVariable);
	}
    
    void refreshLoginActivity() {
    	super.setLastActivity(new DateTime());
    }
    
    public void remove(){
		super.setManager(null);
		super.setCurrentUser(null);
		super.setCurrentDir(null);
		deleteDomainObject();
	}
    
}
