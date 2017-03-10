package com.android.test;

/**
 * 回音库JNI接口类
 * <p>用于处理语音对话回音，##无用</p>
 * @author Vieboo
 *
 */
public class VoiceObject {
	static {
		System.loadLibrary("stlport_shared");
	    System.loadLibrary("voiceproces");
	}
    
	public native int nativeinit(Object object);
	public native int Init(Object object);
	public native int Exit();
	public native int StartRecorder();
	public native int StopRecorder();
	public native int StartPlayer();
	public native int StopPlayer();
    public native int WritePlayData(byte[] byteArray,int length);
    public native int GetRecordData(byte[] byteArray,int length);
    public native int CallbackM(byte[] byteArray);
}
