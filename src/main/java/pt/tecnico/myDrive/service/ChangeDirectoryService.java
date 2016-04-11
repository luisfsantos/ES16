package pt.tecnico.myDrive.service;

import pt.tecnico.myDrive.domain.*;
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
        Manager manager = Manager.getInstance();
        Login login = manager.getLoginByToken(token);

        if (login.validateToken(token)) {
            Directory currentDir = login.getCurrentDir();
            result = currentDir.changeDirectory(login, path);
        }
    }

    public final String result(){
        return result;
    }
}
