package pt.tecnico.myDrive.domain;

public class PlainFile extends PlainFile_Base {

    public PlainFile() {
        super();
    }

    public PlainFile(String name, String permission, Manager manager, User owner, Directory parent, String content) {
        this.initFile(name, permission, manager, owner, parent);
        this.setContent(content);
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
    public int getSize() {
        return getContent().length();
    }

    @Override
    public String getFileType() {
        return "plain-file";
    }
    
	public void remove(){
		setParent(null);
		setOwner(null);
		setManager(null);
		deleteDomainObject();
	}
    
}
