package bz.sunlight.controller;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.w3c.dom.ranges.RangeException;

import bz.sulight.common.BaseConstant;
import bz.sunlight.entity.KeyValueCategory;
import bz.sunlight.entity.KeyValueItem;
import bz.sunlight.exception.RepeatException;
import bz.sunlight.service.KeyValueService;
import bz.sunlight.util.BeanUtil;
import bz.sunlight.util.EncryptUtil;
import bz.sunlight.util.UUIDUtil;
import bz.sunlight.vo.KeyValueVo;

@Controller
@RequestMapping("/keyValue/")
public class KeyValueController {

	@Autowired
	private KeyValueService keyValueService;
	
//	加载字典项可以被创建的内容
	@RequestMapping(value="keyValue",method=RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<List<KeyValueCategory>> selectBuild(){
		List<KeyValueCategory> keyValueCategorys = keyValueService.getKeyValueCategory(BaseConstant.IS_ADMIN_BUILD_TURE, 
				BaseConstant.BASE_TRUE, BaseConstant.BASE_TRUE);
		if(keyValueCategorys!=null&&keyValueCategorys.size()>0){
			return new ResponseEntity<List<KeyValueCategory>>(keyValueCategorys, HttpStatus.OK);
		}
		
		return new ResponseEntity<List<KeyValueCategory>>( HttpStatus.NO_CONTENT);

	}
	
//  新增字典项
	@RequestMapping(value="keyValue",method=RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Void> addtion(@RequestBody KeyValueVo keyValueVo){
		//如果该字典项的中文名称存在则提醒重复
		KeyValueItem keyValueItem=keyValueService.getKeyValueItem(keyValueVo.getName(), BaseConstant.BASE_TRUE,keyValueVo.getKeyValueCategotyId());
		if(keyValueItem!=null){
			//抛出自定义异常
			return new ResponseEntity<Void>(HttpStatus.CONFLICT);
		}
		keyValueItem=new KeyValueItem();
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("keyValueCategotyId", keyValueVo.getKeyValueCategotyId());
		int code=keyValueService.selectMaxCode(map);
		BeanUtil.copyProperties(keyValueItem, keyValueVo);
		keyValueItem.setId(UUIDUtil.getOrigUUID());
		keyValueItem.setCode((short)(code+1));
		keyValueItem.setStatus(BaseConstant.BASE_TRUE);
		keyValueItem.setCreateTime(new Date());
		keyValueService.insert(keyValueItem);
		return new ResponseEntity<Void>(HttpStatus.CREATED);
	}
	
//	更新字典项
	@RequestMapping(value="keyValue",method=RequestMethod.PUT)
	@ResponseBody
	public ResponseEntity<Void> edition(@RequestBody KeyValueVo keyValueVo){
		KeyValueItem keyValueItem=new KeyValueItem();
		BeanUtil.copyProperties(keyValueItem, keyValueVo);
		keyValueService.update(keyValueItem);
		return new ResponseEntity<Void>(HttpStatus.OK);
	}
	
//	查找可添加的类别
	@RequestMapping(value="keyValueCategory",method=RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<List<KeyValueCategory>> selection(){
	    List<KeyValueCategory> keyValueCategorys = keyValueService.getKeyValueCategory(BaseConstant.IS_ADMIN_BUILD_TURE, BaseConstant.BASE_TRUE);
	    if(keyValueCategorys!=null&&keyValueCategorys.size()>0){
	    	return new ResponseEntity<List<KeyValueCategory>>(keyValueCategorys, HttpStatus.OK);
	    }
	    return new ResponseEntity<List<KeyValueCategory>>(HttpStatus.NO_CONTENT);
	}
	
//	删除字典项
	@RequestMapping(value="keyValue/{id}",method=RequestMethod.PUT)
	public ResponseEntity<Void> deletion(@PathVariable String id){
		KeyValueItem keyValueItem = keyValueService.getKeyValueItem(id);
		if(keyValueItem!=null){
			keyValueItem.setStatus(BaseConstant.BASE_FALSE);
			keyValueService.deleteKeyValueItem(keyValueItem);
			return new ResponseEntity<Void>(HttpStatus.OK);
		}
		return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
	}
	
//	根据字典项类别查出字典项内容  根据下划线区分每个类别
	@RequestMapping(value="items/{categoryCode}",method=RequestMethod.GET)
	@ResponseBody
	public List<KeyValueCategory> getByCategory(@PathVariable String categoryCode){
		return keyValueService.getKeyValueItemByCode(categoryCode);
		
		
	}
//	多条件搜索测试
	@RequestMapping(value="conditon",method=RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<List<KeyValueItem>> conditon(String categoryId,String itemName){
		try {
			
			if(itemName!=null){
				itemName=new String(itemName.getBytes("iso-8859-1"), "utf-8");
			}
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("categoryId", categoryId);
			map.put("itemName", itemName);
			map.put("categoryStatus", BaseConstant.BASE_TRUE);
			map.put("itemStatus", BaseConstant.BASE_TRUE);
			List<KeyValueItem> keyValueItems = keyValueService.conditionSearch(map);
			if(keyValueItems!=null&&keyValueItems.size()>0){
				return new ResponseEntity<List<KeyValueItem>>(keyValueItems,HttpStatus.OK);
			}
			return new ResponseEntity<List<KeyValueItem>>(HttpStatus.NO_CONTENT);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return new ResponseEntity<List<KeyValueItem>>(HttpStatus.NOT_FOUND);
	}
	
	
	
}
