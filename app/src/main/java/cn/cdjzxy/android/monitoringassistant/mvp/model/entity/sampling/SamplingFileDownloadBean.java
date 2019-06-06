package cn.cdjzxy.android.monitoringassistant.mvp.model.entity.sampling;



/**
 * 采样单下载的实体类
 * 2019/6/3 嘉泽——昆虫
 */
public class SamplingFileDownloadBean {
    private SamplingFile samplingFile;
    private String fileName;
    private String fileType;
    private String filePath;

    public SamplingFileDownloadBean() {
    }

    public SamplingFileDownloadBean(SamplingFile SamplingFile, String fileName, String fileType, String filePath) {
        this.samplingFile = SamplingFile;
        this.fileName = fileName;
        this.fileType = fileType;
        this.filePath = filePath;
    }

    public SamplingFile getSamplingFile() {
        return samplingFile;
    }

    public void setSamplingFile(SamplingFile samplingFile) {
        this.samplingFile = samplingFile;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public String toString() {
        return "采样单下载{" +
                ", fileName='" + fileName + '\'' +
                ", fileType='" + fileType + '\'' +
                ", filePath='" + filePath + '\'' +
                '}';
    }
}
