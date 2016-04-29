package pt.tecnico.myDrive.domain;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jdom2.Document;
import org.jdom2.Element;
import org.joda.time.DateTime;

import pt.ist.fenixframework.FenixFramework;
import pt.tecnico.myDrive.exception.AccessDeniedException;
import pt.tecnico.myDrive.exception.AccessDeniedToManipulateLoginException;
import pt.tecnico.myDrive.exception.InvalidIdCounter;
import java.io.UnsupportedEncodingException;
import java.util.Set;




public class Manager extends Manager_Base {
	static final Logger log = LogManager.getRootLogger();
	
	// manager use Singleton design pattern
    public static Manager getInstance() {
    	Manager manager = FenixFramework.getDomainRoot().getManager();
    	if (manager != null) {
    		return manager;
    	}
    	return new Manager();
    } 
    
    private Manager() {

    	this.setRoot(FenixFramework.getDomainRoot());
        super.setIdCounter(0);
        
        DummyObject dummy = new DummyObject();
        SuperUser superUser = new SuperUser(this, dummy);
       
        this.setSuperUser(superUser);
        RootDirectory rootDirectory = new RootDirectory(this, superUser, dummy);
        this.setRootDirectory(rootDirectory);
        Directory homeDirectory = new Directory("home", superUser, rootDirectory);
        Directory rootHome = new Directory("root", superUser, homeDirectory);
        superUser.setHome(rootHome);
        
        GuestUser guestUser = new GuestUser(this, dummy);
        this.setGuestUser(guestUser);
    }
	
    
    public Login getLoginByToken(long token) {
		for (Login login : super.getLoginSet()) {

			if (login.validateToken(token)) {
				if (login.getCurrentUser().isValidUserSession(login.getLastActivity())) {
					login.refreshLoginActivity();
					return login;
				} else {
					log.warn("Trying to access with invalid token HEYHEY");
					return null;
				}
			}
		}
		log.warn("Trying to access with invalid token ");
		return null;
	}
    
    public void removeInactiveLogins() {
    	DateTime now = new DateTime();
    	for (Login login: super.getLoginSet()) {
			if (login.getLastActivity().isBefore(now.minusHours(2))){
				login.remove();
			} 
    	}
    }
    
    public boolean tokenAlreadyExist(Long token){
    	for (Login login: super.getLoginSet()){
    		if (login.validateToken(token)){
    			return true;
    		}
    	}
    	return false;
    }
	
    private User getUserByUsername(String username) {
    	for (User user: super.getUserSet()) {
    		if (user.getUsername().equals(username))
    			return user;
    	}
    	return null;
    }
    

	public User fetchUser(String username, String password) {
		if (username.equals("root") && super.getSuperUser().validatePassword(password)){
			return super.getSuperUser();
		}
    	for (User user: super.getUserSet()) {
    		if (user.getUsername().equals(username) && user.validatePassword(password))
    			return user;
    	}
    	return null;
    }
	
	public User fetchUser(Element fileNode) throws UnsupportedEncodingException {
		String owner = fileNode.getChildText("owner");
		if(owner != null) {
			return getUserByUsername(new String(owner.getBytes("UTF-8")));
			}
		else {
			return super.getSuperUser();
		}
    }
	
    
    public boolean hasUser(String username) {
    	return this.getUserByUsername(username) != null;
    }
    
   
    @Override
    public void addUser(User newUser) {
    	super.addUser(newUser);
    }
        
    
    
    @Override
    public void setIdCounter(Integer newCounter) {
    	if (newCounter < this.getIdCounter() ) {
    		throw new InvalidIdCounter(newCounter);
    	} else {
    		super.setIdCounter(newCounter);
    	}
    }
    
    @Override
    public Set <Login> getLoginSet() {
    	throw new AccessDeniedToManipulateLoginException();
    }
    
    
	@Override
	public Set<User> getUserSet() {
		throw new AccessDeniedException("get user set", "Manager");
	}
	
	/*
	 FIXME 
	@Override
	public SuperUser getSuperUser() {
		throw new AccessDeniedException("get Super User", "Manager");
	}
	
	FIXME
	get/SetSuperUser/GuestUser
 	*/
    
	public void xmlImport(Element myDriveElement) throws UnsupportedEncodingException{
		for(Element userNode : myDriveElement.getChildren("user")) {
			new User(this, userNode);
		}

		for (Element dirNode: myDriveElement.getChildren("dir")){
			new Directory(this,dirNode);
		}


		for (Element plainNode : myDriveElement.getChildren("plain")) {
			new PlainFile(this,plainNode);
		}

		for (Element linkNode: myDriveElement.getChildren("link")){
			new Link(this,linkNode);
		}

		for (Element appNode: myDriveElement.getChildren("app")){
			new App(this,appNode);
		}
	}
	
    public Document xmlExport() {
        Element myDrive = new Element("myDrive");
        Document doc = new Document(myDrive);
        
        for (User u: getUserSet())
        	if (!u.getUsername().equals("root"))
        		myDrive.addContent(u.xmlExport());
        
        getRootDirectory().xmlExport(myDrive);
        
        return doc;
    }


}
