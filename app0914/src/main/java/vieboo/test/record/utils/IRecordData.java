package vieboo.test.record.utils;

/**
 * 录音数据接口
 */
public interface IRecordData {
	/**
	 * 返回的录音数组和数组长度
	 */
	public void onRecordData(byte[] data, int dataLength);
}
