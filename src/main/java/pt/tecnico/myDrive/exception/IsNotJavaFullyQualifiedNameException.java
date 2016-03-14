package pt.tecnico.myDrive.exception;

public class IsNotJavaFullyQualifiedNameException extends MyDriveException {

    private static final long serialVersionUID = 1L;

    private String content;

    public IsNotJavaFullyQualifiedNameException(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    @Override
    public String getMessage() {
        return getContent() + " is not a valid Java Fully Qualified Name";
    }
}

