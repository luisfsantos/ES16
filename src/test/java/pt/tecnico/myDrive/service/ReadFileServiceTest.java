package pt.tecnico.myDrive.service;

import org.junit.Test;
import pt.tecnico.myDrive.domain.*;
import pt.tecnico.myDrive.exception.FileDoesntExistsInDirectoryException;
import pt.tecnico.myDrive.exception.InvalidTokenException;

import java.math.BigInteger;
import java.util.Random;

public class ReadFileServiceTest extends AbstractServiceTest {
    private long rootToken;
    private long testUserToken;
    private Directory home;

    @Override
    protected void populate() {
        Manager manager = Manager.getInstance();

        Login rootLogin = new Login("root", "***");
        rootToken = rootLogin.getToken();
        home = (Directory) manager.getRootDirectory().getFileByName("home");
        new PlainFile("existingPlanFile", home, "existingPlainFile");

        User testUser = new User(manager, "testUser");
        Login userLogin = new Login(testUser.getName(), testUser.getName());
    }

    @Test(expected = FileDoesntExistsInDirectoryException.class)
    public void nonExistingFileInDirectory() {
        ReadFileService service = new ReadFileService(rootToken, "InvalidFile");
        service.execute();
    }

    @Test(expected = InvalidTokenException.class)
    public void invalidToken() {
        final long invalidToken = new BigInteger(64, new Random()).longValue();
        ReadFileService service = new ReadFileService(invalidToken, "existingPlanFile");
        service.execute();
    }
}
