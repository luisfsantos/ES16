package pt.tecnico.myDrive.service;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import pt.tecnico.myDrive.domain.App;
import pt.tecnico.myDrive.domain.Directory;
import pt.tecnico.myDrive.domain.Link;
import pt.tecnico.myDrive.domain.Login;
import pt.tecnico.myDrive.domain.Manager;
import pt.tecnico.myDrive.domain.PlainFile;
import pt.tecnico.myDrive.domain.User;
import pt.tecnico.myDrive.exception.CannotRemoveDirectoryException;
import pt.tecnico.myDrive.exception.FileDoesntExistsInDirectoryException;
import pt.tecnico.myDrive.exception.IsHomeDirectoryException;
import pt.tecnico.myDrive.exception.InvalidPermissionException;

public class DeleteFileServiceTest extends AbstractServiceTest {
	private Long rootToken;
	private Long userToken;
	private Login rootLogin;
	private Login userLogin;
	private Directory homeDir;
	private Directory dirContent;
	private Directory dirNoContent;
	private Link linkTest;
	private App appTest;
	private PlainFile plainFileTest;
	private PlainFile noPermissionFile;
	
	@Override
	protected void populate() {
		Manager manager = Manager.getInstance();
		
		rootLogin = new Login("root", "***");
		rootToken = rootLogin.getToken();
		noPermissionFile = new PlainFile("noPermissionFile",
				rootLogin.getCurrentUser(), rootLogin.getCurrentDir(), "contentPlainFile1");
		
		new User(manager, "userTest");
		userLogin = new Login("userTest", "userTest");
		userToken = userLogin.getToken();
		
		dirContent = new Directory("dirContent", userLogin.getCurrentUser(), userLogin.getCurrentDir());
		dirNoContent = new Directory("dirContent", userLogin.getCurrentUser(), dirContent);
		linkTest = new Link("linkTest", userLogin.getCurrentUser(), dirContent, "contentLink");
		appTest = new App("appTest", userLogin.getCurrentUser(), dirContent, "contentApp");
		plainFileTest = new PlainFile("plineFileTest", userLogin.getCurrentUser(), dirContent, "contentPlainFile2");
		
		homeDir = (Directory) manager.getRootDirectory().lookup("home", rootLogin.getCurrentUser());
	}
	
	// 3
	@Test(expected = InvalidPermissionException.class)
	public void noPermission(){
		userLogin.setCurrentDir(rootLogin.getCurrentDir());
		DeleteFileService service = new DeleteFileService(userToken, noPermissionFile.getName());
		service.execute();
	}
	
	
	// 4
	@Test(expected = FileDoesntExistsInDirectoryException.class)
	public void inexistantFile() {
		DeleteFileService service = new DeleteFileService(userToken, "inexistantFile");
		service.execute();
	}
	
	
	// 5
	@Test(expected = CannotRemoveDirectoryException.class)
	public void thisDirectory(){
		DeleteFileService service = new DeleteFileService(rootToken, ".");
		service.execute();
	}
	
	// 6
	@Test(expected = CannotRemoveDirectoryException.class)
	public void parentDirectory(){
		DeleteFileService service = new DeleteFileService(rootToken, "..");
		service.execute();
	}
	
	// 7
	@Test(expected = CannotRemoveDirectoryException.class)
	public void rootDirectory(){
		DeleteFileService service = new DeleteFileService(rootToken, "/");
		service.execute();
	}
	
	// 8
	@Test(expected = IsHomeDirectoryException.class)
	public void rootHomeDirectory(){
		String dirName = rootLogin.getCurrentDir().getName();
		rootLogin.setCurrentDir(homeDir);
		DeleteFileService service = new DeleteFileService(rootToken, dirName);
		service.execute();
	}
	
	// 9
	@Test(expected = IsHomeDirectoryException.class)
	public void userHomeDirectory(){
		String dirName = userLogin.getCurrentDir().getName();
		rootLogin.setCurrentDir(homeDir);
		DeleteFileService service = new DeleteFileService(userToken, dirName);
		service.execute();
	}
	
	// 10
	@Test
	public void removeLink(){
		userLogin.setCurrentDir(dirContent);
		DeleteFileService service = new DeleteFileService(userToken, linkTest.getName());
		service.execute();
		assertEquals("cannot remove link", false, userLogin.getCurrentDir().hasFile(linkTest.getName()));
	}
	
	// 11
	@Test
	public void removeApp(){
		userLogin.setCurrentDir(dirContent);
		DeleteFileService service = new DeleteFileService(userToken, appTest.getName());
		service.execute();
		assertEquals("cannot remove app", false, userLogin.getCurrentDir().hasFile(appTest.getName()));
	}
	
	// 12
	@Test
	public void removePlainFile(){
		userLogin.setCurrentDir(dirContent);
		DeleteFileService service = new DeleteFileService(userToken, plainFileTest.getName());
		service.execute();
		assertEquals("cannot remove plain file", false, userLogin.getCurrentDir().hasFile(plainFileTest.getName()));
	}
	
	// 13
	@Test
	public void removeDirectoryNoContent(){
		userLogin.setCurrentDir(dirContent);
		DeleteFileService service = new DeleteFileService(userToken, dirNoContent.getName());
		service.execute();
		assertEquals("cannot remove directory without content", false, userLogin.getCurrentDir().hasFile(dirNoContent.getName()));
	}
	
	// 14
	@Test
	public void removeDirectoryWithContent(){
		userLogin.setCurrentDir(dirContent);
		DeleteFileService service = new DeleteFileService(userToken, dirContent.getName());
		service.execute();
		assertEquals("cannot remove directory with content", false, userLogin.getCurrentDir().hasFile(dirContent.getName()));
	}

}
