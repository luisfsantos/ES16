package pt.tecnico.myDrive.domain;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pt.tecnico.myDrive.exception.SuperUserCannotBeModified;

public class SuperUser extends SuperUser_Base {

	static final Logger log = LogManager.getRootLogger();

    public SuperUser(Manager manager, DummyObject dummy) {
    	this.setManager(manager);
		this.setRootUsername("root");
		this.setPassword("***");
		this.setName("Super User");
		this.setUmask("rwxdr-x-");
    }

    @Override
	public void setUsername(String username){
		throw new SuperUserCannotBeModified();
	}

	public boolean hasPermission(File file, Mask mask){
		return true;
	}
}
