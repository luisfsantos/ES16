package pt.tecnico.myDrive.domain;

import org.joda.time.DateTime;

public class File extends File_Base {

    public File() {
        super();
    }

    /*
    public File(int id, String name, User username, String permissions, Directory parent) {
        super();
        this.setId(id);
        this.setName(name);
        this.setUser(username);
        this.setPermissions(permissions);
        this.setParent(parent);
        this.setLastModified(new DateTime());
    }
    
    protected void initFile (String name) {
    	
    }
    */
    @Override
    public void setPermissions(String perm){


        super.setPermissions(perm);
    }

    public void setPermissions(){
        super.setPermissions(this.getUser().getUmask());
    }
}
