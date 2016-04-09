package pt.tecnico.myDrive.service;

import pt.tecnico.myDrive.domain.Login;
import pt.tecnico.myDrive.domain.Manager;
import pt.tecnico.myDrive.exception.MyDriveException;

public class TokenValidationService extends MyDriveService {
	protected Login session;
	private long token;

	public TokenValidationService(long token) {
		this.token = token;
	}

	@Override
	protected void dispatch() throws MyDriveException {
		Manager m = Manager.getInstance();
		session = m.getLoginByToken(token);
	}

}
