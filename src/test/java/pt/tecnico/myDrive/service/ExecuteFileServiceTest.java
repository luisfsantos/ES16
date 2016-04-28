package pt.tecnico.myDrive.service;

import org.junit.Test;
import pt.tecnico.myDrive.domain.Link;
import pt.tecnico.myDrive.domain.Directory;
import pt.tecnico.myDrive.exception.CannotExecuteException;
import pt.tecnico.myDrive.exception.InvalidPathException;

public class ExecuteFileServiceTest extends LinkCommonTest {
    private final String INVALID_PATH = "invalid path";
    private final String[] ARGS = {"Olá", "World"};

    @Override
    public MyDriveService createTestInstance(Long token, String name, String arguments) {
        return new ExecuteFileService(token, "./"+name, null);
    }


    @Test(expected = InvalidPathException.class)
    public void invalidPath() {
        ExecuteFileService service = new ExecuteFileService(rootToken, INVALID_PATH, ARGS);
        service.execute();
    }

    @Test(expected = CannotExecuteException.class)
    public void invalidExecuteDirectory() {
        new Directory("test", root, home);
        ExecuteFileService service = new ExecuteFileService(rootToken, "./test", null);
        service.execute();
    }

    @Test(expected = CannotExecuteException.class)
    public void invalidExecuteLinkDirectory() {
        new Link("rootLinkDir", root, home, "/home/root");
        ExecuteFileService service = new ExecuteFileService(rootToken, "./rootLinkDir", null);
        service.execute();
    }
}

