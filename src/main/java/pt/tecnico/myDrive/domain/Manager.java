package pt.tecnico.myDrive.domain;

import org.jdom2.Document;
import org.jdom2.Element;

import pt.ist.fenixframework.FenixFramework;
import pt.tecnico.myDrive.exception.FileAlreadyExistsException;
import pt.tecnico.myDrive.exception.UserAlreadyExistsException;

import java.util.regex.Pattern;



public class Manager extends Manager_Base {
	
	
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
    	newUser.setManager(this);
    }
    

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
    


    public Directory createMissingDirectories(String directoriesToCreate){   }

    public Directory lookUpDir(String pathname){};
    
    public void xmlImport(Element rootDrive) {      //throws UserDoesNotExistException
        

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
        Element element = new Element("myDrive");
        Document doc = new Document(element);

        for (User u: getUserSet())
            element.addContent(u.xmlExport());

        for (File f: getFileSet())
            element.addContent(f.xmlExport());

        return doc;
    }
    

}
