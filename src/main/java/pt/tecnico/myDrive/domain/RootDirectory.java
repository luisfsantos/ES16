package pt.tecnico.myDrive.domain;

import org.joda.time.DateTime;

import pt.tecnico.myDrive.exception.RootDirectoryCannotBeModified;

public class RootDirectory extends RootDirectory_Base {
    
    public RootDirectory(Manager manager, SuperUser superUser, DummyObject dummy) {
    	this.initRootDirectory(this, superUser);
    }
    
    @Override
	public void setName(String username){
		throw new RootDirectoryCannotBeModified();    			
	}
    
    @Override
	public void setLastModified(DateTime dateTime){
		throw new RootDirectoryCannotBeModified();    			
	}
    
    @Override
	public void setPermissions(String permissions){
		throw new RootDirectoryCannotBeModified();    			
	}
    
    @Override
	public void setId(Integer id){
		throw new RootDirectoryCannotBeModified();    			
	}
    
    @Override
    public String getAbsolutePath() {
    	return getName();
    }
    
}
