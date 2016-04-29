package pt.tecnico.myDrive.service;

import org.junit.Test;
import pt.tecnico.myDrive.domain.*;
import pt.tecnico.myDrive.exception.CannotExecuteException;
import pt.tecnico.myDrive.exception.CannotExecutePlainFileException;
import pt.tecnico.myDrive.exception.InvalidArgumentsLengthException;
import pt.tecnico.myDrive.exception.InvalidPathException;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.Assert.assertEquals;

@SuppressWarnings("Duplicates")
public class ExecuteFileServiceTest extends LinkCommonTest {
    private final String INVALID = "invalid path";
    private final String[] ARGS = {"Hello", "World"};
    private final String[] EMPTY_ARGS = {};
    private final String APP = "execApp";
    private final String PLAIN_FILE = "execPlainFile";
    private final String LINK = "execLink";
    private final String APP_METHOD = "pt.tecnico.myDrive.Hello.hello";
    private final String PLAIN_FILE_CONTENT = APP_METHOD + " " + "Hello" + " " + "World";
    private final String APP_METHOD_EMPTY = "pt.tecnico.myDrive.Hello.helloEmpty";
    private final String APP_METHOD_RES = "Method: Hello World";

    @Override
    public MyDriveService createTestInstance(Long token, String name, String arguments) {
        return new ExecuteFileService(token, "./"+name, null);
    }

    private PrintStream console = System.out;
    private ByteArrayOutputStream auxRedirectSout(boolean redirect) {
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        if(redirect) {
            result = new ByteArrayOutputStream();
            System.setOut(new PrintStream(result));

        } else {
            System.out.flush();
            System.setOut(console);
        }
        return result;
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
    public void successRunAppMethodArgsNotEmpty() {
        new App(APP, root, home, APP_METHOD);
        ByteArrayOutputStream result = auxRedirectSout(true);

        ExecuteFileService service = new ExecuteFileService(rootToken, "./"+APP, ARGS);
        service.execute();

        assertEquals("Output don't match", APP_METHOD_RES , result.toString());

        auxRedirectSout(false);
    }

    @Test
    public void successRunAppMethodArgsEmpty() {
        new App(APP, root, home, APP_METHOD_EMPTY);
        ByteArrayOutputStream result = auxRedirectSout(true);

        ExecuteFileService service = new ExecuteFileService(rootToken, "./"+APP, EMPTY_ARGS);
        service.execute();

        final String APP_METHOD_RES_EMPTY = "Method:";
        assertEquals("Output don't match", APP_METHOD_RES_EMPTY, result.toString());

        auxRedirectSout(false);
    }

    @Test
    public void successRunAppMain() {
        final String APP_MAIN = "pt.tecnico.myDrive.Hello";
        new App(APP, root, home, APP_MAIN);
        ByteArrayOutputStream result = auxRedirectSout(true);

        ExecuteFileService service = new ExecuteFileService(rootToken, "./"+APP, ARGS);
        service.execute();

        final String APP_MAIN_RES = "Main: Hello World";
        assertEquals("Output don't match", APP_MAIN_RES, result.toString());

        auxRedirectSout(false);
    }

    @Test(expected = InvalidArgumentsLengthException.class)
    public void invalidRunAppWrongArguments() {
        new App(APP, root, home, APP_METHOD_EMPTY);

        ExecuteFileService service = new ExecuteFileService(rootToken, "./"+APP, ARGS);
        service.execute();
    }

    @Test
    public void successRunPlainFile() {
        new PlainFile(PLAIN_FILE, root, home, PLAIN_FILE_CONTENT);
        ByteArrayOutputStream result = auxRedirectSout(true);

        ExecuteFileService service = new ExecuteFileService(rootToken, "./"+PLAIN_FILE, EMPTY_ARGS);
        service.execute();

        assertEquals("Output don't match", APP_METHOD_RES, result.toString());

        auxRedirectSout(false);
    }

    @Test(expected = CannotExecutePlainFileException.class)
    public void invalidContentRunPlainFile() {
        new PlainFile(PLAIN_FILE, root, home, INVALID);

        ExecuteFileService service = new ExecuteFileService(rootToken, "./"+PLAIN_FILE, ARGS);
        service.execute();
    }

    @Test
    public void successRunLinkPointApp() {
        new App(APP, root, home, APP_METHOD_EMPTY);
        new Link(LINK, root, home, "/home/"+APP);
        ByteArrayOutputStream result = auxRedirectSout(true);

        ExecuteFileService service = new ExecuteFileService(rootToken, "./"+LINK, ARGS);
        service.execute();

        assertEquals("Output don't match", APP_METHOD_RES, result.toString());

        auxRedirectSout(false);
    }

    @Test
    public void successRunLinkPointPlainFile() {
        new PlainFile(PLAIN_FILE, root, home, PLAIN_FILE_CONTENT);
        new Link(LINK, root, home, "/home/"+PLAIN_FILE);
        ByteArrayOutputStream result = auxRedirectSout(true);

        ExecuteFileService service = new ExecuteFileService(rootToken, "./"+PLAIN_FILE, ARGS);
        service.execute();

        assertEquals("Output don't match", APP_METHOD_RES, result.toString());

        auxRedirectSout(false);
    }

    @Test
    public void successRunLinkPointValidBigPathApp() {
        String validLargePath = "";
        for(int i = 0; i < (1024-2)/2; i++) {
            validLargePath += "/a";
        }

        Manager manager = Manager.getInstance();
        Directory rootDir = manager.getRootDirectory();
        Directory lastDir = rootDir.createPath(root, validLargePath);
        new App("a", root, lastDir, APP_METHOD);
        new Link(LINK, root, home, validLargePath + "/a");

        ByteArrayOutputStream result = auxRedirectSout(true);

        ExecuteFileService service = new ExecuteFileService(rootToken, "./"+LINK, ARGS);
        service.execute();

        assertEquals("output don't match", APP_METHOD_RES, result.toString());

        auxRedirectSout(false);
    }

    @Test
    public void successRunLinkPointPathContainsLink() {
        new App(APP, root, home, APP_METHOD);
        new Link("link1", root, home, "/home/" + APP);
        new Link("link2", root, home, "/home/link1/");

        ByteArrayOutputStream result = auxRedirectSout(true);

        ExecuteFileService service = new ExecuteFileService(rootToken, "./link2", ARGS);
        service.execute();

        assertEquals("output don't match", DUMMY_CONTENT, result.toString());

        auxRedirectSout(false);
    }

}

