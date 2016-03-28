package pt.tecnico.myDrive.domain;

import org.jdom2.Element;
import org.joda.time.DateTime;
import pt.tecnico.myDrive.exception.*;

import java.io.UnsupportedEncodingException;
import java.util.regex.Pattern;

public abstract class File extends File_Base {

	
	protected void initFile(String name, String permission, User owner, Directory parent) {
		this.setParent(parent);
		this.setName(name);
		this.setOwner(owner);
		this.setPermissions(permission);
		this.setId(owner.getManager().getNextIdCounter());
		this.setLastModified(new DateTime());
	}


	protected void setRootDirName(String username){
		super.setName(username);
	}
	
	
	@Override
	public void setName(String name){
		
		if ((name.indexOf('/') >= 0) || (name.indexOf('\0') >= 0))
			throw new InvalidFileNameException(name);
		
		for (File f : getParent().getFileSet()) {
			if(f.getName() != null){
				if(f.getName().equals(name)){
					throw new FileAlreadyExistsInDirectoryException(name, this.getParent().getName());
				}
			}
		}
		super.setName(name);
		this.setLastModified(new DateTime());	
	}
	


	@Override
	public void setOwner(User user) {
		if(Manager.getInstance().hasUser(user.getUsername())) {
			super.setOwner(user);
		} else {
			throw new UserDoesNotExistException(user.getUsername());
		}
	}


	@Override
	public void setPermissions(String permission) throws InvalidPermissionException {
		boolean isPermissionString =
				Pattern.matches("[r-][w-][x-][d-][r-][w-][x-][d-]", permission);
		if (!isPermissionString)
			throw new InvalidPermissionException(permission);
		super.setPermissions(permission);
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

		if(owner != null) {
			User ownerUser = manager.getUserByUsername(new String(owner.getBytes("UTF-8")));
			if (ownerUser == null) {
				throw new UserDoesNotExistException(owner);
			}
			setOwner(ownerUser);
		}

		if(perm != null) setPermissions(new String(perm.getBytes("UTF-8")));
		else setPermissions("rwxd----");

		setLastModified(new DateTime());
		setId(manager.getNextIdCounter());

		Directory parentDir = manager.getRootDirectory().createPath(manager.getSuperUser(),
				new String(path.getBytes("UTF-8")));
		parentDir.addFile(this);
	}

	public void remove(){}
	public abstract File lookup(String path);

}

