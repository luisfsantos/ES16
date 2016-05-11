package pt.tecnico.myDrive.exception;

public class InvalidEnvironmentVarValueException   extends MyDriveException {

    private static final long serialVersionUID = 1L;

    private String varValue;

    public InvalidEnvironmentVarValueException(String varValue) {
        this.varValue = varValue;
    }

    public String getVariableValue() {
        return varValue;
    }

    @Override
    public String getMessage() {
        return getVariableValue() + " is not a valid environment variable value";
    }
}