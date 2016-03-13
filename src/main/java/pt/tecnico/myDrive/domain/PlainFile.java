package pt.tecnico.myDrive.domain;

import org.apache.commons.lang.ObjectUtils;
import org.jdom2.Element;
import pt.tecnico.myDrive.exception.FileAlreadyExistsException;

import java.io.UnsupportedEncodingException;

public class PlainFile extends PlainFile_Base {

    public PlainFile() {
        super();
    }

    public PlainFile(String name, String permission, Manager manager, User owner, Directory parent, String content) {
        this.initFile(name, permission, manager, owner, parent);
        this.setContent(content);
    }


    public PlainFile(Manager manager, Element plainNode) { //throws UserDoesNotExistException{

        String path = plainNode.getChild("path").getValue();
        String ownerName = plainNode.getChild("owner").getValue();
        String name = plainNode.getChild("name").getValue();

        User user = manager.getUserByUsername(ownerName);

        Directory barra = manager.getRootDirectory();
        //Directory parent = (Directory) barra.lookup(path);

        if (user == null) {
            //throw new UserDoesNotExistException(ownerName);
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



    public void xmlImport(Element plainNode) {
        super.xmlImport(plainNode);
        /*String a= plainNode.getChild("contents").getValue();
        System.out.println(a);
        try {
            setContent(a);
        } catch (UnsupportedEncodingException e) {
*/
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
    
	
}


