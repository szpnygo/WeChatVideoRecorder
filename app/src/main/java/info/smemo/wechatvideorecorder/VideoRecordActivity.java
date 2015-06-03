package info.smemo.wechatvideorecorder;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;

/**
 * Created by suzhenpeng on 2015/6/1.
 */
public class VideoRecordActivity extends Activity {

    private VideoRecorderView recoderView;
    private Button videoController;
    private TextView message;
    private boolean isCancel = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_record);

        recoderView = (VideoRecorderView) findViewById(R.id.recoder);
        videoController = (Button) findViewById(R.id.videoController);
        message = (TextView) findViewById(R.id.message);

        ViewGroup.LayoutParams params = recoderView.getLayoutParams();
        int[] dev = PhoneUtil.getResolution(this);
        params.width = dev[0];
        params.height = (int) (((float) dev[0]));
        recoderView.setLayoutParams(params);
        videoController.setOnTouchListener(new VideoTouchListener());

        recoderView.setRecorderListener(new VideoRecorderView.RecorderListener() {

            @Override
            public void recording(int maxtime, int nowtime) {

            }

            @Override
            public void recordSuccess(File videoFile) {
                System.out.println("recordSuccess");
                if (videoFile != null)
                    System.out.println(videoFile.getAbsolutePath());
                releaseAnimations();
            }

            @Override
            public void recordStop() {
                System.out.println("recordStop");
            }

            @Override
            public void recordCancel() {
                System.out.println("recordCancel");
                releaseAnimations();
            }

            @Override
            public void recordStart() {
                System.out.println("recordStart");
            }

            @Override
            public void videoStop() {
                System.out.println("videoStop");
            }

            @Override
            public void videoStart() {
                System.out.println("videoStart");
            }


        });

    }

    public class VideoTouchListener implements View.OnTouchListener {

        @Override
        public boolean onTouch(View v, MotionEvent event) {


            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    recoderView.startRecord();
                    isCancel = false;
                    pressAnimations();
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (event.getX() > 0
                            && event.getX() < videoController.getWidth()
                            && event.getY() > 0
                            && event.getY() < videoController.getHeight()) {
                        showPressMessage();
                        isCancel = false;
                    } else {
                        cancelAnimations();
                        isCancel = true;
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    if (isCancel) {
                        recoderView.cancelRecord();
                    }else{
                        recoderView.endRecord();
                    }
                    message.setVisibility(View.GONE);
                    releaseAnimations();
                    break;
                default:
                    break;
            }
            return false;
        }
    }

    /**
     * 移动取消弹出动画
     */
    public void cancelAnimations() {
        message.setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
        message.setTextColor(getResources().getColor(android.R.color.white));
        message.setText("松手取消");
    }

    /**
     * 显示提示信息
     */
    public void showPressMessage() {
        message.setVisibility(View.VISIBLE);
        message.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        message.setTextColor(getResources().getColor(android.R.color.holo_green_light));
        message.setText("上移取消");
    }


    /**
     * 按下时候动画效果
     */
    public void pressAnimations() {
        AnimationSet animationSet = new AnimationSet(true);
        ScaleAnimation scaleAnimation = new ScaleAnimation(1, 1.5f,
                1, 1.5f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setDuration(200);

        AlphaAnimation alphaAnimation = new AlphaAnimation(1, 0);
        alphaAnimation.setDuration(200);

        animationSet.addAnimation(scaleAnimation);
        animationSet.addAnimation(alphaAnimation);
        animationSet.setFillAfter(true);

        videoController.startAnimation(animationSet);
    }

    /**
     * 释放时候动画效果
     */
    public void releaseAnimations() {
        AnimationSet animationSet = new AnimationSet(true);
        ScaleAnimation scaleAnimation = new ScaleAnimation(1.5f, 1f,
                1.5f, 1f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setDuration(200);

        AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
        alphaAnimation.setDuration(200);

        animationSet.addAnimation(scaleAnimation);
        animationSet.addAnimation(alphaAnimation);
        animationSet.setFillAfter(true);

        message.setVisibility(View.GONE);
        videoController.startAnimation(animationSet);
    }


}
