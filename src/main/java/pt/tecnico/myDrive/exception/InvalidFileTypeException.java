package pt.tecnico.myDrive.exception;


public class InvalidFileTypeException extends MyDriveException {

	private static final long serialVersionUID = 1L;

	private String fileType;

	public InvalidFileTypeException(String fileType) {
		this.fileType = fileType;
	}

	public String getFileType() {
		return fileType;
	}

	@Override

	public String getMessage() {
		return getFileType() + " is not a valid file type";
	}
}
