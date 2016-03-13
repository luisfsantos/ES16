package pt.tecnico.myDrive.domain;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jdom2.Document;
import org.jdom2.Element;

import pt.ist.fenixframework.FenixFramework;
import pt.tecnico.myDrive.exception.FileAlreadyExistsException;
import pt.tecnico.myDrive.exception.ImportDocumentException;
import pt.tecnico.myDrive.exception.UserAlreadyExistsException;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;



public class Manager extends Manager_Base {
	static final Logger log = LogManager.getRootLogger();
	private Directory rootDirectory;
	
	// manager use Singleton design pattern
    public static Manager getInstance() {
    	Manager manager = FenixFramework.getDomainRoot().getManager();
    	if (manager != null) {
    		for (File file: manager.getFileSet()) {
        		if (file.getName().equals("/"))
        			manager.rootDirectory = (Directory)file;
        	}
    		return manager;
    	}
    	return new Manager();
    } 
    
    private Manager() {

    	this.setRoot(FenixFramework.getDomainRoot());
        this.setIdCounter(0);
        
        User superUser = new User("root", "***", "Super User", "rwxdr-x-", null);
        this.addUser(superUser);
        		
        File startHome = new Directory("/", "rwxdr-x-", this, superUser, null);
        startHome.setParent((Directory)startHome);
        this.rootDirectory = (Directory)startHome;
        
        Directory home = startHome.createDirectory("home", this, superUser);
        Directory rootHome = home.createDirectory("root", this, superUser);
        superUser.setHome(rootHome);
        
        log.trace("[Manager:getInstance] new Manager created");
    }
	
        
    public Directory getRootDirectory() {
		return rootDirectory;
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
    
    
    public void createNewUser(String username){   
    	this.createNewUser(username, username, username, "rwxd----");
    }
    
    
    // miss exceptions
    public void createNewUser(String username, String password, String name, String umask){
    	User newUser = new User(username, password, name, umask, null);
    	Directory userHome = this.getHomeDirectory().createDirectory(username, this, newUser);
    	newUser.setHome(userHome);
    	this.addUser(newUser);
    }
    
    
    /* C
    public void createNewUser(String username) throws UserAlreadyExistsException, EmptyUsernameException, InvalidUsernameException{      
    	
        this.validateUsername(username);

    	User newUser = new User(username);
    	this.addUser(newUser);
    	newUser.setManager(this);
    }
    
    public boolean validateUsername(String username) throws UserAlreadyExistsException,EmptyUsernameException, InvalidUsernameException{
        
        if (this.hasUser(username)) {
            throw new UserAlreadyExistsException(username);
        }
        
        boolean isAlphanumeric = Pattern.matches("^[a-zA-Z0-9]*$", username);    

        if (username.isEmpty()) throw new EmptyUsernameException();
        else if (!isAlphanumeric) throw new InvalidUsernameException();
    }
	C */ 
    
    
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
 
    

    @Override 
    public void addFile(File newFile) throws FileAlreadyExistsException {
    	if (this.hasFile(newFile.getId())) {
    		throw new FileAlreadyExistsException(newFile.getId());
    	}
    	super.addFile(newFile);
    }
    
    public int getNextIdCounter() {
    	int currCounter = this.getIdCounter();
    	this.setIdCounter(currCounter+1);
    	return currCounter;
    }
    
    
    public Directory getHomeDirectory() {
		for (File h: rootDirectory.getFileSet()) {
			if (h.getName().equals("home")) {
				return (Directory) h;
			}
		}
		return null;
	}

    


    public Directory createMissingDirectories(String dirs){
		String[] tokens = dirs.split("/");
		String building="";
		User sudo = getUserByUsername("root");
		Directory barra = getRootDirectory();

		for(int i=1;i<tokens.length;i++){
			if (barra.lookup(building+'/'+tokens[i])!=null){ //dir exists
				building+='/'+tokens[i];
			}
			else{
				barra.lookup(building).createDirectory(tokens[i],this,sudo);
				building+='/'+tokens[i]; //to add new dir to building
			}
		}
		return (Directory) barra.lookup(dirs);
	}
/*
    public Directory lookUpDir(String pathname){};
*/


	public void xmlImport(Element myDriveElement) throws UnsupportedEncodingException{
		for(Element node : myDriveElement.getChildren("user")) {
			String username = node.getAttributeValue("username"); // TODO Validate username
			User user;

			try {
				if (getUserByUsername(username) != null) {
					throw new UserAlreadyExistsException(username);
				}
			} catch (UserAlreadyExistsException e) {
				throw new ImportDocumentException();
			}

			createNewUser(username);
			user = getUserByUsername(username);
			user.xmlImport(node);
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
    	final int defaultNoFiles= 3;
        Element element = new Element("myDrive");
        Document doc = new Document(element);
        List<File> files = new ArrayList<File>(getFileSet());
        
        for (User u: getUserSet())
        	if (!u.getUsername().equals("root"))
        		element.addContent(u.xmlExport());
        
        if (files.size() > defaultNoFiles)  {
            Collections.sort(files, new Comparator<File>() {
            	public int compare(File f1, File f2) {
            		return (f1.getId() < f2.getId() ? -1 : (f1.getId() == f2.getId() ? 0 : 1));
            	}
            });
            
            for (File f: files)
                element.addContent(f.xmlExport());
        }

        return doc;
    }

}
