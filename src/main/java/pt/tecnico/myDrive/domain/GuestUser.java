package pt.tecnico.myDrive.domain;

import org.joda.time.DateTime;
import pt.tecnico.myDrive.exception.UserCannotBeModified;

public class GuestUser extends GuestUser_Base {
    
    public GuestUser(Manager manager, DummyObject dummy) {
        this.initGuestUser(manager);
    }
    
    @Override
    public String getPassword() {
    	return null;
    }
    
    @Override
	public boolean hasPermission(File file, Mask mask){
		return false;
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
	public boolean isValidUserSession(DateTime recent){
		return true;
	}
}
