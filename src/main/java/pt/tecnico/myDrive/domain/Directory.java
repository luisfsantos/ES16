package pt.tecnico.myDrive.domain;

import java.util.*;

import org.jdom2.Element;
import pt.tecnico.myDrive.exception.FileAlreadyExistsException;


public class Directory extends Directory_Base {
	
	public Directory(String name, String permission, Manager manager, User owner, Directory parent) {
		this.initFile(name, permission, manager, owner, parent);
	}
	
	
	@Override
	public Directory createDirectory(String name, Manager manager, User owner) {
		this.verifyFileNameDir(name);
		Directory dir = new Directory(name, owner.getUmask(), manager, owner, this);
		this.addFile(dir);
		return dir;
	}
	
	@Override
	public App createApp(String name, Manager manager, User owner, String content) {
		this.verifyFileNameDir(name);
		App app = new App(name, owner.getUmask(), manager, owner, this, content);
		this.addFile(app);
		return app;
	}
	
	@Override
	public Link createLink(String name, Manager manager, User owner, String content) {
		this.verifyFileNameDir(name);
		Link link = new Link(name, owner.getUmask(), manager, owner, this, content);
		this.addFile(link);
		return link;
	}

	@Override
	public PlainFile createPlainFile(String name, Manager manager, User owner, String content) {
		this.verifyFileNameDir(name);
		PlainFile plainFile = new PlainFile(name, owner.getUmask(), manager, owner, this, content);
		this.addFile(plainFile);
		return plainFile;
	}
	
	public void verifyFileNameDir(String name) throws FileAlreadyExistsException{ //CHANGE EXCEPTION NAME
		for (File f : this.getFileSet()){
			if(f.getName().equals(name))
				throw new FileAlreadyExistsException(10002);
			else if ((name.indexOf('/') >= 0) || (name.indexOf('\0') >= 0))
				throw new FileAlreadyExistsException(10003);		
		}
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
	C */

	/* 
DAVID 
	 public File getFileByName(String name) throws FileAlreadyExistsException {
	 
		for (File f : getFileSet()){
			if (f.getName().equals(name))
				return f;
		}
		throw new FileAlreadyExistsException(9999);
DAVID		
	}*/
	
	
	
	public void lsDir() {
		List<File> files = new ArrayList<File>(getFileSet());

		Collections.sort(files, new Comparator<File>() {
			public int compare(File f1, File f2) {
				return f1.getName().compareTo(f2.getName());
			}
		});

		System.out.println(this.toString("."));
		System.out.println(getParent().toString(".."));

		for (File f: files)
			System.out.println(f.toString());
    }

	public Element xmlExport() {
		Element element = new Element("dir");
		element.setAttribute("id", getId().toString());
		
		Element pathElement = new Element("path");
		pathElement.setText(getAbsolutePath());
		element.addContent(pathElement);

		Element nameElement = new Element("name");
		nameElement.setText(getName());
		element.addContent(nameElement);

		Element ownerElement = new Element("owner");
		ownerElement.setText(getOwner().getName());
		element.addContent(ownerElement);

		Element permissionElement = new Element("perm");
		permissionElement.setText(getPermissions());
		element.addContent(permissionElement);

		return element;
	}

	@Override
	public String getFileType() {
		return "dir";
	}

	@Override
	public int getSize() {
		return getFileSet().size() + 2;
	}

	public String toString(String name) {
		return getFileType() +
				" " + getPermissions() +
				" " + getSize() +
				" " + getOwner().getUsername() +
				" " + getId() +
				" " + getLastModified().toString("dd/MM/YYYY-HH:mm:ss") +
				" " + name;
	}
	
public void remove() throws FileAlreadyExistsException{                  //CHANGE EXCEPTION NAME!!!
		
		if (this.getFileSet().size()==0){
			this.rmv();                                                  
		}
		else 
			throw new FileAlreadyExistsException(10000);
	}
	
	public void rmv(){                       //TO REVIEW
		
		setParent(null);
		setOwner(null);
		setManager(null);
		deleteDomainObject();	
		
	}
}
