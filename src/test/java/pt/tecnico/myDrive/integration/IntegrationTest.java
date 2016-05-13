package pt.tecnico.myDrive.integration;


import mockit.Expectations;
import mockit.Mock;
import mockit.MockUp;
import mockit.Mocked;
import org.jdom2.Document;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.junit.Test;
import pt.tecnico.myDrive.domain.*;
import pt.tecnico.myDrive.service.*;
import pt.tecnico.myDrive.service.dto.FileDto;
import pt.tecnico.myDrive.service.dto.VariableDto;

import java.io.IOException;
import java.io.StringReader;

import static org.junit.Assert.assertEquals;

public class IntegrationTest extends AbstractServiceTest {

    private Long rootToken;
    private ExecuteFileService executeFileService;
    private Directory home;
    private Directory rootHome;
    private String name = "newFile";
    private final String username = "ladslads";
    private User root;
    private final String APP_METHOD = "pt.tecnico.myDrive.service.Hello.hello";
    private final String[] ARGS = {"Integration", "Test!!"};
    private final String[] plain = {"plain", "plain", "plain"};
    private final String[] link = {"link", "link", "plain"};
    private final String[] app = {"app", "app", APP_METHOD};
    private Document doc;
    private final String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
            +"<myDrive>"
            +"<user username=\"jtb\">"
            +"  <password>fermento</password>"
            +"  <name>Joaquim Teófilo Braga</name>"
            +"  <home>/home/jtb</home>"
            +"  <mask>rwxdr-x-</mask>"
            +"</user>"
            +"<plain id=\"3\">"
            +"  <path>/home/jtb</path>"
            +"  <name>profile</name>"
            +"  <owner>jtb</owner>"
            +"  <perm>rw-dr---</perm>"
            +"  <contents>Primeiro chefe de Estado do regime republicano (acumulando com a chefia do governo), numa capacidade provisória até à eleição do primeiro presidente da República.</contents>"
            +"</plain>"
            +"<dir id=\"4\">"
            +"  <path>/home/jtb</path>"
            +"  <name>documents</name>"
            +"  <owner>jtb</owner>"
            +"  <perm>rwxdrwxd</perm>"
            +"</dir>"
            +"<link id=\"5\">"
            +"  <path>/home/jtb</path>"
            +"  <name>doc</name>"
            +"  <owner>jtb</owner>"
            +"  <perm>r-xdr-x-</perm>"
            +"  <value>/home/jtb/documents</value>"
            +"</link>"
            +"<plain id=\"8\">"
            +"  <path>/home/jtb/documents</path>"
            +"  <name>file</name>"
            +"  <owner>jtb</owner>"
            +"  <perm>rwxdr-x-</perm>"
            +"  <contents>Pri</contents>"
            +"</plain>"
            +"<dir id=\"7\">"
            +"  <path>/home/jtb</path>"
            +"  <owner>jtb</owner>"
            +"  <name>bin</name>"
            +"  <perm>rwxd--x-</perm>"
            +"</dir>"
            +"</myDrive>";

    private final String linkMockStr = "linkMock";
    private final String contentMockStr = "mocktest";
    private final String pathTranslated = "/home/pfMock";
    private final String pathEnvVar = "/$DAVID";
    private final String EXTENSION = "app";
    private final String APP = "execApp";
    private PlainFile pfile;
    private Link linkMock;
    private User testUser;
    private App testApp;
    private App appResult;


    @Override
    protected void populate() {
        Manager m = Manager.getInstance();
        testUser = new User(m, username);
        Login l = new Login("root", "***");
        root = l.getCurrentUser();
        rootToken = l.getToken();
        home = (Directory) m.getRootDirectory().getFileByName("home");
        rootHome = (Directory) home.lookup(username, root);
        testApp = new App("testing."+EXTENSION, root, home, APP_METHOD);
        appResult = new App(APP, root, home, APP_METHOD);
        testApp.setPermissions("--------");
        pfile = new PlainFile("pfMock", root, home, "test");
        pfile.setPermissions("rwxdrwxd");
        linkMock = new Link(linkMockStr, testUser, rootHome, pathEnvVar);
        try {
            doc = new SAXBuilder().build(new StringReader(xml));
        } catch (JDOMException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void success() {

        new ImportService(doc).execute();

        LoginService login= new LoginService(username, username);
        login.execute();
        Long lads = login.result();

        System.out.println("\n------------------------\n");
        AddVariableService variable = new AddVariableService(lads, "Add Variable", "Service");
        variable.execute();
        for (VariableDto var: variable.result()) {
                System.out.println(var.getName() + " = " + var.getValue());
        }
        assertEquals(1, variable.result().size());

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
        assertEquals(6, list.result().size());

        new DeleteFileService(lads, plain[0]).execute();

        list.execute();
        System.out.println("\n------------------------\n");
        for (FileDto dto : list.result()) {
            String entry = dto.getType() + " " + dto.getUmask() + " " + dto.getDimension() + " " +
                    dto.getUsername() + " " + dto.getId() + " " + dto.getLastModified() + " " + dto.getName();
            System.out.println(entry);
        }
        assertEquals(5, list.result().size());

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

        new Expectations(linkMock){{
                linkMock.decodeEnvPath(); result = pathTranslated; times=1;
        }};
        WriteFileService writeFileService = new WriteFileService(lads, linkMockStr, contentMockStr);
        writeFileService.execute();
        assertEquals("write not executed successfully", contentMockStr, pfile.read(root));


    }
}
