package pt.tecnico.myDrive.domain;

import org.jdom2.Element;

import pt.tecnico.myDrive.exception.InvalidCharPermissionException;
import pt.tecnico.myDrive.exception.WrongSizePermissionException;

import java.io.UnsupportedEncodingException;

import org.jdom2.DataConversionException;




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
	
	/*
			if (username == null){
			RAISE EXCEPTION
		}
		else if (password == null){
            password = username;
        }
        else if (name == null){
       	    name = username;
        }
        else if(mask == null){
            mask = "rwxd----";
        }
	*/
	
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

	public void xmlImport(Element userNode){
			//manager nao preciso, certo??
	try {
			setUsername(new String(userNode.getAttribute("username").getValue().getBytes("UTF-8")));
	} catch (UnsupportedEncodingException e){}//DO SOMETHING

	try {
			setPassword(new String(userNode.getChild("password").getValue().getBytes("UTF-8")));
	} catch (UnsupportedEncodingException e){}

	try {
			setName(new String(userNode.getChild("name").getValue().getBytes("UTF-8")));
	} catch (UnsupportedEncodingException e){}
	try {
			setUmask(new String(userNode.getChild("mask").getValue().getBytes("UTF-8")));
	} catch (UnsupportedEncodingException e){}
	try {
			addFile(new Directory(userNode.getChild("home").getValue().getBytes("UTF-8")));  //ASSUMO QUE DIRECTORY RECEBE STRING
	} catch (UnsupportedEncodingException e){}
	
	
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
		if (home == null || !home.equals("/home/" + getUsername())) {
			//TODO create Missing Files
		}
	}
}
