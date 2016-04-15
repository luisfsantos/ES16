package pt.tecnico.myDrive.service;

import pt.tecnico.myDrive.exception.MyDriveException;
import pt.tecnico.myDrive.service.dto.FileDto;

import java.util.List;

public class ListDirectoryService extends MyDriveService {

    private List<FileDto> lsDir;

    public ListDirectoryService(){
        //TODO
    }

    public ListDirectoryService(long token){

    }

    @Override
    protected void dispatch() throws MyDriveException {
        // TODO Auto-generated method stub

    }

    public final List<FileDto> result() {
        //TODO
        return lsDir;
    }
}