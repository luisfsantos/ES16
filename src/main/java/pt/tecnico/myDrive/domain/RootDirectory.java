package pt.tecnico.myDrive.domain;

import org.joda.time.DateTime;

import pt.tecnico.myDrive.exception.RootDirectoryCannotBeModified;

public class RootDirectory extends RootDirectory_Base {
    
    public RootDirectory(Manager manager, SuperUser superUser, DummyObject dummy) {
    	this.setParent(this);
    	this.setRootDirName("/");
		this.setOwner(superUser);
		this.setPermissions("rwxdr-x-");
		this.setId(superUser.getNextIdCounter());
		this.setLastModified(new DateTime());
    }
    
    @Override
	public void setName(String username){
		throw new RootDirectoryCannotBeModified();    			
	}
    
    @Override
    public String getAbsolutePath() {
    	return getName();
    }
    
}
