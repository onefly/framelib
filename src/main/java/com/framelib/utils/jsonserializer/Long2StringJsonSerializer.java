package com.framelib.utils.jsonserializer;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;

/**
 * 序列化成json时把Long转换为String
 * 
 * @Project : maxtp.framelib
 * @Program Name: com.framelib.utils.jsonserializer.LongJsonSerializer.java
 * @ClassName : LongJsonSerializer
 * @Author : zhangyan
 * @CreateDate : 2014-5-4 上午11:41:30
 */
public class Long2StringJsonSerializer extends JsonSerializer<Long> {

	@Override
	public void serialize(Long value, JsonGenerator jgen,
			SerializerProvider provider) throws IOException,
			JsonProcessingException {
		jgen.writeString(String.valueOf(value));
	}

}
