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
		this.initUser(manager, username, username, username, "rwxd----");
	}
	
	
	public User(Manager manager, String username, String password, String name, String umask){
		this.initUser(manager, username, password, name, umask);
	}

	public User(Manager manager, Element userNode) throws UnsupportedEncodingException {
		String username = userNode.getAttributeValue("username");
		String password = userNode.getChildText("password");
		String name = userNode.getChildText("name");
		String mask = userNode.getChildText("mask");
		String home = userNode.getChildText("home");

		password = (password != null) ? new String(password.getBytes("UTF-8")) : username;
		name = (name != null) ? new String(name.getBytes("UTF-8")) : username;
		mask = (mask != null) ? new String(mask.getBytes("UTF-8")) : "rwxd----";

		setUsername(username);
		setManager(manager);
		setPassword(new String(password.getBytes("UTF-8")));
		setName(new String(name.getBytes("UTF-8")));
		setUmask(new String(mask.getBytes("UTF-8")));

		if (home != null) {
			home = new String(home.getBytes("UTF-8"));
			int last = home.lastIndexOf('/');
			String parentPath = home.substring(0, last);
			String dirName = home.substring(last + 1);
			Directory parentDir = manager.getRootDirectory().createPath(manager.getSuperUser(), parentPath);
			Directory homeDir = new Directory(dirName, this, parentDir);
			setHome(homeDir);
		} else {
			setHome(new Directory(username, this, manager.getHomeDirectory()));
		}
	}

	protected void initUser(Manager manager, String username, String password, String name, String umask) {
		this.setUsername(username);
		this.setManager(manager);
		this.setUmask(umask);
		this.setHome(new Directory(username, this, this.getManager().getHomeDirectory()));
		this.setPassword(password);
		this.setName(name);
	}

	protected void setRootUsername(String username){
		super.setUsername(username);
	}


	@Override
	public void setUsername(String username) throws UserAlreadyExistsException, EmptyUsernameException, InvalidUsernameException{
		
        boolean isAlphanumeric = Pattern.matches("^[a-zA-Z0-9]*$", username);

		if (Manager.getInstance().hasUser(username)) throw new UserAlreadyExistsException(username);
		if (username.isEmpty()) throw new EmptyUsernameException();
        if (!isAlphanumeric) throw new InvalidUsernameException(username);

        super.setUsername(username);
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
}
