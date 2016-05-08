package pt.tecnico.myDrive.presentation;

public class ExecuteCommand extends MyDriveCommand {

	public ExecuteCommand(MyDrive sh) {
		super(sh, "do", "execute file, usage: do [path] [args]");
	}

	@Override
	void execute(String[] args) {


	}
}