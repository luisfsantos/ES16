package pt.tecnico.myDrive.domain;

import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import pt.ist.fenixframework.FenixFramework;
import pt.tecnico.myDrive.exception.EmptyUsernameException;
import pt.tecnico.myDrive.exception.InvalidUsernameException;
import pt.tecnico.myDrive.exception.UserAlreadyExistsException;
import pt.tecnico.myDrive.exception.CannotSetRootUsernameException;

public class SuperUser extends SuperUser_Base {
	
	static final Logger log = LogManager.getRootLogger();
    
    public SuperUser(Manager manager) {
    	this.setManager(manager);
		this.setRootUsername("root");
		this.setPassword("***");
		this.setName("Super User");
		this.setUmask("rwxdr-x-");
    }
    
    @Override
	public void setUsername(String username){
		throw new CannotSetRootUsernameException();    			
	}
	
}
