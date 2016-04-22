package pt.tecnico.myDrive.exception;

public class UserCannotBeModified extends MyDriveException {
	
	private static final long serialVersionUID = 1L;
	
	private String name;
	
	public UserCannotBeModified(String name){ 
		this.name = name;
	}
	
	
	@Override
	public String getMessage() {
		return name + " cannot be removed or modified";
	}
	
}
