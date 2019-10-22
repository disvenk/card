package com.resto.msgc.backend.card.dcrf;

import java.util.Random;

@SuppressWarnings("serial")
public class CardReader {
	short status;

	//扇区号（M1卡：0～15；  ML卡：0）
	int secNr = 10;

	//块地址即是扇区*4+偏移(0~3))
	int iAdr = (secNr * 4) + 1;

	/**
	 * 设备ID
	 */
	static int lDevice = 0;
	int[] pSnr = new int[1];
	char[] pSBuffer = new char[16];
	char[] pRBuffer = new char[16];
	/**
	 * 用于存放从读卡器中读出的数据
	 */
	JavaRD800 rd = null;
	/**
	 * 初始化设备失败
	 */
	private static final String INITDEVICEERROR = "-1";
	/** 寻卡失败 */
	private static final String SEARCHERROR = "-2";
	/** 装载密码失败 */
	private static final String LOADKEYEORROR = "-3";
	/** 验证密码失败 */
	private static final String VALIDATEKEYERROR = "-4";
	/** 读卡失败 */
	private static final String READCARDERROR = "-5";
	/** 写卡失败 */
	private static final String WRITECARDERROR = "-6";
	public void init() {
		if (rd == null) {
			rd = new JavaRD800();
		}
		if (lDevice <= 0) {
			lDevice = rd.dc_init(100, 115200);
		}
	}

	/**
	 * 用于外部调用返回学号或错误代码
	 * 
	 * @return 学号或错误代码
	 */
	public String getData() throws Exception {
		String data = null;
		data = INITDEVICEERROR;
		/** 初始化 */
		if (initReader()) {
			data = SEARCHERROR;
			/** 寻卡 */
			if (searchCard()) {
				data = LOADKEYEORROR;
				/** 装载密码 */
				if (loadKey()) {
					data = VALIDATEKEYERROR;
					/** 验证密码 */
					if (validateKey()) {
						data = READCARDERROR;
						/** 读卡 */
						if (readCard()) {
							rd.dc_beep(lDevice, (short) 10);
							/**
							 * 此处为我要处理的数据
							 */
							//白卡
							String blankStr = "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000";
							String content = String.valueOf(pRBuffer);
							if(blankStr.equals(content)){
								String codes = randomCode(16);
								data = WRITECARDERROR;
								if(writeCard(codes)){
									content = String.valueOf(pRBuffer);
									data = content;
								}
							}else{
								data = content;
							}
						}
					}
				}
			}
		}
		rd.dc_exit(lDevice);
		return returnCardStatus(data);
	}

	/**
	 * 初始化
	 */
	private boolean initReader() {
		this.init();
		if (lDevice <= 0) {
			return false;
		}
		return true;
	}

	/**
	 * 寻卡
	 */
	private boolean searchCard() {
		status = rd.dc_card(lDevice, (short) 0, pSnr);
		return returnStatus(status);
	}

	/**
	 * 装载密码
	 */
	private boolean loadKey() {
		// 密码
		pSBuffer[0] = 0xFF;
		pSBuffer[1] = 0xFF;
		pSBuffer[2] = 0xFF;
		pSBuffer[3] = 0xFF;
		pSBuffer[4] = 0xFF;
		pSBuffer[5] = 0xFF;
		// * 装入11扇区的0套A密码 */
		status = rd.dc_load_key(lDevice, (short) 0, (short) secNr, pSBuffer);
		return returnStatus(status);
	}

	/**
	 * 验证密码
	 */
	private boolean validateKey() {
		// 验证11扇区0套A密码
		status = rd.dc_authentication(lDevice, (short) 0, (short) secNr);
		return returnStatus(status);
	}

	/**
	 * 读卡
	 */
	private boolean readCard() {
		// 读M1卡块11的数据(M1卡有0-15扇区，块地址即是扇区*4+偏移(0~3))
		status = rd.dc_read(lDevice, (short) iAdr, pRBuffer);
		return returnStatus(status);
	}

	/**
	 * 写卡
	 */
	private boolean writeCard(String str) {
		pRBuffer = str.toCharArray();
		status = rd.dc_write(lDevice,(short)iAdr,pRBuffer);
		return returnStatus(status);
	}

	/**
	 * 返回成功与否
	 */
	private boolean returnStatus(short status) {
		if (status == 0) {
			return true;
		}
		return false;
	}

	/**
	 * 返回卡状态
	 */
	private String returnCardStatus(String status) {
		if(status.equals(INITDEVICEERROR)){
			status = "初始化设备失败";
		}else if(status.equals(SEARCHERROR)){
			status = "寻卡失败";
		}else if(status.equals(LOADKEYEORROR)){
			status = "装载密码失败";
		}else if(status.equals(VALIDATEKEYERROR)){
			status = "验证密码失败";
		}else if(status.equals(READCARDERROR)){
			status = "读卡失败";
		}else if(status.equals(WRITECARDERROR)){
			status = "写卡失败";
		}
		return status;
	}

	public String randomCode(int length){
		String str = "";
		Random random = new Random();
		for (int i = 0; i < length; i++)
		{
//			char c = (char) (int) (Math.random() * 126);
//			char c = (char) (int) (Math.random() * 26 + 97);
			str += random.nextInt(10);
		}
		return str;
	}
}
