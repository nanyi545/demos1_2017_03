package test1.nh.com.demos1.audioTest;

/**
 * Created by Administrator on 16-3-7.
 */
public interface IRecordData {
    /**
     * 返回的录音数组和数组长度
     */
    void onRecordData(byte[] data, int dataLength);

}
