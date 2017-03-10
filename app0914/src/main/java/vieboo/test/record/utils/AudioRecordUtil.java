package vieboo.test.record.utils;

import android.media.AudioRecord;

public class AudioRecordUtil {
	
	//录音数据接口
	private IRecordData mIRecrodData;
	//录音数据保存的字节数组
	private byte[] buffer;
	//录音数组的长度
	private int bufferLength;
	//是否正在录音
	private boolean isRecording;
	
	//音频获取源设备
	private static int mAudioSource;
	//音频采样率
	private static int mSampleRateInHz;
	//音频录制的声道
	private static int mChannelConfig;
	//音频数据格式
	private static int mAudioFormat;
	
	/**
	 * 启动录音
	 * @param iRecordData 录音数据接口
	 * @param dataLength 录音数组的长度
	 * @param audioSource 音频获取源设备
	 * @param sampleRateInHz 音频采样率
	 * @param channelConfig 音频录制的声道
	 * @param audioFormat 音频数据格式
	 */
	public void onStartRecord(IRecordData iRecordData, int dataLength, 
			int audioSource, int sampleRateInHz, int channelConfig, int audioFormat){
		mIRecrodData = iRecordData;
		bufferLength = dataLength;
		buffer = new byte[bufferLength];
		isRecording = true;
		mAudioSource = audioSource;
		mSampleRateInHz = sampleRateInHz;
		mChannelConfig = channelConfig;
		mAudioFormat = audioFormat;
		
		//启动录制线程
		new Thread(new StartThread(this)).start();
		
	}
	
	/**
	 * 音频录制
	 */
	private void onRun(){
		
		//获得录音缓冲区
		int recordBufferSize = AudioRecord.getMinBufferSize(
				mSampleRateInHz, mChannelConfig, mAudioFormat);
		
		//获得录音对象
		AudioRecord record = new AudioRecord(mAudioSource, mSampleRateInHz, 
				mChannelConfig, mAudioFormat, recordBufferSize * 10);
		
		//开始录音
		record.startRecording();
		
		for(; isRecording; ){
			record.read(buffer, 0, bufferLength);
			if(mIRecrodData != null){
				mIRecrodData.onRecordData(buffer, bufferLength);
			}
		}
		record.stop();
	}
	
	/**
	 * 停止录制
	 */
	public void onStop(){
		isRecording = false;
	}
	
	/**
	 * 调取音频录制的线程
	 */
	private class StartThread implements Runnable{
		
		AudioRecordUtil mAudioRecordUtil;
		
		private StartThread(AudioRecordUtil audioRecordUtil){
			mAudioRecordUtil = audioRecordUtil;
		}
		
		@Override
		public void run() {
			mAudioRecordUtil.onRun();
		}
	}
}
