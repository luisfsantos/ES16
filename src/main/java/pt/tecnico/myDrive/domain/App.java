package pt.tecnico.myDrive.domain;

public class App extends App_Base {
    
    public App(String name, String permission, Manager manager, User owner, Directory parent, String content) {
    	this.initFile(name, permission, manager, owner, parent);
    	this.setContent(content);
    }

    @Override
    public String getFileType() {
        return "app";
    }
}
