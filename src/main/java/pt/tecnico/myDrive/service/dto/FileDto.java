package pt.tecnico.myDrive.service.dto;

public class FileDto implements Comparable<FileDto> {
    private String type;
    private String umask;
    private int dimension;
    private String username;
    private int id;
    private String lastModified;
    private String name;

    public FileDto(String type, String umask, int dimension, String username, int id, String lastModified, String name) {
        this.type = type;
        this.umask = umask;
        this.dimension = dimension;
        this.username = username;
        this.id = id;
        this.lastModified = lastModified;
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public String getUmask() {
        return umask;
    }

    public int getDimension() {
        return dimension;
    }

    public String getUsername() {
        return username;
    }

    public int getId() {
        return id;
    }

    public String getLastModified() {
        return lastModified;
    }

    public String getName() {
        return name;
    }

    @Override
    public int compareTo(FileDto other) {
        return getName().compareTo(other.getName());
    }
}
