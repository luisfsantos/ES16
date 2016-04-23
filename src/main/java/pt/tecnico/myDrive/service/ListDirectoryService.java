package pt.tecnico.myDrive.service;

import pt.tecnico.myDrive.domain.*;
import pt.tecnico.myDrive.exception.MyDriveException;
import pt.tecnico.myDrive.service.dto.FileDto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class ListDirectoryService extends TokenValidationService {

    private List<FileDto> lsDir;

    public ListDirectoryService(long token) {
        super(token);
    }

    @Override
    protected void dispatch() throws MyDriveException {
        super.dispatch();
        Set<File> dirContent = session.getCurrentDir().getFileSet(session.getCurrentUser());
        lsDir = new ArrayList<>();

        for (File f : dirContent) {

            FileDto newDto = new FileDto(f.getType(), f.getPermissions(), f.getSize(), f.getOwnerUsername(), f.getId(),
                    f.getLastModified().toString("dd-MM-YYYY-HH:mm:ss"), f.getName());
            lsDir.add(newDto);
        }

        Collections.sort(lsDir);
    }

    public final List<FileDto> result() {
        return lsDir;
    }
}