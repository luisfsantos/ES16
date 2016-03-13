package pt.tecnico.myDrive.domain;

import org.jdom2.Element;
import pt.tecnico.myDrive.exception.*;

import java.io.UnsupportedEncodingException;
import java.util.*;

public class Directory extends Directory_Base {
	
	public Directory(String name, String permission, Manager manager, User owner, Directory parent) {
		this.initFile(name, permission, manager, owner, parent);
	}

	public Directory(Manager manager, Element dirNode) throws UnsupportedEncodingException {
		String path = new String(dirNode.getChild("path").getValue().getBytes("UTF-8"));
		String ownerName = new String(dirNode.getChild("owner").getValue().getBytes("UTF-8"));
		String name = new String(dirNode.getChild("name").getValue().getBytes("UTF-8"));

		User owner = manager.getUserByUsername(ownerName);
		if (owner == null){
			throw new UserDoesNotExistException(ownerName);
		}

		Directory parentDir = manager.createAbsolutePath(path);
		if (parentDir.hasFile(name)) {
			throw new FileAlreadyExistsInDirectoryException(name, parentDir.getName());
		}
		parentDir.createDirectory(name, manager, owner);
		
		setManager(manager);
		super.xmlImport(dirNode);
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
	
	public void verifyFileNameDir(String name) throws FileAlreadyExistsInDirectoryException, InvalidFileNameException{ //CHANGE EXCEPTION NAME
		for (File f : this.getFileSet()){
			if(f.getName().equals(name))
				throw new FileAlreadyExistsInDirectoryException(name, this.getName());
			else if ((name.indexOf('/') >= 0) || (name.indexOf('\0') >= 0))
				throw new InvalidFileNameException(name);		
		}
	}
	

	public File getFileByName(String name) throws FileDoesntExistsInDirectoryException{
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
