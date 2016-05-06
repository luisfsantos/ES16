package pt.tecnico.myDrive.presentation;

import java.util.List;

import pt.tecnico.myDrive.service.LoginService;

public class LoginCommand extends MyDriveCommand {

	public LoginCommand(MyDrive sh) {
		super(sh, "login", "create new session, usage: login username [password]");
	}

	@Override
	void execute(String[] args) {
		MyDrive md = (MyDrive) this.shell();
		if (args.length == 1 ) {
			LoginService ls = new LoginService(args[0], "");
			ls.execute();
			md.addLogin(args[0], ls.result());
		}
		else if (args.length == 2) {
			LoginService ls = new LoginService(args[0], args[1]);
			ls.execute();
			md.addLogin(args[0], ls.result());
		}
		// TODO exception: invalid arguments
	}



}
