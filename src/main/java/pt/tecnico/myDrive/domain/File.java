package pt.tecnico.myDrive.domain;

import org.jdom2.Element;
import org.joda.time.DateTime;
import pt.tecnico.myDrive.exception.*;

import java.io.UnsupportedEncodingException;
import java.util.regex.Pattern;

public abstract class File extends File_Base {

	
	protected void initFile(String name, String permission, User owner, Directory parent) {
		if(owner.hasPermission(parent, Mask.WRITE)) {
			this.setParent(parent);
			this.setName(name);
			this.setOwner(owner);
			this.setPermissions(permission);
			this.setId(owner.getNextIdCounter());
			this.setLastModified(new DateTime());
		} else {
			throw new AccessDeniedException("create new file " + name +" on", parent.getName());
		}
	}


	protected void initRootDirectory( SuperUser superUser){
    	super.setParent(null);
    	super.setName("/");
		super.setOwner(superUser);
		super.setPermissions("rwxdr-x-");
		super.setId(superUser.getNextIdCounter());
		super.setLastModified(new DateTime());
	}
	
	
	@Override
	public void setName(String name){
		if(name == null)
			throw new InvalidFileNameException(name);
		if((name.indexOf('/') >= 0) || (name.indexOf('\0') >= 0) ||  name.equals(""))
			throw new InvalidFileNameException(name);
		if((getParent().getAbsolutePath().length() + 1 + name.length()) > 1024)
			throw new InvalidFileNameException(name);

		if (name.equals(".") || name.equals(".."))
			throw new FileAlreadyExistsInDirectoryException(name, getParent().getName());
		for (File f : getParent().getFileSet()) {
			if(!f.equals(this) && f.getName().equals(name)){
				throw new FileAlreadyExistsInDirectoryException(name, this.getParent().getName());
			}
		}
		
		super.setName(name);
		this.setLastModified(new DateTime());	
	}

	
	@Override
	public void setParent(Directory parent){
		parent.addFile(this);
	}
	

	@Override
	public void setOwner(User user) {
		if(Manager.getInstance().hasUser(user.getUsername())) {
			super.setOwner(user);
		} else {
			throw new UserDoesNotExistException(user.getUsername());
		}
	}

	public abstract String read(User user);

	@Override
	public void setPermissions(String permission) throws InvalidPermissionException {
		boolean isPermissionString =
				Pattern.matches("[r-][w-][x-][d-][r-][w-][x-][d-]", permission);
		if (!isPermissionString)
			throw new InvalidPermissionException(permission);
		super.setPermissions(permission);
		this.setLastModified(new DateTime());
	}

	public String getAbsolutePath() {
		if (getParent() == getParent().getParent())
			return getParent().getAbsolutePath() + getName();
		else
			return getParent().getAbsolutePath() + "/" + getName();
	}

	public Element xmlExport() {
		Element element = new Element("file");
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
	
	protected void xmlExport(Element myDrive) {}

	public void xmlImport(Manager manager, Element fileNode) throws UnsupportedEncodingException {
		String path = fileNode.getChildText("path");
		String name = fileNode.getChildText("name");
		String owner = fileNode.getChildText("owner");
		String perm = fileNode.getChildText("perm"); 

		if (name == null || path == null) {
			throw new ImportDocumentException("Missing name or path value");
		}

		setName(new String(name.getBytes("UTF-8")));

		
		User ownerUser = manager.fetchUser(fileNode);
		if (ownerUser == null) {
			throw new UserDoesNotExistException(owner);
		}
		setOwner(ownerUser);
		setId(ownerUser.getNextIdCounter());
		

		if(perm != null) setPermissions(new String(perm.getBytes("UTF-8")));
		else setPermissions("rwxd----");

		setLastModified(new DateTime());

		Directory parentDir = manager.getRootDirectory().createPath(manager.getSuperUser(),
				new String(path.getBytes("UTF-8")));
		parentDir.addFile(this);
	}

	public void remove(){
		super.setParent(null);
		super.setOwner(null);
		deleteDomainObject();
	}
	
	public abstract File lookup(String path, User user);

}

