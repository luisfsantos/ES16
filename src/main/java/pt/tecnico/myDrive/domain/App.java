package pt.tecnico.myDrive.domain;

public class App extends App_Base {
    
    public App() {
        super();
    }

    @Override
    public String toString() {
        return "app " +
                super.getPermissions() +
                super.getContent().length() +
                " " + super.getUser().getUsername() +
                " " + super.getId() +
                " " + super.getLastModified() +
                " " + super.getName();
    }
    
}
