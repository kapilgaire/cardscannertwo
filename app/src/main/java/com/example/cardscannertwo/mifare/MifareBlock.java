package com.example.cardscannertwo.mifare;


public class MifareBlock {
	
	/**
	 * the index of the block.
	 */
	public int blockIndex;
	
	/**
	 * need to read or not
	 */
	public boolean needRead=true;
	
	
	/**
	 * need to write or not
	 */
	public boolean needWrite=false;


	/**
	 * Constructor.
	 * @param dataValue the data value.
	 */
	public MifareBlock(byte[] dataValue) {
		if (dataValue == null || dataValue.length != 16) {
			throw new IllegalArgumentException("Invaid data array");
		}
		System.arraycopy(dataValue, 0, data, 0, dataValue.length);

	}
	
	

	/**
	 * Default constructor.
	 * 
	 */
	public MifareBlock() {

	}
	

	/**
	 * Get the data value.
	 * @return the byte array of the block.
	 */
	public byte[] getData(){
		return data;
	}


	/**
	 * Set block data 
	 * @param dataValue new block data
	 */
	public void setData(byte[] dataValue){
		if (dataValue == null || dataValue.length != 16) {
			throw new IllegalArgumentException("Invaid data array");
		}
		System.arraycopy(dataValue, 0, data, 0, dataValue.length);
	}
	
	/**
	 * this is the data values (6 bytes)
	 */
	private final byte[] data=new byte[16];
}
