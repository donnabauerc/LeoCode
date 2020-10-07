package at.htl.control;

import java.io.InputStream;

public class MultipartBody {

    public InputStream file;
    public String fileName;
    public String fileType;

    public MultipartBody(String fileName, InputStream file, String fileType) {
        this.file = file;
        this.fileName = fileName;
        this.fileType = fileType;
    }
}
