package pt.tecnico.myDrive.service;

import pt.tecnico.myDrive.domain.*;
import pt.tecnico.myDrive.exception.DirectoryContentNotNullException;
import pt.tecnico.myDrive.exception.InvalidFileTypeException;
import pt.tecnico.myDrive.exception.MyDriveException;

public class CreateFileService extends TokenValidationService {

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
		super.dispatch();
		User user = session.getCurrentUser();
		Directory parentDir = session.getCurrentDir();

		if (type == null) throw new InvalidFileTypeException(type);
		switch (type) {
			case "app":
				new App(name, user, parentDir, contents);
				break;
			case "dir":
				if(contents != null) throw new DirectoryContentNotNullException();
				new Directory(name, user, parentDir);
				break;
			case "link":
				new Link(name, user, parentDir, contents);
				break;
			case "plain":
				new PlainFile(name, user, parentDir, contents);
				break;
			default:
				throw new InvalidFileTypeException(type);
		}

	}

}
