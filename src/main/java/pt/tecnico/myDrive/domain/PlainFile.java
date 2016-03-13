package pt.tecnico.myDrive.domain;

import org.apache.commons.lang.ObjectUtils;
import org.jdom2.Element;
import pt.tecnico.myDrive.exception.FileAlreadyExistsException;
import pt.tecnico.myDrive.exception.ImportDocumentException;
import pt.tecnico.myDrive.exception.IsNotDirOrLinkException;

import java.io.UnsupportedEncodingException;

public class PlainFile extends PlainFile_Base {

    public PlainFile() {
        super();
    }

    public PlainFile(String name, String permission, Manager manager, User owner, Directory parent, String content) {
        this.initFile(name, permission, manager, owner, parent);
        this.setContent(content);
    }

    public PlainFile(Manager manager, Element plainNode) throws UnsupportedEncodingException {
        try {
            String contents = new String(plainNode.getChildText("contents").getBytes("UTF-8"));
            setContent(contents);
            this.xmlImport(manager, plainNode);
        } catch (UnsupportedEncodingException e) {
            throw new ImportDocumentException("UnsupportedEncodingException");
        }
    }

    public File lookup(String path) throws IsNotDirOrLinkException{
    	
    	if (path.indexOf('/') == -1){
    		return this;
    	}
    	else {
    		throw new IsNotDirOrLinkException(this.getName());
    	}
    }

    public void showContent() {
        if(super.getContent() == null)
            System.out.println("");
        else
            System.out.println(super.getContent());
    }

    @Override
    public int getSize() {
        return getContent().length();
    }

    @Override
    public String getFileType() {
        return "plain-file";
    }
    
	public void remove(){
		setParent(null);
		setOwner(null);
		setManager(null);
		deleteDomainObject();
	}

	@Override
	public Element xmlExport() {
		Element element = new Element("plain");
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

		Element contentsElement = new Element("contents");
		contentsElement.setText(getContent());
		element.addContent(contentsElement);

		return element;
	}

	
}


