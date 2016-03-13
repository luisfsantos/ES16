package pt.tecnico.myDrive.domain;

import org.jdom2.DataConversionException;
import org.jdom2.Element;
import org.joda.time.DateTime;
import pt.tecnico.myDrive.exception.FileAlreadyExistsException;
import pt.tecnico.myDrive.exception.ImportDocumentException;

import java.io.UnsupportedEncodingException;

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
		return null; //TODO - create exception
	}

	public App createApp(String name, Manager manager, User owner, String content) {
		return null; //TODO - create exception
	}

	public Link createLink(String name, Manager manager, User owner, String content) {
		return null; //TODO - create exception
	}

	public PlainFile createPlainFile(String name, Manager manager, User owner, String content) {
		return null; //TODO - create exception
	}

	/* C
	@Override
	public void setPermissions(String permission) throws WrongSizePermissionException, InvalidCharPermissionException {
		if(permission.length() != 8) throw new WrongSizePermissionException(permisson.length());
		Mask mask[] = Mask.values();
		//String mask = "rwxdrwxd";
		for(int i = 0; i < 8; i++){
			//if(permission.charAt(i) != mask.charAt(i) && permission.charAt(i) != '-')
			if(permission.charAt(i) != mask[4 % i].getValue() && permission.charAt(i) != '-')
				throw new InvalidCharPermissionException(permission.charAt(i), i);
		}
	}
	
	public void setPermissions(){
		super.setPermissions(this.getUser().getUmask());
	}

	public File lookup(String path) {
		
		
		
	}
	


	C */

	public String getAbsolutePath() {
		if (this == getParent())
			return "";
		else return getParent().getAbsolutePath() + "/" + getName();
	}

	public Element xmlExport() {
		// TODO Auto-generated method stub
		return null;
	}

	public void xmlImport(Element fileNode) throws UnsupportedEncodingException {
		DateTime dt = new DateTime();
		try {
			setId(getManager().getNextIdCounter());
			setLastModified(dt.minusMillis(0));
			setParent((Directory) getManager().getRootDirectory().lookup(new String(fileNode.getChild("path").getValue().getBytes("UTF-8"))));
			setName(new String(fileNode.getChild("name").getValue().getBytes("UTF-8")));
			setOwner(getManager().getUserByUsername(new String(fileNode.getChild("owner").getValue().getBytes("UTF-8"))));
			setPermissions(new String(fileNode.getChild("perm").getValue().getBytes("UTF-8")));
		} catch (UnsupportedEncodingException e) {
			throw new UnsupportedEncodingException();
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
	public void showContent(){}
	public void setContent(String x){}
	public void lsDir(){}


	public abstract File lookup(String path);

}

