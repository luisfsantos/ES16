package pt.tecnico.myDrive.service;

import mockit.*;
import mockit.integration.junit4.JMockit;
import org.junit.Test;
import org.junit.runner.RunWith;
import pt.tecnico.myDrive.domain.*;
import pt.tecnico.myDrive.exception.*;

import static org.junit.Assert.assertEquals;

@RunWith(JMockit.class)
public class WriteFileServiceTest extends ReadWriteCommonTest {

	private Long token;
	private User root;
	private Directory home;

	private String strA = "";
	private String strB = "";
	private String strC = "";
	private String Y22 = "yyyyyyyyyyyyyyy";

	private Login rootlogin;
	private Link linkMock;
	private PlainFile pfile;

	private final String linkMockStr = "linkMock";
	private final String contentMockStr = "mocktest";
	private final String envVar = "$DAVID";
	private final String pathEnvVar = "/$DAVID";
	private final String pathTranslated = "/home/pfMock";


	public MyDriveService createTestInstance(Long token, String name, String dummy) {
		return new WriteFileService(token, name, dummy);
	}

	@Override
	protected void populate() {
		Manager manager = Manager.getInstance();
		super.populate();

		rootlogin = new Login("root", "***");
		root = rootlogin.getCurrentUser();
		home = (Directory) manager.getRootDirectory().lookup("home", root);
		rootlogin.setCurrentDir(home);
		token = rootlogin.getToken();


		pfile = new PlainFile("pfMock", root, home, "test");
		linkMock = new Link(linkMockStr, root, home, pathEnvVar);

		User userTest = new User(Manager.getInstance(), "userTest");
		Login testLogin = new Login(userTest.getUsername(), userTest.getUsername());
		testLogin.setCurrentDir(home);

		int i;
		for(i=0; i<333; i++){
			strA += "a";
			strB += "b";
			strC += "c";
		}

		new PlainFile("validplain",root ,home ,"valid");
		new App("validapp",root, home ,"pt.tecnico.myDrive.domain.User");
		new PlainFile("validplain2",root ,home ,"valid");
		new Link("validlink",root , home ,"/home/validplain2");


		Directory dirA = new Directory(strA,root,home);
		Directory dirB = new Directory(strB,root,dirA);
		Directory dirC = new Directory(strC,root,dirB);

		new PlainFile(Y22,root,dirC ,"valid");

		new Link("link1024",root, home , strA+"/"+strB+"/"+strC+"/"+Y22);

		new Link("loop1", root, home,"loop2");
		new Link("loop2", root, home, "loop3");
		new Link("loop3", root, home, "loop1");

		new Link("linktoplain", root,home, "plain1");
		new PlainFile("plain1", root , home, "valid");

		new Link("linktoapp",root ,home, "app1");
		new App("app1",root ,home, "pt.tecnico.myDrive.domain.User");

		new Link("link1",root ,home, "link2");
		new Link("link2",root ,home, "textfile");
		new PlainFile("textfile",root, home, "valid");

		new Link("linktoNE",root, home, "batata");
	}

	//TEST 7
	@Test
	public void successEmptyWritePlain(){
		WriteFileService service = new WriteFileService(token, "validplain", "");
		service.execute();
		
		PlainFile pfile = (PlainFile)home.lookup("validplain", root);
		assertEquals("Empty write not executed", pfile.read(root), "");

	}

	//TEST 8
	@Test
	public void successWritePlain(){
		WriteFileService service = new WriteFileService(token, "validplain", "mydrive");
		service.execute();
		
		PlainFile pfile = (PlainFile)home.lookup("validplain", root);
		assertEquals("Write not executed", pfile.read(root), "mydrive");

	}

	//TEST 9
	@Test(expected=IsNotJavaFullyQualifiedNameException.class)
	public void insuccessEmptyWriteApp(){
		WriteFileService service = new WriteFileService(token, "validapp", "");
		service.execute();
	}

	//TEST 10
	@Test(expected=IsNotJavaFullyQualifiedNameException.class)
	public void insuccessWriteApp(){
		WriteFileService service = new WriteFileService(token, "validapp", "pt..tecnico.myDrive.domain.App");
		service.execute();
	}

	//TEST 11
	@Test
	public void successWriteApp(){
		WriteFileService service = new WriteFileService(token, "validapp", "pt.tecnico.myDrive.domain.App");
		service.execute();
		
		App appfile = (App) home.lookup("validapp", root);
		assertEquals("Write not executed", appfile.read(root), "pt.tecnico.myDrive.domain.App");
	}

	//TEST 12
	@Test(expected=InvalidWriteException.class)
	public void insuccessWriteDir(){
		WriteFileService service = new WriteFileService(token, "root", "/home");
		service.execute();
	}


	//TEST 14
	@Test
	public void success1024CharWriteLink(){
		WriteFileService service = new WriteFileService(token, "link1024", "writelink");
		service.execute();

		PlainFile pfile = (PlainFile)home.lookup(strA+"/"+strB+"/"+strC+"/"+Y22, root);
		assertEquals("1024 characters write not executed successfully", pfile.read(root), "writelink");
	}

	//TEST 16
	@Test
	public void successWriteLinkToPlain(){
		WriteFileService service = new WriteFileService(token, "linktoplain", "writelink");
		service.execute();

		PlainFile pfile = (PlainFile)home.lookup("plain1", root);
		assertEquals("write not executed successfully", pfile.read(root), "writelink");
	}

	//TEST 17
	@Test
	public void successWriteLinkToApp(){
		WriteFileService service = new WriteFileService(token, "linktoapp", "pt.tecnico.myDrive.domain.User");
		service.execute();

		App appfile = (App)home.lookup("app1", root);
		assertEquals("write not executed successfully", appfile.read(root), "pt.tecnico.myDrive.domain.User");
	}

	//TEST 18
	@Test
	public void successWriteDoubleLinkToPlain(){
		WriteFileService service = new WriteFileService(token, "link1", "writelink");
		service.execute();

		PlainFile pfile = (PlainFile)home.lookup("textfile", root);
		assertEquals("write not executed successfully", pfile.read(root), "writelink");
	}

	//Test 19
	@Test(expected = FileDoesntExistsInDirectoryException.class)
	public void insuccessWriteLinkToNotExistingPlain(){
		WriteFileService service = new WriteFileService(token, "linktoNE", "batata");
		service.execute();
	}

	/*------------------------------------>MOCKUP TESTS - ENVIRONMENT LINKS <-----------------------------------------*/
	@Test
	public void successWriteEnvLink(@Mocked final Manager m) throws Exception{
		new Expectations(linkMock){{
				m.getInstance().getLoginByToken(token); result = rootlogin; times=1;
				linkMock.decodeEnvPath(); result = pathTranslated; times=1;
		}};
		WriteFileService service = new WriteFileService(token, linkMockStr, contentMockStr);
		service.execute();
		assertEquals("write not executed successfully", contentMockStr, pfile.read(root));
	}

	@Test(expected = EnvironmentVarDoesNotExistException.class)
	public void insuccessWriteEnvLink(@Mocked final Manager m){
		new Expectations(linkMock){{
			m.getInstance().getLoginByToken(token); result = rootlogin; times=1;
			linkMock.decodeEnvPath();
			result = new EnvironmentVarDoesNotExistException(envVar); times=1;
		}};
		WriteFileService service = new WriteFileService(token, linkMockStr, contentMockStr);
		service.execute();
	}
}


