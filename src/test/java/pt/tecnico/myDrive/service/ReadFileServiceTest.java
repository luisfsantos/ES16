package pt.tecnico.myDrive.service;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import pt.tecnico.myDrive.domain.*;
import pt.tecnico.myDrive.exception.CannotReadException;

public class ReadFileServiceTest extends LinkCommonTest {
    private Long rootToken;
    private Directory home;
    private User root;
    private String dummyContent = "dummyContent";
    private String fullyQualifiedName = "pt.tecnico.myDrive.Main";

    public MyDriveService createTestInstance(Long token, String name, String content) {
        return new ReadFileService(token, name);
    }

    @Override
    protected void populate() {
    	super.populate();
        Manager manager = Manager.getInstance();

        Login rootLogin = new Login("root", "***");
        root = rootLogin.getCurrentUser();
        rootToken = rootLogin.getToken();

        home = (Directory) manager.getRootDirectory().getFileByName("home");

        rootLogin.setCurrentDir(home);
    }

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

    @Test
    public void successReadLinkPointPathContainsLink() {
        new Link("link1", root, home, "/home/" + rootPlainFile);
        new Link("link2", root, home, "/home/link1/");

        ReadFileService service = new ReadFileService(rootToken, "link2");
        service.execute();

        assertEquals("output don't match", dummyContent, service.result());
    }
}
