package pt.tecnico.myDrive.domain;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pt.tecnico.myDrive.exception.SuperUserCannotBeModified;

public class SuperUser extends SuperUser_Base {

	static final Logger log = LogManager.getRootLogger();

    public SuperUser(Manager manager, DummyObject dummy) {
    	this.initSuperUser(manager);
    }

    @Override
	public void setUsername(String username){
		throw new SuperUserCannotBeModified();
	}
    
    @Override
	public void setPassword(String password){
		throw new SuperUserCannotBeModified();
	}
    
    @Override
	public void setName(String name){
		throw new SuperUserCannotBeModified();
	}
    
    @Override
	public void setUmask(String umask){
		throw new SuperUserCannotBeModified();
	}
    
    @Override
	public void setManager(Manager manager){
		throw new SuperUserCannotBeModified();
	}
    

	public boolean hasPermission(File file, Mask mask){
		return true;
	}
}
