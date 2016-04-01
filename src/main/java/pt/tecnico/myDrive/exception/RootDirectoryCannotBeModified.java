package pt.tecnico.myDrive.exception;

public class RootDirectoryCannotBeModified extends MyDriveException {
	
	private static final long serialVersionUID = 1L;
	
	
	public RootDirectoryCannotBeModified(){ }
	
	
	@Override
	public String getMessage() {
		return "Root Directory cannot be removed or modified";
	}
}
