package pt.tecnico.myDrive.domain;

import org.jdom2.Element;

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
    
    protected File lookup(String path, User user, int psize) {
    	int psize_resolved = psize + this.getName().length() + 1;
    	return this.getParent().lookup(this.viewContent().concat("/" + path), user, psize_resolved);
    }

	@Override
	public String getType() {
		return "Link";
	}

	@Override
    public String read(User user) {
    	File endpoint = this.resolveLink(user);
    	if (endpoint == null) {
    		throw new CannotReadException("File does not exist");
    	} else {
    		return endpoint.read(user);
    	}
    }
    
    public File resolveLink(User user) {
    	int max_content = 1024;
    	max_content -= Math.max(viewContent().lastIndexOf("/"), 0);
    	File endpoint = this.getParent().lookup(viewContent(), user);
    	while ((endpoint instanceof Link) && max_content > 0) {
    		if (((Link) endpoint).viewContent().contains("/")) {
    			max_content -= ((Link) endpoint).viewContent().lastIndexOf("/");
    		} else {
    			max_content -= ((Link) endpoint).viewContent().length();
    		}
    		endpoint = endpoint.lookup("", user, max_content);
    	}
    	return endpoint;

    }
    
    @Override
	public Element xmlExport() {
    	Element linkElement = super.xmlExport();
		linkElement.setName("link");
		
		linkElement.getChild("contents").setName("value");
		
		return linkElement;
	}
}
