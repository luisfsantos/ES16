package pt.tecnico.myDrive.domain;

public class Link extends Link_Base {
    
    public Link(String name, String permission, Manager manager, User owner, Directory parent, String content) {
        this.initFile(name, permission, manager, owner, parent);
        this.setContent(content);
    }

    @Override
    public String getFileType() {
        return "link";
    }
}
