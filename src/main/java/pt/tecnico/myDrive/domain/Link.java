package pt.tecnico.myDrive.domain;

import org.jdom2.Element;
import pt.tecnico.myDrive.exception.FileAlreadyExistsException;

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
        //Directory parent = (Directory) barra.lookup(path);

        if (user == null){
            //throw new UserDoesNotExistException(ownerName);
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
    public void xmlImport(Element dirNode) {
        super.xmlImport(dirNode);
        /*try {
            setContent(new String(dirNode.getChild("value").getValue().getBytes("UTF-8")));
        } catch (UnsupportedEncodingException e) {
            //throw new ImportDocumentException();
        }*/
    }

    @Override
    public String getFileType() {
        return "link";
    }
}
