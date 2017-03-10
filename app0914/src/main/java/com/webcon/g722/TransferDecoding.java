package com.webcon.g722;

public class TransferDecoding {
	
	static{
		System.loadLibrary("g722");
	}

	public native void g722_encoder_init(int bitrate, int samplerate);
	
	public native int g722_encoder(byte[] encodingData, byte[] orifinnalData, int length);
	
	public native void g722_encoder_release();
	
	public native void g722_decoder_init(int bitrate, int samplerate);
	
	public native int g722_decoder(byte[] orifinnalData, byte[] decodingData, int length);
	
	public native void g722_decoder_release();
	
}
