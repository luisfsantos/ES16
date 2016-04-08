package pt.tecnico.myDrive.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import pt.tecnico.myDrive.domain.Manager;
import pt.tecnico.myDrive.domain.User;
import pt.tecnico.myDrive.domain.Directory;
import pt.tecnico.myDrive.domain.PlainFile;
import pt.tecnico.myDrive.domain.Link;
import pt.tecnico.myDrive.domain.App;
import pt.tecnico.myDrive.domain.Login;
import pt.tecnico.myDrive.exception.AccessDeniedException;
import pt.tecnico.myDrive.exception.FileAlreadyExistsInDirectoryException;
import pt.tecnico.myDrive.exception.InvalidFileNameException;
import pt.tecnico.myDrive.exception.MyDriveException;

public class CreateFileServiceTest extends AbstractServiceTest {
	private Long rootToken;
	private Directory home;
	private String name = "newFile";
	@Override
	protected void populate() {
		Manager m = Manager.getInstance();
		new User(m, "lads");
		Login l = new Login("root", "***");
		rootToken = l.getToken();
		
		home = (Directory) m.getRootDirectory().getFileByName("home");
		new Directory("myhome", home);
	}
	
	// 1
	@Test
	public void successNewDirectory() {
		CreateFileService service = new CreateFileService(rootToken, name, "dir", null);
		service.execute();
		assertEquals("Directory not created" , true, home.hasFile(name));
		assertThat(home.getFileByName(name), instanceOf(Directory.class));
	}
	
	// 2
	@Test
	public void successNewPlainFile() {
		CreateFileService service = new CreateFileService(rootToken, name, "plain", null);
		service.execute();
		assertEquals("Plain File not created" , true, home.hasFile(name));
		assertThat(home.getFileByName(name), instanceOf(PlainFile.class));
	}
	
	// 3
	@Test
	public void successNewApp() {
		CreateFileService service = new CreateFileService(rootToken, name, "app", null);
		service.execute();
		assertEquals("App not created" , true, home.hasFile(name));
		assertThat(home.getFileByName(name), instanceOf(App.class));
	}
	
	// 4
	@Test
	public void successNewLink() {
		CreateFileService service = new CreateFileService(rootToken, name, "link", "/home");
		service.execute();
		assertEquals("Link not created" , true, home.hasFile(name));
		assertThat(home.getFileByName(name), instanceOf(Link.class));
	}

	// 5
	@Test(expected = MyDriveException.class) //TODO will depend on actual exception for wrong file type
	public void noFileType() {
		CreateFileService service = new CreateFileService(rootToken, name, null, null);
		service.execute();
	}
	
	// 7
	@Test(expected = AccessDeniedException.class) //TODO will depend on actual exception no permission in creating
	public void noPermission() {
		Login l = new Login("lads", "lads");
		Long ladsToken = l.getToken();
		l.setCurrentDir(home);
		CreateFileService service = new CreateFileService(ladsToken, name, "dir", null);
		service.execute();
	}
	
	// 9
	@Test (expected = FileAlreadyExistsInDirectoryException.class)
	public void existingFile() {
		final String existingName = "myhome";
		CreateFileService service = new CreateFileService(rootToken, existingName, "dir", null);
		service.execute();
	}
	
	// 10
	@Test (expected = FileAlreadyExistsInDirectoryException.class)
	public void selfExsits() {
		final String existingName = ".";
		CreateFileService service = new CreateFileService(rootToken, existingName, "dir", null);
		service.execute();
	}
	
	// 11
	@Test (expected = FileAlreadyExistsInDirectoryException.class)
	public void parentExsits() {
		final String existingName = "..";
		CreateFileService service = new CreateFileService(rootToken, existingName, "dir", null);
		service.execute();
	}
	
	// 12
	@Test (expected = InvalidFileNameException.class)
	public void noName() {
		final String invalidName = "";
		CreateFileService service = new CreateFileService(rootToken, invalidName, "dir", null);
		service.execute();
	}
	
	// 13
	@Test (expected = InvalidFileNameException.class)
	public void nameIsSpace() {
		final String invalidName = " ";
		CreateFileService service = new CreateFileService(rootToken, invalidName, "dir", null);
		service.execute();
	}
	
	// 14
	@Test (expected = InvalidFileNameException.class)
	public void forwarSlashInName() {
		final String invalidName = "foo/bar";
		CreateFileService service = new CreateFileService(rootToken, invalidName, "dir", null);
		service.execute();
	}
	
	// 15
	@Test (expected = InvalidFileNameException.class)
	public void nulTerminatorInName() {
		final String invalidName = "foo\0";
		CreateFileService service = new CreateFileService(rootToken, invalidName, "dir", null);
		service.execute();
	}
	
	// 16
	@Test (expected = InvalidFileNameException.class)
	public void nullName() {
		final String invalidName = null;
		CreateFileService service = new CreateFileService(rootToken, invalidName, "dir", null);
		service.execute();
	}
	
	// 17
	@Test
	public void largestName() {
		final String largeName = "8aSeWJuGBGfj8iyoW6sVDGlZnnqUUaHncp1HJv0I7fwMe483wepNeJR0A26K42yqzq1XpO3w2tM1ZZ8TN8rsnkNgnYTPwFDJlzEBqhnrIMY4FTfOhBDE4vhqInIPiAV1Er4xOHA4q45c9R6j411Ojt4wXPlm2RKKQaibLKrmmZUst0CrvUmBPvxpTu9vQnZ93YBChUU8scUWoZTYfKhpaXoo0gvbPoW4UuE5l049Ohuc0lkx5bKD3aSlz5sDTkzJNCMmBGXKHQGrsRMLojk5jis1rbXgDWIeQi42VGYPBsyPfRDMqhXkagBQLA7EhSaBO3yWeh8seG1qX7X1WquBYobluNZMoBSzz6saxBjwCWVheFejejJaBVcFVEWwPhHjrjtlPUEv5zaK7fj7qkAcEjWsB3XUmstUCaLuM8vzaGJTP0E3s1aC4u8nuw1yFKpBCqmlYCyvFxDVSy3sPvp8DOBjbq1GIHCtWl7XHsgyiaMYif5Qc00LZ0JyRxYLyvC8a9zE5loGLsuelry7cjle1fuJyI3PPAV8FIoujQufeN6q9WWrS5cu9xEfBcPhBlLtmrCjmcna1e2tc6xQ7szJTq6Ckvlc0xaGLBKUCx4VGljiSPrpgchwaIi011THafV9qbll4MACqESWmPfGxb7U3avong2UAK0SU6P1uayTNcaJzpGfn54nhnFfTBEYAF3q4A9aHEzoYzpe5uTwWzaQbvBO4f8ccogn8t4SIYo9EFOACNAUNiAUXashTRfGLoUXIeqg8GKAhUwUG4aMoyQTrOWx3tsEs9xIv8Py0muDlKVvVOt6kHa6ERL0zUmSIKeoDyE7EoayLhnCiqMrl9e1KKCxkYIfKXVsTaXkH1cu407WqOlburrQqM4JaNbDMEK8u0i8ilZVTLSXWMV5bEOYipMcjvjOFrs6zzyOyDWJbXQhvmhmbHUW2VDC8hyJ1qR8UWLeBrbaLEBxxLj938GYX9mwHRYKws9M7TugjxkNaclvhgxip7KsB9cE3HGaAKvh";
		CreateFileService service = new CreateFileService(rootToken, largeName, "dir", null);
		service.execute();
		assertEquals("File with 1024 characters not created" , true, home.hasFile(largeName));
		assertThat(home.getFileByName(largeName), instanceOf(Directory.class));
		
	}
	
	// 18
	@Test (expected = InvalidFileNameException.class)
	public void nameTooLarge() {
		final String largeName = "a8aSeWJuGBGfj8iyoW6sVDGlZnnqUUaHncp1HJv0I7fwMe483wepNeJR0A26K42yqzq1XpO3w2tM1ZZ8TN8rsnkNgnYTPwFDJlzEBqhnrIMY4FTfOhBDE4vhqInIPiAV1Er4xOHA4q45c9R6j411Ojt4wXPlm2RKKQaibLKrmmZUst0CrvUmBPvxpTu9vQnZ93YBChUU8scUWoZTYfKhpaXoo0gvbPoW4UuE5l049Ohuc0lkx5bKD3aSlz5sDTkzJNCMmBGXKHQGrsRMLojk5jis1rbXgDWIeQi42VGYPBsyPfRDMqhXkagBQLA7EhSaBO3yWeh8seG1qX7X1WquBYobluNZMoBSzz6saxBjwCWVheFejejJaBVcFVEWwPhHjrjtlPUEv5zaK7fj7qkAcEjWsB3XUmstUCaLuM8vzaGJTP0E3s1aC4u8nuw1yFKpBCqmlYCyvFxDVSy3sPvp8DOBjbq1GIHCtWl7XHsgyiaMYif5Qc00LZ0JyRxYLyvC8a9zE5loGLsuelry7cjle1fuJyI3PPAV8FIoujQufeN6q9WWrS5cu9xEfBcPhBlLtmrCjmcna1e2tc6xQ7szJTq6Ckvlc0xaGLBKUCx4VGljiSPrpgchwaIi011THafV9qbll4MACqESWmPfGxb7U3avong2UAK0SU6P1uayTNcaJzpGfn54nhnFfTBEYAF3q4A9aHEzoYzpe5uTwWzaQbvBO4f8ccogn8t4SIYo9EFOACNAUNiAUXashTRfGLoUXIeqg8GKAhUwUG4aMoyQTrOWx3tsEs9xIv8Py0muDlKVvVOt6kHa6ERL0zUmSIKeoDyE7EoayLhnCiqMrl9e1KKCxkYIfKXVsTaXkH1cu407WqOlburrQqM4JaNbDMEK8u0i8ilZVTLSXWMV5bEOYipMcjvjOFrs6zzyOyDWJbXQhvmhmbHUW2VDC8hyJ1qR8UWLeBrbaLEBxxLj938GYX9mwHRYKws9M7TugjxkNaclvhgxip7KsB9cE3HGaAKvh";
		CreateFileService service = new CreateFileService(rootToken, largeName, "dir", null);
		service.execute();
	}
	
	// 18
	@Test (expected = MyDriveException.class) //TODO give right exception
	public void contentInDirectoryCreation() {
		final String content = "foo";
		CreateFileService service = new CreateFileService(rootToken, name, "dir", content);
		service.execute();
	}
		
	
	
}
