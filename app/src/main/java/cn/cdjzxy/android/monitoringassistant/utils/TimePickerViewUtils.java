package cn.cdjzxy.android.monitoringassistant.utils;

import android.content.Context;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;

import java.util.Calendar;

public class TimePickerViewUtils {
    /**
     * 布尔型数组，长度需要设置为6。
     * 控制“年”“月”“日”“时”“分”“秒”的显示或隐藏。 true显示
     * <p>
     *
     */
    //显示年月日时分秒
    public static boolean[] YEAR_MONTH_DAY_HOURS_MIN_SECONDS = new boolean[]{true, true, true, true, true, true};
    //显示年月日
    public static boolean[] YEAR_MONTH_DAY = new boolean[]{true, true, true, false, false, false};
    //显示年月
    public static boolean[] YEAR_MONTH = new boolean[]{true, true, false, false, false, false};
    //显示年月日时分
    public static boolean[] YEAR_MONTH_DAY_HOURS_MIN = new boolean[]{true, true, true, true, true, false};

    public static boolean[] HOURS_MIN=new boolean[]{false, false, false, true, true, false};


    /**
     * 显示时间选择器
     *
     * @param context    context
     * @param timeFormat 显示时间格式
     * @param listener   回调
     */
    public static void showTimePickerView(Context context, boolean[] timeFormat, OnTimeSelectListener listener) {
        TimePickerView pickerView = new TimePickerBuilder(context, listener)
                .setType(timeFormat)
                .setDate(Calendar.getInstance()).build();
        pickerView.show();
    }

}
