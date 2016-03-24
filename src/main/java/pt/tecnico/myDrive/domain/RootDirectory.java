package pt.tecnico.myDrive.domain;

import org.joda.time.DateTime;

import pt.tecnico.myDrive.exception.CannotSetRootDirNameException;

public class RootDirectory extends RootDirectory_Base {
    
    public RootDirectory(Manager manager, SuperUser superUser) {
    	this.setParent(this);
    	this.setRootDirName("/");
		this.setOwner(superUser);
		this.setPermissions("rwxdr-x-");
		this.setId(manager.getNextIdCounter());
		this.setLastModified(new DateTime());
		
    }
    
    @Override
	public void setName(String username){
		throw new CannotSetRootDirNameException();    			
	}
    
}
