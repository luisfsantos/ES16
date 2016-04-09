package pt.tecnico.myDrive.service;

import pt.tecnico.myDrive.domain.File;
import pt.tecnico.myDrive.domain.Login;
import pt.tecnico.myDrive.domain.Manager;
import pt.tecnico.myDrive.domain.User;
import pt.tecnico.myDrive.exception.MyDriveException;

public class WriteFileService extends MyDriveService{

	private Long token;
	private String filename;
	private String content;

	public WriteFileService(long token, String filename, String content){
		this.token = token;
		this.filename = filename;
		this.content = content;
	}

	public final void dispatch(){

		Manager manager = Manager.getInstance();
		Login login = manager.getLoginByToken(token);
		User user = login.getCurrentUser();

		if (login.validateToken(token)){
			File f = login.getCurrentDir().getFileByName(filename);
			f.write(user,content);  //polymorph
		}
	}
}
	
}