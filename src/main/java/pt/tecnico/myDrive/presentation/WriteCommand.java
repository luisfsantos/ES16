package pt.tecnico.myDrive.presentation;


import pt.tecnico.myDrive.domain.Directory;
import pt.tecnico.myDrive.domain.Login;
import pt.tecnico.myDrive.domain.Manager;
import pt.tecnico.myDrive.domain.User;
import pt.tecnico.myDrive.exception.MyDriveException;
import pt.tecnico.myDrive.service.WriteFileService;

public class WriteCommand extends MyDriveCommand {

    public WriteCommand(Shell sh){
        super(sh, "update", "Replaces the file contents of the file" +
                " referred by the path with text, usage: update path text");
    }

    @Override
    void execute(String[] args){
        MyDrive drive = (MyDrive) this.shell();

        if (args.length != 2){
            println("Invalid arguments!\nCorrect usage: update path text");
            return;
        }

        WriteFileService service = new WriteFileService(drive.getActiveToken(), args[0], args[1]);

        try{
            service.execute();
        }catch(MyDriveException e){
            println(e.getMessage());
        }
    }



}
