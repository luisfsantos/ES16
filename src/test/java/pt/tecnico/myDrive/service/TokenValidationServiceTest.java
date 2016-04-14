package pt.tecnico.myDrive.service;

import org.joda.time.DateTime;
import org.junit.Test;

import pt.tecnico.myDrive.domain.Login;
import pt.tecnico.myDrive.exception.InvalidTokenException;

public class TokenValidationServiceTest extends AbstractServiceTest {

	Long noSession = 1L;
	Long expiredSession;

	@Override
	protected void populate() {
		Login root = new Login("root", "***");
		expiredSession = root.getToken();
		//FIXME
		//root.setLastActivity(new DateTime());
	}
	/*
	@Test (expected = InvalidTokenException.class)
	public void noSession() {
		TokenValidationService validate = new TokenValidationService(noSession);
		validate.execute();
	}
	
	@Test (expected = InvalidTokenException.class)
	public void expiredSession() {
		TokenValidationService validate = new TokenValidationService(expiredSession);
		validate.execute();
	}*/

}
