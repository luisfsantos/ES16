package pt.tecnico.myDrive.domain;

import pt.tecnico.myDrive.exception.RootDirectoryCannotBeModified;

public class RootDirectory extends RootDirectory_Base {
    
    public RootDirectory(Manager manager, SuperUser superUser, DummyObject dummy) {
    	this.initRootDirectory(superUser);
    }
    
    @Override
	public void setName(String username){
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
    public Directory getParent(){
    	return this;
    }
    
    @Override
    public String getAbsolutePath() {
    	return getName();
    }
    
}
