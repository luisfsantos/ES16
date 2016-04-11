package pt.tecnico.myDrive.service;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import pt.tecnico.myDrive.domain.Directory;
import pt.tecnico.myDrive.domain.Login;
import pt.tecnico.myDrive.domain.Manager;
import pt.tecnico.myDrive.domain.PlainFile;
import pt.tecnico.myDrive.domain.User;
import pt.tecnico.myDrive.exception.CannotRemoveDirectoryException;
import pt.tecnico.myDrive.exception.FileDoesntExistsInDirectoryException;

public class DeleteFileServiceTest extends AbstractServiceTest {
	private Long rootToken;
	private Long userToken;
	private Login rootLogin;
	private Login userLogin;
	private Directory dirContent;
	private Directory dirNoContent;
	private PlainFile noPermissionFile;
	
	@Override
	protected void populate() {
		Manager manager = Manager.getInstance();
		rootLogin = new Login("root", "***");
		rootToken = rootLogin.getToken();
		noPermissionFile = new PlainFile("noPermissionFile",
				rootLogin.getCurrentUser(), rootLogin.getCurrentDir(), "content");
		
		User user = new User(manager, "userTest");
		userLogin = new Login("userTest", "userTest");
		userToken = userLogin.getToken();		
	}
	
	// 3
	@Test
	public void noPermission(){
		userLogin.setCurrentDir(rootLogin.getCurrentDir());
		DeleteFileService service = new DeleteFileService(userToken, noPermissionFile.getName());
		service.execute();
		assertEquals("user has permission to remove", false, userLogin.getCurrentDir().hasFile(noPermissionFile.getName()));
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
		DeleteFileService service = new DeleteFileService(userToken, ".");
		service.execute();
		assertEquals("user has permission to remove", false, userLogin.getCurrentDir().hasFile(noPermissionFile.getName()));
	}

}
