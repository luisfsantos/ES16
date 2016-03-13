package pt.tecnico.myDrive.domain;

import org.jdom2.Element;
import pt.tecnico.myDrive.exception.FileAlreadyExistsException;

import java.io.UnsupportedEncodingException;
import java.util.*;

import org.jdom2.Element;

public class Directory extends Directory_Base {
	
	public Directory(String name, String permission, Manager manager, User owner, Directory parent) {
		this.initFile(name, permission, manager, owner, parent);
	}

	public Directory(Manager manager, Element dirNode) throws UnsupportedEncodingException{ //throws UserDoesNotExistException{

		String path = dirNode.getChild("path").getValue();
		String ownerName = dirNode.getChild("owner").getValue();
		String name = dirNode.getChild("name").getValue();

		User user = manager.getUserByUsername(ownerName);

		Directory barra = manager.getHomeDirectory().getParent();
		//Directory parent = (Directory)barra.lookup(path);


		if (user == null){
			//throw new UserDoesNotExistException;
		}

		try {
			barra.lookup(path);
		} catch (NullPointerException a) {
			manager.createMissingDirectories(path);
			setManager(manager);
			super.xmlImport(dirNode);
			return;
		} finally {
			Directory parent = (Directory) barra.lookup(path);
			if (!parent.hasFile(name)) {
				setManager(manager);
				super.xmlImport(dirNode);
			} else {
				throw new FileAlreadyExistsException(1111); //random
			}
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
*/

	@Override
	public PlainFile createPlainFile(String name, Manager manager, User owner, String content) {
		PlainFile plainFile = new PlainFile(name, owner.getUmask(), manager, owner, this, content);
		this.addFile(plainFile);
		return plainFile;
	}

	public File getFile(String name) {
		for (File file : getFileSet())
			if (file.getName().equals(name))
				return file;
		return null;
	}


	public boolean hasFile(String name){
		Iterator iterator = getFileSet().iterator();
		while(iterator.hasNext()){
			File file = (File)iterator.next();
			if(file.getName() == name) return true;
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
			return getFile(name);
		}

		name = path.substring(0, path.indexOf("/", 1));
		path = path.substring(path.indexOf("/", 1) + 1);
		while(path.startsWith("/"))
			path = path.substring(1);
		return this.getFile(name).lookup(path);
	}
	/*
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
}
