package pt.tecnico.myDrive.presentation;

import pt.tecnico.myDrive.exception.MyDriveException;
import pt.tecnico.myDrive.service.ExecuteFileService;

public class ExecuteCommand extends MyDriveCommand {

	public ExecuteCommand(MyDrive sh) {
		super(sh, "do", "execute file, usage: do path [args]");
	}

	@Override
	void execute(String[] args) {
		MyDrive myDrive = (MyDrive) this.shell();
		ExecuteFileService service;

		if (args.length < 1){
			println("Invalid arguments!\nCorrect usage: do path [args]");
			return;
		}
		String[] args1 = new String[args.length - 1];
		for (int aux = 1; aux < args.length; aux++) {
			args1[aux - 1] = args[aux];
		}
		service = new ExecuteFileService(myDrive.getActiveToken(), args[0], args1);

		try{
			service.execute();
		}catch(MyDriveException e){
			println(e.getMessage());
		}

	}
}