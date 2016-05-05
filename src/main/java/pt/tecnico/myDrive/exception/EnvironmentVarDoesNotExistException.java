package pt.tecnico.myDrive.exception;

public class EnvironmentVarDoesNotExistException extends MyDriveException {

    private static final long serialVersionUID = 1L;

    private String varName;

    public EnvironmentVarDoesNotExistException(String vName) { this.varName = vName; }

    public String getVarName() {
        return varName;
    }

    @Override
    public String getMessage() {
        return "Environment variable " + getVarName() + " does not exist";
    }
}

