package pt.tecnico.myDrive.domain;

import org.jdom2.Element;
import pt.tecnico.myDrive.exception.*;


import pt.tecnico.myDrive.exception.CannotReadException;
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
        String value = new String(linkNode.getChildText("value").getBytes("UTF-8"));
        setContent(value);
        this.xmlImport(manager, linkNode);
    }

    @Override
    public String read(User user) {
    	File endpoint = lookup(super.getContent(), user);
    	if (endpoint == null) {
    		throw new CannotReadException("File does not exist");
    	} else {
    		return endpoint.read(user);
    	}
    }
    
    @Override
	public Element xmlExport() {
    	Element linkElement = super.xmlExport();
		linkElement.setName("link");

		linkElement.getChild("contents").setName("value");

		return linkElement;
	}

    public File lookup(String path, User user, int psize) {
        return this.getParent().lookup(this.getContent().concat("/"+path), user, psize);
    }

    @Override
    public void write(User u, String content) {
        try {
            File target = this.lookup(this.getContent(),u);
            if (u.hasPermission(target, Mask.WRITE)) {
                target.write(u, content);
            } else {
                throw new InvalidPermissionException("Write in App"); //not sure about argument
            }
        }catch (StackOverflowError e){
            throw new PathHasMoreThan1024CharactersException();
        }
    }




}
