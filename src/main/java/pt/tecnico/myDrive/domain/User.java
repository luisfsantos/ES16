package pt.tecnico.myDrive.domain;

import org.jdom2.Element;

import pt.tecnico.myDrive.exception.ImportDocumentException;
import pt.tecnico.myDrive.exception.InvalidCharPermissionException;
import pt.tecnico.myDrive.exception.InvalidPathException;
import pt.tecnico.myDrive.exception.WrongSizePermissionException;


public class User extends User_Base {

    public User() {
        super();
    }
	
	public User(String username, String password, String name, String umask, Directory home) {
		super();
		this.initUser(username, password, name, umask, home);
	}

	private void initUser(String username, String password, String name, String umask, Directory home) {
		
		this.setHome(home);
		this.setUsername(username);
		this.setPassword(password);
		this.setName(name);
		this.setUmask(umask);
	}

	@Override
	public void setManager(Manager manager) {
		if (manager == null) {	// to remove user
			super.setManager(null);
			return;
		}
		manager.addUser(this);
	}

	/* C


	@Override
	public void setUmask(String permission) throws WrongSizePermissionException, InvalidCharPermissionException{
		if(permission.length() != 8) throw new WrongSizePermissionException(permission.length());
		Mask mask[] = Mask.values();
		//String mask = "rwxdrwxd";
		for(int i = 0; i < 8; i++)
			//if(permission.charAt(i) != mask.charAt(i) && permission.charAt(i) != '-')
			if(permission.charAt(i) != mask[4 % i].getValue() && permission.charAt(i) != '-')
				throw new InvalidCharPermissionException(permission.charAt(i), i);
		super.setUmask(permission);
	}

	public boolean hasPermission(File file, Mask mask){
		if(this.equals(file.getUser())) return ownerHasPermission(file, mask);
		else { return allHasPermission(file, mask);}
	}

	public boolean ownerHasPermission(File file, Mask mask){
		switch(mask){
			case READ:
				return mask.getValue() == file.getPermissions().charAt(0);
			case WRITE:
				return mask.getValue() == file.getPermissions().charAt(1);
			case EXEC:
				return mask.getValue() == file.getPermissions().charAt(2);
			case DELETE:
				return mask.getValue() == file.getPermissions().charAt(3);
			default:
				return false;
		}
	}

	public boolean allHasPermission(File file, Mask mask){
		switch(mask){
			case READ:
				return mask.getValue() == file.getPermissions().charAt(4);
			case WRITE:
				return mask.getValue() == file.getPermissions().charAt(5);
			case EXEC:
				return mask.getValue() == file.getPermissions().charAt(6);
			case DELETE:
				return mask.getValue() == file.getPermissions().charAt(7);
			default:
				return false;
		}
	}

	public boolean equals(User user){
		return this.getUsername() == user.getUsername();
	}
	C */
	
	public Element xmlExport() {
		Element element = new Element("user");
		element.setAttribute("username", getUsername());
		
		Element passwordElement = new Element("password");
		passwordElement.setText(getPassword());
		element.addContent(passwordElement);

		Element nameElement = new Element("name");
		nameElement.setText(getName());
		element.addContent(nameElement);

		Element homeElement = new Element("home");
		homeElement.setText(getHome().getAbsolutePath());
		element.addContent(homeElement);

		Element maskElement = new Element("mask");
		maskElement.setText(getUmask());
		element.addContent(maskElement);

		return element;
	}

	public void xmlImport(Element userElement) {
		String password = userElement.getChildText("password");
		String name = userElement.getChildText("name");
		String mask = userElement.getChildText("mask");
		String home = userElement.getChildText("home");

		if (password != null) setPassword(password);
		if (name != null) setName(name);
		if (mask != null) setUmask(mask);
		if (home != null && !home.equals("/home/" + getUsername())) {
			int last = home.lastIndexOf('/');
			String parentPath = home.substring(0, last);
			String dirName = home.substring(last + 1);
			Directory parentDir = super.getManager().createAbsolutePath(parentPath);

			Directory homeDir = parentDir.createDirectory(dirName, super.getManager(), this);
			//TODO Delete previous user home dir from database
			super.setHome(homeDir);
		}
	}
}
