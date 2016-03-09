package pt.tecnico.myDrive.domain;

public class PlainFile extends PlainFile_Base {

    public PlainFile() {
        super();
    }

    public  PlainFile(String content) {
        super();
        setContent(content);
    }

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
}
