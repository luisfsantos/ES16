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
    protected boolean allHasPermission(File file, Mask mask){
		switch(mask){
			case READ:
				return mask.getValue() == file.getPermissions().charAt(4);
			case EXEC:
				return mask.getValue() == file.getPermissions().charAt(6);
			default:
				return false;
		}
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

	@Override
	public boolean canLogin(String password){
		return super.validatePassword(password);
	}
}
