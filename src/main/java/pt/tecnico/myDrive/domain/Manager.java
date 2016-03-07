package pt.tecnico.myDrive.domain;

import org.jdom2.Element;
import org.jdom2.Document;

import pt.ist.fenixframework.FenixFramework;
import pt.tecnico.myDrive.domain.User;

public class Manager extends Manager_Base {
    private User superUser;
	
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
        this.setIdCounter(0);
    }
    
	public User getSuperUser() {
		return superUser;
	}

	public void setSuperUser(User superUser) {
		this.superUser = superUser;
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
    
    // will be replaced with exception
    @Override 
    public void addUser(User newUser) {
    	if (this.hasUser(newUser.getUsername())) {
    		return;
    	}
    	super.addUser(newUser);
    }
    
    
    public File getFileById(int id) {
    	for (File file: this.getFileSet()) {
    		if (file.getId().equals(id))
    			return file;
    	}
    	return null;
    }
    
    public boolean hasFile(int id) {
    	return this.getFileById(id) != null;
    }
 
    
    // will be replaced with exception
    @Override 
    public void addFile(File newFile) {
    	if (this.hasFile(newFile.getId())) {
    		return;
    	}
    	super.addFile(newFile);
    }
    
    public int getNextIdCounter() {
    	int currCounter = this.getIdCounter();
    	this.setIdCounter(currCounter+1);
    	return currCounter;
    }
    

    public void initManager() {
    	if (!this.hasUser("root")) {
    		this.setSuperUser(SuperUser.getSuperUser());
    		this.addUser(superUser);
    	}
    	else {
    		this.setSuperUser(this.getUserByUsername("root"));
    	}
    }

    public Document xmlExport() {
        Element element = new Element("myDrive");
        Document doc = new Document(element);

        for (User u: getUserSet())
            element.addContent(u.xmlExport());

        for (File f: getFileSet())
            element.addContent(f.xmlExport());

        return doc;
    }

    
}
