package pt.tecnico.myDrive.service;

import pt.ist.fenixframework.Atomic;
import pt.tecnico.myDrive.domain.*;
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
		Directory parentDir = login.getCurrentDir();

		if (login.validateToken(token)){
			switch (type) {
				case "app":
					new App(name, user, parentDir, contents);
					break;
				case "dir":
					//FIXME directory must not have contents
					new Directory(name, user, parentDir);
					break;
				case "link":
					new Link(name, user, parentDir, contents);
					break;
				case "plain":
					new PlainFile(name, user, parentDir, contents);
					break;
				default:
					//FIXME type must be one of the above
					break;
			}
		}

	}

}
