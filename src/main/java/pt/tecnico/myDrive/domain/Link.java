package pt.tecnico.myDrive.domain;

public class Link extends Link_Base {
    
    public Link() {
        super();
    }

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

}
