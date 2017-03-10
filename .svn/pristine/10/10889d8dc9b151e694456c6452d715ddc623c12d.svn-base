package com.webcon.sus.CircleProgressButton;
/**
 * 状态管理器
 *
 */
public class StateManager {

    private boolean mIsEnabled;		//button是否可点击
    private int mProgress;			//当前进度

    /**
     * 状态管理器-构造器
     * @param progressButton
     */
    public StateManager(CircularProgressButton progressButton) {
        mIsEnabled = progressButton.isEnabled();
        mProgress = progressButton.getProgress();
    }
    
    /**
     * 保存当前进度
     * @param progressButton 自定义按钮
     */
    public void saveProgress(CircularProgressButton progressButton) {
        mProgress = progressButton.getProgress();
    }

    /* ---- getter & setter ----*/
    public boolean isEnabled() {
        return mIsEnabled;
    }
    
    /**
     * 获取进度
     * @return
     */
    public int getProgress() {
        return mProgress;
    }
    
    /**
     * 按钮状态发生改变，则同步按钮状态（更新进度，可用状况）
     * <p>当按钮从一个状态转变到另一个状态，动画结束时调用
     * @param progressButton 自定义按钮
     */
    public void checkState(CircularProgressButton progressButton) {
        if (progressButton.getProgress() != getProgress()) {
            progressButton.setProgress(progressButton.getProgress());
        } else if(progressButton.isEnabled() != isEnabled()) {
            progressButton.setEnabled(progressButton.isEnabled());
        }
    }
}
