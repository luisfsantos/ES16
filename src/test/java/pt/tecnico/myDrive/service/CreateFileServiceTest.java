package pt.tecnico.myDrive.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import pt.tecnico.myDrive.domain.Manager;
import pt.tecnico.myDrive.domain.Directory;
import pt.tecnico.myDrive.domain.Login;

public class CreateFileServiceTest extends AbstractServiceTest {
	private Long token;
	private Directory home;
	@Override
	protected void populate() {
		Manager m = Manager.getInstance();
		
		Login l = new Login("root", "***");
		token = l.getToken();
		
		home = (Directory) m.getRootDirectory().getFileByName("home");
		new Directory("myhome", home);
	}
	
	@Test
	public void successNewDirectory() {
		final String directoryName = "newDirectory";
		CreateFileService service = new CreateFileService(token, directoryName, "dir", null);
		service.execute();
		assertEquals("File not created" , true, home.hasFile(directoryName));
	}

}
