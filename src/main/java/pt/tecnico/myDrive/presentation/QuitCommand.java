package pt.tecnico.myDrive.presentation;

import pt.tecnico.myDrive.service.LogoutService;

public class QuitCommand extends MyDriveCommand {

    public QuitCommand(MyDrive sh) {
        super(sh, "quit", "Quit MyDrive and loggout guest.");
    }


    @Override
    void execute(String[] args) {
        MyDrive md = (MyDrive) this.shell();
        Long guestToken = md.logoutGuestUser();
        if (guestToken != null) {
            LogoutService logout = new LogoutService(guestToken);
            logout.execute();
        }
        System.exit(0);
    }


}
