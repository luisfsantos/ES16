package pt.tecnico.myDrive.service;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import pt.tecnico.myDrive.domain.*;
import pt.tecnico.myDrive.exception.AccessDeniedException;
import pt.tecnico.myDrive.exception.FileDoesntExistsInDirectoryException;
import pt.tecnico.myDrive.exception.InvalidPathException;
import pt.tecnico.myDrive.exception.IsNotDirectoryException;
import pt.tecnico.myDrive.service.dto.FileDto;

import java.util.List;

public class ListDirectoryServiceTest extends TokenValidationServiceTest{

	private Login login;
	private Login loginBad;
	private Login emptyLogin;
	private String appContent="i.am.fully.qualified.name";
	private String fileContent="I AM FILE CONTENT";

	protected void populate(){
		Manager manager = Manager.getInstance();
		super.populate();

		User thunder = new User(manager,"Thor","pass","kami","rwxdrwxd");  //creates /home/Thor
		User lies = new User(manager,"Loki","pass","badkami","-wxd-wx-");
		this.login = new Login("Thor","pass");
		this.loginBad = new Login("Loki","pass");
		this.emptyLogin = new Login("Loki","pass");
		Directory emptyDir = new Directory("nada",thunder,emptyLogin.getCurrentDir());
		emptyLogin.setCurrentDir(emptyDir);

		Directory currentThorDir = login.getCurrentDir(); //has permissions for everything

		
		App app = new App("application", thunder, currentThorDir, appContent);
		PlainFile file = new PlainFile("ficheiro",lies, currentThorDir, fileContent);
		Directory dir = new Directory("pasta",lies, currentThorDir);
			PlainFile child = new PlainFile("filho",thunder, dir,"I AM IRRELEVANT CONTENT");
		Link link = new Link("zelda", thunder, currentThorDir, "/root");
	}

	@Test(expected = AccessDeniedException.class)
	public void failUserDoesntHaveReadPermissions () {
		Directory currentThorDir = login.getCurrentDir();
		currentThorDir.setPermissions("-wxd-wx-");
		loginBad.setCurrentDir(currentThorDir);
		ListDirectoryService service = new ListDirectoryService(loginBad.getToken());
		service.execute();
	}

	@Test
	public void successWithNonEmptyDirectory(){
	
		ListDirectoryService service = new ListDirectoryService(login.getToken());
		service.execute();

		List<FileDto> result = service.result();

		assertEquals("List with 6 elements",6, result.size());


		//DTO should be ordered alphabetically, after . and ..
		assertEquals("Type of first element is Directory","Directory",result.get(0).getType());
		assertEquals("Type of second element is Directory","Directory",result.get(1).getType());
		assertEquals("Type of third element is App","App",result.get(2).getType());
		assertEquals("Type of forth element is PlainFile","PlainFile",result.get(3).getType());
		assertEquals("Type of fifth element is Directory","Directory",result.get(4).getType());
		assertEquals("Type of sixth element is Link","Link",result.get(5).getType());


		assertEquals("Permissions of first element","rwxdrwxd",result.get(0).getUmask());
		assertEquals("Permissions of third element","rwxdrwxd",result.get(2).getUmask());
		assertEquals("Permissions of forth element","-wxd-wx-",result.get(3).getUmask());
		assertEquals("Permissions of fifth element","-wxd-wx-",result.get(4).getUmask());
		assertEquals("Permissions of sixth element","rwxdrwxd",result.get(5).getUmask());



		assertEquals("Dimension of first element is wrong",6,result.get(0).getDimension());
		assertEquals("Dimension of third element is wrong",appContent.length(),result.get(2).getDimension());
		assertEquals("Dimension of forth element is wrong",fileContent.length(),result.get(3).getDimension());
		assertEquals("Dimension of fifth element is wrong",3,result.get(4).getDimension());
		assertEquals("Dimension of sixth element is wrong",5,result.get(5).getDimension());


		assertEquals("Username of first element is Thor","Thor",result.get(0).getUsername());
		assertEquals("Username of third element is Thor","Thor",result.get(2).getUsername());
		assertEquals("Username of forth element is Loki","Loki",result.get(3).getUsername());
		assertEquals("Username of fifth element is Loki","Loki",result.get(4).getUsername());
		assertEquals("Username of sixth element is Thor","Thor",result.get(5).getUsername());

		assertEquals("Name of file is application","application",result.get(2).getName());
		assertEquals("Name of file is ficheiro","ficheiro",result.get(3).getName());
		assertEquals("Name of file is pasta","pasta",result.get(4).getName());
		assertEquals("Name of file is zelda","zelda",result.get(5).getName());

	}

	@Test
	public void successWithEmptyDirectory(){
		ListDirectoryService service = new ListDirectoryService(emptyLogin.getToken());
		service.execute();

		List<FileDto> result = service.result();


		assertEquals("List with 2 elements",2, result.size());

		assertEquals("Type of first element is Directory","Directory",result.get(0).getType());
		assertEquals("Type of second element is Directory","Directory",result.get(1).getType());

		assertEquals("Permissions of first element","rwxdrwxd",result.get(0).getUmask());

		assertEquals("Dimension of first element is 2",2,result.get(0).getDimension());
		assertEquals("Username of first element is Thor","Thor",result.get(0).getUsername());
	}

	@Test
	public void successListDirectoryFromPath() {
		ListDirectoryService service = new ListDirectoryService(login.getToken(), "/home/root");
		service.execute();

		assertNotNull("dto is null", service.result());
	}

	@Test(expected = InvalidPathException.class)
	public void invalidPath() {
		ListDirectoryService service = new ListDirectoryService(login.getToken(), "/home/invalidDir/invalidFile");
		service.execute();
	}

	@Test(expected = FileDoesntExistsInDirectoryException.class)
	public void invalidFile() {
		ListDirectoryService service = new ListDirectoryService(login.getToken(), "/home/invalidFile");
		service.execute();
	}

	@Test(expected = IsNotDirectoryException.class)
	public void listNonDirFileShouldThrowException() {
		ListDirectoryService service = new ListDirectoryService(login.getToken(), "application");
		service.execute();
	}
}