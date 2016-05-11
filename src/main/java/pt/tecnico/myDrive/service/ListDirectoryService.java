package pt.tecnico.myDrive.service;

import pt.tecnico.myDrive.domain.*;
import pt.tecnico.myDrive.exception.InvalidPathException;
import pt.tecnico.myDrive.exception.MyDriveException;
import pt.tecnico.myDrive.service.dto.FileDto;

import java.util.*;

public class ListDirectoryService extends TokenValidationService {

    private List<FileDto> lsDir;
    private String path;

    public ListDirectoryService(long token) {
        this(token, ".");
    }

    public ListDirectoryService(long token, String path) {
        super(token);
        this.path = path;
    }

    @Override
    protected void dispatch() throws MyDriveException {
        super.dispatch();
        File dirFile = session.getCurrentDir().lookup(path, session.getCurrentUser());
        if (dirFile == null) throw new InvalidPathException(path);

        lsDir = new ArrayList<>();
        Map<String, File> dirContentMap = dirFile.getDirContentMap(session.getCurrentUser());
        Set<Map.Entry<String, File>> dirContentSet = dirContentMap.entrySet();

        for (Map.Entry<String, File> f : dirContentSet) {
            FileDto newDto = new FileDto(f.getValue().getType(), f.getValue().getPermissions(), f.getValue().getSize(),
                    f.getValue().getOwnerUsername(), f.getValue().getId(),
                    f.getValue().getLastModified().toString("dd-MM-YYYY-HH:mm:ss"), f.getKey());
            lsDir.add(newDto);
        }
        Collections.sort(lsDir);
    }

    public final List<FileDto> result() {
        return lsDir;
    }
}