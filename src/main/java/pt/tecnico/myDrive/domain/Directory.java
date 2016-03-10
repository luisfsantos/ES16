package pt.tecnico.myDrive.domain;

import java.util.*;

public class Directory extends Directory_Base {
	
	public Directory(String name, String permission, Manager manager, User owner, Directory parent) {
		this.initFile(name, permission, manager, owner, parent);
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
	
	/* C
	public File getFile(String name) throws NoSuchFileInThisDirectoryException{
		Iterator iterator = getFileSet().iterator();
		while(iterator.hasNext()){
			File file = iterator.next();
			if(file.getName() == name) return file;
		}
		throw new NoSuchFileInThisDirectoryException(name);
	}

	public boolean hasFile(String name){
		Iterator iterator = getFileSet().iterator();
		while(iterator.hasNext()){
			File file = iterator.next();
			if(file.getName() == name) return true;
		}
		return false;
	}

	
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
