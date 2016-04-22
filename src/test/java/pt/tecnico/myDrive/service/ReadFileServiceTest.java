package pt.tecnico.myDrive.service;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import pt.tecnico.myDrive.domain.*;
import pt.tecnico.myDrive.exception.AccessDeniedException;
import pt.tecnico.myDrive.exception.CannotReadException;
import pt.tecnico.myDrive.exception.FileDoesntExistsInDirectoryException;
import pt.tecnico.myDrive.exception.PathTooBigException;

public class ReadFileServiceTest extends TokenValidationServiceTest {
    private Long rootToken;
    private Long testUserToken;
    private Directory home;
    private User root;
    private String rootPlainFile = "rootPlainFile";
    private String rootApp = "rootApp";
    private String rootLinkPlainFile = "rootLinkPlainFile";
    private String rootLinkApp = "rootLinkApp";
    private String dummyContent = "dummyContent";
    private String fullyQualifiedName = "pt.tecnico.myDrive.Main";

    @Override
    protected void populate() {
    	super.populate();
        Manager manager = Manager.getInstance();

        Login rootLogin = new Login("root", "***");
        root = rootLogin.getCurrentUser();
        rootToken = rootLogin.getToken();

        User testUser = new User(manager, "testUser");
        Login testUserLogin = new Login(testUser.getName(), testUser.getName());
        testUserToken = testUserLogin.getToken();

        home = (Directory) manager.getRootDirectory().getFileByName("home");

        rootLogin.setCurrentDir(home);
        testUserLogin.setCurrentDir(home);

        new PlainFile("rootPlainFile", root, home, dummyContent);
        new App("rootApp", root, home, fullyQualifiedName);
        new Link("rootLinkPlainFile", root, home, "/home/" + rootPlainFile);
        new Link("rootLinkApp", root, home, "/home/" + rootApp);
    }

    /*<--------------toDelete---------->
    @Test(expected = FileDoesntExistsInDirectoryException.class)
    public void nonExistingFileInDirectory() {
        ReadFileService service = new ReadFileService(rootToken, "InvalidFile");
        service.execute();
    }
    */

    @Test
    public void successReadPlainFile() {
        ReadFileService service = new ReadFileService(rootToken, rootPlainFile);
        service.execute();

        assertEquals("output don't match", dummyContent, service.result());
    }

    @Test
    public void successReadApp() {
        ReadFileService service = new ReadFileService(rootToken, rootApp);
        service.execute();

        assertEquals("output don't match", fullyQualifiedName, service.result());
    }

    @Test(expected = CannotReadException.class)
    public void invalidReadDirectory() {
        ReadFileService service = new ReadFileService(rootToken, "root");
        service.execute();
    }

    /*<------------------Todelete---------->
    @Test(expected = AccessDeniedException.class)
    public void noPermissionToReadPlainFile() {
        PlainFile plainFile = (PlainFile) home.getFileByName(rootPlainFile);
        plainFile.setPermissions("rwxd----");

        ReadFileService service = new ReadFileService(testUserToken, rootPlainFile);
        service.execute();
    }
    */

    /*<------------------Todelete---------->
    @Test(expected = AccessDeniedException.class)
    public void noPermissionToReadApp() {
        App app = (App) home.getFileByName(rootApp);
        app.setPermissions("rwxd----");

        ReadFileService service = new ReadFileService(testUserToken, rootApp);
        service.execute();
    }
    */
    @Test
    public void successReadLinkPointsPlainFile() {
        ReadFileService service = new ReadFileService(rootToken, rootLinkPlainFile);
        service.execute();

        assertEquals("output don't match", dummyContent, service.result());
    }

    @Test
    public void successReadLinkPointsApp() {
        ReadFileService service = new ReadFileService(rootToken, rootLinkApp);
        service.execute();

        assertEquals("output don't match", fullyQualifiedName, service.result());
    }

    @Test(expected = CannotReadException.class)
    public void invalidReadLinkDirectory() {
        new Link("rootLinkDir", root, home, "/home/root");
        ReadFileService service = new ReadFileService(rootToken, "rootLinkDir");
        service.execute();
    }

    /*<------------------Todelete---------->
    @Test(expected = AccessDeniedException.class)
    public void noPermissionToReadLinkPointsPlainFile() {
        PlainFile plainFile = (PlainFile) home.getFileByName(rootPlainFile);
        plainFile.setPermissions("rwxd----");

        ReadFileService service = new ReadFileService(testUserToken, rootLinkPlainFile);
        service.execute();
    }
    */

    /*<------------------Todelete---------->
    @Test(expected = AccessDeniedException.class)
    public void noPermissionToReadLinkPointsApp() {
        App app = (App) home.getFileByName(rootApp);
        app.setPermissions("rwxd----");

        ReadFileService service = new ReadFileService(testUserToken, rootLinkApp);
        service.execute();
    }
    */


    @Test(expected = FileDoesntExistsInDirectoryException.class)
    public void invalidReadLinkPointsNonExistingFile() {
        new Link("link", root, home, "/home/invalidFile");
        ReadFileService service = new ReadFileService(rootToken, "link");
        service.execute();
    }



    @Test(expected = PathTooBigException.class)
    public void invalidReadLinkPointsToSelf() {
        new Link("link", root, home, "/home/link");
        ReadFileService service = new ReadFileService(rootToken, "link");
        service.execute();
    }

    @Test(expected = PathTooBigException.class)
    public void invalidReadOfLoopGeneratedByLinks() {
        new Link("link1", root, home, "/home/link2");
        new Link("link2", root, home, "/home/link1");
        ReadFileService service = new ReadFileService(rootToken, "link1");
        service.execute();
    }

    @Test
    public void successReadLinkPointValidBigPath() {
        String validLargePath = "";
        for(int i = 0; i < (1024-2)/2; i++) {
            validLargePath += "/a";
        }

        Manager manager = Manager.getInstance();
        Directory rootDir = manager.getRootDirectory();
        Directory lastDir = rootDir.createPath(root, validLargePath);
        new PlainFile("a", root, lastDir, dummyContent);
        new Link("link", root, home, validLargePath + "/a");
        ReadFileService service = new ReadFileService(rootToken, "link");
        service.execute();

        assertEquals("output don't match", dummyContent, service.result());
    }

    @Test(expected = PathTooBigException.class)
    public void readLinkPointInvalidBigPath() {
        String invalidLargePath = "";
        for(int i = 0; i < (1022/2); i++) {
            invalidLargePath += "/a";
        }
        Manager manager = Manager.getInstance();
        Directory rootDir = manager.getRootDirectory();
        Directory lastDir = rootDir.createPath(root, invalidLargePath);
        new PlainFile("a", root, lastDir, dummyContent);
        new Link("link", root, home, invalidLargePath + "/aa");

        ReadFileService service = new ReadFileService(rootToken, "link");
        service.execute();
    }

    @Test
    public void successReadLinkPointPathContainsLink() {
        new Link("link1", root, home, "/home/" + rootPlainFile);
        new Link("link2", root, home, "/home/link1/");

        ReadFileService service = new ReadFileService(rootToken, "link2");
        service.execute();

        assertEquals("output don't match", dummyContent, service.result());
    }
}
