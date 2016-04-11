package pt.tecnico.myDrive.service;

import pt.tecnico.myDrive.domain.*;
import pt.tecnico.myDrive.exception.InvalidPermissionException;
import pt.tecnico.myDrive.exception.IsNotDirOrLinkException;
import pt.tecnico.myDrive.exception.MyDriveException;

public class ChangeDirectoryService extends TokenValidationService{

    private long token;
    private String path;
    private String result;

    public ChangeDirectoryService(long token, String path){
        super(token);
        this.token = token;
        this.path = path;
    }

    @Override
    protected void dispatch() throws MyDriveException {
        super.dispatch();

        Directory currentDir = session.getCurrentDir();
        User u = session.getCurrentUser();
        File newDir= currentDir.lookup(path, u);

        if (newDir instanceof Directory) {
            session.setCurrentDir((Directory) newDir);
            result = newDir.getAbsolutePath();
        }
        else throw new  IsNotDirOrLinkException(path);
    }

    public final String result(){
        return result;
    }
}
