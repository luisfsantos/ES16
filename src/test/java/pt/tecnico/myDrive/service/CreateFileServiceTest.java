package pt.tecnico.myDrive.service;

import static org.junit.Assert.assertEquals;
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
import pt.tecnico.myDrive.exception.IsNotJavaFullyQualifiedNameException;
import pt.tecnico.myDrive.exception.MyDriveException;

public class CreateFileServiceTest extends TokenValidationServiceTest {
	private Long rootToken;
	private Directory home;
	private Directory rootHome;
	private String name = "newFile";
	private User root;
	
	@Override
	protected void populate() {
		Manager m = Manager.getInstance();
		new User(m, "lads");
		Login l = new Login("root", "***");
		root = l.getCurrentUser();
		rootToken = l.getToken();
		
		home = (Directory) m.getRootDirectory().getFileByName("home");
		rootHome = (Directory) home.lookup("root", root);
		new Directory("myhome", root, rootHome);
	}
	
	// 1
	@Test
	public void successNewDirectory() {
		CreateFileService service = new CreateFileService(rootToken, name, "dir", null);
		service.execute();
		assertEquals("Directory not created" , true, rootHome.hasFile(name));
		assertThat(rootHome.getFileByName(name), instanceOf(Directory.class));
	}
	
	// 2
	@Test
	public void successNewPlainFile() {
		CreateFileService service = new CreateFileService(rootToken, name, "plain", null);
		service.execute();
		assertEquals("Plain File not created" , true, rootHome.hasFile(name));
		assertThat(rootHome.getFileByName(name), instanceOf(PlainFile.class));
	}
	
	// 3
	@Test
	public void successNewApp() {
		CreateFileService service = new CreateFileService(rootToken, name, "app", null);
		service.execute();
		assertEquals("App not created" , true, rootHome.hasFile(name));
		assertThat(rootHome.getFileByName(name), instanceOf(App.class));
	}
	
	// 4
	@Test
	public void successNewLink() {
		CreateFileService service = new CreateFileService(rootToken, name, "link", "/home");
		service.execute();
		assertEquals("Link not created" , true, rootHome.hasFile(name));
		assertThat(rootHome.getFileByName(name), instanceOf(Link.class));
	}

	// 5
	@Test(expected = MyDriveException.class)
	public void noFileType() {
		CreateFileService service = new CreateFileService(rootToken, name, null, null);
		service.execute();
	}
	
	// 7
	@Test(expected = AccessDeniedException.class)
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
	public void forwarSlashInName() {
		final String invalidName = "foo/bar";
		CreateFileService service = new CreateFileService(rootToken, invalidName, "dir", null);
		service.execute();
	}
	
	// 14
	@Test (expected = InvalidFileNameException.class)
	public void nulTerminatorInName() {
		final String invalidName = "foo\0";
		CreateFileService service = new CreateFileService(rootToken, invalidName, "dir", null);
		service.execute();
	}
	
	// 15
	@Test (expected = InvalidFileNameException.class)
	public void nullName() {
		final String invalidName = null;
		CreateFileService service = new CreateFileService(rootToken, invalidName, "dir", null);
		service.execute();
	}
	
	// 16
	@Test
	public void largestName() {
		final String largeName = "PzSMsyGKBUrvsDTcAiQcfCmnuxhbRvmIjrlPenASYECmvvWRvVzuePazCnVlDXjHbilmjDvwRuXKnCWouRvSsjFleEHLzZiSDWNeObLwTjbLUGajEUbDiAhnqeDPWlYQaWlNMjtSgqopTfSPoNirhxzahJyZczRCKOScjFYwcoThEwZksyuYtONWlfOAgUOfsxiEWwTRETzhxOblTxJkTwIKGHZBeWxTnLPXUsiYAVZNSyMCeitBWXpwBpsXprHLyDeaCXZGvETZYNHjfRmlkakVUTrROhlHXKslpLGmPIyIRcfgCHGkGKRZWamMZhvInEqnkYeUVtGVmhItvKJAgDssEUMoVENzaZEojCYxuLylMRIDcCjfPzeIwnMwsCLCIuCWAIJRwewlmrDiYaMjQUQzwiECPSiqBlZniXIQNHefgEfDMosFIinBSAFGuOBnNcFEZwEBoAPIEwskAYosmEBFpkmKrLqIutIfOffDmafPhLKngkLrNSDMSpcKgeLVeUFpUhhtojNBHUDtmiBsFysuzZhTmNhDAVQWmTuLGcYvnDAMxpaQhPuAqRyqhTwVNnUSKzIByApbjAHEzgFBhBexljDDIWfIWOmlJuiDSEvQNyzawODwfPGPpEoMUmVRFwOPltTfQMeLzoqveuqwwqMkwzNqfGXZHaJAcrgFmwBRFWyOFqQbvQKsKEZmmvybTaaESpXvDMnHeWBjYfkQYIeArZQebLTZKbsxxqNNkHpCHDRCBYFNBWeZLDOmbDFvkeDVCZjJsgixaZSZmGQIVUzpSehLnPwnMqUWIDiMxJLszSqibowDDGiLYJWmloTPYbnIICYeFiKnKHAhvUweNyiSiTWrmeHFIYoIRycimLilwRsfmqNSlzjEollkSoyjNfOHgsQNzRJsSAcwJSqqFvuYfaBgbqGvOSWRnhCTLnglgyGEhEqXrCUqiDMnzZyfaYUfosTNVHjhIRZVCrnTNjplcUUcfsQvxroPpPZnRLQTqEhlVlsFm";
		CreateFileService service = new CreateFileService(rootToken, largeName, "dir", null);
		service.execute();
		assertEquals("File with full path 1024 (11 + 1013) characters not created" , true, rootHome.hasFile(largeName));
		assertThat(rootHome.getFileByName(largeName), instanceOf(Directory.class));
		
	}
	
	// 17
	@Test (expected = InvalidFileNameException.class)
	public void nameTooLarge() {
		final String largeName = "aPzSMsyGKBUrvsDTcAiQcfCmnuxhbRvmIjrlPenASYECmvvWRvVzuePazCnVlDXjHbilmjDvwRuXKnCWouRvSsjFleEHLzZiSDWNeObLwTjbLUGajEUbDiAhnqeDPWlYQaWlNMjtSgqopTfSPoNirhxzahJyZczRCKOScjFYwcoThEwZksyuYtONWlfOAgUOfsxiEWwTRETzhxOblTxJkTwIKGHZBeWxTnLPXUsiYAVZNSyMCeitBWXpwBpsXprHLyDeaCXZGvETZYNHjfRmlkakVUTrROhlHXKslpLGmPIyIRcfgCHGkGKRZWamMZhvInEqnkYeUVtGVmhItvKJAgDssEUMoVENzaZEojCYxuLylMRIDcCjfPzeIwnMwsCLCIuCWAIJRwewlmrDiYaMjQUQzwiECPSiqBlZniXIQNHefgEfDMosFIinBSAFGuOBnNcFEZwEBoAPIEwskAYosmEBFpkmKrLqIutIfOffDmafPhLKngkLrNSDMSpcKgeLVeUFpUhhtojNBHUDtmiBsFysuzZhTmNhDAVQWmTuLGcYvnDAMxpaQhPuAqRyqhTwVNnUSKzIByApbjAHEzgFBhBexljDDIWfIWOmlJuiDSEvQNyzawODwfPGPpEoMUmVRFwOPltTfQMeLzoqveuqwwqMkwzNqfGXZHaJAcrgFmwBRFWyOFqQbvQKsKEZmmvybTaaESpXvDMnHeWBjYfkQYIeArZQebLTZKbsxxqNNkHpCHDRCBYFNBWeZLDOmbDFvkeDVCZjJsgixaZSZmGQIVUzpSehLnPwnMqUWIDiMxJLszSqibowDDGiLYJWmloTPYbnIICYeFiKnKHAhvUweNyiSiTWrmeHFIYoIRycimLilwRsfmqNSlzjEollkSoyjNfOHgsQNzRJsSAcwJSqqFvuYfaBgbqGvOSWRnhCTLnglgyGEhEqXrCUqiDMnzZyfaYUfosTNVHjhIRZVCrnTNjplcUUcfsQvxroPpPZnRLQTqEhlVlsFm";
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
	
	// 19
	@Test
	public void contentInPlainFileCreation() {
		final String content = "foo";
		CreateFileService service = new CreateFileService(rootToken, name, "plain", content);
		service.execute();
		assertEquals("Plain File not created" , true, rootHome.hasFile(name));
		assertThat(rootHome.getFileByName(name), instanceOf(PlainFile.class));
		assertEquals("Content differs from input", content, rootHome.getFileByName(name).read(root));
	}
	
	// 20
	@Test (expected = IsNotJavaFullyQualifiedNameException.class)
	public void invalidContentApp() {
		final String content = "foo";
		CreateFileService service = new CreateFileService(rootToken, name, "app", content);
		service.execute();
	}
	
	// 21
	@Test
	public void fullyQualifiedNameInApp() {
		final String content = "pt.tecnico.myDrive.Apps.print";
		CreateFileService service = new CreateFileService(rootToken, name, "app", content);
		service.execute();
		assertEquals("App not created" , true, rootHome.hasFile(name));
		assertThat(rootHome.getFileByName(name), instanceOf(App.class));
		assertEquals("Content differs from input", content, rootHome.getFileByName(name).read(root));
	}
	
	// 22
	@Test
	public void fullyQualifiedNameInAppToMain() {
		final String content = "pt.tecnico.myDrive.Apps";
		CreateFileService service = new CreateFileService(rootToken, name, "app", content);
		service.execute();
		assertEquals("App not created" , true, rootHome.hasFile(name));
		assertThat(rootHome.getFileByName(name), instanceOf(App.class));
		assertEquals("Content differs from input", content, rootHome.getFileByName(name).read(root));
	}
	
	// 23
	@Test (expected = IsNotJavaFullyQualifiedNameException.class)
	public void notJavaFullyQualifiedNameApp() {
		final String content = "pt.tecnico.myDrive..";
		CreateFileService service = new CreateFileService(rootToken, name, "app", content);
		service.execute();
	}
		
	
	
}
