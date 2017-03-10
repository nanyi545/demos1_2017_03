package com.webcon.sus.media;

/**
 * 启动视频视频播放，初始化视频解码，视频解码释放,回调接口类
 * @author Vieboo
 *
 */
public interface IOtherVideoStart {

	/**
	 * 启动视频播放
	 * @param width	解码需要的视频宽度
	 * @param height	解码需要的视频高度
	 */
	public void startPlayVideo(int width, int height, short type);
	
	/**
	 * 视频解码释放
	 */
	public void relaseDecode();
	
}
