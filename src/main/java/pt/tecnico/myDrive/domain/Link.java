package pt.tecnico.myDrive.domain;

import org.jdom2.Element;
import org.joda.time.DateTime;
import pt.tecnico.myDrive.exception.FileAlreadyExistsException;
import pt.tecnico.myDrive.exception.UserDoesNotExistException;

import java.io.UnsupportedEncodingException;

public class Link extends Link_Base {
    
    public Link(String name, String permission, Manager manager, User owner, Directory parent, String content) {
        this.initFile(name, permission, manager, owner, parent);
        this.setContent(content);
    }

    public Link(Manager manager, Element linkNode) throws UnsupportedEncodingException {
        String path = linkNode.getChild("path").getValue();
        String ownerName = linkNode.getChild("owner").getValue();
        String name = linkNode.getChild("name").getValue();

        User user = manager.getUserByUsername(ownerName);

        Directory barra = manager.getHomeDirectory().getParent();

        if (user == null){
            throw new UserDoesNotExistException(ownerName);
        }

        try {
            barra.lookup(path);
        } catch (NullPointerException a) {
            manager.createMissingDirectories(path);
            setManager(manager);
            xmlImport(linkNode);
            return;
        } finally {
            Directory parent = (Directory) barra.lookup(path);
            if (!parent.hasFile(name)) {
                setManager(manager);
                xmlImport(linkNode);
            } else {
                throw new FileAlreadyExistsException(1111); //random
            }
        }
    }

    
    @Override
    public void xmlImport(Element dirNode) throws UnsupportedEncodingException {
        DateTime dt = new DateTime();
        try {
            setId(getManager().getNextIdCounter());
            setLastModified(dt.minusMillis(0));
            setParent((Directory) getManager().getRootDirectory().lookup(new String(dirNode.getChild("path").getValue().getBytes("UTF-8"))));
            setName(new String(dirNode.getChild("name").getValue().getBytes("UTF-8")));
            setOwner(getManager().getUserByUsername(new String(dirNode.getChild("owner").getValue().getBytes("UTF-8"))));
            setPermissions(new String(dirNode.getChild("perm").getValue().getBytes("UTF-8")));
            setContent(new String(dirNode.getChild("value").getValue().getBytes("UTF-8")));
        } catch (UnsupportedEncodingException e) {
            throw new UnsupportedEncodingException();
        }
    }

    @Override
    public String getFileType() {
        return "link";
    }
    
    @Override
	public Element xmlExport() {
		Element element = new Element("link");
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
		
		Element valueElement = new Element("value");
		valueElement.setText(getContent());
		element.addContent(valueElement);
		
		return element;
	}
}
