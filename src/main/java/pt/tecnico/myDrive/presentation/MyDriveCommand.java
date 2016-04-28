package pt.tecnico.myDrive.presentation;

import pt.tecnico.myDrive.service.LoginService;

public abstract class MyDriveCommand extends Command {
	protected Long activeToken;

	public MyDriveCommand(Shell sh, String n) {
		super(sh, n);
        setupGuestUser();
	}

	public MyDriveCommand(Shell sh, String n, String h) {
		super(sh, n, h);
        setupGuestUser();
	}

    private void setupGuestUser() {
        LoginService guestLogin = new LoginService("nobody", "");
        guestLogin.execute();
        activeToken = guestLogin.result();
        ((MyDrive)shell()).addLogin("nobody", activeToken); //bad trick for token keeping
    }

}
