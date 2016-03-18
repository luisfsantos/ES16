package pt.tecnico.myDrive.domain;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jdom2.Document;
import org.jdom2.Element;

import pt.ist.fenixframework.FenixFramework;
import pt.tecnico.myDrive.exception.FileAlreadyExistsException;
import pt.tecnico.myDrive.exception.ImportDocumentException;
import pt.tecnico.myDrive.exception.InvalidPathException;
import pt.tecnico.myDrive.exception.UserAlreadyExistsException;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;




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
        
        SuperUser superUser = new SuperUser(this); 
        this.setSuperUser(superUser);
        RootDirectory rootDirectory = new RootDirectory(this, superUser);
        this.setRootDirectory(rootDirectory);
        Directory homeDirectory = new Directory("home", superUser, rootDirectory);
        Directory rootHome = new Directory("root", superUser, homeDirectory);
        superUser.setHome(rootHome);
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
        
    
    public int getNextIdCounter() {
    	int currCounter = this.getIdCounter();
    	this.setIdCounter(currCounter+1);
    	return currCounter;
    }
    
    
    
    public Directory getHomeDirectory() {
    	for (File f: this.getRootDirectory().getFileSet() ) {
    		if (f.getName().equals("home")) {
    			return (Directory)f;
    		}
    	}
    	return null;
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
	/* C
    public Document xmlExport() {
        Element element = new Element("myDrive");
        Document doc = new Document(element);
        List<File> files = new ArrayList<File>(getFileSet());
        
        for (User u: getUserSet())
        	if (!u.getUsername().equals("root"))
        		element.addContent(u.xmlExport());

        Collections.sort(files, new Comparator<File>() {
        	public int compare(File f1, File f2) {
        		return (f1.getId() < f2.getId() ? -1 : (f1.getId() == f2.getId() ? 0 : 1));
        	}
        });
        
        for (File f: files)
        	if (!(f.getAbsolutePath().equals("/") ||
        		f.getAbsolutePath().equals("/home") ||
        		f.getAbsolutePath().equals("/home/root")))
        		element.addContent(f.xmlExport());
        
        return doc;
    }
    C */
	
	

	public Directory createAbsolutePath(String path) {
		if(!path.startsWith("/")) {
			throw new InvalidPathException(path);
		}
		Directory startDir = getRootDirectory();

		return createAbsolutePath(startDir, path.substring(1));
	}
	

	private Directory createAbsolutePath(Directory dir, String path) {
		Directory nextDir;
		int first = path.indexOf('/');

		if(first == -1) {
			if (!dir.hasFile(path)) return new Directory(path,  this.getSuperUser(), dir);
			else return (Directory) dir.getFileByName(path);
		}
		String dirName = path.substring(0, first);
		String nextPath =  path.substring(first + 1);

		if(dir.hasFile(dirName)) {
			nextDir = (Directory) dir.getFileByName(dirName);
			return createAbsolutePath(nextDir, nextPath);
		} else {
			nextDir = new Directory(dirName,  this.getSuperUser(), dir);
			return createAbsolutePath(nextDir, nextPath);
		}
	}

}
