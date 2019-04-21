package com.direct.ai.service.impl;

import java.io.File;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Part;

import org.json.JSONArray;
import org.json.JSONObject;

import com.baidu.aip.speech.AipSpeech;
import com.baidu.aip.speech.TtsResponse;
import com.direct.ai.service.BaiduAIBase;
import com.direct.ai.service.VoiceHandleServiceI;
import com.direct.ai.util.ResponseData;
import com.direct.ai.util.VoiceUtil;

public class BaiduVoiceHandleServiceImpl extends BaiduAIBase implements VoiceHandleServiceI {
	// 调用百度api接口
	AipSpeech client = new AipSpeech(APP_ID, API_KEY, SECRET_KEY);

	@Override
	public ResponseData recognize(Part voicePart) {
		// 文件类型
		String fileType = voicePart.getContentType();
		if(fileType.endsWith("mp3")) {
			fileType = MP3;
		}else if(fileType.endsWith("wav")) {
			fileType = WAV;
		}else {
			return ResponseData.fail("请上传mp3、wav音频");
		}
		try {
			// 获取音频字符流
			InputStream is = voicePart.getInputStream();
			// 保存临时音频文件
			String filename = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(Calendar.getInstance().getTime());
			File tmpVoice = new File(workspace + File.separator + filename+fileType);
			VoiceUtil.saveVoiceFile(is, tmpVoice);
			if(fileType.equals(MP3)) {
				File mp3File = tmpVoice;
				tmpVoice = new File(tmpVoice.getPath().replace(MP3, WAV));
				if(!VoiceUtil.mp3ToWav(mp3File, tmpVoice)) {
					return ResponseData.fail("mp3音频文件错误，转换失败，请用wav音频。");
				}
				mp3File.delete();
			}
			
			JSONObject json = client.asr(tmpVoice.getPath(), "wav", 16000, null);
			tmpVoice.delete();
			Integer status = json.getInt("err_no"); //状态码
			if(status != 0) {
				// 异常响应处理
				String msg = json.getString("err_msg");
				log.warn("百度接口调用响应异常,error_code:"+status + " error_msg:"+msg);
				return ResponseData.fail(msg);
			}
			// 响应数据处理
			Map<String, Object> map = new HashMap<>();
			// 获取结果
			JSONArray jsonArray = json.getJSONArray("result");
			// 识别文本
			String text = jsonArray.getString(0);
			map.put("text", text);
			return ResponseData.success(map);
		} catch (Exception e) {
			log.warn("识别音频文件错误：", e);
			return ResponseData.fail("服务器异常");
		}

	}

	@Override
	public byte[] genVoice(String text, Integer voiceType) {
		// 请求参数
		HashMap<String, Object> options = new HashMap<String, Object>();
		// 语速，取值0-9，默认为5中语速
		options.put("spd", "5");
		// 音调，取值0-9，默认为5中语调
		options.put("pit", "5");
		// 发音人选择, 0为女声，1为男声， 3为情感合成-度逍遥，4为情感合成-度丫丫，默认为普通女
		options.put("per", voiceType);
		
		TtsResponse res = client.synthesis(text, "zh", 1, options);
		byte[] data = res.getData();
		
		return data;

	}

}
