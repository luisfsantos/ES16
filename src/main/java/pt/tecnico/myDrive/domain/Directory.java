package pt.tecnico.myDrive.domain;

import org.jdom2.Element;
import org.joda.time.DateTime;

import pt.tecnico.myDrive.exception.*;

import java.io.UnsupportedEncodingException;
import java.util.*;

public class Directory extends Directory_Base {
	final int max_path = 1024;
	
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
	
	@Override
	public Set<File> getFile() {
		throw new AccessDeniedException("read", super.getName());
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

	public File lookup(String path, User user) {
		if(path.length() <= max_path) {
			return lookup(path, user, max_path);
		} else {
			throw new PathTooBigException();
		}
	}

	public File lookup(String path, User user, int psize) {
		if (path.startsWith("/")) {
			if (this != getParent()) {
				return getParent().lookup(path, user);
			} else {
				while (path.startsWith("/")) {
					path = path.substring(1);
					psize--;
					if(psize < 0 )
						throw new PathTooBigException();
				}
			}
		}
		
		if(user.hasPermission(this, Mask.EXEC)) {
			String name;

			while (path.endsWith("/")) {
				path = path.substring(0, path.lastIndexOf('/'));
				psize--;
				if(psize < 0 )
					throw new PathTooBigException();
			}

			if (path.startsWith("/")) {
				if (this != getParent()) {
					return getParent().lookup(path, user);
				} else {
					while (path.startsWith("/")) {
						path = path.substring(1);
						psize--;
						if(psize < 0 )
							throw new PathTooBigException();
					}
				}
			}

			if (path.indexOf('/') == -1) {
				name = path;
				psize -= path.length();
				if(psize < 0 )
					throw new PathTooBigException();
				return getFileByName(name);
			}

			name = path.substring(0, path.indexOf("/", 1));
			path = path.substring(path.indexOf("/", 1) + 1);
			psize -= (name.length());
			while (path.startsWith("/"))
				path = path.substring(1);
				psize--;
				if(psize < 0 )
					throw new PathTooBigException();
			if (hasFile(name))
				return this.getFileByName(name).lookup(path, user, psize);

			return null;
		} else {
			throw new AccessDeniedException("search", getName());
		}
	}
	
	@Override
	public void setHomeOwner(User homeOwner) {
		homeOwner.setHome(this);
	}
	
	@Override 
	public void addFile(File file){
		super.addFile(file);
		this.setLastModified(new DateTime());
	}
	
	@Override
	public void addLogin(Login login){
		throw new AccessDeniedToManipulateLoginException();
	}
	
	@Override
	public Set <Login> getLoginSet(){
		throw new AccessDeniedToManipulateLoginException();
	}
	
	@Override
	public User getHomeOwner() {
		throw new AccessDeniedException("get home owner", "Directory");
	}
	
	@Override
	public void remove() throws IsHomeDirectoryException {
		
		if (super.getHomeOwner() == null) {
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
	
	@Override
	public String read(User user) {
		throw new CannotReadException("A directory cannot be read");
	}

	public List<File> getOrderByNameFileList(User user) {
		if (user.hasPermission(this, Mask.READ)) {
			List<File> files = new ArrayList<File>(super.getFileSet());

			Collections.sort(files, new Comparator<File>() {
				public int compare(File f1, File f2) {
					return f1.getName().compareToIgnoreCase(f2.getName());
				}
			});

			return files;
		} else {
			throw new AccessDeniedException("list directory contents", super.getName());
		}
		
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

}


