package pt.tecnico.myDrive.service;

import org.junit.Test;
import pt.tecnico.myDrive.domain.*;
import pt.tecnico.myDrive.exception.AccessDeniedException;
import pt.tecnico.myDrive.exception.FileDoesNotExistInDirectoryException;

public abstract class PermissionsCommonTest extends TokenValidationServiceTest {
    Long rootToken;
    User root;
    private Long testUserToken;
    Directory home;

    final String PLAIN_FILE = "plainFile";
    final String APP = "APP";
    final String LINK_APP = "linkApp";
    final String LINK_PLAIN_FILE = "linkPlainFile";
    final String DUMMY_CONTENT = "dummyContent";
    final String FULLY_QUALIFIED_NAME = "pt.tecnico.myDrive.Main";

    @Override
    protected void populate() {
        super.populate();
        Manager manager = Manager.getInstance();
        home = (Directory) manager.getRootDirectory().getFileByName("home");

        Login rootLogin = new Login("root", "***");
        root = rootLogin.getCurrentUser();
        rootToken = rootLogin.getToken();

        User testUser = new User(manager, "testUser");
        Login testUserLogin = new Login(testUser.getName(), testUser.getName());
        testUserToken = testUserLogin.getToken();

        rootLogin.setCurrentDir(home);
        testUserLogin.setCurrentDir(home);

        new PlainFile(PLAIN_FILE, root, home, DUMMY_CONTENT);
        new App(APP, root, home, FULLY_QUALIFIED_NAME);
        new Link(LINK_PLAIN_FILE, root, home, "/home/" + PLAIN_FILE);
        new Link(LINK_APP, root, home, "/home/" + APP);
    }

    public abstract MyDriveService createTestInstance(Long token, String name, String content);

    @Test(expected = FileDoesNotExistInDirectoryException.class)
    public void nonExistingFileInDirectory() {

        MyDriveService service = createTestInstance(rootToken, "InvalidFile", DUMMY_CONTENT);
        service.execute();
    }

    @Test(expected = AccessDeniedException.class)
    public void noPermissionInPlainFile() {
        PlainFile plainFile = (PlainFile) home.getFileByName(PLAIN_FILE);
        plainFile.setPermissions("rwxd----");

        MyDriveService service = createTestInstance(testUserToken, PLAIN_FILE, DUMMY_CONTENT);
        service.execute();
    }

    @Test(expected = AccessDeniedException.class)
    public void noPermissionInApp() {
        App app = (App) home.getFileByName(APP);
        app.setPermissions("rwxd----");

        MyDriveService service = createTestInstance(testUserToken, APP, DUMMY_CONTENT);
        service.execute();
    }

    @Test(expected = AccessDeniedException.class)
    public void noPermissionInLinkPointsPlainFile() {
        PlainFile plainFile = (PlainFile) home.getFileByName(PLAIN_FILE);
        plainFile.setPermissions("rwxd----");

        MyDriveService service = createTestInstance(testUserToken, LINK_PLAIN_FILE, DUMMY_CONTENT);
        service.execute();
    }

    @Test(expected = AccessDeniedException.class)
    public void noPermissionInLinkPointsApp()  {
        App app = (App) home.getFileByName(APP);
        app.setPermissions("rwxd----");

        MyDriveService service = createTestInstance(testUserToken, LINK_APP, DUMMY_CONTENT);
        service.execute();
    }
}
