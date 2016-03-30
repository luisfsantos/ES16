package pt.tecnico.myDrive.exception;


public class InvalidHomeDirectoryException extends MyDriveException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6617207831008829488L;
	private String fileName;
	
	public InvalidHomeDirectoryException(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }

    @Override
    public String getMessage() {
        return "Cannot change home because " + getFileName() + " is not a directory or does not belong to the User";
    }

}
