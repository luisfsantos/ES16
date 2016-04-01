package pt.tecnico.myDrive.exception;

public class InvalidIdCounter extends MyDriveException {
	private static final long serialVersionUID = 1L;
	
	private int idCounter;
	
	public InvalidIdCounter(int counter){
		idCounter = counter;
	}
	
	public int getIdCounter(){
		return idCounter;
	}
	
	@Override
	public String getMessage() {
		return getIdCounter() + "is not a valid id";
	}
}
