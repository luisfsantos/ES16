package pt.tecnico.myDrive.presentation;

import pt.tecnico.myDrive.exception.MyDriveException;
import pt.tecnico.myDrive.service.ChangeDirectoryService;

public class ChangeWorkingDirectoryCommand extends MyDriveCommand{

    public ChangeWorkingDirectoryCommand(Shell sh) {
        super(sh, "cwd", "Change working directory, usage: cwd [path]");
    }

    @Override
    void execute(String[] args) {
        MyDrive md = (MyDrive) this.shell();
        ChangeDirectoryService cwd;
        if (args.length == 1 ) {
            cwd = new ChangeDirectoryService(md.getActiveToken(), args[0]);
        }
        else {
            println("Wrong arguments!!!\nUSAGE: cwd [path]");
            return;
        }
            cwd.execute();
            String absolutePath = cwd.result();
            println(absolutePath);

    }
}
