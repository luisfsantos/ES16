package pt.tecnico.myDrive.service;

import pt.tecnico.myDrive.domain.File;
import pt.tecnico.myDrive.domain.Login;
import pt.tecnico.myDrive.domain.Manager;
import pt.tecnico.myDrive.domain.User;
import pt.tecnico.myDrive.exception.MyDriveException;

public class CreateFileService extends TokenValidationService {
	
	private Long token;
	private String name; 
	private String type; 
	private String contents;
	
	public CreateFileService(Long token, String name, String type, String contents) {
		super(token);
		this.name = name;
		this.type = type;
		this.contents = contents;
	}

	@Override
	protected void dispatch() throws MyDriveException {

		Manager manager = Manager.getInstance();
		Login login = manager.getLoginByToken(token);
		User user = login.getCurrentUser();

		if (login.validateToken(token)){
			login.getCurrentDir().createFile(user,name, type, contents);
		}

	}

}
