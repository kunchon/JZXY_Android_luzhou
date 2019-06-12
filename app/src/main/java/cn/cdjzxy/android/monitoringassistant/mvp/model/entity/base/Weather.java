package cn.cdjzxy.android.monitoringassistant.mvp.model.entity.base;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Keep;

import java.util.List;

import cn.cdjzxy.android.monitoringassistant.utils.converter.StringConverter;
import org.greenrobot.greendao.annotation.Generated;


@Entity
public class Weather {
    //    [
    //            "晴天",
    //            "多云",
    //            "阴天",
    //            "小雨",
    //            "中雨",
    //            "暴雨",
    //            "雪天",
    //            "冰雹"
    //            ]
    @Convert(columnType = String.class, converter = StringConverter.class)
    private List<String> weathers;

    @Keep()
    public Weather(List<String> weathers) {
        this.weathers = weathers;
    }

    @Keep()
    public Weather() {
    }

    public List<String> getWeathers() {
        return this.weathers;
    }

    public void setWeathers(List<String> weathers) {
        this.weathers = weathers;
    }


}
