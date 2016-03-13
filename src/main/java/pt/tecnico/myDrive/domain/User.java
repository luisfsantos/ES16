package pt.tecnico.myDrive.domain;

import org.jdom2.Element;

import pt.tecnico.myDrive.exception.*;

import java.util.regex.Pattern;
import java.io.UnsupportedEncodingException;

public class User extends User_Base {

    public User() {
        super();
    }

	public User(Manager manager, Element userNode) {
		String username = userNode.getAttributeValue("username");

		if (username == null) {
			throw new ImportDocumentException("Missing username value");
		}

		try {
			username = new String(username.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			throw new ImportDocumentException("UnsupportedEncoding");
		}

		initUser(username, username, username, "rwxd----", null);
		setManager(manager);
		xmlImport(userNode);
	}
	
	public User(String username, String password, String name, String umask, Directory home){
		super();
		this.initUser(username, password, name, umask, home);
	}

	

	private void initUser(String username, String password, String name, String umask, Directory home) {
		this.validateUsername(username);
		this.setHome(home);
		this.setUsername(username);
		this.setPassword(password);
		this.setName(name);
		this.setUmask(umask);
	}
	

	public void validateUsername(String username) throws UserAlreadyExistsException, EmptyUsernameException, InvalidUsernameException {
		if (Manager.getInstance().hasUser(username)) {
			throw new UserAlreadyExistsException(username);
	    }
		
        boolean isAlphanumeric = Pattern.matches("^[a-zA-Z0-9]*$", username);    

        if (username.isEmpty()) throw new EmptyUsernameException();
        else if (!isAlphanumeric) throw new InvalidUsernameException(username);
		
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
	@Override
	public void setUmask(String permission) throws WrongSizePermissionException, InvalidCharPermissionException{
		if(permission.length() != 8) throw new WrongSizePermissionException(permission.length());
		Mask mask[] = Mask.values();
		if (permission.charAt(0) != mask[0].getValue() && permission.charAt(0) != '-')
			throw new InvalidCharPermissionException(permission.charAt(0), 0);
		for(int i = 1; i < 8; i++) {
			if (permission.charAt(i) != mask[4 % i].getValue() && permission.charAt(i) != '-')
				throw new InvalidCharPermissionException(permission.charAt(i), i);
		}
		super.setUmask(permission);
	}
*/
	public boolean hasPermission(File file, Mask mask){
		if(this.getUsername() == "root") return true;
		if(this.equals(file.getOwner())) return ownerHasPermission(file, mask);
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

		try {
			if (password != null) setPassword(new String(password.getBytes("UTF-8")));
			if (name != null) setName(new String(name.getBytes("UTF-8")));
			if (mask != null) setUmask(new String(mask.getBytes("UTF-8")));
			if (home != null && !home.equals("/home/" + getUsername())) {
				home = new String(home.getBytes("UTF-8"));
				int last = home.lastIndexOf('/');
				String parentPath = home.substring(0, last);
				String dirName = home.substring(last + 1);
				Directory parentDir = super.getManager().createAbsolutePath(parentPath);
				Directory homeDir = parentDir.createDirectory(dirName, super.getManager(), this);
				super.setHome(homeDir);
			} else {
				Directory homeDir = getManager().getHomeDirectory().createDirectory(getUsername(), getManager(), this);
				super.setHome(homeDir);
			}
		} catch (UnsupportedEncodingException e) {
			throw new ImportDocumentException("UnsupportedEncodingException");
		}
	}
}
