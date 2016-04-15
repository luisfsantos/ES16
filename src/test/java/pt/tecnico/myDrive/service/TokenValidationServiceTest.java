package pt.tecnico.myDrive.service;

import org.junit.Test;

import pt.tecnico.myDrive.domain.Login;
import pt.tecnico.myDrive.exception.InvalidTokenException;

public abstract class TokenValidationServiceTest extends AbstractServiceTest {

	Long noSession = 1L;
	Long expiredSession;

	@Override
	protected void populate() {
		Login root = new Login("root", "***");
		expiredSession = root.getToken();
		root.setLastActivity(root.getLastActivity().minusHours(10));
	}
	
	@Test (expected = InvalidTokenException.class)
	public void noSession() {
		TokenValidationService validate = new TokenValidationService(noSession);
		validate.execute();
	}
	
	@Test (expected = InvalidTokenException.class)
	public void expiredSession() {
		TokenValidationService validate = new TokenValidationService(expiredSession);
		validate.execute();
	}

}
