package pt.tecnico.myDrive.domain;

import org.jdom2.Element;
import pt.tecnico.myDrive.exception.ImportDocumentException;

import java.io.UnsupportedEncodingException;

public class App extends App_Base {

    public App(String name, String permission, Manager manager, User owner, Directory parent, String content) {
    	this.initFile(name, permission, manager, owner, parent);
    	this.setContent(content);
    }

    public App(Manager manager, Element appNode) throws UnsupportedEncodingException {
        try {
            String method = new String(appNode.getChildText("method").getBytes("UTF-8"));
            setContent(method);
            this.xmlImport(manager, appNode);
        } catch (UnsupportedEncodingException e) {
            throw new ImportDocumentException();
        }
    }

    @Override
    public String getFileType() {
        return "app";
    }
}
