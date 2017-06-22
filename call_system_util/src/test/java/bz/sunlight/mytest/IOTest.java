package bz.sunlight.mytest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class IOTest {

	public static void main(String[] args) throws IOException {
		File file = new File("D:/temp/IO/03.txt");
		FileOutputStream fileOutputStream = new FileOutputStream(file);
//		FileUtils.writeStringToFile(file, "beppe is a good guy", true);
//		FileInputStream fileInputStream = new FileInputStream(file);
//		String readFileToString = FileUtils.readFileToString(file);
//		System.out.println(readFileToString);
	}
}
