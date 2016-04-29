package pt.tecnico.myDrive.exception;

public class MyDrive_Exception extends MyDriveException {
    private static final long serialVersionUID = 1L;

    public MyDrive_Exception(Throwable e) {
        super(e.getMessage());
    }


}
