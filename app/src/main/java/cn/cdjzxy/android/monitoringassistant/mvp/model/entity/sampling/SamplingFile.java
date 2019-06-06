package cn.cdjzxy.android.monitoringassistant.mvp.model.entity.sampling;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Keep;

@Entity
public class SamplingFile implements Parcelable {

    @Id
    private String Id;
    private String FileName;
    private String FilePath;
    private String FileType;
    private String UpdateTime;
    private boolean IsUploaded;

    @Keep()
    public SamplingFile(String Id, String FileName, String FilePath,
                        String FileType, String UpdateTime, boolean IsUploaded) {
        this.Id = Id;
        this.FileName = FileName;
        this.FilePath = FilePath;
        this.FileType = FileType;
        this.UpdateTime = UpdateTime;
        this.IsUploaded = IsUploaded;
    }

    @Keep()
    public SamplingFile() {
    }

    public String getId() {
        return this.Id;
    }

    public void setId(String Id) {
        this.Id = Id;
    }

    public String getFileName() {
        return this.FileName;
    }

    public void setFileName(String FileName) {
        this.FileName = FileName;
    }

    public String getFilePath() {
        return this.FilePath;
    }

    public void setFilePath(String FilePath) {
        this.FilePath = FilePath;
    }

    public String getFileType() {
        return this.FileType;
    }

    public void setFileType(String FileType) {
        this.FileType = FileType;
    }

    public String getUpdateTime() {
        return this.UpdateTime;
    }

    public void setUpdateTime(String UpdateTime) {
        this.UpdateTime = UpdateTime;
    }

    public boolean getIsUploaded() {
        return this.IsUploaded;
    }

    public void setIsUploaded(boolean IsUploaded) {
        this.IsUploaded = IsUploaded;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.Id);
        dest.writeString(this.FileName);
        dest.writeString(this.FilePath);
        dest.writeString(this.FileType);
        dest.writeString(this.UpdateTime);
        dest.writeByte(this.IsUploaded ? (byte) 1 : (byte) 0);
    }

    protected SamplingFile(Parcel in) {
        this.Id = in.readString();
        this.FileName = in.readString();
        this.FilePath = in.readString();
        this.FileType = in.readString();
        this.UpdateTime = in.readString();
        this.IsUploaded = in.readByte() != 0;
    }

    public static final Creator<SamplingFile> CREATOR = new Creator<SamplingFile>() {
        @Override
        public SamplingFile createFromParcel(Parcel source) {
            return new SamplingFile(source);
        }

        @Override
        public SamplingFile[] newArray(int size) {
            return new SamplingFile[size];
        }
    };
}
