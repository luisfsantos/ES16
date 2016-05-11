package pt.tecnico.myDrive.system;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import pt.tecnico.myDrive.service.AbstractServiceTest ;
import pt.tecnico.myDrive.presentation.*;

public class SystemTest extends AbstractServiceTest {

    private MyDrive sh;

    protected void populate() {
        sh = new MyDrive();
        sh.xmlScan(new java.io.File("src/main/resources/drive.xml"));
    }

    @Test
    public void success() {
        new LoginCommand(sh).execute(new String[]{"mja", "Peyrelongue"}); //requires 2 logins for KeyCommand
        new LoginCommand(sh).execute(new String[]{"jtb", "fernandes"});
        new ListCommand(sh).execute(new String[]{"."});
        new WriteCommand(sh).execute(new String[]{"profile","i am plainfile content"});
        new ChangeWorkingDirectoryCommand(sh).execute(new String[]{"bin"});
        new ExecuteCommand(sh).execute(new String[]{"hello"});
        new EnvironmentCommand(sh).execute(new String[]{"var1","val1"});
        new EnvironmentCommand(sh).execute(new String[]{});
        new KeyCommand(sh).execute(new String[]{"mja"});

    }
}