package cn.cdjzxy.android.monitoringassistant.mvp.ui.task.wastewater.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.cdjzxy.android.monitoringassistant.R;
import cn.cdjzxy.android.monitoringassistant.base.mvp.Message;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.project.Project;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.sampling.FsExtends;
import cn.cdjzxy.android.monitoringassistant.mvp.ui.task.activity.MethodActivity;
import cn.cdjzxy.android.monitoringassistant.mvp.ui.task.activity.TypeActivity;
import cn.cdjzxy.android.monitoringassistant.mvp.ui.task.activity.UserActivity;
import cn.cdjzxy.android.monitoringassistant.mvp.ui.task.activity.WeatherActivity;
import cn.cdjzxy.android.monitoringassistant.mvp.ui.task.fragment.SampleBaseFragment;
import cn.cdjzxy.android.monitoringassistant.mvp.ui.task.point.EnterRelatePointSelectActivity;
import cn.cdjzxy.android.monitoringassistant.mvp.ui.task.point.PointSelectActivity;
import cn.cdjzxy.android.monitoringassistant.mvp.ui.task.wastewater.WasteWaterActivity;
import cn.cdjzxy.android.monitoringassistant.utils.ArtUtils;
import cn.cdjzxy.android.monitoringassistant.utils.DbHelpUtils;
import cn.cdjzxy.android.monitoringassistant.utils.SamplingUtil;
import cn.cdjzxy.android.monitoringassistant.utils.TimePickerViewUtils;
import cn.cdjzxy.android.monitoringassistant.utils.onactivityresult.AvoidOnResult;
import cn.cdjzxy.android.monitoringassistant.utils.rx.RxDataTool;
import cn.cdjzxy.android.monitoringassistant.utils.rx.RxDateTool;
import cn.cdjzxy.android.monitoringassistant.widget.MyDrawableLinearLayout;

public class WasteWaterBasic extends SampleBaseFragment {

    @BindView(R.id.my_layout_number)
    MyDrawableLinearLayout base_sample_no;//采样单编号
    @BindView(R.id.my_layout_sample_md)
    MyDrawableLinearLayout base_sample_md;//采样单目的
    @BindView(R.id.my_layout_sample_xz)
    MyDrawableLinearLayout base_sample_xz;//采样性质
    @BindView(R.id.my_layout_sample_date)
    MyDrawableLinearLayout base_sample_date;//采样日期
    @BindView(R.id.my_layout_sample_user)
    MyDrawableLinearLayout base_sample_user;//采样人

    @BindView(R.id.my_layout_sample_property)
    MyDrawableLinearLayout base_sample_property;//样品性质
    @BindView(R.id.my_layout_sample_point)
    MyDrawableLinearLayout base_sample_point;//采样点位
    @BindView(R.id.my_layout_sample_method)
    MyDrawableLinearLayout base_sample_method;//采样方法
    @BindView(R.id.base_sample_handle)
    CheckedTextView base_sample_handle;
    @BindView(R.id.text_view_sample_handle)
    TextView textViewSampleHandler;
    @BindView(R.id.text_view_weather_arrow)
    TextView text_view_weather_arrow;
    @BindView(R.id.weather_arrow)
    TextView weather_arrow;
    @BindView(R.id.layout_weather_information_container)
    View layout_weather_information_container;
    @BindView(R.id.my_layout_weather_state)
    MyDrawableLinearLayout weather_state;
    @BindView(R.id.my_layout_weather_temp)
    MyDrawableLinearLayout weather_temp;
    @BindView(R.id.my_layout_weather_pressure)
    MyDrawableLinearLayout weather_pressure;
    //更多信息
    @BindView(R.id.text_view_more_arrow)
    TextView text_view_more_arrow;
    @BindView(R.id.more_arrow)
    TextView more_arrow;
    @BindView(R.id.layout_more_information)
    View layout_more_information;
    @BindView(R.id.my_layout_more_name)
    MyDrawableLinearLayout more_name;//企业名称
    @BindView(R.id.my_layout_more_address)
    MyDrawableLinearLayout more_address;//企业地址
    @BindView(R.id.my_layout_more_device)
    MyDrawableLinearLayout more_device;//处理设施
    @BindView(R.id.my_layout_more_waterbody)
    MyDrawableLinearLayout more_waterbody;//受纳水体
    @BindView(R.id.my_layout_more_build_date)
    MyDrawableLinearLayout more_build_date;//建设时间
    @BindView(R.id.text_view_more_gw)
    TextView text_view_more_gw;
    @BindView(R.id.more_gw)
    CheckedTextView more_gw;//是否进入管网
    //流转信息
    @BindView(R.id.text_view_tv_arrow)
    TextView text_view_tv_arrow;
    @BindView(R.id.tv_arrow)
    TextView tv_arrow;
    @BindView(R.id.layout_flow_information_container)
    View layout_flow_information_container;
    @BindView(R.id.my_layout_flow_method)
    MyDrawableLinearLayout tv_flow_method;//运输方式
    @BindView(R.id.my_layout_flow_date)
    MyDrawableLinearLayout tv_flow_date;//送样时间
    @BindView(R.id.my_layout_receive_date)
    MyDrawableLinearLayout tv_receive_date;//收样时间

    @BindView(R.id.base_sample_comment)
    TextView base_sample_comment;

    private Project project;
    private FsExtends fsExtends;

    @Override
    public View initView(@NonNull LayoutInflater inflater
            , @Nullable ViewGroup container
            , @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_wastewater_basic_info, null);
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        if (!RxDataTool.isNull(mSampling)) {
            project = DbHelpUtils.getDbProject(mSampling.getProjectId());
            Gson gson = new Gson();
            fsExtends = gson.fromJson(mSampling.getPrivateData(), FsExtends.class);
            if (fsExtends == null) {
                fsExtends = new FsExtends();
            }
            base_sample_no.setRightTextStr(mSampling.getSamplingNo());
            base_sample_md.setRightTextStr(mSampling.getProjectName());
            //base_sample_xz.setText(WastewaterActivity.mSampling.getMontype());
            base_sample_xz.setRightTextStr(project.getMonType());
            String sampleDate = RxDateTool.simpleDateFormat(
                    mSampling.getSamplingTimeBegin(), RxDateTool.DATE_FORMAT);

            base_sample_date.setRightTextStr(sampleDate);
            base_sample_user.setRightTextStr(mSampling.getSamplingUserName());
            base_sample_property.setRightTextStr(mSampling.getTagName());
            base_sample_point.setRightTextStr(mSampling.getAddressName());
            base_sample_method.setRightTextStr(mSampling.getMethodName());
            boolean sewageDisposal = false;
            if (fsExtends != null && !TextUtils.isEmpty(fsExtends.getSewageDisposal()) && fsExtends.getSewageDisposal().equals("是")) {
                sewageDisposal = true;
                fsExtends.setSewageDisposal("是");
            } else {
                fsExtends.setSewageDisposal("否");
            }
            base_sample_handle.setChecked(sewageDisposal);
            base_sample_comment.setText(mSampling.getComment());
            setViewStyleDrawable(sewageDisposal, textViewSampleHandler);
            /*
            //水体信息
            water_temp.setText(fsExtends == null ? "" : fsExtends.getWaterWD());
            water_speed.setText(fsExtends == null ? "" : fsExtends.getWaterLS());
            water_flow.setText(fsExtends == null ? "" : fsExtends.getWaterLL());
            */

            //气象信息
            //只要填写填了一个就设置选中颜色
            setViewStyleDrawable(!RxDataTool.isEmpty(mSampling.getWeather())
                            || !RxDataTool.isEmpty(mSampling.getTemprature())
                            || !RxDataTool.isEmpty(mSampling.getPressure()),
                    text_view_weather_arrow);
            weather_state.setRightTextStr(mSampling.getWeather());
            weather_temp.setEditTextStr(mSampling.getTemprature());
            weather_pressure.setEditTextStr(mSampling.getPressure());
            setViewStyle(false, weather_arrow);
            //更多信息
            boolean gw = false;
            if (fsExtends != null && !TextUtils.isEmpty(fsExtends.getAccessPipeNetwork()) && fsExtends.getAccessPipeNetwork().equals("是")) {
                gw = true;
                fsExtends.setAccessPipeNetwork("是");
            } else {
                fsExtends.setAccessPipeNetwork("否");
            }
            more_gw.setChecked(gw);
            setViewStyleDrawable(gw, text_view_more_gw);
            setViewStyleDrawable(gw
                            || !RxDataTool.isEmpty(fsExtends == null ? "" : fsExtends.getClientName())
                            || !RxDataTool.isEmpty(fsExtends == null ? "" : fsExtends.getClientAdd())
                            || !RxDataTool.isEmpty(fsExtends == null ? "" : fsExtends.getHandleDevice())
                            || !RxDataTool.isEmpty(fsExtends == null ? "" : fsExtends.getReceiving())
                            || !RxDataTool.isEmpty(fsExtends == null ? "" : fsExtends.getBuildTime())
                    , text_view_more_arrow);
            more_name.setEditTextStr(fsExtends == null ? "" : fsExtends.getClientName());
            more_address.setEditTextStr(fsExtends == null ? "" : fsExtends.getClientAdd());
            more_device.setEditTextStr(fsExtends == null ? "" : fsExtends.getHandleDevice());
            more_waterbody.setEditTextStr(fsExtends == null ? "" : fsExtends.getReceiving());
            more_build_date.setRightTextStr(fsExtends == null ? "" : fsExtends.getBuildTime());
            setViewStyle(false, more_arrow);

            //流转信息
            setViewStyleDrawable(!RxDataTool.isEmpty(mSampling.getTransfer())
                            || !RxDataTool.isEmpty(mSampling.getSendSampTime())
                            || !RxDataTool.isEmpty(mSampling.getReciveTime()),
                    text_view_tv_arrow);
            tv_flow_method.setEditTextStr(mSampling.getTransfer());

            tv_flow_date.setRightTextStr(RxDateTool.simpleDateFormat(
                    mSampling.getSendSampTime(), RxDateTool.DATE_FORMAT));
            tv_receive_date.setRightTextStr(RxDateTool.simpleDateFormat(
                    mSampling.getReciveTime(), RxDateTool.DATE_FORMAT));
            setViewStyle(false, tv_arrow);

        } else {
            fsExtends = new FsExtends();
        }

        base_sample_handle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                base_sample_handle.setChecked(!base_sample_handle.isChecked());
                setViewStyleDrawable(base_sample_handle.isChecked(), textViewSampleHandler);
                if (base_sample_handle.isChecked()) {
                    fsExtends.setSewageDisposal("是");
                } else {
                    fsExtends.setSewageDisposal("否");
                }
            }
        });

        more_gw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                more_gw.setChecked(!more_gw.isChecked());
                setViewStyleDrawable(more_gw.isChecked(), text_view_more_gw);
                if (more_gw.isChecked()) {
                    fsExtends.setAccessPipeNetwork("是");
                } else {
                    fsExtends.setAccessPipeNetwork("否");
                }
            }
        });
    }

    @OnClick({ R.id.my_layout_sample_date, R.id.my_layout_sample_user,
            R.id.my_layout_sample_property, R.id.my_layout_sample_point,
            R.id.my_layout_sample_method, R.id.weather_info_layout, R.id.more_info_layout,
            R.id.layout_flow_information, R.id.my_layout_weather_state, R.id.my_layout_flow_date,
            R.id.my_layout_receive_date, R.id.my_layout_more_build_date})
    public void onClick(View view) {
        hideSoftInput();
        switch (view.getId()) {
            case R.id.my_layout_sample_date:
                selectSampleDate();
                break;
            case R.id.my_layout_sample_user:
                selectUser();
                break;
            case R.id.my_layout_sample_property:
                selectSampleType();
                break;
            case R.id.my_layout_sample_point:
                selectSamplePoint();
                break;
            case R.id.my_layout_sample_method:
                selectSampleMethod();
                break;
//            case R.id.water_info_layout:
//                setArrowAnimate(water_arrow, layout_water_information_container);
//                break;
            case R.id.weather_info_layout:
                setArrowAnimate(weather_arrow, layout_weather_information_container);
                break;
            case R.id.more_info_layout:
                setArrowAnimate(more_arrow, layout_more_information);
                break;
            case R.id.layout_flow_information:
                setArrowAnimate(tv_arrow, layout_flow_information_container);
                break;
            case R.id.my_layout_weather_state:
                selectWeather();
                break;
            case R.id.my_layout_flow_date:
                selectSampleSendDate();
                break;

            case R.id.my_layout_more_build_date:
                selectSampleBuildDate();
                break;
            default:
                break;
        }
    }

    /**
     * 选择建设时间
     */
    private void selectSampleBuildDate() {
        TimePickerViewUtils.showTimePickerView(mContext,
                TimePickerViewUtils.YEAR_MONTH, new OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {
                        String dateStr = RxDateTool.getDate(RxDateTool.DATE_YYYY_MMM, date);
                        fsExtends.setBuildTime(dateStr);
                        more_build_date.getRightTextView().setText(dateStr);
                        setViewStyleDrawable(!RxDataTool.isEmpty(fsExtends == null ? "" : fsExtends.getClientName())
                                        || !RxDataTool.isEmpty(fsExtends == null ? "" : fsExtends.getClientAdd())
                                        || !RxDataTool.isEmpty(fsExtends == null ? "" : fsExtends.getHandleDevice())
                                        || !RxDataTool.isEmpty(fsExtends == null ? "" : fsExtends.getReceiving())
                                        || !RxDataTool.isEmpty(fsExtends == null ? "" : fsExtends.getBuildTime())
                                , text_view_more_arrow);
                    }
                });
    }

    /**
     * 选择送样时间
     */
    private void selectSampleSendDate() {
        TimePickerViewUtils.showTimePickerView(mContext,
                TimePickerViewUtils.YEAR_MONTH_DAY_HOURS_MIN, new OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {
                        String dateStr = RxDateTool.getDate(RxDateTool.DATE_FORMAT_YYYY_MM_DD_HH_MM, date);
                        mSampling.setSendSampTime(dateStr);
                        tv_flow_date.getRightTextView().setText(dateStr);
                        setViewStyleDrawable(!RxDataTool.isEmpty(mSampling.getTransfer())
                                        || !RxDataTool.isEmpty(mSampling.getSendSampTime())
                                        || !RxDataTool.isEmpty(mSampling.getReciveTime()),
                                text_view_tv_arrow);
                    }
                });
    }

    /**
     * 选择天气
     */
    private void selectWeather() {
        Intent intent = new Intent(getContext(), WeatherActivity.class);
        new AvoidOnResult(getActivity()).startForResult(intent, new AvoidOnResult.Callback() {
            @Override
            public void onActivityResult(int resultCode, Intent data) {
                if (resultCode == Activity.RESULT_OK) {
                    mSampling.setWeather(data.getStringExtra("weather"));
                    weather_state.setRightTextStr(mSampling.getWeather());
                    setViewStyleDrawable(!RxDataTool.isEmpty(mSampling.getWeather())
                            || !RxDataTool.isEmpty(mSampling.getTemprature())
                            || !RxDataTool.isEmpty(mSampling.getPressure()), text_view_weather_arrow);

                }
            }
        });
    }

    /**
     * 设置箭头的动画效果
     *
     * @param arrow
     * @param container
     */
    private void setArrowAnimate(TextView arrow, View container) {
        if (arrow.getRotation() == 90f) {
            arrow.animate().rotation(0f);
            setViewStyle(false, arrow);
            container.setVisibility(View.GONE);
        } else {
            arrow.animate().rotation(90f);
            setViewStyle(true, arrow);
            container.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 选择采样方法
     */
    private void selectSampleMethod() {
        if (RxDataTool.isEmpty(mSampling.getFormType())) {
            ArtUtils.makeText(getContext(), "请先设置表单类型");
            return;
        }
        Intent intent = new Intent(getContext(), MethodActivity.class);
        intent.putExtra("tagId", mSampling.getFormType());
        new AvoidOnResult(getActivity()).startForResult(intent, new AvoidOnResult.Callback() {
            @Override
            public void onActivityResult(int resultCode, Intent data) {
                if (resultCode == Activity.RESULT_OK) {
                    mSampling.setMethodName(data.getStringExtra("MethodName"));
                    mSampling.setMethodId(data.getStringExtra("MethodId"));
                    base_sample_method.setRightTextStr(mSampling.getMethodName());
                }
            }
        });
    }

    /**
     * 选择采样点位
     */
    private void selectSamplePoint() {
        if (RxDataTool.isEmpty(mSampling.getTagId())) {
            ArtUtils.makeText(getContext(), "请先选择样品性质");
            return;
        }
        Intent intent = new Intent();
        if (mSampling.getMontype() == 3) {//环境质量
            intent.setClass(getContext(), PointSelectActivity.class);
            intent.putExtra("projectId", mSampling.getProjectId());
            intent.putExtra("tagId", mSampling.getTagId());
        } else {//污染源
            intent.setClass(getContext(), EnterRelatePointSelectActivity.class);
            intent.putExtra("projectId", mSampling.getProjectId());
            intent.putExtra("rcvId", project.getRcvId());
            intent.putExtra("tagId", mSampling.getTagId());
        }

        new AvoidOnResult(getActivity()).startForResult(intent, new AvoidOnResult.Callback() {
            @Override
            public void onActivityResult(int resultCode, Intent data) {
                if (resultCode == Activity.RESULT_OK) {
                    mSampling.setAddressName(data.getStringExtra("Address"));
                    mSampling.setAddressId(data.getStringExtra("AddressId"));
                    mSampling.setAddressNo(data.getStringExtra("AddressNo"));
                    base_sample_point.setRightTextStr(mSampling.getAddressName());
                }
            }
        });
    }

    /**
     * 选择样品性质
     */
    private void selectSampleType() {
        Intent intent = new Intent(getContext(), TypeActivity.class);
        intent.putExtra("tagId", mSampling.getParentTagId());
        intent.putExtra("title", "请选择样品性质");
        new AvoidOnResult(getActivity()).startForResult(intent, new AvoidOnResult.Callback() {
            @Override
            public void onActivityResult(int resultCode, Intent data) {
                if (resultCode == Activity.RESULT_OK) {
                    mSampling.setTagId(data.getStringExtra("TagId"));
                    mSampling.setTagName(data.getStringExtra("TagName"));
                    base_sample_property.setRightTextStr(mSampling.getTagName());
                }
            }
        });
    }

    /**
     * 选择人员
     */
    private void selectUser() {
        Intent intent = new Intent(getContext(), UserActivity.class);
        intent.putExtra("projectId", mSampling.getProjectId());
        intent.putExtra("selectUserIds", mSampling.getSamplingUserId());
        new AvoidOnResult(getActivity()).startForResult(intent, new AvoidOnResult.Callback() {
            @Override
            public void onActivityResult(int resultCode, Intent data) {
                if (resultCode == Activity.RESULT_OK) {
                    if (!RxDataTool.isEmpty(data.getStringExtra("UserId")) &&
                            !RxDataTool.isEmpty(data.getStringExtra("UserName"))) {
                        mSampling.setSamplingUserId(data.getStringExtra("UserId"));
                        mSampling.setSamplingUserName(data.getStringExtra("UserName"));
                        base_sample_user.setRightTextStr(mSampling.getSamplingUserName());
                    }
                }
            }
        });
    }

    /**
     * 选择采样时间
     */
    private void selectSampleDate() {
        TimePickerViewUtils.showTimePickerView(mContext,
                TimePickerViewUtils.YEAR_MONTH_DAY, new OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {
                        String time = RxDateTool.getDate(RxDateTool.DATE_FORMAT, date);
                        mSampling.setSamplingTimeBegin(time);
                        base_sample_date.getRightTextView().setText(time);
                        mSampling.setSamplingNo(SamplingUtil.createSamplingNo(time));
                        base_sample_no.setRightTextStr(mSampling.getSamplingNo());
                    }
                });
    }


    @Override
    public void handleMessage(@NonNull Message message) {

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
