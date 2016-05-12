package pt.tecnico.myDrive.domain;

import org.jdom2.Element;

import pt.tecnico.myDrive.exception.*;

import pt.tecnico.myDrive.exception.ImportDocumentException;

import org.joda.time.DateTime;
import java.io.UnsupportedEncodingException;
import java.util.Set;
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
		if (username == null) throw new ImportDocumentException("Missing username");

		username = new String (username.getBytes());
		String password = userNode.getChildText("password");
		String name = userNode.getChildText("name");
		String mask = userNode.getChildText("mask");
		String home = userNode.getChildText("home");

		password = (password != null) ? new String(password.getBytes("UTF-8")) : username;
		name = (name != null) ? new String(name.getBytes("UTF-8")) : username;
		mask = (mask != null) ? new String(mask.getBytes("UTF-8")) : "rwxd----";

		initUserXMLImport(manager, username, password, name, mask, home);
	}

	private void initUserXMLImport(Manager manager, String username, String password, String name, String umask,
								   String home) throws UnsupportedEncodingException {
		this.setUsername(username);
		this.setManager(manager);
		this.setUmask(umask);

		SuperUser superUser = this.getManager().getSuperUser();
		String homeName = username;
		Directory homeParent = (Directory) manager.getRootDirectory().lookup("/home", superUser);
		if (home != null) {
			home = new String(home.getBytes("UTF-8"));
			int last = home.lastIndexOf('/');
			String parentPath = home.substring(0, last);
			homeName = home.substring(last + 1);
			homeParent = manager.getRootDirectory().createPath(superUser, parentPath);
		}
		Directory userHome = new Directory(homeName, superUser, homeParent);
		userHome.setOwner(this);
		userHome.setPermissions(this.getUmask());

		this.setHome(userHome);
		this.setPassword(password);
		this.setName(name);
	}

	protected void initUser(Manager manager, String username, String password, String name, String umask) {
		if(password.length() < 8) {
			throw new PasswordTooSmallException();
		}

		this.setUsername(username);
		this.setManager(manager);
		this.setUmask(umask);


		SuperUser superUser = this.getManager().getSuperUser();
		Directory home = (Directory) Manager.getInstance().getRootDirectory().lookup("/home", superUser);
		Directory userHome = new Directory(username, superUser, home);
		userHome.setOwner(this);
		userHome.setPermissions(this.getUmask());

		this.setHome(userHome);
		this.setPassword(password);
		this.setName(name);
	}

	protected void initSuperUser( Manager manager){
		super.setUsername("root");
    	super.setManager(manager);
		super.setPassword("***");
		super.setName("Super User");
		super.setUmask("rwxdr-x-");
	}
	
	protected void initGuestUser( Manager manager){
		super.setUsername("nobody");
    	super.setManager(manager);
		super.setPassword("");
		super.setName("Guest");
		super.setUmask("rwxdr-x-");
		
		SuperUser superUser = this.getManager().getSuperUser();
		Directory home = (Directory) manager.getRootDirectory().lookup("/home", superUser);
		Directory userHome = new Directory("nobody", superUser, home);
		userHome.setOwner(this);
		userHome.setPermissions(this.getUmask());
		this.setHome(userHome);
	}


	@Override
	public void setUsername(String username) throws UserAlreadyExistsException, EmptyUsernameException, InvalidUsernameException{
		
        boolean isAlphanumeric = Pattern.matches("^[a-zA-Z0-9]*$", username);

		if (username.length() < 3){
			throw new InvalidUsernameException(username);
		}

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
	
	

	@Override
	public void setHome(Directory home) {
		if (home == null) {
			throw new InvalidHomeDirectoryException("null");
		} else {
			if (this.getUsername().equals(home.getOwnerUsername())) {
				super.setHome(home);
			} else {
				throw new InvalidHomeDirectoryException(home.getName());
			}
		}
	}

	@Override
	public void addLogin(Login login){
		throw new AccessDeniedToManipulateLoginException();
	}
	
	@Override
	public String getPassword() {
		throw new AccessDeniedToGetPasswordException();
	}

	protected String getUserPassword() {return super.getPassword();}

	@Override
	public Set <Login> getLoginSet(){
		throw new AccessDeniedToManipulateLoginException();
	}
	
	
	public boolean validatePassword(String password){
		return super.getPassword().equals(password);
	}
	
	public boolean canLogin(String password) {
		if (password.length() < 8) {
			throw new PasswordTooSmallException();
		}
		return validatePassword(password);
	}

	public int getNextIdCounter() {
		int currCounter = this.getManager().getIdCounter();
    	this.getManager().setIdCounter(currCounter+1);
    	return currCounter;
	}


	public boolean hasPermission(File file, Mask mask){
		if(this.getUsername().equals(file.getOwnerUsername())) return ownerHasPermission(file, mask);
		else { return allHasPermission(file, mask);}
	}

	private boolean ownerHasPermission(File file, Mask mask){
		switch(mask){
			case READ:
				return mask.getValue() == file.getPermissions().charAt(0);
			case WRITE:
				return 'w' == file.getPermissions().charAt(1);
			case EXEC:
				return mask.getValue() == file.getPermissions().charAt(2);
			case DELETE:
				return mask.getValue() == file.getPermissions().charAt(3);
			default:
				return false;
		}
	}

	private boolean allHasPermission(File file, Mask mask){
		switch(mask){
			case READ:
				return mask.getValue() == file.getPermissions().charAt(4);
			case WRITE:
				return 'w' == file.getPermissions().charAt(5);
			case EXEC:
				return mask.getValue() == file.getPermissions().charAt(6);
			case DELETE:
				return mask.getValue() == file.getPermissions().charAt(7);
			default:
				return false;
		}
	}

	public boolean equals(String username){
		return this.getUsername().equals(username);
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
		
		if (getHome() != null) {
			Element homeElement = new Element("home");
			homeElement.setText(getHome().getAbsolutePath());
			userElement.addContent(homeElement);
		}
		
		Element maskElement = new Element("mask");
		maskElement.setText(getUmask());
		userElement.addContent(maskElement);

		return userElement;
	}

	public boolean isValidUserSession(DateTime recent){
		DateTime now = new DateTime();
		if (recent.isBefore(now.minusHours(2))){
			return false;
		}
		return true;
	}
}
