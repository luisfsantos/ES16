package pt.tecnico.myDrive.service;

import org.junit.Test;
import pt.tecnico.myDrive.domain.Login;
import pt.tecnico.myDrive.domain.Manager;
import static org.junit.Assert.assertNull;

public class LogoutServiceTest extends TokenValidationServiceTest{

    private Manager manager;
    private Long guestToken;

    @Override
    protected void populate() {
        super.populate();
        manager = Manager.getInstance();

        Login guestLogin = new Login("nobody", "");
        guestToken = guestLogin.getToken();
    }

    @Test
    public void verifyLogoutGuestCorrectToken(){
        LogoutService service = new LogoutService(guestToken);
        service.execute();

        Login log = manager.getLoginByToken(guestToken);

        assertNull("guest login is not null", log);
    }

}
