package pt.tecnico.myDrive.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertThat;

import java.math.BigInteger;
import java.util.Random;

import org.junit.Test;

import pt.tecnico.myDrive.domain.Manager;
import pt.tecnico.myDrive.domain.Directory;
import pt.tecnico.myDrive.domain.PlainFile;
import pt.tecnico.myDrive.domain.Link;
import pt.tecnico.myDrive.domain.App;
import pt.tecnico.myDrive.domain.Login;

import pt.tecnico.myDrive.exception.MyDriveException;

public class CreateFileServiceTest extends AbstractServiceTest {
	private Long token;
	private Directory home;
	private String name = "newFile";
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
		CreateFileService service = new CreateFileService(token, name, "dir", null);
		service.execute();
		assertEquals("Directory not created" , true, home.hasFile(name));
		assertThat(home.getFileByName(name), instanceOf(Directory.class));
	}
	
	@Test
	public void successNewPlainFile() {
		CreateFileService service = new CreateFileService(token, name, "plain", null);
		service.execute();
		assertEquals("Plain File not created" , true, home.hasFile(name));
		assertThat(home.getFileByName(name), instanceOf(PlainFile.class));
	}
	
	@Test
	public void successNewApp() {
		CreateFileService service = new CreateFileService(token, name, "app", null);
		service.execute();
		assertEquals("App not created" , true, home.hasFile(name));
		assertThat(home.getFileByName(name), instanceOf(App.class));
	}
	
	@Test
	public void successNewLink() {
		CreateFileService service = new CreateFileService(token, name, "link", null);
		service.execute();
		assertEquals("Link not created" , true, home.hasFile(name));
		assertThat(home.getFileByName(name), instanceOf(Link.class));
	}

	@Test(expected = MyDriveException.class) //TODO will depend on actual exception for wrong file type
	public void noFileType() {
		CreateFileService service = new CreateFileService(token, name, null, null);
		service.execute();
	}
	
	@Test(expected = MyDriveException.class) //TODO will depend on actual exception for wrong file type
	public void noSession() {
		final long noSessionToken = new BigInteger(64, new Random()).longValue();
		CreateFileService service = new CreateFileService(noSessionToken, name, "dir", null);
		service.execute();
	}
}
