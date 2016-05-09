package pt.tecnico.myDrive.service;

import org.junit.Test;
import pt.tecnico.myDrive.domain.Login;
import pt.tecnico.myDrive.domain.Manager;
import static org.junit.Assert.assertNull;

public class LogoutServiceTest extends AbstractServiceTest{

    private Manager manager;
    private Long guestToken;
    private Long rootToken;

    @Override
    protected void populate() {
        manager = Manager.getInstance();

        Login guestLogin = new Login("nobody", "");
        guestToken = guestLogin.getToken();

        Login rootLogin = new Login("root", "***");
        rootToken = rootLogin.getToken();
    }

    @Test
    public void verifyLogoutGuestCorrectToken(){
        LogoutService service = new LogoutService(guestToken);
        service.execute();

        Login log = manager.getLoginByToken(guestToken);

        assertNull("guest login is not null", log);
    }

/*
    @Test(expected = InvalidGuestToken.class)
    public void verifyLogoutGuestIncorrectToken(){
        LogoutGuestService service = new LogoutGuestService(rootToken);
        service.execute();
    }
*/

}
