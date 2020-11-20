package at.htl.entities;

import org.jboss.resteasy.annotations.jaxrs.FormParam;
import org.jboss.resteasy.annotations.providers.multipart.PartType;

import javax.ws.rs.core.MediaType;
import java.io.InputStream;

public class MultipartBody {
    @FormParam("file")
    @PartType(MediaType.APPLICATION_OCTET_STREAM)
    public InputStream file;

    @FormParam("fileName")
    @PartType(MediaType.TEXT_PLAIN)
    public String fileName;

    public MultipartBody(String fileName, InputStream file) {
        this.fileName = fileName;
        this.file = file;
    }

    @Override
    public String toString() {
        return "MultipartBody{" +
                "fileName='" + fileName + '\'' +
                '}';
    }
}
