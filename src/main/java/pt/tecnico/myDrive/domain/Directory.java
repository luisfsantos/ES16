package pt.tecnico.myDrive.domain;

import org.jdom2.Element;
import pt.tecnico.myDrive.exception.FileAlreadyExistsException;

import java.io.UnsupportedEncodingException;
import java.util.*;

public class Directory extends Directory_Base {
	
	public Directory(String name, String permission, Manager manager, User owner, Directory parent) {
		this.initFile(name, permission, manager, owner, parent);
	}

	public Directory(Manager manager, Element dirNode) { //throws UserDoesNotExistException{

		String path = dirNode.getChild("path").getValue();
		String ownerName = dirNode.getChild("owner").getValue();
		String name = dirNode.getChild("name").getValue();

		User user = manager.getUserByUsername(ownerName);

		Directory barra = manager.getHomeDirectory().getParent();
		Directory parent = (Directory)barra.lookup(path);


		if (user == null){
			//throw new UserDoesNotExistException;
		}
		else if(parent == null){
			manager.createMissingDirectories(path);
			setManager(manager);
			super.xmlImport(dirNode);
		}
		else if (!parent.hasFile(name)){
			setManager(manager);
			super.xmlImport(dirNode);
		}
		else {
			throw new FileAlreadyExistsException(2222); //random
		}
	}


	@Override
	public Directory createDirectory(String name, Manager manager, User owner) {
		Directory dir = new Directory(name, owner.getUmask(), manager, owner, this);
		this.addFile(dir);
		return dir;
	}
	
	@Override
	public App createApp(String name, Manager manager, User owner, String content) {
		App app = new App(name, owner.getUmask(), manager, owner, this, content);
		this.addFile(app);
		return app;
	}
	
	@Override
	public Link createLink(String name, Manager manager, User owner, String content) {
		Link link = new Link(name, owner.getUmask(), manager, owner, this, content);
		this.addFile(link);
		return link;
	}
/*
	public Directory lookup(String path){
		String[] names = path.split("/");
		System.out.println("lookup:filename:"+this.getName()+this.getFile(names[1]).getName());
		return (Directory) this.getFile(names[1]);
	}
*/



	public boolean hasFile(String name){
		Iterator iterator = getFileSet().iterator();
		while(iterator.hasNext()){
			File file = (File)iterator.next();
			if(file.getName() == name) return true;
		}
		return false;
	}
	/*

	
	public File lookup(String path) throws ExpectedSlashPathStartException, NoSuchFileInThisDirectoryException{
		String name;

		if(path.charAt(0) != '/') 
			throw new ExpectedSlashPathStartException();
		if(path.charAt(1) == '/' || path.charAt(1) == '.'){
			path = path.subString(path.indexOf("/", 1));
			return this.lookup(path);
		}
		if(!path.subString(1).contains('/'))
			return getFile(path);

		name = path.subString(1, path.indexOf("/", 1));
		path = path.subString(path.indexOf("/", 1));
		return getFile(name).lookup(path);
	}

	public void remove() throws notEmptyDirectoryException{                  //TO REVIEW!!!
		
		if (this.getFileSet().size()==0){
			
			this.rmv();                                                  
		}
		else 
			throw new notEmptyDirectoryException();
	}
	
	public void rmv(){                       //TO REVIEW
		
		setParent(null);
		setUser(null);
		setManager(null);
		deleteDomainObject();	
		
	}
	
    public void lsDir() {
        for(File file : super.getFileSet()) {
            System.out.println(file.toString());
        }
    }

    
    @Override
    public String toString() {
        return "app " +
                super.getPermissions() +
                super.getFileSet().size() + // TODO need to add + 2 ???
                " " + super.getUser().getUsername() +
                " " + super.getId() +
                " " + super.getLastModified() +
                " " + super.getName();
    }
	C */ 
}
