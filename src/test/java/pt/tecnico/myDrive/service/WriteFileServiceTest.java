package pt.tecnico.myDrive.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import java.math.BigInteger;
import java.util.Random;

import org.junit.Test;

import pt.tecnico.myDrive.domain.*;
import pt.tecnico.myDrive.exception.FileDoesntExistsInDirectoryException;
import pt.tecnico.myDrive.exception.InvalidPermissionException;


public class WriteFileServiceTest extends AbstractServiceTest {
	
	private long token;
	private Directory home;

	@Override
	protected void populate() {
		Manager manager = Manager.getInstance();
		Directory rootDir = manager.getRootDirectory();
		home = (Directory) rootDir.lookup("home");

		Login l = new Login("root", "***");
		l.setCurrentDir(home);
		token = l.getToken();

		new PlainFile("validplain", home ,"valid");
		new App("validapp", home ,"pt.tecnico.myDrive.User");
		new Link("validlink", home ,"/");
		
	}
	
	@Test(expected = InvalidPermissionException.class)
	public void invalidPermissionsPlain(){
		
		User usertest = new User(Manager.getInstance(), "usertest");
		Login testLogin = new Login(usertest.getUsername(), usertest.getUsername());
		testLogin.setCurrentDir(home);
		long nopermtoken = testLogin.getToken();
		
		WriteFileService service = new WriteFileService(nopermtoken, "validplain", "");
		service.execute();	
	}

	@Test(expected = FileDoesntExistsInDirectoryException.class)
	public void notExistsPlain(){
		WriteFileService service = new WriteFileService(token, "notexists", "");
		service.execute();
	}
	
	
	@Test(expected = FileDoesntExistsInDirectoryException.class)
	public void notExistsApp(){
		WriteFileService service = new WriteFileService(token, "notexists", "pt.tecnico.myDrive.App");
		service.execute();
	}
	
	
	@Test(expected = FileDoesntExistsInDirectoryException.class)
	public void notExistsLink(){
		WriteFileService service = new WriteFileService(token, "notexists", "/home");
		service.execute();
	}

	@Test
	public void successEmptyWritePlain(){
		WriteFileService service = new WriteFileService(token, "validplain", "");
		service.execute();
		
		PlainFile pfile = (PlainFile)home.lookup("validplain");

		assertEquals("Empty write not executed", pfile.getContent(), "");
	}


	@Test
	public void successWritePlain(){
		WriteFileService service = new WriteFileService(token, "validplain", "mydrive");
		service.execute();
		
		PlainFile pfile = (PlainFile)home.lookup("validplain");
		
		assertEquals("Write not executed", pfile.getContent(), "mydrive");
	}
	/*
	@Test
	public void insuccessEmptyWriteApp(){
		WriteFileService service = new WriteFileService(token, "validapp", "");
		service.execute();
		
		App appfile = (App) home.getFileByName("validapp");
		
		assertNotEquals("Write executed successfully", appfile.getContent(), "");
	}
	
	@Test
	public void insuccessWriteApp(){
		WriteFileService service = new WriteFileService(token, "validapp", "pt..tecnico.myDrive.domain.App");
		service.execute();
		
		App appfile = (App) home.getFileByName("validapp");
		
		assertNotEquals("Write executed successfully", appfile.getContent(), "pt..tecnico.myDrive.domain.App");
	
	}
	
	@Test
	public void successWriteApp(){
		WriteFileService service = new WriteFileService(token, "validapp", "pt.tecnico.myDrive.domain.App");
		service.execute();
		
		App appfile = (App) home.getFileByName("validapp");
		
		assertEquals("Write not executed", appfile.getContent(), "pt.tecnico.myDrive.domain.App");
	
	}
	
	@Test
	public void insuccessWriteLink(){
		WriteFileService service = new WriteFileService(token, "validlink", "/home");
		service.execute();
		
		Link linkfile = (Link) home.getFileByName("validlink");
		
		assertNotEquals("Write executed successfully", linkfile.getContent(), "/home");
	
	}
	*/
	
}



