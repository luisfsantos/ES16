package pt.tecnico.myDrive.exception;

public class PathHasMoreThan1024CharactersException extends MyDriveException{
    private static final long serialVersionUID = 1L;

    public PathHasMoreThan1024CharactersException(){}

    @Override
    public String getMessage() {
        return "The path to file has more then 1024 characters";
    }
}
