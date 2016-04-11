package pt.tecnico.myDrive.service;

import java.math.BigInteger;
import java.util.Random;

import org.junit.Test;

import pt.tecnico.myDrive.domain.*;
import pt.tecnico.myDrive.exception.FileDoesntExistsInDirectoryException;
import pt.tecnico.myDrive.exception.InvalidContentException;
import pt.tecnico.myDrive.exception.InvalidPermissionException;
import pt.tecnico.myDrive.exception.InvalidSetContentException;

import static org.junit.Assert.*;


public class WriteFileServiceTest extends AbstractServiceTest {

	private long token;
	private Directory home;

	@Override
	protected void populate() {
		Manager manager = Manager.getInstance();
		home = (Directory) manager.getRootDirectory().lookup("home");

		Login l = new Login("root", "***");
		l.setCurrentDir(home);
		token = l.getToken();

		new PlainFile("validplain", home ,"valid");
		new App("validapp", home ,"pt.tecnico.myDrive.domain.User");
		new Link("validlink", home ,"/");

	}

	//TEST 1
	@Test(expected = InvalidPermissionException.class)
	public void invalidPermissionsPlain(){
		
		User usertest = new User(Manager.getInstance(), "usertest");
		Login testLogin = new Login(usertest.getUsername(), usertest.getUsername());
		testLogin.setCurrentDir(home);
		long nopermtoken = testLogin.getToken();
		
		WriteFileService service = new WriteFileService(nopermtoken, "validplain", "");
		service.execute();

	}

	//TEST 2
	@Test(expected = FileDoesntExistsInDirectoryException.class)
	public void notExistsPlain(){
		WriteFileService service = new WriteFileService(token, "notexists", "");
		service.execute();

	}

	//TEST 3
	@Test(expected = FileDoesntExistsInDirectoryException.class)
	public void notExistsApp(){
		WriteFileService service = new WriteFileService(token, "notexists", "pt.tecnico.myDrive.domain.App");
		service.execute();

	}

	//TEST 4
	@Test(expected = FileDoesntExistsInDirectoryException.class)
	public void notExistsLink(){
		WriteFileService service = new WriteFileService(token, "notexists", "/home");
		service.execute();

	}

	//TEST 5
	@Test
	public void successEmptyWritePlain(){
		WriteFileService service = new WriteFileService(token, "validplain", "");
		service.execute();
		
		PlainFile pfile = (PlainFile)home.lookup("validplain");

		assertEquals("Empty write not executed", pfile.getContent(), "");

	}

	//TEST 6
	@Test
	public void successWritePlain(){
		WriteFileService service = new WriteFileService(token, "validplain", "mydrive");
		service.execute();
		
		PlainFile pfile = (PlainFile)home.lookup("validplain");
		
		assertEquals("Write not executed", pfile.getContent(), "mydrive");

	}
	//TEST 7
	@Test(expected=InvalidContentException.class)
	public void insuccessEmptyWriteApp(){
		WriteFileService service = new WriteFileService(token, "validapp", "");
		service.execute();

//		App appfile = (App) home.lookup("validapp");
//
//		assertNotEquals("Write executed successfully", appfile.getContent(), "");

	}

	//TEST 8
	@Test(expected=InvalidContentException.class)
	public void insuccessWriteApp(){
		WriteFileService service = new WriteFileService(token, "validapp", "pt..tecnico.myDrive.domain.App");
		service.execute();

//		App appfile = (App) home.lookup("validapp");
//
//		assertNotEquals("Write executed successfully", appfile.getContent(), "pt..tecnico.myDrive.domain.App");

	}

	//TEST 9
	@Test
	public void successWriteApp(){
		WriteFileService service = new WriteFileService(token, "validapp", "pt.tecnico.myDrive.domain.App");
		service.execute();
		
		App appfile = (App) home.lookup("validapp");
		
		assertEquals("Write not executed", appfile.getContent(), "pt.tecnico.myDrive.domain.App");
	
	}

	//TEST 10
	@Test(expected=InvalidSetContentException.class)
	public void insuccessWriteLink(){
		WriteFileService service = new WriteFileService(token, "validlink", "/home");
		service.execute();
		
//		Link linkfile = (Link) home.lookup("validlink");
//
//		assertNotEquals("Write executed successfully", linkfile.getContent(), "/home");
	
	}

	//TEST 11
	@Test(expected=InvalidSetContentException.class)
	public void insuccessWriteDir(){
		WriteFileService service = new WriteFileService(token, "root", "/home");
		service.execute();

	}

}



