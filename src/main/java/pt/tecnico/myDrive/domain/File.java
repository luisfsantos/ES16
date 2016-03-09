package pt.tecnico.myDrive.domain;

import org.jdom2.Element;
import org.joda.time.DateTime;

public class File extends File_Base { //TODO Change class to abstract ???

	public File() {
		super();
	}

	public File(int id, String name, User username, String permissions, Directory parent) {
		super();
		this.setId(id);
		this.setName(name);
		this.setUser(username);
		this.setPermissions(permissions);
		this.setParent(parent);
		this.setLastModified(new DateTime());
	}
	
	//protected void initFile (String name) { }

	@Override
	public void setPermissions(String permission) throws WrongSizePermissionException, InvalidCharPermissionException {
		if(permission.length() != 8) throw new WrongSizePermissionException(permisson.length());
		Mask mask[] = Mask.values();
		//String mask = "rwxdrwxd";
		for(int i = 0; i < 8; i++){
			//if(permission.charAt(i) != mask.charAt(i) && permission.charAt(i) != '-')
			if(permission.charAt(i) != mask[4 % i].getValue() && permission.charAt(i) != '-')
				throw new InvalidCharPermissionException(permission.charAt(i), i);
		}
	}
	
	public void setPermissions(){
		super.setPermissions(this.getUser().getUmask());
	}

	public File lookup(String path) {}
	
	public String getAbsolutePath() {
		if (this == getParent())
				return "";
		else return getParent().getAbsolutePath() + "/" + getName();
	}

	public Element xmlExport() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
