package pt.tecnico.myDrive.exception;

public class SuperUserCannotBeModified extends MyDriveException {
	
	private static final long serialVersionUID = 1L;
	
	
	public SuperUserCannotBeModified(){ }
	
	
	@Override
	public String getMessage() {
		return "Super User cannot be removed or modified";
	}
	
}
