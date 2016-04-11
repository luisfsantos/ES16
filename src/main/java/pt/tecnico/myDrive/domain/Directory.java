package pt.tecnico.myDrive.domain;

import org.jdom2.Element;
import pt.tecnico.myDrive.exception.*;

import java.io.UnsupportedEncodingException;
import java.util.*;

public class Directory extends Directory_Base {
	
	protected Directory() {
		super();
	}
	
	public Directory(String name, User owner, Directory parent) {
		this.initFile(name, owner.getUmask(), owner, parent);
	}
	
	public Directory(String name, Directory parent) {
		this.initFile(name, Manager.getInstance().getSuperUser().getUmask(), Manager.getInstance().getSuperUser(), parent);
	}
	

	public Directory(Manager manager, Element dirNode) throws UnsupportedEncodingException {
		this.xmlImport(manager, dirNode);
	}

	private File getFileByName(String name) throws FileDoesntExistsInDirectoryException{
		if (name.equals("."))
			return this;
		if (name.equals(".."))
			return getParent();

		for (File file : getFileSet()){
			if (file.getName().equals(name))
				return file;
		}
		throw new FileDoesntExistsInDirectoryException(name, getName());
	}

	public boolean hasFile(String name){
		try{
			getFileByName(name);
		} catch (FileDoesntExistsInDirectoryException e) {
			return false;
		}
		return true;
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
	public void setHomeOwner(User homeOwner) {
		homeOwner.setHome(this);
	}
	
	@Override
	public Element xmlExport() {
		Element dirElement = super.xmlExport();
		dirElement.setName("dir");
		return dirElement;
	}
	
	@Override
	protected void xmlExport(Element myDrive) {
		if (!getFileSet().isEmpty()) {
			for(File f: getFileSet()) {
				if (f.getId() > 2)
					myDrive.addContent(f.xmlExport());	
			}
			for(File f: getFileSet()) {
				if (f!=this)
					f.xmlExport(myDrive);
			}
		}	
	}
	
	@Override
	public void remove() throws IsHomeDirectoryException {
		
		if (this.getHomeOwner() == null) {
			if(!this.getFileSet().isEmpty()) { 
				for(File f: this.getFileSet()){
					f.remove();
				}
			}
			
			super.remove();
		}
		else{ 
			throw new IsHomeDirectoryException(this.getName());
		}
	}
	

	public List<File> getOrderByNameFileList() {
		List<File> files = new ArrayList<File>(getFileSet());

		Collections.sort(files, new Comparator<File>() {
			public int compare(File f1, File f2) {
				return f1.getName().compareToIgnoreCase(f2.getName());
			}
		});

		return files;
	}

	protected Directory createPath(User owner, String path) {
		if(path.startsWith("/")) path = path.substring(1);

		return createPath(owner, path, this);
	}


	private Directory createPath(User owner, String path, Directory dir) {
		Directory nextDir;
		int first = path.indexOf('/');

		if(first == -1) {
			if (!dir.hasFile(path)) return new Directory(path,  owner, dir);
			else return (Directory) dir.getFileByName(path);
		}
		String dirName = path.substring(0, first);
		String nextPath =  path.substring(first + 1);

		if(dir.hasFile(dirName)) {
			nextDir = (Directory) dir.getFileByName(dirName);
			return createPath(owner, nextPath, nextDir);
		} else {
			nextDir = new Directory(dirName,  owner, dir);
			return createPath(owner, nextPath, nextDir);
		}
	}

	public void write(User u, String content){
		throw new CannotWriteException("Cannot write in a Directory");
	}
}


