package pt.tecnico.myDrive.domain;

import org.jdom2.Element;
import pt.tecnico.myDrive.exception.FileAlreadyExistsException;

import java.io.UnsupportedEncodingException;

public class App extends App_Base {
    
    public App(String name, String permission, Manager manager, User owner, Directory parent, String content) {
    	this.initFile(name, permission, manager, owner, parent);
    	this.setContent(content);
    }

    public App(Manager manager, Element appNode){ //throws UserDoesNotExistException{

        String path = appNode.getChild("path").getValue();
        String ownerName = appNode.getChild("owner").getValue();
        String name = appNode.getChild("name").getValue();

        User user = manager.getUserByUsername(ownerName);

        Directory barra = manager.getHomeDirectory().getParent();
        //Directory parent = (Directory) barra.lookup(path);

        if (user == null){
            //throw new UserDoesNotExistException(ownerName);
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
    public void xmlImport(Element appNode) { //throws ImportDocumentException {
        super.xmlImport(appNode);
        /*try {
            setContent(new String(appNode.getChild("method").getValue().getBytes("UTF-8")));
        } catch (UnsupportedEncodingException e) {
            //throw new ImportDocumentException();
        }*/
    }

    @Override
    public String getFileType() {
        return "app";
    }
}
