package pt.tecnico.myDrive.domain;

import org.apache.commons.lang.ObjectUtils;
import org.jdom2.Element;
import pt.tecnico.myDrive.exception.FileAlreadyExistsException;
import pt.tecnico.myDrive.exception.UserDoesNotExistException;

import java.io.UnsupportedEncodingException;

public class PlainFile extends PlainFile_Base {

    public PlainFile() {
        super();
    }

    public PlainFile(String name, String permission, Manager manager, User owner, Directory parent, String content) {
        this.initFile(name, permission, manager, owner, parent);
        this.setContent(content);
    }

    public PlainFile(Manager manager, Element plainNode) throws UnsupportedEncodingException { //throws UserDoesNotExistException{


        String path = plainNode.getChild("path").getValue();
        String ownerName = plainNode.getChild("owner").getValue();
        String name = plainNode.getChild("name").getValue();

        User user = manager.getUserByUsername(ownerName);

        Directory barra = manager.getRootDirectory();

        if (user == null) {
            throw new UserDoesNotExistException(ownerName);
        }

        try {
            barra.lookup(path);
        } catch (NullPointerException a) {
            manager.createMissingDirectories(path);
            setManager(manager);
            xmlImport(plainNode);
            return;
        } finally {
            Directory parent = (Directory) barra.lookup(path);
            if (!parent.hasFile(name)) {
                setManager(manager);
                xmlImport(plainNode);
            } else {
                throw new FileAlreadyExistsException(1111); //random
            }
        }
    }



    public void xmlImport(Element plainNode) throws UnsupportedEncodingException {
        super.xmlImport(plainNode);
        setContent(plainNode.getChild("contents").getValue());
        }



    @Override
    public void setContent(String newContent) {
        String content = super.getContent();
        if(content == null)
            super.setContent(newContent);
        else {
            content += newContent;
            super.setContent(content);
        }
    }


    public File lookup(String path){
    	
    	if (path.indexOf('/') == -1){
    		return this;
    	}
    	else {
    		throw new FileAlreadyExistsException(20000); // CHANGE EXCEPTION
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


