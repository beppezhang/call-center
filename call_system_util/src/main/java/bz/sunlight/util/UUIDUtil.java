package bz.sunlight.util;

public class UUIDUtil {
    /**
     *  生成32位的
     * @date 2016年7月3日
     * @author sxc
     * @return
     * @since JDK 1.6
     * @Description
     */
    public static String  getUUID(){  
        return  java.util.UUID.randomUUID().toString().replaceAll("-", "");  
    }  

    /**
     *  生成36位的
     * @date 2016年7月3日
     * @author sxc
     * @return
     * @since JDK 1.6
     * @Description
     */
    public static String  getOrigUUID(){  
        return  java.util.UUID.randomUUID().toString();  
    }  
    public static void main(String[] args) {
    	for (int i = 0; i < 10; i++) {
    		System.out.println(UUIDUtil.getOrigUUID());	
		}
		
	}
}
