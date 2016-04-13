package pt.tecnico.myDrive.domain;

public class EnvironmentVariable extends EnvironmentVariable_Base {
    
    public EnvironmentVariable(Login login, String name,String value) {
    	this.setLogin(login);
    	this.setName(name);
    	this.setValue(value);
    }
    
}
