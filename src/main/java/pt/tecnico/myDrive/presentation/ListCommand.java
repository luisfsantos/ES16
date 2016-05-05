package pt.tecnico.myDrive.presentation;

import pt.tecnico.myDrive.exception.MyDriveException;
import pt.tecnico.myDrive.service.ListDirectoryService;
import pt.tecnico.myDrive.service.dto.FileDto;

public class ListCommand extends MyDriveCommand {

    public ListCommand(Shell sh) {
        super(sh, "ls", "list directory content, usage: ls [path]");
    }

    @Override
    void execute(String[] args) {
        MyDrive myDrive = (MyDrive) this.shell();
        ListDirectoryService service;

        if (args.length == 0 ) service = new ListDirectoryService(myDrive.getActiveToken());
        else if (args.length == 1) service = new ListDirectoryService(myDrive.getActiveToken(), args[0]);
        else {
            println("Wrong arguments!!!\nUSAGE: ls [path]");
            return;
        }

        try {
            service.execute();
            for (FileDto dto : service.result()) {
                String entry = dto.getType() + " " + dto.getUmask() + " " + dto.getDimension() + " " +
                        dto.getUsername() + " " + dto.getId() + " " + dto.getLastModified() + " " + dto.getLastModified() +
                        " " + dto.getName();
                println(entry);
            }
        } catch (MyDriveException e) {
            println(e.getMessage());
        }
    }
}
