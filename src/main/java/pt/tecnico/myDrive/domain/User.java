package pt.tecnico.myDrive.domain;

import org.jdom2.Element;
import pt.tecnico.myDrive.exception.*;

import java.io.UnsupportedEncodingException;
import java.util.regex.Pattern;

public class User extends User_Base {
	
	protected User() {
		super();
	}
	
	
	public User(Manager manager, String username){
		this.validateUsername(username);
		this.initUser(manager, username, username, username, "rwxd----");
	}
	
	
	public User(Manager manager, String username, String password, String name, String umask){
		this.validateUsername(username);
		this.initUser(manager, username, password, name, umask);
	}

	public User(Manager manager, Element userNode) throws UnsupportedEncodingException {
		String username = userNode.getAttributeValue("username");

		if (username == null) {
			throw new ImportDocumentException("Missing username value");
		}

		username = new String(username.getBytes("UTF-8"));

		initUser(manager, username, username, username, "rwxd----");
		setManager(manager);
		xmlImport(userNode);
	}
	
	
	protected void initUser(Manager manager, String username, String password, String name, String umask) {
		this.setManager(manager);
		this.setUmask(umask);
		this.setHome(new Directory(username, this, this.getManager().getHomeDirectory()));
		this.setUsername(username);
		this.setPassword(password);
		this.setName(name);
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

	@Override
	public void setUmask(String permission) {
		boolean isPermissionString =
				Pattern.matches("[r-][w-][x-][d-][r-][w-][x-][d-]", permission);
		if(!isPermissionString) {
			throw new InvalidPermissionException(permission);
		}
		super.setUmask(permission);
	}

	public boolean hasPermission(File file, Mask mask){
		if(this.getUsername().equals("root")) return true;
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
		return this.getUsername().equals(user.getUsername());
	}

	public Element xmlExport() {
		Element userElement = new Element("user");
		userElement.setAttribute("username", getUsername());
		
		Element passwordElement = new Element("password");
		passwordElement.setText(getPassword());
		userElement.addContent(passwordElement);

		Element nameElement = new Element("name");
		nameElement.setText(getName());
		userElement.addContent(nameElement);

		Element homeElement = new Element("home");
		homeElement.setText(getHome().getAbsolutePath());
		userElement.addContent(homeElement);

		Element maskElement = new Element("mask");
		maskElement.setText(getUmask());
		userElement.addContent(maskElement);

		return userElement;
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
				Directory homeDir = new Directory(dirName, this, parentDir);
				super.setHome(homeDir);
			} else {
				Directory homeDir = new Directory(getUsername(), this, getManager().getHomeDirectory());
				super.setHome(homeDir);
			}
			
		} catch (UnsupportedEncodingException e) {
			throw new ImportDocumentException("UnsupportedEncodingException");
		}
	}
}
