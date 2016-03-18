package pt.tecnico.myDrive.domain;

import org.joda.time.DateTime;

public class RootDirectory extends RootDirectory_Base {
    
    public RootDirectory(Manager manager, SuperUser superUser) {
		this.setOwner(superUser);
		this.setName("/");
		this.setPermissions("rwxdr-x-");
		this.setId(manager.getNextIdCounter());
		this.setLastModified(new DateTime());
		this.setParent(this);
    }
}
