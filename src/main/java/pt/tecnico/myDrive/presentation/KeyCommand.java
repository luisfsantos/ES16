package pt.tecnico.myDrive.presentation;

/**
 * Created by lads on 05-05-2016.
 */
public class KeyCommand extends MyDriveCommand {

    public KeyCommand(MyDrive sh) {
        super(sh, "token", "change the current user or list token of the current user, usage: token [username]");
    }

    void execute(String[] args) {
        MyDrive myDrive = (MyDrive) this.shell();

        if (args.length == 0) {
            print("Username: "); println(myDrive.getActiveUser());
            print("Token: "); println(myDrive.getActiveToken().toString());
        }
        else if (args.length == 1) {
            if(myDrive.swapUser(args[0])) {
            	print("Username: "); println(myDrive.getActiveUser());
                print("Token: "); println(myDrive.getActiveToken().toString());
            } else {
            	throw new RuntimeException(args[0] + " is not logged in");
            }
            
        }
        else {
            println("Wrong arguments!!!\nUSAGE: token [username]");
            return;
        }
    }
}
