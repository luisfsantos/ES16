package pt.tecnico.myDrive.service;

import static org.junit.Assert.assertEquals;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import pt.tecnico.myDrive.domain.Manager;
import pt.tecnico.myDrive.domain.User;
import pt.tecnico.myDrive.domain.Directory;
import pt.tecnico.myDrive.domain.PlainFile;
import pt.tecnico.myDrive.domain.Link;
import pt.tecnico.myDrive.domain.App;
import pt.tecnico.myDrive.domain.Login;
import pt.tecnico.myDrive.exception.EmptyUsernameException;
import pt.tecnico.myDrive.exception.InvalidUsernameOrPasswordException;
import pt.tecnico.myDrive.exception.UserDoesNotExistException;

public class LoginServiceTest extends AbstractServiceTest {


    @Override
    protected void populate() {
    }
}
