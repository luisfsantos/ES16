package pt.tecnico.myDrive.service;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import pt.tecnico.myDrive.domain.*;

public class ListDirectoryServiceTest extends TokenValidationServiceTest{

	private Login login;

	protected void populate(){
		Manager manager = Manager.getInstance();

		User thunder = new User(manager,"Thor","pass","kami","rwxdrwxd");  //creates /home/Thor
		User lies = new User(manager,"Loki","pass","badkami","rwxdrwx-");
		this.login = new Login("Thor","pass");

		Directory currentDir = thunder.getHome(); //has permissions for everything

		App app = new App("application", thunder, currentDir, "i.am.fully.qualified.name");
		PlainFile file = new PlainFile("ficheiro",lies, currentDir, "I AM FILE CONTENT");
		Directory dir = new Directory("pasta",lies, currentDir);
			PlainFile child = new PlainFile("filho",thunder, dir,"I AM IRRELEVANT CONTENT");
		Link link = new Link("zelda", thunder, currentDir, "/root");
	}

	@Test
	public void sucessWithNonEmptyDirectory(){
	
		ListDirectoryService service = new ListDirectoryService(login.getToken());
		service.execute();

		List<FileDTO> result = service.result();

		assertEquals("List with 6 elements",6, result.size());

		//DTO should be ordered alphabetically, after . and ..
		assertEquals("Type of first element is Directory","Directory",result.get(0).getType());
		assertEquals("Type of second element is Directory","Directory",result.get(1).getType());
		assertEquals("Type of third element is App","App",result.get(2).getType());
		assertEquals("Type of forth element is PlainFile","PlainFile",result.get(3).getType());
		assertEquals("Type of fifth element is Directory","Directory",result.get(4).getType());
		assertEquals("Type of sixth element is Link","Directory",result.get(5).getType());

		assertEquals("Permissions of first element","rwxdrwxd",result.get(0).getUmask());
		assertEquals("Permissions of third element","rwxdrwxd",result.get(2).getUmask());
		assertEquals("Permissions of forth element","rwxdrwx-",result.get(3).getUmask());
		assertEquals("Permissions of fifth element","rwxdrwx-",result.get(4).getUmask());
		assertEquals("Permissions of sixth element","rwxdrwxd",result.get(5).getUmask());
		
		assertEquals("Dimension of first element is 6",6,result.get(0).getDimension());
		assertEquals("Dimension of third element is 1",1,result.get(2).getDimension());
		assertEquals("Dimension of forth element is 1",1,result.get(3).getDimension());
		assertEquals("Dimension of fifth element is 3",3,result.get(4).getDimension());
		assertEquals("Dimension of sixth element is 1",1,result.get(5).getDimension());

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
	public void sucessWithEmptyDirectory(){
		ListDirectoryService service = new ListDirectoryService(login.getToken());
		service.execute();

		List<FileDTO> result = service.result();


		assertEquals("List with 2 elements",2, result.size());

		assertEquals("Type of first element is Directory","Directory",result.get(0).getType());
		assertEquals("Type of second element is Directory","Directory",result.get(1).getType());

		assertEquals("Permissions of first element","rwxdrwxd",result.get(0).getUmask());
		assertEquals("Dimension of first element is 2",2,result.get(0).getDimension());
		assertEquals("Username of first element is Thor","Thor",result.get(0).getUsername());
	}
}