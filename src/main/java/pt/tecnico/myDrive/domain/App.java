package pt.tecnico.myDrive.domain;

import org.jdom2.Element;
import org.joda.time.DateTime;
import pt.tecnico.myDrive.exception.FileAlreadyExistsException;
import pt.tecnico.myDrive.exception.UserDoesNotExistException;

import java.io.UnsupportedEncodingException;

public class App extends App_Base {
    
    public App(String name, String permission, Manager manager, User owner, Directory parent, String content) {
    	this.initFile(name, permission, manager, owner, parent);
    	this.setContent(content);
    }

    public App(Manager manager, Element appNode) throws UnsupportedEncodingException { //throws UserDoesNotExistException{


        String path = appNode.getChild("path").getValue();
        String ownerName = appNode.getChild("owner").getValue();
        String name = appNode.getChild("name").getValue();

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
            xmlImport(appNode);
            return;
        } finally {
            Directory parent = (Directory) barra.lookup(path);
            if (!parent.hasFile(name)) {
                setManager(manager);
                xmlImport(appNode);
            } else {
                throw new FileAlreadyExistsException(1111); //random
            }
        }

    }


    @Override
    public void xmlImport(Element appNode) throws UnsupportedEncodingException {
        DateTime dt = new DateTime();
        try {
            setId(getManager().getNextIdCounter());
            setLastModified(dt.minusMillis(0));
            setParent((Directory) getManager().getRootDirectory().lookup(new String(appNode.getChild("path").getValue().getBytes("UTF-8"))));
            setName(new String(appNode.getChild("name").getValue().getBytes("UTF-8")));
            setOwner(getManager().getUserByUsername(new String(appNode.getChild("owner").getValue().getBytes("UTF-8"))));
            setPermissions(new String(appNode.getChild("perm").getValue().getBytes("UTF-8")));
            setContent(new String(appNode.getChild("method").getValue().getBytes("UTF-8")));
        } catch (UnsupportedEncodingException e) {
            throw new UnsupportedEncodingException();
        }
   }



    @Override
    public String getFileType() {
        return "app";
    }
    
    @Override
	public Element xmlExport() {
		Element element = new Element("app");
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
		
		Element methodElement = new Element("method");
		methodElement.setText(getContent());
		element.addContent(methodElement);
		
		return element;
	}
}
