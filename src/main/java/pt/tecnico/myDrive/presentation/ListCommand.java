package pt.tecnico.myDrive.presentation;

import pt.tecnico.myDrive.service.ListDirectoryService;
import pt.tecnico.myDrive.service.dto.FileDto;

public class ListCommand extends MyDriveCommand {

    public ListCommand(MyDrive sh) {
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

        service.execute();
        for (FileDto dto : service.result()) {
            String entry = dto.getType() + " " + dto.getUmask() + " " + dto.getDimension() + " " +
                    dto.getUsername() + " " + dto.getId() + " " + dto.getLastModified() + " " + dto.getName();
            println(entry);
        }
    }
}
