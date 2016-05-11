package pt.tecnico.myDrive.presentation;

import pt.tecnico.myDrive.service.LoginService;
import pt.tecnico.myDrive.service.LogoutService;

public class LoginCommand extends MyDriveCommand {

	public LoginCommand(MyDrive sh) {
		super(sh, "login", "create new session, usage: login username [password]");
	}

	@Override
	public void execute(String[] args) {
		MyDrive md = (MyDrive) this.shell();
		if (args.length == 1 ) {
			if (args[0].equals("nobody") && md.getActiveUser().equals("nobody")) {
				return;
			}
			LoginService ls = new LoginService(args[0], "");
			ls.execute();
			md.addLogin(args[0], ls.result());
		}
		else if (args.length == 2) {
			LoginService ls = new LoginService(args[0], args[1]);
			ls.execute();
			Long guestToken = md.logoutGuestUser();
			if (guestToken != null) {
				LogoutService logout = new LogoutService(guestToken);
				logout.execute();
			}
			md.addLogin(args[0], ls.result());
		}
		else {
			println("Wrong arguments!!!\nUSAGE: login username [password]");
            return;
		}
	}



}
