package pt.tecnico.myDrive.service;

import pt.tecnico.myDrive.domain.File;
import pt.tecnico.myDrive.exception.MyDriveException;

public class ReadFileService extends TokenValidationService {
	private String contents;
	private String fileToRead;
	
	public ReadFileService(Long token, String name) {
		super(token);
		fileToRead = name;
	}

	@Override
	protected void dispatch() throws MyDriveException {
		super.dispatch();
		File file = session.getCurrentDir().lookup(fileToRead, session.getCurrentUser());
		if (file != null) {
			contents = file.read(session.getCurrentUser());
		}
	}

	public final String result() {
		return contents;
	}

}
