package pt.tecnico.myDrive.domain;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jdom2.Document;
import org.jdom2.Element;
import org.joda.time.DateTime;

import pt.ist.fenixframework.FenixFramework;
import pt.tecnico.myDrive.exception.AccessDeniedToManipulateLoginException;
import pt.tecnico.myDrive.exception.FileAlreadyExistsException;
import pt.tecnico.myDrive.exception.ImportDocumentException;
import pt.tecnico.myDrive.exception.InvalidIdCounter;
import pt.tecnico.myDrive.exception.InvalidPathException;
import pt.tecnico.myDrive.exception.InvalidTokenException;
import pt.tecnico.myDrive.exception.UserAlreadyExistsException;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
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
    }
	
    
    public Login getLoginByToken(long token) {
    	DateTime now = new DateTime();
    	for (Login login: super.getLoginSet()) {
    		if (login.validateToken(token)){
    			if (login.getLastActivity().isBefore(now.minusHours(2))){
    				throw new InvalidTokenException();
    			} else {
    				return login;
    			}
    		}
    	}
    	throw new InvalidTokenException();
    }
    
    public void removeInactiveLogins() {
    	DateTime now = new DateTime();
    	for (Login login: super.getLoginSet()) {
			if (login.getLastActivity().isBefore(now.minusHours(2))){
				login.remove();
			} 
    	}
    }
	

	public User getUserByUsername(String username) {
    	for (User user: this.getUserSet()) {
    		if (user.getUsername().equals(username))
    			return user;
    	}
    	return null;
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
