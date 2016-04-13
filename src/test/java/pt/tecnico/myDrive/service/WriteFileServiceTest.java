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
	private Directory home;


	String strA = "";
	String strB = "";
	String strC = "";
	String Y19 = "yyyyyyyyyyyyyyyyyyy";
	String Y20 = "yyyyyyyyyyyyyyyyyyyy";


	@Override
	protected void populate() {
		Manager manager = Manager.getInstance();
		home = (Directory) manager.getRootDirectory().lookup("home");

		Login l = new Login("root", "***");
		l.setCurrentDir(home);
		token = l.getToken();

		User usertest = new User(Manager.getInstance(), "usertest");
		Login testLogin = new Login(usertest.getUsername(), usertest.getUsername());
		testLogin.setCurrentDir(home);
		nopermtoken = testLogin.getToken();

		int i;
		for(i=0; i<333; i++){
			strA += "a";
			strB += "b";
			strC += "c";
		}

		new PlainFile("validplain",home ,"valid");
		new App("validapp", home ,"pt.tecnico.myDrive.domain.User");
		new Link("validlink", home ,"/");


		Directory dirA = new Directory(strA,home);
		Directory dirB = new Directory(strB, dirA);
		Directory dirC = new Directory(strC, dirB);

		new PlainFile(Y19,dirC ,"valid");
		new PlainFile(Y20,dirC ,"valid");

		new Link("link1024",home , strA+"/"+strB+"/"+strC+"/"+Y19);
		new Link("link1025",home , strA+"/"+strB+"/"+strC+"/"+Y20);

		new Link("loop1",home , "loop2");
		new Link("loop2",home , "loop3");
		new Link("loop3",home , "loop1");


		new Link("linktoplain",home , "plain1");
		new PlainFile("plain1",home , "valid");

		new Link("linktoapp",home , "app1");
		new App("app1",home , "valid");

		new Link("link1",home , "link2");
		new Link("link2",home , "textfile");
		new PlainFile("textfile",home , "valid");

		new Link("linktoNE", home, "batata");
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
	@Test(expected = FileDoesntExistsInDirectoryException.class)
	public void notExistsPlain(){
		WriteFileService service = new WriteFileService(token, "notexists", "");
		service.execute();

	}

	//TEST 4
	@Test(expected = FileDoesntExistsInDirectoryException.class)
	public void notExistsApp(){
		WriteFileService service = new WriteFileService(token, "notexists", "pt.tecnico.myDrive.domain.App");
		service.execute();

	}

	//TEST 5
	@Test(expected = FileDoesntExistsInDirectoryException.class)
	public void notExistsLink(){
		WriteFileService service = new WriteFileService(token, "notexists", "/home");
		service.execute();

	}

	//TEST 6
	@Test
	public void successEmptyWritePlain(){
		WriteFileService service = new WriteFileService(token, "validplain", "");
		service.execute();
		
		PlainFile pfile = (PlainFile)home.lookup("validplain");

		assertEquals("Empty write not executed", pfile.getContent(), "");

	}

	//TEST 7
	@Test
	public void successWritePlain(){
		WriteFileService service = new WriteFileService(token, "validplain", "mydrive");
		service.execute();
		
		PlainFile pfile = (PlainFile)home.lookup("validplain");
		
		assertEquals("Write not executed", pfile.getContent(), "mydrive");

	}
	//TEST 8
	@Test(expected=InvalidContentException.class)
	public void insuccessEmptyWriteApp(){
		WriteFileService service = new WriteFileService(token, "validapp", "");
		service.execute();
	}

	//TEST 9
	@Test(expected=InvalidContentException.class)
	public void insuccessWriteApp(){
		WriteFileService service = new WriteFileService(token, "validapp", "pt..tecnico.myDrive.domain.App");
		service.execute();
	}

	//TEST 10
	@Test
	public void successWriteApp(){
		WriteFileService service = new WriteFileService(token, "validapp", "pt.tecnico.myDrive.domain.App");
		service.execute();
		
		App appfile = (App) home.lookup("validapp");
		
		assertEquals("Write not executed", appfile.getContent(), "pt.tecnico.myDrive.domain.App");
	}

	//TEST 11
	@Test(expected=InvalidWriteException.class)
	public void insuccessWriteDir(){
		WriteFileService service = new WriteFileService(token, "root", "/home");
		service.execute();

	}

	//TEST 12
	@Test(expected = PathHasMoreThan1024CharactersException.class)
	public void insuccessWriteLinkLoop(){
		WriteFileService service = new WriteFileService(token, "loop1", "writelink");
		service.execute();
	}


	//TEST 13
	@Test
	public void success1024CharWriteLink(){
		WriteFileService service = new WriteFileService(token, "link1024", "writelink");
		service.execute();

		PlainFile pfile = (PlainFile)home.lookup(strA+"/"+strB+"/"+strC+"/"+Y19);
		assertEquals("1024 characters write not executed successfully", pfile.getContent(), "writelink");
	}

	//TEST 14
	@Test(expected = PathHasMoreThan1024CharactersException.class)
	public void insuccess1025CharWriteLink(){
		WriteFileService service = new WriteFileService(token, "link1025", "writelink");
		service.execute();
	}

	//TEST 15
	@Test
	public void successWriteLinkToPlain(){
		WriteFileService service = new WriteFileService(token, "linktoplain", "writelink");
		service.execute();

		PlainFile pfile = (PlainFile)home.lookup("plain1");
		assertEquals("write not executed successfully", pfile.getContent(), "writelink");
	}

	//TEST 16
	@Test
	public void successWriteLinkToApp(){
		WriteFileService service = new WriteFileService(token, "linktoapp", "writelink");
		service.execute();

		App appfile = (App)home.lookup("app1");
		assertEquals("write not executed successfully", appfile.getContent(), "writelink");
	}

	//TEST 17
	@Test
	public void successWriteDoubleLinkToPlain(){
		WriteFileService service = new WriteFileService(token, "Link1", "writelink");
		service.execute();

		PlainFile pfile = (PlainFile)home.lookup("textfile");
		assertEquals("write not executed successfully", pfile.getContent(), "writelink");
	}

	//Test 18
	@Test(expected = FileDoesntExistsInDirectoryException.class)
	public void insuccessWriteLinkToNotExistingPlain(){
		WriteFileService service = new WriteFileService(token, "linktoNE", "batata");
		service.execute();
	}
}



