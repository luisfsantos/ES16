package pt.tecnico.myDrive.domain;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;
import pt.tecnico.myDrive.exception.PasswordTooSmallException;
import pt.tecnico.myDrive.exception.UserCannotBeModified;

public class SuperUser extends SuperUser_Base {

	static final Logger log = LogManager.getRootLogger();

    public SuperUser(Manager manager, DummyObject dummy) {
    	this.initSuperUser(manager);
    }

    @Override
	public void setUsername(String username){
		throw new UserCannotBeModified(this.getName());
	}
    
    @Override
	public void setPassword(String password){
		throw new UserCannotBeModified(this.getName());
	}
    
    @Override
	public void setName(String name){
		throw new UserCannotBeModified(this.getName());
	}
    
    @Override
	public void setUmask(String umask){
		throw new UserCannotBeModified(this.getName());
	}
    
    @Override
	public void setManager(Manager manager){
		throw new UserCannotBeModified(this.getName());
	}
    
    @Override
	public boolean hasPermission(File file, Mask mask){
		return true;
	}

	@Override
	public boolean isValidUserSession(DateTime recent){
		DateTime now = new DateTime();
		if (recent.isBefore(now.minusMinutes(10))){
			return false;
		}
		return true;
	}

	@Override
	public boolean validatePassword(String password){
		return getUserPassword().equals(password);
	}
}
