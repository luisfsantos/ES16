package pt.tecnico.myDrive.service;

import pt.tecnico.myDrive.domain.Login;
import pt.tecnico.myDrive.exception.EmptyUsernameException;
import pt.tecnico.myDrive.exception.MyDriveException;

public class LoginService extends MyDriveService {

    private String username;
    private String password;
    private Long token;

    public LoginService(String username, String password) {
		if (username == ""){
			throw new EmptyUsernameException();
		}
    	this.username = username;
    	this.password = password;
    }
    
    
    public final Long result() {
        return token;
    }

    @Override
    protected void dispatch() throws MyDriveException {
        Login login = new Login(username, password);
        token = login.getToken();
    }

}