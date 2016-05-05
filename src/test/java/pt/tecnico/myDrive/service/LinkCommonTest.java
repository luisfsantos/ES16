package pt.tecnico.myDrive.service;

import org.junit.Test;
import pt.tecnico.myDrive.domain.Directory;
import pt.tecnico.myDrive.domain.Link;
import pt.tecnico.myDrive.domain.Manager;
import pt.tecnico.myDrive.domain.PlainFile;
import pt.tecnico.myDrive.exception.FileDoesNotExistInDirectoryException;
import pt.tecnico.myDrive.exception.PathTooBigException;

public abstract class LinkCommonTest extends PermissionsCommonTest {

    @Test(expected = FileDoesNotExistInDirectoryException.class)
    public void invalidLinkPointsNonExistingFile() {
        new Link("link", root, home, "/home/invalidFile");

        MyDriveService service = createTestInstance(rootToken, "link", DUMMY_CONTENT);
        service.execute();
    }


    @Test(expected = PathTooBigException.class)
    public void invalidLinkPointsToSelf() {
        new Link("link", root, home, "/home/link");

        MyDriveService service = createTestInstance(rootToken, "link", DUMMY_CONTENT);
        service.execute();
    }

    @Test(expected = PathTooBigException.class)
    public void invalidOfLoopGeneratedByLinks() {
        new Link("l1", root, home, "/home/l2");
        new Link("l2", root, home, "/home/l1");

        MyDriveService service = createTestInstance(rootToken, "l1", DUMMY_CONTENT);
        service.execute();
    }

    @Test(expected = PathTooBigException.class)
    public void linkPointInvalidBigPath() {
        String invalidLargePath = "";
        for(int i = 0; i < (1022/2); i++) {
            invalidLargePath += "/a";
        }
        Manager manager = Manager.getInstance();
        Directory rootDir = manager.getRootDirectory();
        Directory lastDir = rootDir.createPath(root, invalidLargePath);
        new PlainFile("a", root, lastDir, DUMMY_CONTENT);
        new Link("link", root, home, invalidLargePath + "/aa");

        MyDriveService service = createTestInstance(rootToken, "link", DUMMY_CONTENT);
        service.execute();
    }

}
