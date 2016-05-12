package pt.tecnico.myDrive.system;


import org.junit.Test;
import pt.tecnico.myDrive.domain.Directory;
import pt.tecnico.myDrive.domain.Login;
import pt.tecnico.myDrive.domain.Manager;
import pt.tecnico.myDrive.domain.User;
import pt.tecnico.myDrive.service.*;
import pt.tecnico.myDrive.service.dto.FileDto;

import static org.junit.Assert.assertEquals;

public class IntegrationTest extends AbstractServiceTest {

    private Long rootToken;
    private Directory home;
    private Directory rootHome;
    private String name = "newFile";
    private final String username = "ladslads";
    private User root;
    private final String APP_METHOD = "pt.tecnico.myDrive.service.Hello.hello";
    private final String[] ARGS = {"Hello", "World!"};
    private final String[] plain = {"plain", "plain", "plain"};
    private final String[] link = {"link", "link", "plain"};
    private final String[] app = {"app", "app", APP_METHOD};

    @Override
    protected void populate() {
        Manager m = Manager.getInstance();
        new User(m, username);
        Login l = new Login("root", "***");
        root = l.getCurrentUser();
        rootToken = l.getToken();

        home = (Directory) m.getRootDirectory().getFileByName("home");
        rootHome = (Directory) home.lookup("root", root);
        new Directory("myhome", root, rootHome);
    }

    @Test
    public void success() {

        LoginService login= new LoginService(username, username);
        login.execute();
        Long lads = login.result();

        //FIXME import service test

        new ChangeDirectoryService(lads, "/home").execute();

        new ChangeDirectoryService(lads, "/home/"+username).execute();

        new CreateFileService(lads, plain[0], plain[1], plain[2]).execute();
        new CreateFileService(lads, app[0], app[1], app[2]).execute();
        new CreateFileService(lads, link[0], link[1], link[2]).execute();

        ListDirectoryService list = new ListDirectoryService(lads, ".");
        list.execute();
        System.out.println("\n------------------------\n");
        for (FileDto dto : list.result()) {
            String entry = dto.getType() + " " + dto.getUmask() + " " + dto.getDimension() + " " +
                    dto.getUsername() + " " + dto.getId() + " " + dto.getLastModified() + " " + dto.getName();
            System.out.println(entry);
        }
        assertEquals(5, list.result().size());

        new DeleteFileService(lads, plain[0]).execute();

        list.execute();
        System.out.println("\n------------------------\n");
        for (FileDto dto : list.result()) {
            String entry = dto.getType() + " " + dto.getUmask() + " " + dto.getDimension() + " " +
                    dto.getUsername() + " " + dto.getId() + " " + dto.getLastModified() + " " + dto.getName();
            System.out.println(entry);
        }
        assertEquals(4, list.result().size());

        System.out.println("\n------------------------\n");
        new ExecuteFileService(lads, app[0], ARGS).execute();

        System.out.println("\n------------------------\n");
        ReadFileService readFileService = new ReadFileService(lads, app[0]);
        readFileService.execute();
        assertEquals(app[2], readFileService.result());
        System.out.println(readFileService.result());

        new CreateFileService(lads, plain[0], plain[1], plain[2]).execute();
        new WriteFileService(lads, link[0], app[2]).execute();
        System.out.println("\n------------------------\n");
        readFileService = new ReadFileService(lads, link[0]);
        readFileService.execute();
        assertEquals(app[2], readFileService.result());
        System.out.println(readFileService.result());

        //FIXME add mock tests for execution and environment variables

    }
}
