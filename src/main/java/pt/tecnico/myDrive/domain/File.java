package pt.tecnico.myDrive.domain;

import org.jdom2.Element;
import org.joda.time.DateTime;
import pt.tecnico.myDrive.exception.*;

import java.io.UnsupportedEncodingException;
import java.util.regex.Pattern;

public abstract class File extends File_Base {

	public File() {
		super();
	}

	protected void initFile(String name, String permission, Manager manager, User owner, Directory parent) {
		this.setManager(manager);
		this.setOwner(owner);
		this.setParent(parent);
		this.setName(name);
		this.setPermissions(permission);
		this.setId(manager.getNextIdCounter());
		this.setLastModified(new DateTime());
	}


	public Directory createDirectory(String name, Manager manager, User owner) {
		throw new CannotCreateNewFileException(getFileType());
	}

	public App createApp(String name, Manager manager, User owner, String content) {
		throw new CannotCreateNewFileException(getFileType());
	}

	public Link createLink(String name, Manager manager, User owner, String content) {
		throw new CannotCreateNewFileException(getFileType());
	}

	public PlainFile createPlainFile(String name, Manager manager, User owner, String content) {
		throw new CannotCreateNewFileException(getFileType());
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
		if (this == getParent())
			return getName();
		else if (getParent() == getParent().getParent())
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

	public void xmlImport(Manager manager, Element fileNode) {
		String path = fileNode.getChildText("path");
		String name = fileNode.getChildText("name");
		String owner = fileNode.getChildText("owner");
		String perm = fileNode.getChildText("perm"); //TODO Validate length and character

		if (name == null || path == null) {
			throw new ImportDocumentException("Missing name or path value");
		}

		try {
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

			Directory parentDir = manager.createAbsolutePath(new String(path.getBytes("UTF-8")));
			parentDir.verifyFileNameDir(name);
			parentDir.addFile(this);

			setManager(manager);
		} catch (UnsupportedEncodingException e) {
			throw new ImportDocumentException("UnsupportedEncodingException");
		}
	}

	@Override
	public String toString() {
		return getFileType() +
				" " + getPermissions() +
				" " + getSize() +
				" " + getOwner().getUsername() +
				" " + getId() +
				" " + getLastModified().toString("dd/MM/YYYY-HH:mm:ss") +
				" " + getName();
	}

	public abstract String getFileType();
	public abstract int getSize();


	public void remove(){}
	public abstract void showContent();
	public abstract File lookup(String path);

}

