package pt.tecnico.myDrive.service;

import org.junit.Test;
import pt.tecnico.myDrive.domain.*;
import pt.tecnico.myDrive.exception.AccessDeniedException;
import pt.tecnico.myDrive.exception.FileDoesntExistsInDirectoryException;

public abstract class PermissionsCommonTest extends TokenValidationServiceTest {
    Long rootToken;
    User root;
    private Long testUserToken;
    Directory home;

    String rootPlainFile = "rootPlainFile";
    String rootApp = "rootApp";
    String rootLinkApp = "rootLinkApp";
    String rootLinkPlainFile = "rootLinkPlainFile";
    String dummyContent = "dummyContent";

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

        new PlainFile("rootPlainFile", root, home, dummyContent);
        String fullyQualifiedName = "pt.tecnico.myDrive.Main";
        new App(rootApp, root, home, fullyQualifiedName);
        new Link(rootLinkPlainFile, root, home, "/home/" + rootPlainFile);
        new Link(rootLinkApp, root, home, "/home/" + rootApp);
    }

    public abstract MyDriveService createTestInstance(Long token, String name, String content);

    @Test(expected = FileDoesntExistsInDirectoryException.class)
    public void nonExistingFileInDirectory() {

        MyDriveService service = createTestInstance(rootToken, "InvalidFile", dummyContent);
        service.execute();
    }

    @Test(expected = AccessDeniedException.class)
    public void noPermissionInPlainFile() {
        PlainFile plainFile = (PlainFile) home.getFileByName(rootPlainFile);
        plainFile.setPermissions("rwxd----");

        MyDriveService service = createTestInstance(testUserToken, rootPlainFile, dummyContent);
        service.execute();
    }

    @Test(expected = AccessDeniedException.class)
    public void noPermissionInApp() {
        App app = (App) home.getFileByName(rootApp);
        app.setPermissions("rwxd----");

        MyDriveService service = createTestInstance(testUserToken, rootApp, dummyContent);
        service.execute();
    }

    @Test(expected = AccessDeniedException.class)
    public void noPermissionInLinkPointsPlainFile() {
        PlainFile plainFile = (PlainFile) home.getFileByName(rootPlainFile);
        plainFile.setPermissions("rwxd----");

        MyDriveService service = createTestInstance(testUserToken, rootLinkPlainFile, dummyContent);
        service.execute();
    }

    @Test(expected = AccessDeniedException.class)
    public void noPermissionInLinkPointsApp()  {
        App app = (App) home.getFileByName(rootApp);
        app.setPermissions("rwxd----");

        MyDriveService service = createTestInstance(testUserToken, rootLinkApp, dummyContent);
        service.execute();
    }
}
