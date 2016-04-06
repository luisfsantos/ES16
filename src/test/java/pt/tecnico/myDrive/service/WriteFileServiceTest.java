package pt.tecnico.myDrive.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertThat;

import java.math.BigInteger;
import java.util.Random;

import org.junit.Test;

import pt.tecnico.myDrive.domain.Manager;
import pt.tecnico.myDrive.domain.Directory;
import pt.tecnico.myDrive.domain.PlainFile;
import pt.tecnico.myDrive.domain.Link;
import pt.tecnico.myDrive.domain.App;
import pt.tecnico.myDrive.domain.Login;

import pt.tecnico.myDrive.exception.MyDriveException;

public class WriteFileServiceTest extends AbstractServiceTest {
	
	private long token;
	private String filename;
	private String content;
		
	
	@Override
	protected void populate() {
		Manager m = Manager.getInstance();
		Login l = new Login("root", "***");
		token = l.getToken();
		
	}
	
}