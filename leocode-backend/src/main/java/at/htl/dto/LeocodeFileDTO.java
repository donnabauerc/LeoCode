package at.htl.dto;

import at.htl.entity.LeocodeFile;
import at.htl.entity.LeocodeFileType;

public class LeocodeFileDTO {
    public String name;
    public LeocodeFileType fileType;
    public String content;

    public LeocodeFileDTO(String name, LeocodeFileType fileType, String content) {
        this.name = name;
        this.fileType = fileType;
        this.content = content;
    }

    public LeocodeFileDTO(LeocodeFile file){
        this.name = file.name;
        this.fileType = file.fileType;
        this.content = new String(file.content);
    }
}
