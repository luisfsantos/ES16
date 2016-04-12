package pt.tecnico.myDrive.service;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import pt.tecnico.myDrive.domain.Directory;
import pt.tecnico.myDrive.domain.Link;
import pt.tecnico.myDrive.domain.Login;
import pt.tecnico.myDrive.domain.Manager;
import pt.tecnico.myDrive.domain.PlainFile;
import pt.tecnico.myDrive.domain.User;
import pt.tecnico.myDrive.exception.AccessDeniedException;
import pt.tecnico.myDrive.exception.FileDoesntExistsInDirectoryException;
import pt.tecnico.myDrive.exception.IsNotDirOrLinkException;
import pt.tecnico.myDrive.exception.MyDriveException;
import pt.tecnico.myDrive.exception.PathTooBigException;

public class ChangeDirectoryServiceTest extends TokenValidationServiceTest {
	private String name1024 = "CiRZTFGYrJjLuqKlkJIytJcgcaOhvziwgVkMnqOOHfniKzlnMITGHkHzSVQUFkhzMrxDKKSnsQHfRmBYuqMmkrWFhJClWWbUEqvfseHwCBfAanpBSctLaYnatGDsFKtbUaCASgrLajLwUzJxcQmPPoXHbTFEjCTtrJNquktyYZeJxOLEEuAEkkfbCRVntVgcaEJYnFXWmZIfMPPlgbAxQoMEbVygWJkJPbQZwaOGzAzYvBMUGtcYjwgoAzYXVfofgtkxQtQwlTblXJogxSWoNHPYIYbbnvKwZLyjfCcAPEeuttTqrbWrsQStengQgPSMBRXhFaHhcUVhDQZMuhaWyBCmVJFYequcZbMHchAYlIDzFeGNUDMcTpfSOrFmZFLrhIBKjOWfkJbaoioLjAYgKRlcjGQeLtVTkzjfmpAQfVrLjFlOceCzxsqNbuNNpiCrxzOsxeFwXkZIZzeaqOrTaTSmjxUejQRfmlYqUtXIQkYaiETclNWqEGROCFmyqmIzvYTJebnVshjRNoBrtNMTFnZeAFmiPkzLBIkGRpnTHooENNECEMSpyrGflobyujOVUoUfzEsBVCZAYuLTjmlTVyaNPFrrqqSXztrUbcYMVhslzmtoQmeONQTVUKtfDTQeGgfAtNNhGDepKvpUJjxgvASFhIOGPtFeEEfqFJGEMAucZlusWjuAXnmHEZClxZTGhiEyuTMbrIyaySPElklzKQscWQcUWoXGmljrfYqauGtTIkDnpDAryEWbYtrIICgrmIZXkCFhSmVqyhTvgCbiuwSAkSBSRTGcrlGNSiJOcuYchFpZiFNmqgXvXeifcjJaKBvepqsqokQnqkWPjhrgTAVLvllXVOVRztZurGzgXRfBaGJVJCKxhkcFcfCuQLwkNDzKHvHzCrDFmfngQhucAYfWJEKIcVloioPKiMZRjOcarDgAMhkFwHkOZGMDffkVyupxQhRZBbHzGyKnZFbYDWfTZGZfEthcJqOCPQTiXzcMhSgPCWWsn";
	private String name1025 = "a" + name1024;
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
		new Directory(name1024, root, rootHome);
		new Directory(name1025, root, rootHome);
		new PlainFile("file", root, rootHome, "file");
		new Link("link", root, rootHome, "/home");
		new Link("link1025", root, rootHome, name1025);
		new Link("recursivelink", root, rootHome, "/home/root/recursivelink");
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
	
	// 5
	@Test
	public void executionPermissionOnRootNotParent() {
		Login l = new Login("lads", "lads");
		l.setCurrentDir((Directory) home.lookup("foo", root));
		
		ChangeDirectoryService service = new ChangeDirectoryService(l.getToken(), "/home");
		service.execute();
		assertEquals("Current directory not changed" , home.getAbsolutePath(), service.result());	
	}
	
	// 7
	@Test
	public void successChangeDirectorySelf() {
		ChangeDirectoryService service = new ChangeDirectoryService(rootToken, ".");
		service.execute();
		assertEquals("Current directory not changed" , rootHome.getAbsolutePath(), service.result());
	}
	
	// 8
	@Test
	public void successChangeDirectoryParent() {
		ChangeDirectoryService service = new ChangeDirectoryService(rootToken, "..");
		service.execute();
		assertEquals("Current directory not changed" , home.getAbsolutePath(), service.result());
	}
	
	// 9
	@Test (expected = FileDoesntExistsInDirectoryException.class)
	public void nonExistantDirectory() {
		ChangeDirectoryService service = new ChangeDirectoryService(rootToken, "notExistant");
		service.execute();
	}
	
	// 10
	@Test (expected = MyDriveException.class) //FIXME
	public void existantFileDirectory() {
		ChangeDirectoryService service = new ChangeDirectoryService(rootToken, "file");
		service.execute();
	}
	
	// 11
	@Test
	public void linkInPathSuccess() {
		ChangeDirectoryService service = new ChangeDirectoryService(rootToken, "link/root");
		service.execute();
		assertEquals("Current directory not changed" , rootHome.getAbsolutePath(), service.result());
	}
	
	// 12
	@Test (expected = MyDriveException.class) //FIXME
	public void linkInPathFail() {
		ChangeDirectoryService service = new ChangeDirectoryService(rootToken, "recursivelink");
		service.execute();
	}
	
	// 13
	@Test (expected = IsNotDirOrLinkException.class)
	public void fileInPath() {
		ChangeDirectoryService service = new ChangeDirectoryService(rootToken, "file/ola");
		service.execute();
	}
	
	// 14
	@Test
	public void pathWithinCharacterLimit() {
		ChangeDirectoryService service = new ChangeDirectoryService(rootToken, name1024);
		service.execute();
		assertEquals("Current directory not changed" , rootHome.lookup(name1024, root).getAbsolutePath(), service.result());
	}
	
	// 15
	@Test (expected = PathTooBigException.class)
	public void pathoOutOfCharacterLimit() {
		ChangeDirectoryService service = new ChangeDirectoryService(rootToken, name1025);
		service.execute();
	}
	
	// 16
	@Test (expected = PathTooBigException.class)
	public void pathoResolvedOutOfCharacterLimit() {
		ChangeDirectoryService service = new ChangeDirectoryService(rootToken, "link1025");
		service.execute();
	}
	

}
