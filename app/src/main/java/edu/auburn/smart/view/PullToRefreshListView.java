package edu.auburn.smart.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

import edu.auburn.smart.R;

/**
 * Author: Gary
 * Time: 4/17/17
 */

public class PullToRefreshListView extends ListView implements AbsListView.OnScrollListener{
    private static final int STATE_PULL_TO_REFRESH = 1;
    private static final int STATE_RELEASE_TO_REFRESH = 2;
    private static final int STATE_REFRESHING = 3;
    private static int mCurrentState = STATE_PULL_TO_REFRESH;
    private  View mHeaderView;
    private TextView tvTitle;
    private TextView tvTime;
    private ImageView ivArrow;
    private ProgressBar pb;
    private View footerView;
    public PullToRefreshListView(Context context) {
        super(context);
        initHeaderView();
        initFooterView();
    }

    public PullToRefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initHeaderView();
        initFooterView();
    }


    public PullToRefreshListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initHeaderView();
        initFooterView();
    }

    private int measuredHeight;
    /**
     * init header view
     */
    private void initHeaderView(){
        mHeaderView = View.inflate(getContext(), R.layout.pull_to_refresh, null);
        tvTitle = (TextView) mHeaderView.findViewById(R.id.tv_refresh);
        tvTime = (TextView) mHeaderView.findViewById(R.id.tv_time);
        ivArrow = (ImageView) mHeaderView.findViewById(R.id.iv_arr);
        pb = (ProgressBar) mHeaderView.findViewById(R.id.pb_loading);
        this.addHeaderView(mHeaderView);
        mHeaderView.measure(0,0);
        measuredHeight = mHeaderView.getMeasuredHeight();
        mHeaderView.setPadding(0, -measuredHeight, 0, 0);
        initAnim();
        setRefreshTime();
    }
    int startY = -1;
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                startY = (int) ev.getY();
                break;
            case MotionEvent.ACTION_UP:
                startY = -1;
                if (mCurrentState == STATE_RELEASE_TO_REFRESH){
                    mCurrentState = STATE_REFRESHING;
                    refreshState();
                    mHeaderView.setPadding(0,0,0,0);// 完整显示头布局
                    // 进行回调
                    if (mListener != null){
                        mListener.onRefresh();
                    }
                }else if(mCurrentState == STATE_PULL_TO_REFRESH){
                    //隐藏头布局
                    mHeaderView.setPadding(0,-measuredHeight,0,0);

                }

                break;
            case MotionEvent.ACTION_MOVE:
                if(startY == -1){
                    startY = (int) ev.getY();
                }

                if (mCurrentState == STATE_REFRESHING){// 如果正在刷新就跳出去
                    break;
                }

                int endY = (int) ev.getY();
                int dy = endY - startY;

                int firstVisiblePosition = getFirstVisiblePosition();
                // 必须下拉，且当前显示的是第一个item
                if(dy > 0 && firstVisiblePosition == 0){
                    int padding = dy - measuredHeight;
                    mHeaderView.setPadding(0, padding, 0, 0);
                    if(padding > 0 && mCurrentState != STATE_RELEASE_TO_REFRESH){
                        // 改为松开刷新
                        mCurrentState = STATE_RELEASE_TO_REFRESH;
                        refreshState();
                    } else if (padding < 0 && mCurrentState != STATE_PULL_TO_REFRESH){
                        // 改为下拉刷新
                        mCurrentState = STATE_PULL_TO_REFRESH;
                        refreshState();
                    }
                   // return true;
                }
                break;
        }
        return super.onTouchEvent(ev);
    }

    private RotateAnimation animUp, animDown;
    private void initAnim(){
        animUp = new RotateAnimation(0, -180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animUp.setDuration(200);
        animUp.setFillAfter(true);

        animDown = new RotateAnimation(-180, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animDown.setDuration(200);
        animDown.setFillAfter(true);

    }

    /**
     * 根据当前状态刷新界面
     */
    private void refreshState() {
        switch (mCurrentState){
            case STATE_PULL_TO_REFRESH:
                tvTitle.setText("Pull to refresh");
                ivArrow.setVisibility(VISIBLE);
                pb.setVisibility(INVISIBLE);
                ivArrow.startAnimation(animDown);
                break;
            case STATE_REFRESHING:
                tvTitle.setText("Refreshing");
                ivArrow.clearAnimation();//清除箭头动画，否则无法隐藏
                pb.setVisibility(VISIBLE);
                ivArrow.setVisibility(INVISIBLE);
                break;
            case STATE_RELEASE_TO_REFRESH:
                tvTitle.setText("Release");
                pb.setVisibility(INVISIBLE);
                ivArrow.setVisibility(VISIBLE);
                ivArrow.startAnimation(animUp);
                break;
        }
    }
    private boolean isLoadMore;//标记是否正在加载更多

    // 滑动状态发生变化
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState == SCROLL_STATE_IDLE){ // 空闲状态
            int lastVisiblePosition = getLastVisiblePosition();
            if(lastVisiblePosition == getCount() - 1 && !isLoadMore){ //当前显示的是最后一个item并且没有正在加载更多
                // 到底了
                isLoadMore = true;
                footerView.setPadding(0,0,0,0);// 显示 加载更多布局
                setSelection(getCount() - 1); // 将listview显示在最后一个item上， 从而加载更多会直接显示出来
                // 通知主界面加载下一页
                if (mListener != null){
                    mListener.onLoadMore();
                }
            }
        }
    }
    // 滑动过程回调
    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }

    /**
     * 下拉刷新的回调接口
     */
    public interface OnRefreshListener {
        public void onRefresh();
        public void onLoadMore();
    }

    /**
     * 暴露接口，设置监听
     */
    public void setOnRefreshListener(OnRefreshListener listener){
        mListener = listener;
    }
    /**
     * 定义成员变量，接受监听对象
     */
    private OnRefreshListener mListener;

    /**
     * 刷新结束，收起控件
     */
    public void OnRefreshComplete(boolean success){
        if (!isLoadMore){
            mHeaderView.setPadding(0, -measuredHeight, 0, 0);
            mCurrentState = STATE_PULL_TO_REFRESH;
            tvTitle.setText("Pull to refresh");
            pb.setVisibility(INVISIBLE);
            ivArrow.setVisibility(VISIBLE);
            if (success)
                setRefreshTime();
        }else{
            footerView.setPadding(0, -footerHeight, 0, 0);
            isLoadMore = false;
        }

    }

    private void setRefreshTime(){
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        String time = format.format(new Date());
        tvTime.setText(time);
    }

    private int footerHeight;
    private void initFooterView(){
        footerView = View.inflate(getContext(), R.layout.poll_refresh_footer,null);
        this.addFooterView(footerView);
        footerView.measure(0, 0);
        footerHeight = footerView.getMeasuredHeight();
        footerView.setPadding(0, -footerHeight, 0, 0);
        this.setOnScrollListener(this);
    }
}
