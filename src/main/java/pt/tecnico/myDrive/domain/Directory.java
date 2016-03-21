package pt.tecnico.myDrive.domain;

import org.jdom2.Element;
import pt.tecnico.myDrive.exception.*;

import java.util.*;

public class Directory extends Directory_Base {
	
	protected Directory() {
		super();
	}
	
	public Directory(String name, User owner, Directory parent) {
		verifyFileNameDir(name);
		this.initFile(name, owner.getUmask(), owner, parent);
	}
	
	public Directory(String name, Directory parent) {
		verifyFileNameDir(name);
		this.initFile(name, Manager.getInstance().getSuperUser().getUmask(), Manager.getInstance().getSuperUser(), parent);
	}
	

	public Directory(Manager manager, Element dirNode) {
		this.xmlImport(manager, dirNode);
	}


	public void verifyFileNameDir(String name) throws FileAlreadyExistsInDirectoryException, InvalidFileNameException{ //CHANGE EXCEPTION NAME
		if ((name.indexOf('/') >= 0) || (name.indexOf('\0') >= 0))
			throw new InvalidFileNameException(name);
		for (File f : this.getFileSet()) {
			if(f.getName().equals(name))
				throw new FileAlreadyExistsInDirectoryException(name, this.getName());
		}
	}
	

	public File getFileByName(String name) throws FileDoesntExistsInDirectoryException{
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
	public void showContent() {
		List<File> files = new ArrayList<File>(getFileSet());

		Collections.sort(files, new Comparator<File>() {
			public int compare(File f1, File f2) {
				return f1.getName().compareToIgnoreCase(f2.getName());
			}
		});

		System.out.println(this.toString("."));
		System.out.println(getParent().toString(".."));

		for (File f: files)
			System.out.println(f.toString());
    }
	
	@Override
	public Element xmlExport() {
		Element dirElement = super.xmlExport();
		dirElement.setName("dir");
		return dirElement;
	}
	
	@Override
	public void xmlExport(Element myDrive) {
		if (!getFileSet().isEmpty()) {
			for(File f: getFileSet()) {
				if (f.getId() > 2)
					myDrive.addContent(f.xmlExport());	
			}
			for(File f: getFileSet()) { //why does the fileSet have itself...?
				if (f!=this)
					f.xmlExport(myDrive);
			}
		}	
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
	
public void remove() throws NotEmptyDirectoryException {
		if (this.getFileSet().size() == 0) this.rmv();
		else throw new NotEmptyDirectoryException(this.getName());
	}
	
	public void rmv() {
		setParent(null);
		setOwner(null);
		deleteDomainObject();	
		
	}
}
