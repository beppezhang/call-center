package bz.sunlight.util;





import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesUtil {
	private static String fileName;

	private Properties p;

	private InputStream in = null;

	private FileOutputStream out;

	private static PropertiesUtil instanse = new PropertiesUtil();
	private String configFile = null;

	private PropertiesUtil() {
	}

	public synchronized static PropertiesUtil getInstanse() {
		return instanse;
	}

	public String getStringByProperties(String key, String properties) {

		String value = null;

		String path = null;
		
		in = this.getClass().getResourceAsStream("/server.properties");
//		path = Thread.currentThread().getContextClassLoader().getResource("")
//				.getPath();

		try {
			in = new FileInputStream(path + properties);
			p = new Properties();

			// 载入文件

			p.load(in);

			value = p.getProperty(key);

		}

		catch (FileNotFoundException e) {

			System.err.println("配置文件" + properties + "找不到！！");

			e.printStackTrace();

		}

		catch (Exception e) {

			System.err.println("读取配置文件" + properties + "错误！！");

			e.printStackTrace();

		} finally {
			try {
				in.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return value;

	}

	public String getString(String key) {

		String value = null;

		String path = null;
//		 in =
//		 Thread.currentThread().getContextClassLoader().getResourceAsStream("server.properties");
//		path = Thread.currentThread().getContextClassLoader().getResource("")
//				.getPath();
		in = this.getClass().getResourceAsStream("/server.properties");
		System.out.println(path);

		try {

//			in = new FileInputStream(path+"server.properties");
			p = new Properties();

			// 载入文件

			p.load(in);

			value = p.getProperty(key);

		}

		catch (FileNotFoundException e) {

			System.err.println("配置文件server.properties找不到！！");

			e.printStackTrace();

		}

		catch (Exception e) {

			System.err.println("读取配置文件server.properties错误！！");

			e.printStackTrace();

		} finally {
			try {
				in.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return value;

	}

	public static void main(String[] args) {
//		System.out.println(PropertiesUtil.getInstanse().getString("dmc.url"));
		String server=getInstanse().getString("server.url");
		System.out.println(server);
	}
}
