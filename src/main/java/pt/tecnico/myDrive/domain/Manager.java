package pt.tecnico.myDrive.domain;

import org.jdom2.Document;
import org.jdom2.Element;

import pt.ist.fenixframework.FenixFramework;
import pt.tecnico.myDrive.exception.FileAlreadyExistsException;
import pt.tecnico.myDrive.exception.UserAlreadyExistsException;




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
    

    public void createNewUser(String username) throws UserAlreadyExistsException{
    	if (this.hasUser(username)) {
    		throw new UserAlreadyExistsException(username);
    	}
    	User newUser = new User(username);
    	this.addUser(newUser);
    	newUser.setManager(this);
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
    


    public void xmlImport(Element rootDrive) {      
        

        for (Element userNode: rootDrive.getChildren("user")){
            String username = userNode.getAttribute("username").getValue();  
            String password = userNode.getChild("password").getValue();
            String name = userNode.getChild("name").getValue();
            Directory home = lookUpDir(userNode.getChild("home").getValue());  
            String mask = userNode.getChild("mask").getValue();
            User user = getUserByUsername(username);
            
            if (user == null){
                if (password == null){
                    password = username;
                }
                else if (name == null){
                    name = username;
                }
                else if(home == null){
                    String dirs_to_create = userNode.getChild("home").getValue();
                    home = createMissingDirectories(dirs_to_create);  //devolve o objecto
                }
                else if(mask == null){
                    mask = "rwxd----";
                }
                user = new User(username,password,name,mask,this,home);          
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


            plain = new PlainFile();
            plain.xmlImport(plainNode);
        }

        for(Element dirNode: rootDrive.getChildren("dir")){
           
            String path = dirNode.getChild("path").getValue(); 
            String name = dirNode.getChild("name").getValue();
            String owner = dirNode.getChild("owner").getValue();
            String perm = dirNode.getChild("perm").getValue(); 
            int id = getNextIdCounter();

                dir = new Directory();
                dir.xmlImport(dirNode);
        }
            
        for(Element linkNode: rootDrive.getChildren("link")){
            String path =linkNode.getChild("path").getValue(); 
            String name = linkNode.getChild("name").getValue();
            String owner = linkNode.getChild("owner").getValue();
            String perm = linkNode.getChild("perm").getValue();
            String value = linkNode.getChild("value").getValue();
            int id = getNextIdCounter();

                link = new Link();
                link.xmlImport(linkNode);   
        }
            
        for(Element appNode: rootDrive.getChildren("app")){
            String path = appNode.getChild("path").getValue(); 
            String name = appNode.getChild("name").getValue();
            String owner = appNode.getChild("owner").getValue();
            String perm = appNode.getChild("perm").getValue(); 
            String method = appNode.getChild("method").getValue();
            int id = getNextIdCounter();

                app = new App();
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
