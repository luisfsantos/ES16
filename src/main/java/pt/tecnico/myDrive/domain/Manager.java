package pt.tecnico.myDrive.domain;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jdom2.Document;
import org.jdom2.Element;

import pt.ist.fenixframework.FenixFramework;
import pt.tecnico.myDrive.exception.FileAlreadyExistsException;
import pt.tecnico.myDrive.exception.UserAlreadyExistsException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;



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
        this.setIdCounter(0);
        
        User superUser = new User("root", "***", "Super User", "rwxdr-x-", null);
        this.addUser(superUser);
        		
        File startHome = new Directory("/", "rwxdr-x-", this, superUser, null);
        startHome.setParent((Directory)startHome);
        
        Directory home = startHome.createDirectory("home", this, superUser);
        Directory rootHome = home.createDirectory("root", this, superUser);
        superUser.setHome(rootHome);
        
        log.trace("[Manager:getInstance] new Manager created");
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
    
    
    public void createNewUser(String username){    //throws UserAlreadyExistsException

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
    	for (File f: this.getFileSet()) {
    		if (f.getName().equals("/")) {
    			Directory pathStart = (Directory) f;
    			for (File h: pathStart.getFileSet()) {
    				if (h.getName().equals("home")) {
    					return (Directory) h;
    				}
    			}
    			return null;
    		}
    	}
    	return null;
    }
    

    /* 
    public Directory createMissingDirectories(String directoriesToCreate){   }

    public Directory lookUpDir(String pathname){};
    
    
    public void xmlImport(Element rootDrive) {      
        

        for (Element userNode: rootDrive.getChildren("user")){
            String username = userNode.getAttribute("username").getValue();
            String home = userNode.getChild("home").getValue();  
            
            User user = getUserByUsername(username);
            Directory homeDir = lookUpDir(home);

            if (user == null){

                if(homeDir == null){
                    homeDir = createMissingDirectories(home);
                }
            user = new User(username,this,homeDir);          
            }
            user.xmlImport(userNode);

        }
    }
	
    C */
    
        /*
        for(Element plainNode: rootDrive.getChildren("plain")){
            
            String path = plainNode.getChild("path").getValue(); 
            String name = plainNode.getChild("name").getValue();
            String owner = plainNode.getChild("owner").getValue();
            String perm = plainNode.getChild("perm").getValue(); 
            String contents = plainNode.getChild("contents").getValue();
            int id = getNextIdCounter();

            Directory parent = lookUpDir(path);
            User user = getUserByUsername(owner);
            
            if (user == null){
                throw new UserDoesNotExistException(owner);
            }
            if(parent == null){
                parent = createMissingDirectories(path);  //devolve o objecto
            }
            PlainFile plain = new PlainFile(id,name,user,perm,parent,contents);//verificacoes devem ser feitas no constructor
            plain.xmlImport(plainNode);
        }

        for(Element dirNode: rootDrive.getChildren("dir")){
           
            String path = dirNode.getChild("path").getValue(); 
            String name = dirNode.getChild("name").getValue();
            String owner = dirNode.getChild("owner").getValue();
            String perm = dirNode.getChild("perm").getValue(); 
            int id = getNextIdCounter();

            Directory parent = lookUpDir(path);
            User user = getUserByUsername(owner);

            if(user == null){
                throw new UserDoesNotExistException(owner)
            }
            if(parent == null){
                parent = createMissingDirectories(path);
            }
            Directory dir = new Directory(id,name,user,perm,parent)
            dir.xmlImport(dirNode);
        }
        
            
        for(Element linkNode: rootDrive.getChildren("link")){
            String path =linkNode.getChild("path").getValue(); 
            String name = linkNode.getChild("name").getValue();
            String owner = linkNode.getChild("owner").getValue();
            String perm = linkNode.getChild("perm").getValue();
            String value = linkNode.getChild("value").getValue();
            int id = getNextIdCounter();

            Directory parent = lookUpDir(path);
            User user = getUserByUsername(owner);

            if(user == null){
                throw new UserDoesNotExistException(owner)
            }
            if(parent == null){
                parent = createMissingDirectories(path);
            }
            Link link = new Link(id,name,user,perm,parent,value);
            link.xmlImport(linkNode);   
        }
        
        for(Element appNode: rootDrive.getChildren("app")){
            String path = appNode.getChild("path").getValue(); 
            String name = appNode.getChild("name").getValue();
            String owner = appNode.getChild("owner").getValue();
            String perm = appNode.getChild("perm").getValue(); 
            String method = appNode.getChild("method").getValue();
            int id = getNextIdCounter();

            Directory parent = lookUpDir(path);
            User user = getUserByUsername(owner);

            if(user == null){
                throw new UserDoesNotExistException(owner)
            }
            if(parent == null){
                parent = createMissingDirectories(path);
            }
            App app = new App(id,name,user,perm,parent,method);
            app.xmlImport(appNode);
        }
        */

    
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
