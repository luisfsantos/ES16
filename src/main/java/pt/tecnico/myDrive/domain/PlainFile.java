package pt.tecnico.myDrive.domain;

import org.jdom2.Element;
import pt.tecnico.myDrive.exception.FileAlreadyExistsException;
import pt.tecnico.myDrive.exception.ImportDocumentException;

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
            throw new ImportDocumentException();
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


