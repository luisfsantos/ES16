package pt.tecnico.myDrive.exception;

public class InvalidEnvironmentVarNameException  extends MyDriveException {

    private static final long serialVersionUID = 1L;

    private String varName;

    public InvalidEnvironmentVarNameException(String varName) {
        this.varName = varName;
    }

    public String getVariableName() {
        return varName;
    }

    @Override
    public String getMessage() {
        return getVariableName() + " is not a valid environment variable name";
    }
}