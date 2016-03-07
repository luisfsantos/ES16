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
	public void setPermissions(String permission){
		if(permission.length() != 8) return ;
		Mask mask[] = Mask.values();
		//String mask = "rwxdrwxd";
		for(int i = 0; i < 8; i++)
			//if(permission.charAt(i) != mask.charAt(i) && permission.charAt(i) != '-')
			if(permission.charAt(i) != mask[8 % i].getValue() && permission.charAt(i) != '-')
				return ;

		super.setPermissions(permission);
	}

	public void setPermissions(){
		super.setPermissions(this.getUser().getUmask());
	}
}
