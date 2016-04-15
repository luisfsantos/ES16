package pt.tecnico.myDrive.service;

import pt.tecnico.myDrive.domain.Login;
import pt.tecnico.myDrive.exception.MyDriveException;

public class LoginService extends MyDriveService {

    private String name;
    private String password;
    private Long token;

    public LoginService(String name, String password) {
    	this.name = name;
    	this.password = password;
    }
    
    
    public final Long result() {
        return token;
    }

    @Override
    protected void dispatch() throws MyDriveException {
        Login login = new Login(name, password);
        token = login.getToken();
    }

}