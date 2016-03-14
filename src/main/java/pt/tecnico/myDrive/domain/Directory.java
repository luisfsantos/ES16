package pt.tecnico.myDrive.domain;

import org.jdom2.Element;
import pt.tecnico.myDrive.exception.*;

import java.util.*;

public class Directory extends Directory_Base {
	
	public Directory(String name, String permission, Manager manager, User owner, Directory parent) {
		this.initFile(name, permission, manager, owner, parent);
	}

	public Directory(Manager manager, Element dirNode) {
		this.xmlImport(manager, dirNode);
	}


	@Override
	public Directory createDirectory(String name, Manager manager, User owner) {
		this.verifyFileNameDir(name);
		Directory dir = new Directory(name, owner.getUmask(), manager, owner, this);
		this.addFile(dir);
		return dir;
	}
	
	public Directory createDirectory(String name) {
		return this.createDirectory(name, this.getManager(), this.getOwner());
	}
	
	@Override
	public App createApp(String name, Manager manager, User owner, String content) {
		this.verifyFileNameDir(name);
		App app = new App(name, owner.getUmask(), manager, owner, this, content);
		this.addFile(app);
		return app;
	}
	
	public App createApp(String name, String content) {
		return this.createApp(name, this.getManager(), this.getOwner(), content);
	}
	
	@Override
	public Link createLink(String name, Manager manager, User owner, String content) {
		this.verifyFileNameDir(name);
		Link link = new Link(name, owner.getUmask(), manager, owner, this, content);
		this.addFile(link);
		return link;
	}
	
	public Link createLink(String name, String content) {
		return this.createLink(name, this.getManager(), this.getOwner(), content);
	}
	

	@Override
	public PlainFile createPlainFile(String name, Manager manager, User owner, String content) {
		this.verifyFileNameDir(name);
		PlainFile plainFile = new PlainFile(name, owner.getUmask(), manager, owner, this, content);
		this.addFile(plainFile);
		return plainFile;
	}
	
	
	public PlainFile createPlainFile(String name, String content) {
		return this.createPlainFile(name, this.getManager(), this.getOwner(), content);
	}
	
	
	
	public void verifyFileNameDir(String name) throws FileAlreadyExistsInDirectoryException, InvalidFileNameException{ //CHANGE EXCEPTION NAME
		if ((name.indexOf('/') >= 0) || (name.indexOf('\0') >= 0))
			throw new InvalidFileNameException(name);
		for (File f : this.getFileSet()) {
			if(f.getName().equals(name))
				throw new FileAlreadyExistsInDirectoryException(name, this.getName());
		}
	}
	

	public File getFileByName(String name){
		if (name.equals("."))
			return this;
		if (name.equals(".."))
			return getParent();
		
		for (File file : getFileSet()){
			if (file.getName().equals(name))
				return file;
		}
		return null;
	}

	public boolean hasFile(String name){
		for (File file : getFileSet()){
			if (file.getName().equals(name))
				return true;
		}
		return false;
	}

	public File lookup(String path) {
		String name;

		while(path.endsWith("/"))
			path = path.substring(0, path.lastIndexOf('/'));

		if(path.startsWith("/")) {
			if(this != getParent()) {
				return getParent().lookup(path);
			} else {
				while(path.startsWith("/"))
					path = path.substring(1);
			}
		}
		if(path.startsWith("..")){
			if(path.indexOf('/') == -1) return getParent();
			path = path.substring(path.indexOf("/", 1) + 1);
			while(path.startsWith("/"))
				path = path.substring(1);
			return getParent().lookup(path);
		}
		if(path.startsWith(".")){
			if(path.indexOf('/') == -1) return this;
			path = path.substring(path.indexOf("/", 1) + 1);
			while(path.startsWith("/"))
				path = path.substring(1);
			return this.lookup(path);
		}
		if(path.indexOf('/') == -1) {
			name = path;
			return getFileByName(name);
		}

		name = path.substring(0, path.indexOf("/", 1));
		path = path.substring(path.indexOf("/", 1) + 1);
		while(path.startsWith("/"))
			path = path.substring(1);
		if(hasFile(name))
			return this.getFileByName(name).lookup(path);

		return null;
	}

	@Override
	public void showContent() {
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
		Element dirElement = super.xmlExport();
		dirElement.setName("dir");

		return dirElement;
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
	
public void remove() throws NotEmptyDirectoryException{                  //CHANGE EXCEPTION NAME!!!
		
		if (this.getFileSet().size()==0){
			this.rmv();                                                  
		}
		else 
			throw new NotEmptyDirectoryException(this.getName());
	}
	
	public void rmv(){                       //TO REVIEW
		
		setParent(null);
		setOwner(null);
		setManager(null);
		deleteDomainObject();	
		
	}
}
