package pt.tecnico.myDrive.service;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import pt.tecnico.myDrive.domain.Directory;
import pt.tecnico.myDrive.domain.Login;
import pt.tecnico.myDrive.domain.Manager;
import pt.tecnico.myDrive.domain.User;
import pt.tecnico.myDrive.exception.AccessDeniedException;
import pt.tecnico.myDrive.exception.FileDoesntExistsInDirectoryException;

public class ChangeDirectoryServiceTest extends TokenValidationServiceTest {

	private Long rootToken;
	private Directory home;
	private Directory rootHome;
	private String path = "/home";
	private User root;
	private Login rootLogin;
	@Override
	protected void populate() {
		Manager m = Manager.getInstance();
		new User(m, "lads");
		
		rootLogin = new Login("root", "***");
		root = rootLogin.getCurrentUser();
		rootToken = rootLogin.getToken();
		
		home = (Directory) m.getRootDirectory().getFileByName("home");
		
		rootHome = (Directory) home.lookup("root", root);
		
		new Directory("myhome", root, rootHome);
		new Directory("newDirectory", new User(m, "foo"), (Directory) home.lookup("foo", root));
	}
	
	// 1
	@Test
	public void successChangeDirectoryRelative() {
		ChangeDirectoryService service = new ChangeDirectoryService(rootToken, "myhome");
		service.execute();
		assertEquals("Current directory not changed" , rootHome.lookup("myhome", root).getAbsolutePath(), service.result());
	}
	
	// 2
	@Test
	public void successChangeDirectoryAbsolute() {
		ChangeDirectoryService service = new ChangeDirectoryService(rootToken, path);
		service.execute();
		assertEquals("Current directory not changed" , home.getAbsolutePath(), service.result());
	}
	
	// 4
	@Test (expected = AccessDeniedException.class)
	public void noExecutionPermission() {
		Login l = new Login("lads", "lads");
		l.setCurrentDir((Directory) home.lookup("foo", root));
		
		ChangeDirectoryService service = new ChangeDirectoryService(l.getToken(), "newDirectory");
		service.execute();
	}
	
	// 6
	@Test
	public void successChangeDirectorySelf() {
		ChangeDirectoryService service = new ChangeDirectoryService(rootToken, ".");
		service.execute();
		assertEquals("Current directory not changed" , rootHome.getAbsolutePath(), service.result());
	}
	
	// 7
	@Test
	public void successChangeDirectoryParent() {
		ChangeDirectoryService service = new ChangeDirectoryService(rootToken, "..");
		service.execute();
		assertEquals("Current directory not changed" , home.lookup("myhome", root).getAbsolutePath(), service.result());
	}
	
	// 8
	@Test (expected = FileDoesntExistsInDirectoryException.class)
	public void nonExistantDirectory() {
		ChangeDirectoryService service = new ChangeDirectoryService(rootToken, "notExistant");
		service.execute();
		assertEquals("Current directory not changed" , rootHome.getAbsolutePath(), service.result());
	}

}
