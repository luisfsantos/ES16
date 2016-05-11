package pt.tecnico.myDrive.system;


import org.junit.Test;
import pt.tecnico.myDrive.domain.Directory;
import pt.tecnico.myDrive.domain.Login;
import pt.tecnico.myDrive.domain.Manager;
import pt.tecnico.myDrive.domain.User;
import pt.tecnico.myDrive.service.AbstractServiceTest;

public class IntegrationTest extends AbstractServiceTest {

    private Long rootToken;
    private Directory home;
    private Directory rootHome;
    private String name = "newFile";
    private User root;

    @Override
    protected void populate() {
        Manager m = Manager.getInstance();
        new User(m, "ladslads");
        Login l = new Login("root", "***");
        root = l.getCurrentUser();
        rootToken = l.getToken();

        home = (Directory) m.getRootDirectory().getFileByName("home");
        rootHome = (Directory) home.lookup("root", root);
        new Directory("myhome", root, rootHome);
    }

    @Test
    public void success() {

    }
}
