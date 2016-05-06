package pt.tecnico.myDrive.presentation;


public abstract class MyDriveCommand extends Command {

	public MyDriveCommand(MyDrive sh, String n) {
		super(sh, n);
	}

	public MyDriveCommand(MyDrive sh, String n, String h) {
		super(sh, n, h);
	}

}
