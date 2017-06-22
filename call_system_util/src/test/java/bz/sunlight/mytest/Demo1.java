package bz.sunlight.mytest;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.junit.Test;

public class Demo1 {

	public static void main(String[] args) {
		StudentVo vo=new StudentVo();
		vo.setId("111111");
		vo.setName("beppe");
		vo.setCity("shagnhai");
		Student s=new Student();
		try {
			BeanUtils.copyProperties(s, vo);
			System.out.println(s.getId());
			System.out.println(s.getName());
			System.out.println(s.getCode());
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void demo1(){
//		遍历二维数组
		List<String> list1=new ArrayList<String>();
		List<City> list2=new ArrayList<City>();
		list1.add("id");
		list1.add("name");
		list1.add("code");
		list2.add(new City("1", "shanghai", "001"));
		list2.add(new City("2", "beijing", "002"));
//		list2.add(new City("3", "guangzhou", "003"));
		
			for (int j = 0; j < list2.size(); j++) {
				System.out.println(list2.get(j).getId());
				System.out.println(list2.get(j).getName());
				System.out.println(list2.get(j).getCode());
			}
		
	}
}
