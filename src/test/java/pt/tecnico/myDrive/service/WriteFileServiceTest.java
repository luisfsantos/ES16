package pt.tecnico.myDrive.service;

import java.math.BigInteger;
import java.util.Random;

import org.junit.Test;

import pt.tecnico.myDrive.domain.*;
import pt.tecnico.myDrive.exception.*;

import static org.junit.Assert.*;


public class WriteFileServiceTest extends TokenValidationServiceTest {

	private long token;
	private long nopermtoken;
	private User root;
	private User usertest;
	private Directory home;


	String strA = "";
	String strB = "";
	String strC = "";
	String Y22 = "yyyyyyyyyyyyyyyyyyyyyy";
	String Y23 = "yyyyyyyyyyyyyyyyyyyyyyy";


	@Override
	protected void populate() {
		Manager manager = Manager.getInstance();


		Login rootlogin = new Login("root", "***");
		root = rootlogin.getCurrentUser();
		home = (Directory) manager.getRootDirectory().lookup("home", root);
		rootlogin.setCurrentDir(home);
		token = rootlogin.getToken();

		usertest = new User(Manager.getInstance(), "usertest");
		Login testLogin = new Login(usertest.getUsername(), usertest.getUsername());
		testLogin.setCurrentDir(home);
		nopermtoken = testLogin.getToken();

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
		new PlainFile(Y23,root,dirC ,"valid");

		new Link("link1024",root, home , strA+"/"+strB+"/"+strC+"/"+Y22);
		new Link("link1025", root, home , strA+"/"+strB+"/"+strC+"/"+Y23);

		new Link("loop1", root, home,"loop2");
		new Link("loop2", root, home, "loop3");
		new Link("loop3", root, home, "loop1");

		new Link("linktoplain", root,home, "plain1");
		new PlainFile("plain1", root , home, "valid");

		new Link("linktoapp",root ,home, "app1");
		new App("app1",root ,home, "valid");

		new Link("link1",root ,home, "link2");
		new Link("link2",root ,home, "textfile");
		new PlainFile("textfile",root, home, "valid");

		new Link("linktoNE",root, home, "batata");
	}


	//TEST 1
	@Test(expected = InvalidPermissionException.class)
	public void invalidPermissionsPlain(){
		WriteFileService service = new WriteFileService(nopermtoken, "validplain", "");
		service.execute();
	}

	//TEST 2
	@Test(expected = InvalidPermissionException.class)
	public void invalidPermissionsApp(){
		WriteFileService service = new WriteFileService(nopermtoken, "validapp", "pt.tecnico.myDrive.domain.App");
		service.execute();
	}

	//TEST 3
	@Test(expected = InvalidPermissionException.class)
	public void invalidPermissionsLink(){
		WriteFileService service = new WriteFileService(nopermtoken, "validlink", "/home");
		service.execute();
	}


	//TEST 4
	@Test(expected = FileDoesntExistsInDirectoryException.class)
	public void notExistsPlain(){
		WriteFileService service = new WriteFileService(token, "notexists", "");
		service.execute();

	}

	//TEST 5
	@Test(expected = FileDoesntExistsInDirectoryException.class)
	public void notExistsApp(){
		WriteFileService service = new WriteFileService(token, "notexists", "pt.tecnico.myDrive.domain.App");
		service.execute();

	}

	//TEST 6
	@Test(expected = FileDoesntExistsInDirectoryException.class)
	public void notExistsLink(){
		WriteFileService service = new WriteFileService(token, "notexists", "/home");
		service.execute();

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
	@Test(expected=InvalidContentException.class)
	public void insuccessEmptyWriteApp(){
		WriteFileService service = new WriteFileService(token, "validapp", "");
		service.execute();
	}

	//TEST 10
	@Test(expected=InvalidContentException.class)
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

	//TEST 13
	@Test(expected = PathTooBigException.class)
	public void insuccessWriteLinkLoop(){
		WriteFileService service = new WriteFileService(token, "loop1", "writelink");
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

	//TEST 15
	@Test(expected = PathTooBigException.class)
	public void insuccess1025CharWriteLink(){
		WriteFileService service = new WriteFileService(token, "link1025", "writelink");
		service.execute();
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
		WriteFileService service = new WriteFileService(token, "linktoapp", "writelink");
		service.execute();

		App appfile = (App)home.lookup("app1", root);
		assertEquals("write not executed successfully", appfile.read(root), "writelink");
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
}



