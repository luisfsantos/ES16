package pt.tecnico.myDrive.domain;

import org.jdom2.Element;
import pt.tecnico.myDrive.exception.ImportDocumentException;

import java.io.UnsupportedEncodingException;

public class Link extends Link_Base {
    
    public Link(String name, User owner, Directory parent, String content) {
        this.initFile(name, owner.getUmask(), owner, parent);
        this.setContent(content);
    }
    
    public Link(String name, Directory parent, String content) {
        this.initFile(name, Manager.getInstance().getSuperUser().getUmask(), Manager.getInstance().getSuperUser(), parent);
        this.setContent(content);
    }

    public Link(Manager manager, Element linkNode) throws UnsupportedEncodingException {
        try {
            String value = new String(linkNode.getChildText("value").getBytes("UTF-8"));
            setContent(value);
            this.xmlImport(manager, linkNode);
        } catch (UnsupportedEncodingException e) {
            throw new ImportDocumentException("UnsupportedEncodingException");
        }
    }

    @Override
    public String getFileType() {
        return "link";
    }
    
    @Override
	public Element xmlExport() {
    	Element linkElement = super.xmlExport();
		linkElement.setName("link");
		
		linkElement.getChild("contents").setName("value");
		
		return linkElement;
	}
}
