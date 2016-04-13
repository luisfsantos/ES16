package pt.tecnico.myDrive.exception;

public class CannotRemoveDirectoryException extends MyDriveException {

	private String dirName;

	public CannotRemoveDirectoryException(String dirName) {
		this.dirName = dirName;
	}

	public String getDirName() {
		return dirName;
	}

	@Override
	public String getMessage() {
		return "Can not remove the directory:" + getDirName();
	}
}