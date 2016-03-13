package pt.tecnico.myDrive.domain;

import org.jdom2.Element;
import pt.tecnico.myDrive.exception.FileAlreadyExistsException;

import java.io.UnsupportedEncodingException;

public class PlainFile extends PlainFile_Base {

    public PlainFile() {
        super();
    }
    /*
    public PlainFile(String content) {
        super();
        setContent(content);
    }
    */

    public PlainFile(Manager manager, Element plainNode) { //throws UserDoesNotExistException{

        String path = plainNode.getChild("path").getValue();
        String ownerName = plainNode.getChild("owner").getValue();
        String name = plainNode.getChild("name").getValue();

        User user = manager.getUserByUsername(ownerName);

        Directory barra = manager.getHomeDirectory().getParent();
        Directory parent = (Directory) barra.lookup(path);

        if (user == null){
            //throw new UserDoesNotExistException(ownerName);
        }

        else if(parent == null){
            manager.createMissingDirectories(path);
            setManager(manager);
            xmlImport(plainNode);
        }
        else if (!parent.hasFile(name)){
            setManager(manager);
            xmlImport(plainNode);
        }
        else {
            throw new FileAlreadyExistsException(1111); //random
        }

    }

    public void xmlImport(Element plainNode) {
        super.xmlImport(plainNode);
        try {
            setContent(new String(plainNode.getChild("contents").getValue().getBytes("UTF-8")));
        } catch (UnsupportedEncodingException e) {

        }
    }
    /* C
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

    public void showContent() {
        if(super.getContent() == null)
            System.out.println("");
        else
            System.out.println(super.getContent());
    }

    @Override
    public String toString() {
        return "plain file " +
                super.getPermissions() +
                super.getContent().length() +
                " " + super.getUser().getUsername() +
                " " + super.getId() +
                " " + super.getLastModified() +
                " " + super.getName();
    }
    C */
}
