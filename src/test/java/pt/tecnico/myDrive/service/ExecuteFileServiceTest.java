package pt.tecnico.myDrive.service;

import mockit.Mocked;
import mockit.Verifications;
import org.junit.Test;
import pt.tecnico.myDrive.domain.*;
import pt.tecnico.myDrive.exception.CannotExecuteException;
import pt.tecnico.myDrive.exception.CannotExecutePlainFileException;
import pt.tecnico.myDrive.exception.InvalidPathException;

public class ExecuteFileServiceTest extends LinkCommonTest {
    private final String INVALID = "invalid path";
    private final String[] ARGS = {"Hello", "World!"};
    private final String[] ARGS_EMPTY = {};
    private final String APP = "execApp";
    private final String PLAIN_FILE = "execPlainFile";
    private final String LINK = "execLink";
    private final String APP_METHOD = "pt.tecnico.myDrive.service.Hello.hello";
    private final String APP_METHOD_EMPTY = "pt.tecnico.myDrive.Hello.helloEmpty";
    private final String PLAIN_FILE_CONTENT = APP_METHOD + " " + "Hello" + " " + "World";

    @Override
    public MyDriveService createTestInstance(Long token, String name, String arguments) {
        return new ExecuteFileService(token, "./"+name, null);
    }

    @Test(expected = InvalidPathException.class)
    public void invalidPath() {
        ExecuteFileService service = new ExecuteFileService(rootToken, INVALID, ARGS);
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

    @Test
    public void successRunAppMethodArgsNotEmpty(@Mocked Hello helloMock) {
        new App(APP, root, home, APP_METHOD);

        ExecuteFileService service = new ExecuteFileService(rootToken, "./"+APP, ARGS);
        service.execute();

        new Verifications() {{
            helloMock.hello(ARGS); times = 1;
        }};
    }

    @Test
    public void successRunAppMethodArgsEmpty(@Mocked Hello helloMock) {
        new App(APP, root, home, APP_METHOD_EMPTY);

        ExecuteFileService service = new ExecuteFileService(rootToken, "./"+APP, ARGS_EMPTY);
        service.execute();

        new Verifications() {{
            helloMock.helloEmpty(); times = 1;
        }};
    }

    @Test
    public void successRunAppMain(@Mocked Hello helloMock) {
        final String APP_MAIN = "pt.tecnico.myDrive.Hello";
        new App(APP, root, home, APP_MAIN);

        ExecuteFileService service = new ExecuteFileService(rootToken, "./"+APP, ARGS);
        service.execute();

        new Verifications() {{
            helloMock.main(ARGS); times = 1;
        }};
    }

    @Test(expected = IllegalArgumentException.class)
    public void invalidRunAppWrongArguments() {
        new App(APP, root, home, APP_METHOD_EMPTY);

        ExecuteFileService service = new ExecuteFileService(rootToken, "./"+APP, ARGS);
        service.execute();
    }

    @Test
    public void successRunPlainFile(@Mocked Hello helloMock) {
        new PlainFile(PLAIN_FILE, root, home, PLAIN_FILE_CONTENT);

        ExecuteFileService service = new ExecuteFileService(rootToken, "./"+PLAIN_FILE, ARGS_EMPTY);
        service.execute();

        new Verifications() {{
            helloMock.hello(ARGS); times = 1;
        }};
    }

    @Test(expected = CannotExecutePlainFileException.class)
    public void invalidContentRunPlainFile() {
        new PlainFile(PLAIN_FILE, root, home, INVALID);

        ExecuteFileService service = new ExecuteFileService(rootToken, "./"+PLAIN_FILE, ARGS);
        service.execute();
    }

    @Test
    public void successRunLinkPointApp(@Mocked Hello helloMock) {
        new App(APP, root, home, APP_METHOD);
        new Link(LINK, root, home, "/home/"+APP);

        ExecuteFileService service = new ExecuteFileService(rootToken, "./"+LINK, ARGS);
        service.execute();

        new Verifications() {{
            helloMock.hello(ARGS); times = 1;
        }};
    }

    @Test
    public void successRunLinkPointPlainFile(@Mocked Hello helloMock) {
        new PlainFile(PLAIN_FILE, root, home, PLAIN_FILE_CONTENT);
        new Link(LINK, root, home, "/home/"+PLAIN_FILE);

        ExecuteFileService service = new ExecuteFileService(rootToken, "./"+PLAIN_FILE, ARGS);
        service.execute();

        new Verifications() {{
            helloMock.hello(ARGS); times = 1;
        }};
    }

    @Test
    public void successRunLinkPointValidBigPathApp(@Mocked Hello helloMock) {
        String validLargePath = "";
        for(int i = 0; i < (1024-2)/2; i++) {
            validLargePath += "/a";
        }

        Manager manager = Manager.getInstance();
        Directory rootDir = manager.getRootDirectory();
        Directory lastDir = rootDir.createPath(root, validLargePath);
        new App("a", root, lastDir, APP_METHOD);
        new Link(LINK, root, home, validLargePath + "/a");

        ExecuteFileService service = new ExecuteFileService(rootToken, "./"+LINK, ARGS);
        service.execute();

        new Verifications() {{
            helloMock.hello(ARGS); times = 1;
        }};
    }

    @Test
    public void successRunLinkPointPathContainsLink(@Mocked Hello helloMock) {
        new App(APP, root, home, APP_METHOD);
        new Link("link1", root, home, "/home/" + APP);
        new Link("link2", root, home, "/home/link1/");

        ExecuteFileService service = new ExecuteFileService(rootToken, "./link2", ARGS);
        service.execute();

        new Verifications() {{
            helloMock.hello(ARGS); times = 1;
        }};
    }

}

