package bz.sunlight.common;

import java.io.IOException;
import java.text.SimpleDateFormat;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;


//后台的日期格式展示到前端
@Component
public class CustomJson {  
  
    private static final String DEFAULT_DATE_FORMAT="yyyy-MM-dd HH:mm:ss";  
    private static final ObjectMapper mapper;  
    
    
  
    public static String getDefaultDateFormat() {
		return DEFAULT_DATE_FORMAT;
	}

	public static ObjectMapper getMapper() {
		return mapper;
	}

	static {  
        SimpleDateFormat dateFormat = new SimpleDateFormat(DEFAULT_DATE_FORMAT);  
        mapper = new ObjectMapper();  
        mapper.setDateFormat(dateFormat);  
    }  
      
    public static String toJson(Object obj) {  
        try {  
            return mapper.writeValueAsString(obj);  
        } catch (Exception e) {  
            throw new RuntimeException("转换json字符失败!");  
        }  
    }  
      
    public <t> t toObject(String json,Class<t> clazz) {  
        try {  
            return mapper.readValue(json, clazz);  
        } catch (IOException e) {  
            throw new RuntimeException("将json字符转换为对象时失败!");  
        }  
    }  
}
