package com.direct.ai.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.log4j.Logger;

import ws.schild.jave.AudioAttributes;
import ws.schild.jave.Encoder;
import ws.schild.jave.EncodingAttributes;
import ws.schild.jave.MultimediaObject;

public class VoiceUtil {
	private static Logger log = Logger.getLogger(VoiceUtil.class);

	/**
	 * mp3转换wav
	 * 
	 * @param sourceFile
	 * @param targetFile
	 */
	public static boolean mp3ToWav(File sourceFile, File targetFile) {
		try {
			AudioAttributes audio = new AudioAttributes();
			audio.setCodec("pcm_s16le");
			audio.setBitRate(128000); // 音频比特率
			audio.setChannels(1); // 声道
			audio.setSamplingRate(16000);// 采样率
			EncodingAttributes attrs = new EncodingAttributes();
			attrs.setFormat("wav");
			attrs.setAudioAttributes(audio);
			Encoder encoder = new Encoder();
			MultimediaObject sourceMo = new MultimediaObject(sourceFile);
			encoder.encode(sourceMo, targetFile, attrs);
			log.info("mp3 转 wav完成");
			return true;
		} catch (Exception e) {
			log.error("mp3转换mav错误", e);
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * pcm转wav，格式转换成功后立即删除源文件，得到的wav文件头信息为：16位声双道 8000 hz
	 * 
	 * @param source 源文件
	 * @param target 目标文件
	 * @param isDeleteSource 是否删除源文件，true 删除，false 保留
	 * @throws IOException 
	 */
	public static void pcmToWav(File source, File target, boolean isDeleteSource) {
		try {
			FileInputStream fis = new FileInputStream(source);
			FileOutputStream fos = new FileOutputStream(target);
			// 计算长度
			byte[] buf = new byte[1024 * 4];
			int size = fis.read(buf);
			int PCMSize = 0;
			while (size != -1) {
				PCMSize += size;
				size = fis.read(buf);
			}
			fis.close();
			// 填入参数，比特率等等。这里用的是16位声双道 8000 hz
			WaveHeader header = new VoiceUtil().new WaveHeader();
			// 长度字段 = 内容的大小（PCMSize) + 头部字段的大小(不包括前面4字节的标识符RIFF以及fileLength本身的4字节)
			header.fileLength = PCMSize + (44 - 8);
			header.FmtHdrLeth = 16;// 头字节数，16或18，如果是18则又附加信息
			header.BitsPerSample = 16;// 每个采样需要的bit数，相当于64K，计算方式为16位(16bit)则代表2的16次方=65536 / 1024 =64K
			header.Channels = 1;// 声道，单声道为1，双声道为2
			header.FormatTag = 0x0001;// 编码方式，一般为0x0001
			header.SamplesPerSec = 16000;// 采样频率
			header.BlockAlign = (short) (header.Channels * header.BitsPerSample / 8);// 数据块对齐单位(每个采样需要的字节数)
			header.AvgBytesPerSec = header.BlockAlign * header.SamplesPerSec;// 每秒所需字节数
			header.DataHdrLeth = PCMSize;// 采样数据字节长度
			byte[] head = header.getHeader();
			assert head.length == 44; // WAV标准，头部应该是44字节
			// 写入wav头信息
			fos.write(head, 0, head.length);
			// 写入数据流
			fis = new FileInputStream(source);
			size = fis.read(buf);
			while (size != -1) {
				fos.write(buf, 0, size);
				size = fis.read(buf);
			}
			fis.close();
			fos.close();
			System.out.println("pcm 转 wav完成");
			if (isDeleteSource) {
				source.delete();// 删除源文件
			}
		} catch (Exception e) {
			log.error("pcm转wav失败", e);
		}
	}

	/**
	 * wav转mp3，格式转换成功后立即删除源文件
	 * 
	 * @param source 源文件
	 * @param target 转换后得到的文件
	 * @param isDeleteSource 是否删除源文件，true 删除，false 保留
	 * @param bitRate 音频比特率，默认128kbit/s
	 * @param channels 声道，默认双声道
	 * @param samplingRate  采样率，默认44100Hz
	 */
	public static void wavToMp3(File source, File target, boolean isDeleteSource, Integer bitRate, Integer channels,Integer samplingRate) {
		try {
			AudioAttributes audio = new AudioAttributes();
			audio.setCodec("libmp3lame");
			audio.setBitRate(bitRate == null ? 128000 : bitRate);
			audio.setChannels(channels == null ? 2 : channels);
			audio.setSamplingRate(samplingRate == null ? 44100 : samplingRate);
			EncodingAttributes attrs = new EncodingAttributes();
			attrs.setFormat("mp3");
			attrs.setAudioAttributes(audio);
			Encoder encoder = new Encoder();
			MultimediaObject sourceMo = new MultimediaObject(source);
			encoder.encode(sourceMo, target, attrs);
			System.out.println("wav转 mp3完成");
			if (isDeleteSource){
				source.delete();// 删除源文件
			}
		}catch (Exception e) {
			log.error("wav转mp3错误：", e);
		}
	}
	/**
	 * 保存音频文件
	 * @param is
	 * @param tmpVoice
	 * @throws IOException
	 */
	public static void saveVoiceFile(InputStream is, File tmpVoice) throws IOException {
		FileOutputStream fos = null;
		try {
			if(!tmpVoice.getParentFile().exists()) {//不存在目录则创建
				tmpVoice.getParentFile().mkdirs();
			}
			fos = new FileOutputStream(tmpVoice);
			byte[] buffer = new byte[1024];
			int len = 0;
			while((len = is.read(buffer)) != -1) {
				fos.write(buffer, 0, len);
			}
			fos.flush();
		}catch (Exception e) {
			log.error("保存音频文件错误", e);
		}finally {
			is.close();
			if(fos != null) {
				fos.close();
			}
		}
	}
	
	private class WaveHeader {
		public final char fileID[] = { 'R', 'I', 'F', 'F' };
		public int fileLength;
		public char wavTag[] = { 'W', 'A', 'V', 'E' };;
		public char FmtHdrID[] = { 'f', 'm', 't', ' ' };
		public int FmtHdrLeth;
		public short FormatTag;
		public short Channels;
		public int SamplesPerSec;
		public int AvgBytesPerSec;
		public short BlockAlign;
		public short BitsPerSample;
		public char DataHdrID[] = { 'd', 'a', 't', 'a' };
		public int DataHdrLeth;

		public byte[] getHeader() throws IOException {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			WriteChar(bos, fileID);
			WriteInt(bos, fileLength);
			WriteChar(bos, wavTag);
			WriteChar(bos, FmtHdrID);
			WriteInt(bos, FmtHdrLeth);
			WriteShort(bos, FormatTag);
			WriteShort(bos, Channels);
			WriteInt(bos, SamplesPerSec);
			WriteInt(bos, AvgBytesPerSec);
			WriteShort(bos, BlockAlign);
			WriteShort(bos, BitsPerSample);
			WriteChar(bos, DataHdrID);
			WriteInt(bos, DataHdrLeth);
			bos.flush();
			byte[] r = bos.toByteArray();
			bos.close();
			return r;
		}

		private void WriteShort(ByteArrayOutputStream bos, int s) throws IOException {
			byte[] mybyte = new byte[2];
			mybyte[1] = (byte) ((s << 16) >> 24);
			mybyte[0] = (byte) ((s << 24) >> 24);
			bos.write(mybyte);
		}

		private void WriteInt(ByteArrayOutputStream bos, int n) throws IOException {
			byte[] buf = new byte[4];
			buf[3] = (byte) (n >> 24);
			buf[2] = (byte) ((n << 8) >> 24);
			buf[1] = (byte) ((n << 16) >> 24);
			buf[0] = (byte) ((n << 24) >> 24);
			bos.write(buf);
		}

		private void WriteChar(ByteArrayOutputStream bos, char[] id) {
			for (int i = 0; i < id.length; i++) {
				char c = id[i];
				bos.write(c);
			}
		}
	}
}
