package at.htl.control;

import java.io.InputStream;

public class MultipartBody {

    public InputStream file;
    public String fileName;

    public MultipartBody(String fileName, InputStream file) {
        this.file = file;
        this.fileName = fileName;
    }
}
