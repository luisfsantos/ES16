package pt.tecnico.myDrive.domain;

public class Link extends Link_Base {
    
    public Link(String name, String permission, Manager manager, User owner, Directory parent, String content) {
        this.initFile(name, permission, manager, owner, parent);
        this.setContent(content);
    }

    /* C
    @Override
    public String toString() {
        return "link " +
                super.getPermissions() +
                super.getContent().length() +
                " " + super.getUser().getUsername() +
                " " + super.getId() +
                " " + super.getLastModified() +
                " " + super.getName();
    }
	C */
}
