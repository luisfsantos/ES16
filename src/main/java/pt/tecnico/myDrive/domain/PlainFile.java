package pt.tecnico.myDrive.domain;

import org.apache.commons.lang.ObjectUtils;
import org.jdom2.Element;
import pt.tecnico.myDrive.exception.FileAlreadyExistsException;
import pt.tecnico.myDrive.exception.ImportDocumentException;
import pt.tecnico.myDrive.exception.IsNotDirOrLinkException;

import java.io.UnsupportedEncodingException;

public class PlainFile extends PlainFile_Base {

    protected PlainFile() {
        super();
    }

    public PlainFile(String name, User owner, Directory parent, String content) {
        this.initFile(name, owner.getUmask(), owner, parent);
        this.setContent(content);
    }
    
    public PlainFile(String name, Directory parent, String content) {
        this.initFile(name, Manager.getInstance().getSuperUser().getUmask(), Manager.getInstance().getSuperUser(), parent);
        this.setContent(content);
    }

    public PlainFile(Manager manager, Element plainNode) throws UnsupportedEncodingException {
        String contents = new String(plainNode.getChildText("contents").getBytes("UTF-8"));
        setContent(contents);
        this.xmlImport(manager, plainNode);
    }

    public File lookup(String path) throws IsNotDirOrLinkException{
    	
    	if (path.indexOf('/') == -1){
    		return this;
    	}
    	else {
    		throw new IsNotDirOrLinkException(this.getName());
    	}
    }

	@Override
	public Element xmlExport() {
		Element plainElement = super.xmlExport();
		plainElement.setName("plain");

		Element contentsElement = new Element("contents");
		contentsElement.setText(getContent());
		plainElement.addContent(contentsElement);

		return plainElement;
	}

	public void write(User u, String content){};
}


