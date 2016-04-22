package pt.tecnico.myDrive.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import pt.tecnico.myDrive.domain.*;
import pt.tecnico.myDrive.exception.AccessDeniedException;
import pt.tecnico.myDrive.exception.FileDoesntExistsInDirectoryException;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Collection;

@RunWith(value = Parameterized.class)
public class DeleteReadServiceBaseTest extends AbstractServiceTest {
    private Class<MyDriveService> serviceClass;
    private Constructor serviceClassConstructor;

    private Long rootToken;
    private User root;
    private Long testUserToken;
    private Directory home;

    private String rootPlainFile = "rootPlainFile";
    private String rootApp = "rootApp";
    private String rootLinkApp = "rootLinkApp";
    private String rootLinkPlainFile = "rootLinkPlainFile";
    private String dummyContent = "dummyContent";
    private String fullyQualifiedName = "pt.tecnico.myDrive.Main";

    public DeleteReadServiceBaseTest(Class<MyDriveService> serviceClass) throws NoSuchMethodException {
        this.serviceClass = serviceClass;
        serviceClassConstructor = serviceClass.getConstructor(Long.class, String.class);
    }

    @Parameters
    public static Collection<Object[]> services() {
        Object[][] data = new Object[][] { { ReadFileService.class }, { DeleteFileService.class }};
        return Arrays.asList(data);
    }

    @Override
    protected void populate() {
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
        new App(rootApp, root, home, fullyQualifiedName);
        new Link(rootLinkPlainFile, root, home, "/home/" + rootPlainFile);
        new Link(rootLinkApp, root, home, "/home/" + rootApp);
    }

    @Test(expected = FileDoesntExistsInDirectoryException.class)
    public void nonExistingFileInDirectory()
            throws IllegalAccessException, InvocationTargetException, InstantiationException {

        MyDriveService service = (MyDriveService) serviceClassConstructor.newInstance(rootToken, "InvalidFile");
        service.execute();
    }

    @Test(expected = AccessDeniedException.class)
    public void noPermissionInPlainFile()
            throws IllegalAccessException, InvocationTargetException, InstantiationException {
        PlainFile plainFile = (PlainFile) home.getFileByName(rootPlainFile);
        plainFile.setPermissions("rwxd----");

        MyDriveService service = (MyDriveService) serviceClassConstructor.newInstance(testUserToken, rootPlainFile);
        service.execute();
    }

    @Test(expected = AccessDeniedException.class)
    public void noPermissionInApp()
            throws IllegalAccessException, InvocationTargetException, InstantiationException {
        App app = (App) home.getFileByName(rootApp);
        app.setPermissions("rwxd----");

        MyDriveService service = (MyDriveService) serviceClassConstructor.newInstance(testUserToken, rootApp);
        service.execute();
    }

    @Test(expected = AccessDeniedException.class)
    public void noPermissionInLinkPointsPlainFile()
            throws IllegalAccessException, InvocationTargetException, InstantiationException {
        PlainFile plainFile = (PlainFile) home.getFileByName(rootPlainFile);
        plainFile.setPermissions("rwxd----");

        MyDriveService service = (MyDriveService) serviceClassConstructor.newInstance(testUserToken, rootLinkPlainFile);
        service.execute();
    }

    @Test(expected = AccessDeniedException.class)
    public void noPermissionInLinkPointsApp()
            throws IllegalAccessException, InvocationTargetException, InstantiationException {
        App app = (App) home.getFileByName(rootApp);
        app.setPermissions("rwxd----");

        MyDriveService service = (MyDriveService) serviceClassConstructor.newInstance(testUserToken, rootLinkApp);
        service.execute();
    }
}
