package bz.sunlight.util;



import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.FieldNamingStrategy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;


public final class GsonUtil
{
	public static class JsonExclusionStrategy implements ExclusionStrategy{
	
		//字段排除方式
		public enum TransformType{
			PC,  MOBILE;
		}
		
		private TransformType transformType;
		
		public JsonExclusionStrategy(TransformType transformType) {
			super();
			this.transformType = transformType;
		}
	
		@Override
		public boolean shouldSkipField(FieldAttributes f) {
			JsonExclusion type = (JsonExclusion)f.getAnnotation(JsonExclusion.class);
	
			//如果是两种方式都排除的字段则不用继续讨论，直接返回true(从整个策略的构造逻辑来看,应该是可以做成只给加了某个特定注解的字段进行转化的模式)
			if (type.value() == ExclusionType.PC_MOBILE){
				return true;
			}else if (type.value() == ExclusionType.PC && transformType == TransformType.PC){
				return true;
			}else if(type.value() == ExclusionType.MOBILE && transformType == TransformType.MOBILE){
				return true;
			}else{
				return false;
			}
			
		}
	
		@Override
		public boolean shouldSkipClass(Class<?> clazz) {
			return false;
		}
		
	}	

	public static class JsonFieldNamingStrategy implements FieldNamingStrategy{
		
		private Map<String,String> exclusionFieldsMap;//key:要转化的字段;value:要转化成的字符串

		public JsonFieldNamingStrategy(Map<String, String> exclusionFieldsMap) {
			super();
			this.exclusionFieldsMap = exclusionFieldsMap;
		}

		@Override
		public String translateName(Field f) {
			
			//遍历传入map中的key，如有与转化目标中字段相同的则返回map中的转化目标
			for (String key : exclusionFieldsMap.keySet()){
				if (key.equals(f.getName())){
					return exclusionFieldsMap.get(key);
				}
			}
			return f.getName();
		}
		
	}

	static final String dateFormat = "yyyy-MM-dd";
    static final String timeFormat = "HH:mm:ss";
    static final String dateTimeFormat = "yyyy-MM-dd HH:mm:ss";
    static final String timestampFormat = "yyyy-MM-dd HH:mm:ss.SSS";

//    @SuppressWarnings("unused")
//    private Gson gson = new Gson();
    private static Gson gson = new Gson();


    static
    {
        init();
    }

    private GsonUtil()
    {
    }

    private static void init()
    {
        //defaultConfig = instanceJsonConfig();
    }
    
    private static final Log log = LogFactory.getLog(GsonUtil.class);
    public static final String EMPTY = "";    
    /** 空的 {@code JSON} 数据 - <code>"{}"</code>。 */    
    public static final String EMPTY_JSON = "{}";    
    /** 空的 {@code JSON} 数组(集合)数据 - {@code "[]"}。 */    
    public static final String EMPTY_JSON_ARRAY = "[]";    
    /** 默认的 {@code JSON} 日期/时间字段的格式化模式。 */    
//    public static final String DEFAULT_DATE_PATTERN = "yyyy-MM-dd HH:mm:ss SSS";
    public static final String DEFAULT_DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";   
    /** {@code Google Gson} 的 {@literal @Since} 注解常用的版本号常量 - {@code 1.0}。 */    
    public static final Double SINCE_VERSION_10 = 1.0d;    
    /** {@code Google Gson} 的 {@literal @Since} 注解常用的版本号常量 - {@code 1.1}。 */    
    public static final Double SINCE_VERSION_11 = 1.1d;    
    /** {@code Google Gson} 的 {@literal @Since} 注解常用的版本号常量 - {@code 1.2}。 */    
    public static final Double SINCE_VERSION_12 = 1.2d;    
    
    /**  
     * 将给定的目标对象根据指定的条件参数转换成 {@code JSON} 格式的字符串。  
     * <p />  
     * <strong>该方法转换发生错误时，不会抛出任何异常。若发生错误时，曾通对象返回 <code>"{}"</code>； 集合或数组对象返回  
     * <code>"[]"</code></strong>  
     *   
     * @param target  
     *            目标对象。  
     * @param targetType  
     *            目标对象的类型。  
     * @param isSerializeNulls  
     *            是否序列化 {@code null} 值字段。  
     * @param version  
     *            字段的版本号注解。  
     * @param datePattern  
     *            日期字段的格式化模式。  
     * @param excludesFieldsWithoutExpose  
     *            是否排除未标注 {@literal @Expose} 注解的字段。  
     * @param namingConvention  
     *            对于没有使用@SerializedName注解的字段设置字段打印格式取值规则如下:
     * <ul>  
     * <li>FieldNamingPolicy.UPPER_CAMEL_CASE 首字母大写</li>  
     * <li>FieldNamingPolicy.UPPER_CAMEL_CASE_WITH_SPACES 首字母大写并将字段插分成一个个单词用空格格开</li>  
     * <li>FieldNamingPolicy.LOWER_CASE_WITH_DASHES 转小写并用-将各个单词格开</li>  
     * <li>FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES 转小写并用下划线格开</li>  
     * </ul> 
     * @param jsonExclusionStrategy  
     *            带不同 @JsonExclusion 注解的字段在转json时会被不同处理
     * @param jsonFieldNamingStrategy  
     *            制定一个转json时的字段名修改规则
     * @return 目标对象的 {@code JSON} 格式的字符串。  
     */    
    public static String toJson(Object target, Type targetType,    
            boolean isSerializeNulls, Double version, String datePattern,    
            boolean excludesFieldsWithoutExpose,FieldNamingPolicy namingConvention,
            JsonExclusionStrategy jsonExclusionStrategy, JsonFieldNamingStrategy jsonFieldNamingStrategy) {    
    	if (target == null)    
            return EMPTY_JSON;    
        GsonBuilder builder = new GsonBuilder();    
        if (isSerializeNulls)    
            builder.serializeNulls();    
        if (version != null)    
            builder.setVersion(version.doubleValue());    
        if (isEmpty(datePattern))    
            datePattern = DEFAULT_DATE_PATTERN;    
        builder.setDateFormat(datePattern);    
        if (excludesFieldsWithoutExpose)    
            builder.excludeFieldsWithoutExposeAnnotation();
        if (namingConvention != null)
        	builder.setFieldNamingPolicy(namingConvention);
        if (jsonExclusionStrategy != null)
        	builder.setExclusionStrategies(jsonExclusionStrategy);
        if (jsonFieldNamingStrategy != null)
        	builder.setFieldNamingStrategy(jsonFieldNamingStrategy);
        
        //有效防止在待转对象 map 的 key 为复杂类对象时所导致的乱码问题
        if (target instanceof Map)
        	builder.enableComplexMapKeySerialization();
        
        builder.disableHtmlEscaping();	//针对特殊字符（如单引号）的转化进行有效处理
        
        String result = EMPTY;    
//        Gson gson = builder.create();    
        gson = builder.create();    
        try {    
            if (targetType != null) {    
                result = gson.toJson(target, targetType);    
            } else {    
                result = gson.toJson(target);    
            }    
        } catch (Exception ex) {    
            log.warn("目标对象 " + target.getClass().getName()    
                    + " 转换 JSON 字符串时，发生异常！", ex);    
            if (target instanceof Collection || target instanceof Iterator    
                    || target instanceof Enumeration    
                    || target.getClass().isArray()) {    
                result = EMPTY_JSON_ARRAY;    
            } else    
                result = EMPTY_JSON;    
        }    
        return result;    
    }    
    
    /**  
     * 将给定的目标对象转换成 {@code JSON} 格式的字符串。<strong>此方法只用来转换普通的 {@code JavaBean}  
     * 对象。</strong>  
     * <ul>  
     * <li>该方法只会转换标有 {@literal @Expose} 注解的字段；</li>  
     * <li>该方法不会转换 {@code null} 值字段；</li>  
     * <li>该方法会转换所有未标注或已标注 {@literal @Since} 的字段；</li>  
     * <li>该方法转换时使用默认的 日期/时间 格式化模式 - {@code yyyy-MM-dd HH:mm:ss SSS}；</li>  
     * </ul>  
     *   
     * @param target  
     *            要转换成 {@code JSON} 的目标对象。  
     * @return 目标对象的 {@code JSON} 格式的字符串。  
     */    
    public static String toJson(Object target) {    
        return toJson(target, null, false, null, null, false, null, null, null);    
    }    
    
    /**  
     * 将给定的目标对象转换成 {@code JSON} 格式的字符串。<strong>此方法只用来转换普通的 {@code JavaBean}  
     * 对象。</strong>  
     * <ul>  
     * <li>该方法只会转换标有 {@literal @Expose} 注解的字段；</li>  
     * <li>该方法不会转换 {@code null} 值字段；</li>  
     * <li>该方法会转换所有未标注或已标注 {@literal @Since} 的字段；</li>  
     * </ul>  
     *   
     * @param target  
     *            要转换成 {@code JSON} 的目标对象。  
     * @param datePattern  
     *            日期字段的格式化模式。  
     * @return 目标对象的 {@code JSON} 格式的字符串。  
     */    
    public static String toJson(Object target, String datePattern) {    
        return toJson(target, null, false, null, datePattern, false, null, null, null);    
    }    
    
    /**  
     * 将给定的目标对象转换成 {@code JSON} 格式的字符串。<strong>此方法只用来转换普通的 {@code JavaBean}  
     * 对象。</strong>  
     * <ul>  
     * <li>该方法只会转换标有 {@literal @Expose} 注解的字段；</li>  
     * <li>该方法不会转换 {@code null} 值字段；</li>  
     * <li>该方法转换时使用默认的 日期/时间 格式化模式 - {@code yyyy-MM-dd HH:mm:ss SSS}；</li>  
     * </ul>  
     *   
     * @param target  
     *            要转换成 {@code JSON} 的目标对象。  
     * @param version  
     *            字段的版本号注解({@literal @Since})。  
     * @return 目标对象的 {@code JSON} 格式的字符串。  
     */    
    public static String toJson(Object target, Double version) {    
        return toJson(target, null, false, version, null, false, null, null, null);    
    }    
    
    /**  
     * 将给定的目标对象转换成 {@code JSON} 格式的字符串。<strong>此方法只用来转换普通的 {@code JavaBean}  
     * 对象。</strong>  
     * <ul>  
     * <li>该方法会转出所有值为空的字段（转为null）</li>  
     * <li>该方法转换时使用默认的 日期/时间 格式化模式 - {@code yyyy-MM-dd HH:mm:ss SSS}；</li>  
     * </ul>  
     *   
     * @param target  
     *            要转换成 {@code JSON} 的目标对象。  
     * @param isSerializeNulls  
     *            是否转出值为空的字段  
     * @return 目标对象的 {@code JSON} 格式的字符串。  
     */    
    public static String toJson(Object target,    
    		 boolean isSerializeNulls) {    
        return toJson(target, null, isSerializeNulls, null, null,    
                false, null, null, null);    
    }    
    /**  
     * 将给定的目标对象转换成 {@code JSON} 格式的字符串。<strong>此方法只用来转换普通的 {@code JavaBean}  
     * 对象。</strong>  
     * <ul>  
     * <li>该方法不会转换 {@code null} 值字段；</li>  
     * <li>该方法转换时使用默认的 日期/时间 格式化模式 - {@code yyyy-MM-dd HH:mm:ss SSS}；</li>  
     * </ul>  
     *   
     * @param target  
     *            要转换成 {@code JSON} 的目标对象。  
     * @param namingConvention  
     *            对于没有使用@SerializedName注解的字段设置字段打印格式取值规则如下:
     * <ul>  
     * <li>FieldNamingPolicy.UPPER_CAMEL_CASE 首字母大写</li>  
     * <li>FieldNamingPolicy.UPPER_CAMEL_CASE_WITH_SPACES 首字母大写并将字段插分成一个个单词用空格格开</li>  
     * <li>FieldNamingPolicy.LOWER_CASE_WITH_DASHES 转小写并用-将各个单词格开</li>  
     * <li>FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES 转小写并用下划线格开</li>  
     * </ul>  
     * @return 目标对象的 {@code JSON} 格式的字符串。  
     */    
    public static String toJson(Object target,    
    		FieldNamingPolicy namingConvention) {    
        return toJson(target, null, false, null, null,    
        		false, namingConvention, null, null);    
    }    
    
    /**  
     * 将给定的目标对象转换成 {@code JSON} 格式的字符串。
     * <ul>  
     * <li>该方法可以设置不同的转json时需要忽略的字段</li>  
     * </ul>  
     *   
     * @param target  
     *            要转换成 {@code JSON} 的目标对象。  
     * @param jsonExclusionStrategy  
     *            带不同 @JsonExclusion 注解的字段在转json时会被不同处理
     * @return 目标对象的 {@code JSON} 格式的字符串。  
     */    
    public static String toJson(Object target,
    		JsonExclusionStrategy jsonExclusionStrategy) {    
        return toJson(target, null, false, null, null,    
                false, null, jsonExclusionStrategy, null);    
    }    
    
    /**  
     * 将给定的目标对象转换成 {@code JSON} 格式的字符串。
     * <ul>  
     * <li>该方法可以给不同的转化方式给出不同的字段名</li>  
     * </ul>  
     *   
     * @param target  
     *            要转换成 {@code JSON} 的目标对象。  
     * @param jsonFieldNamingStrategy  
     *            制定一个转json时的字段名修改规则
     * @return 目标对象的 {@code JSON} 格式的字符串。  
     */    
    public static String toJson(Object target,
    		JsonFieldNamingStrategy jsonFieldNamingStrategy) {    
        return toJson(target, null, false, null, null,    
                false, null, null, jsonFieldNamingStrategy);    
    }    

    /**  
     * 将给定的目标对象转换成 {@code JSON} 格式的字符串。<strong>此方法只用来转换普通的 {@code JavaBean}  
     * 对象。</strong>  
     * <ul>  
     * <li>该方法不会转换 {@code null} 值字段；</li>  
     * <li>该方法转换时使用默认的 日期/时间 格式化模式 - {@code yyyy-MM-dd HH:mm:ss SSS}；</li>  
     * </ul>  
     *   
     * @param target  
     *            要转换成 {@code JSON} 的目标对象。  
     * @param version  
     *            字段的版本号注解({@literal @Since})。  
     * @param excludesFieldsWithoutExpose  
     *            是否排除未标注 {@literal @Expose} 注解的字段。  
     * @return 目标对象的 {@code JSON} 格式的字符串。  
     */    
    public static String toJson(Object target, Double version,    
            boolean excludesFieldsWithoutExpose) {    
        return toJson(target, null, false, version, null,    
                excludesFieldsWithoutExpose, null, null, null);    
    }    
    
    /**  
     * 将给定的目标对象转换成 {@code JSON} 格式的字符串。<strong>此方法通常用来转换使用泛型的对象。</strong>  
     * <ul>  
     * <li>该方法只会转换标有 {@literal @Expose} 注解的字段；</li>  
     * <li>该方法不会转换 {@code null} 值字段；</li>  
     * <li>该方法会转换所有未标注或已标注 {@literal @Since} 的字段；</li>  
     * <li>该方法转换时使用默认的 日期/时间 格式化模式 - {@code yyyy-MM-dd HH:mm:ss SSSS}；</li>  
     * </ul>  
     *   
     * @param target  
     *            要转换成 {@code JSON} 的目标对象。  
     * @param targetType  
     *            目标对象的类型。  
     * @return 目标对象的 {@code JSON} 格式的字符串。  
     */    
    public static String toJson(Object target, Type targetType) {    
        return toJson(target, targetType, false, null, null, false, null, null, null);    
    }    
    
    /**  
     * 将给定的目标对象转换成 {@code JSON} 格式的字符串。<strong>此方法通常用来转换使用泛型的对象。</strong>  
     * <ul>  
     * <li>该方法只会转换标有 {@literal @Expose} 注解的字段；</li>  
     * <li>该方法不会转换 {@code null} 值字段；</li>  
     * <li>该方法转换时使用默认的 日期/时间 格式化模式 - {@code yyyy-MM-dd HH:mm:ss SSSS}；</li>  
     * </ul>  
     *   
     * @param target  
     *            要转换成 {@code JSON} 的目标对象。  
     * @param targetType  
     *            目标对象的类型。  
     * @param version  
     *            字段的版本号注解({@literal @Since})。  
     * @return 目标对象的 {@code JSON} 格式的字符串。  
     */    
    public static String toJson(Object target, Type targetType, Double version) {    
        return toJson(target, targetType, false, version, null, false, null, null, null);    
    }    
    
    /**  
     * 将给定的目标对象转换成 {@code JSON} 格式的字符串。<strong>此方法通常用来转换使用泛型的对象。</strong>  
     * <ul>  
     * <li>该方法不会转换 {@code null} 值字段；</li>  
     * <li>该方法会转换所有未标注或已标注 {@literal @Since} 的字段；</li>  
     * <li>该方法转换时使用默认的 日期/时间 格式化模式 - {@code yyyy-MM-dd HH:mm:ss SSS}；</li>  
     * </ul>  
     *   
     * @param target  
     *            要转换成 {@code JSON} 的目标对象。  
     * @param targetType  
     *            目标对象的类型。  
     * @param excludesFieldsWithoutExpose  
     *            是否排除未标注 {@literal @Expose} 注解的字段。  
     * @return 目标对象的 {@code JSON} 格式的字符串。  
     */    
    public static String toJson(Object target, Type targetType,    
            boolean excludesFieldsWithoutExpose) {    
        return toJson(target, targetType, false, null, null,    
                excludesFieldsWithoutExpose, null, null, null);    
    }    
    
    /**  
     * 将给定的目标对象转换成 {@code JSON} 格式的字符串。<strong>此方法通常用来转换使用泛型的对象。</strong>  
     * <ul>  
     * <li>该方法不会转换 {@code null} 值字段；</li>  
     * <li>该方法会转换所有未标注或已标注 {@literal @Since} 的字段；</li>  
     * <li>该方法转换时使用默认的 日期/时间 格式化模式 - {@code yyyy-MM-dd HH:mm:ss SSS}；</li>  
     * </ul>  
     *   
     * @param target  
     *            要转换成 {@code JSON} 的目标对象。  
     * @param targetType  
     *            目标对象的类型。  
     * @param excludesFieldsWithoutExpose  
     *            是否排除未标注 {@literal @Expose} 注解的字段。  
     * @param namingConvention  
     *            对于没有使用@SerializedName注解的字段设置字段打印格式取值规则如下:
     * <ul>  
     * <li>FieldNamingPolicy.UPPER_CAMEL_CASE 首字母大写</li>  
     * <li>FieldNamingPolicy.UPPER_CAMEL_CASE_WITH_SPACES 首字母大写并将字段插分成一个个单词用空格格开</li>  
     * <li>FieldNamingPolicy.LOWER_CASE_WITH_DASHES 转小写并用-将各个单词格开</li>  
     * <li>FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES 转小写并用下划线格开</li>  
     * </ul>  
     */    
    public static String toJson(Object target, Type targetType,    
            boolean excludesFieldsWithoutExpose,FieldNamingPolicy namingConvention) {    
        return toJson(target, targetType, false, null, null,    
                excludesFieldsWithoutExpose, namingConvention, null, null);    
    }    
    
    /**  
     * 将给定的目标对象转换成 {@code JSON} 格式的字符串。<strong>此方法通常用来转换使用泛型的对象。</strong>  
     * <ul>  
     * <li>该方法不会转换 {@code null} 值字段；</li>  
     * <li>该方法转换时使用默认的 日期/时间 格式化模式 - {@code yyyy-MM-dd HH:mm:ss SSS}；</li>  
     * </ul>  
     *   
     * @param target  
     *            要转换成 {@code JSON} 的目标对象。  
     * @param targetType  
     *            目标对象的类型。  
     * @param version  
     *            字段的版本号注解({@literal @Since})。  
     * @param excludesFieldsWithoutExpose  
     *            是否排除未标注 {@literal @Expose} 注解的字段。  
     * @return 目标对象的 {@code JSON} 格式的字符串。  
     */    
    public static String toJson(Object target, Type targetType, Double version,    
            boolean excludesFieldsWithoutExpose) {    
        return toJson(target, targetType, false, version, null,    
                excludesFieldsWithoutExpose, null, null, null);    
    }
    
    /**  
     * 底层方法
     * 1.如果传入一个GsonBuilder则用它创建一个新的gson对象实现回转;
     * 2.如果不传builder则用静态成员gson实现回转。
     * @param <T>  
     *            要转换的目标类型。  
     * @param json  
     *            给定的 {@code JSON} 字符串。  
     * @param clazz  
     *            要转换的目标类。  
     * @param builder  
     *            转换规则的承载器。  
     * @return 给定的 {@code JSON} 字符串表示的指定的类型对象。  
     */    
    public static <T> T fromJson(String json, Class<T> clazz, GsonBuilder builder) {    
        if (isEmpty(json)) {    
            return null;    
        }    
        if (builder != null) {    
        	Gson newGson = builder.create();    
            try {    
                return newGson.fromJson(json, clazz);    
            } catch (Exception ex) {    
                log.error(json + " 无法转换为 " + clazz.getName() + " 对象!", ex);    
                return null;    
            }    
        }else{
        	try {    
        		return gson.fromJson(json, clazz);    
        	} catch (Exception ex) {    
        		log.error(json + " 无法转换为 " + clazz.getName() + " 对象!", ex);    
        		return null;    
        	}
        }
    }

    /**  
     * 底层方法
     * 1.如果传入一个GsonBuilder则用它创建一个新的gson对象实现回转;
     * 2.如果不传builder则用静态成员gson实现回转。
     * 
     * @param <T>  
     *            要转换的目标类型。  
     * @param json  
     *            给定的 {@code JSON} 字符串。  
     * @param token  
     *            {@code com.google.gson.reflect.TypeToken} 的类型指示类对象。  
     * @param builder  
     *            转换规则的承载器。  
     * @return 给定的 {@code JSON} 字符串表示的指定的类型对象。  
     */    
    public static <T> T fromJson(String json, TypeToken<T> token,    
    		GsonBuilder builder) {    
        if (isEmpty(json)) {    
            return null;    
        }
        if (builder != null) {    
        	Gson newGson = builder.create();
            try {    
                return newGson.fromJson(json, token.getType());    
            } catch (Exception ex) {    
                log.error(json + " 无法转换为 " + token.getRawType().getName() + " 对象!",    
                        ex);    
                return null;    
            }    
        }else{
        	try {    
        		return gson.fromJson(json, token.getType());    
        	} catch (Exception ex) {    
        		log.error(json + " 无法转换为 " + token.getRawType().getName() + " 对象!",    
        				ex);    
        		return null;    
        	}    
        }
    }    

    /**  
     * 将给定的 {@code JSON} 字符串转换成指定的类型对象。  
     *   
     * @param <T>  
     *            要转换的目标类型。  
     * @param json  
     *            给定的 {@code JSON} 字符串。  
     * @param token  
     *            {@code com.google.gson.reflect.TypeToken} 的类型指示类对象。  
     * @return 给定的 {@code JSON} 字符串表示的指定的类型对象。  
     */    
    public static <T> T fromJson(String json, TypeToken<T> token) {    
        return fromJson(json, token, null);    
    }    
    
    /**  
     * 将给定的 {@code JSON} 字符串转换成指定的类型对象。<strong>此方法通常用来转换普通的 {@code JavaBean}  
     * 对象。</strong>  
     *   
     * @param <T>  
     *            要转换的目标类型。  
     * @param json  
     *            给定的 {@code JSON} 字符串。  
     * @param clazz  
     *            要转换的目标类。  
     * @return 给定的 {@code JSON} 字符串表示的指定的类型对象。  
     */    
    public static <T> T fromJson(String json, Class<T> clazz) {    
        return fromJson(json, clazz, null);    
    }    
    
    /**  
     * 将给定的 {@code JSON} 字符串转换成指定的类型对象。<strong>此方法通常用来转换普通的 {@code JavaBean}  
     * 对象。</strong>  
     *   
     * @param <T>  
     *            要转换的目标类型。  
     * @param json  
     *            给定的 {@code JSON} 字符串。  
     * @param clazz  
     *            要转换的目标类。  
     * @param jsonFieldNamingStrategy
     *            字段名更改策略。  
     *            
     * @return 给定的 {@code JSON} 字符串表示的指定的类型对象。  
     */    
    public static <T> T fromJson(String json, Class<T> clazz,
    		JsonFieldNamingStrategy jsonFieldNamingStrategy,
    		JsonExclusionStrategy jsonExclusionStrategy) {
        GsonBuilder builder = null;
        if (jsonFieldNamingStrategy != null || jsonExclusionStrategy != null){
        	builder = new GsonBuilder();
        	if (jsonFieldNamingStrategy != null){
        		builder.setFieldNamingStrategy(jsonFieldNamingStrategy);
        	}
        	if (jsonExclusionStrategy != null){
        		builder.setExclusionStrategies(jsonExclusionStrategy);
        	}
        }
        
        return fromJson(json, clazz, builder);    
    }    
    
    public static boolean isEmpty(String inStr) {    
        boolean reTag = false;    
        if (inStr == null || "".equals(inStr)) {    
            reTag = true;    
        }    
        return reTag;    
    }    
}    

//    public static JsonConfig instanceJsonConfig()
//    {
//        JsonConfig config = new JsonConfig();
//        config.registerJsonValueProcessor(java.util.Date.class, new JsonValueProcessor()
//        {
//
//            @Override
//            public Object processObjectValue(String key, Object value, JsonConfig config)
//            {
//                if(value instanceof Date)
//                {
//                    String str = new SimpleDateFormat(timestampFormat).format((Date) value);
//                    return str;
//                }
//                return value;
//            }
//
//            @Override
//            public Object processArrayValue(Object value, JsonConfig config)
//            {
//
//                String[] obj = {};
//                if(value instanceof Date[])
//                {
//                    SimpleDateFormat sf = new SimpleDateFormat(timestampFormat);
//                    Date[] dates = (Date[]) value;
//                    obj = new String[dates.length];
//                    for(int i = 0; i < dates.length; i++)
//                    {
//                        obj[i] = sf.format(dates[i]);
//                    }
//                }
//                return obj;
//            }
//        });
//        return config;
//    }

   

//   
//
//    /** */
//    /**
//    * 从一个JSON 对象字符格式中得到一个java对象
//    * @param jsonString
//    * @param pojoCalss
//    * @return
//    */
//    public static Object getObjectJsonString(String jsonString, Class<?> pojoCalss)
//    {
//        Object pojo;
//        JSONObject jsonObject = JSONObject.fromObject(jsonString);
//        pojo = JSONObject.toBean(jsonObject, pojoCalss);
//        return pojo;
//    }
//
//    /** */
//    /**
//    * 从json HASH表达式中获取一个map，改map支持嵌套功能
//    * @param jsonString
//    * @return
//    */
//
//    public static Map<String, Object> getMap4Json(String jsonString)
//    {
//        JSONObject jsonObject = JSONObject.fromObject(jsonString);
//        Iterator<?> keyIter = jsonObject.keys();
//        String key;
//        Object value;
//        Map<String, Object> valueMap = new HashMap<String, Object>();
//
//        while(keyIter.hasNext())
//        {
//            key = (String) keyIter.next();
//            value = jsonObject.get(key);
//            valueMap.put(key, value);
//        }
//
//        return valueMap;
//    }
//
//    /** */
//    /**
//    * 从json数组中得到相应java数组
//    * @param jsonString
//    * @return
//    */
//    public static Object[] getObjectArray4Json(String jsonString)
//    {
//        JSONArray jsonArray = JSONArray.fromObject(jsonString);
//        return jsonArray.toArray();
//
//    }
//
//    /** */
//    /**
//    * 从json对象集合表达式中得到一个java对象列表
//    * @param jsonString
//    * @param pojoClass
//    * @return
//    */
//    public static List<?> getListJson(String jsonString, Class<?> pojoClass)
//    {
//
//        JSONArray jsonArray = JSONArray.fromObject(jsonString);
//        JSONObject jsonObject;
//        Object pojoValue;
//        List<Object> list = new ArrayList<Object>();
//        for(int i = 0; i < jsonArray.size(); i++)
//        {
//
//            jsonObject = jsonArray.getJSONObject(i);
//            pojoValue = JSONObject.toBean(jsonObject, pojoClass);
//            list.add(pojoValue);
//
//        }
//        return list;
//
//    }
//
//    /** */
//    /**
//    * 从json数组中解析出java字符串数组
//    * @param jsonString
//    * @return
//    */
//    public static String[] getStringArray4Json(String jsonString)
//    {
//
//        JSONArray jsonArray = JSONArray.fromObject(jsonString);
//        String[] stringArray = new String[jsonArray.size()];
//        for(int i = 0; i < jsonArray.size(); i++)
//        {
//            stringArray[i] = jsonArray.getString(i);
//
//        }
//
//        return stringArray;
//    }
//
//    /** */
//    /**
//    * 从json数组中解析出javaLong型对象数组
//    * @param jsonString
//    * @return
//    */
//    public static Long[] getLongArray4Json(String jsonString)
//    {
//
//        JSONArray jsonArray = JSONArray.fromObject(jsonString);
//        Long[] longArray = new Long[jsonArray.size()];
//        for(int i = 0; i < jsonArray.size(); i++)
//        {
//            longArray[i] = jsonArray.getLong(i);
//
//        }
//        return longArray;
//    }
//
//    /** */
//    /**
//    * 从json数组中解析出java Integer型对象数组
//    * @param jsonString
//    * @return
//    */
//    public static Integer[] getIntegerArray4Json(String jsonString)
//    {
//
//        JSONArray jsonArray = JSONArray.fromObject(jsonString);
//        Integer[] integerArray = new Integer[jsonArray.size()];
//        for(int i = 0; i < jsonArray.size(); i++)
//        {
//            integerArray[i] = jsonArray.getInt(i);
//
//        }
//        return integerArray;
//    }
//
//    /** */
//    /**
//    * 从json数组中解析出java Integer型对象数组
//    * @param jsonString
//    * @return
//    */
//    public static Double[] getDoubleArray4Json(String jsonString)
//    {
//
//        JSONArray jsonArray = JSONArray.fromObject(jsonString);
//        Double[] doubleArray = new Double[jsonArray.size()];
//        for(int i = 0; i < jsonArray.size(); i++)
//        {
//            doubleArray[i] = jsonArray.getDouble(i);
//
//        }
//        return doubleArray;
//    }
//
//    /** */
//    /**
//    * 将java对象转换成json字符串
//    * @param javaObj
//    * @return
//    */
//    public static String getJsonString4JavaPOJO(Object javaObj)
//    {
//
//        JSONObject json;
//        json = JSONObject.fromObject(javaObj);
//        return json.toString();
//
//    }


