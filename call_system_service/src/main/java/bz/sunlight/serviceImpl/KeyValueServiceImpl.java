package bz.sunlight.serviceImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import bz.sulight.common.BaseConstant;
import bz.sunlight.dao.KeyValueCategoryMapper;
import bz.sunlight.dao.KeyValueItemMapper;
import bz.sunlight.entity.KeyValueCategory;
import bz.sunlight.entity.KeyValueCategoryExample;
import bz.sunlight.entity.KeyValueItem;
import bz.sunlight.entity.KeyValueItemExample;
import bz.sunlight.service.KeyValueService;

@Service("keyValueService")
public class KeyValueServiceImpl implements KeyValueService{

	
	@Resource
	private KeyValueCategoryMapper keyValueCategoryMapper;
	@Resource
	private KeyValueItemMapper keyValueItemMapper;
	@Override
	public List<KeyValueCategory> getKeyValueCategory(short isAdminBuild,short categoryStatus,short itemStatus) {
		return keyValueCategoryMapper.getAllKeyValueCategory(isAdminBuild, categoryStatus, itemStatus);
	}
	@Override
	public void insert(KeyValueItem keyValueItem) {
		keyValueItemMapper.insert(keyValueItem);
		
	}
	@Override
	public void update(KeyValueItem keyValueItem) {
		keyValueItemMapper.updateByPrimaryKeySelective(keyValueItem);
		
	}
	@Override
	public List<KeyValueCategory> getKeyValueCategory(short isAdminBuild,
			short categoryStatus) {
		KeyValueCategoryExample example=new KeyValueCategoryExample();
		example.createCriteria().andIsAdminBuildEqualTo(isAdminBuild).andStatusEqualTo(categoryStatus);
		return keyValueCategoryMapper.selectByExample(example);
	}
	@Override
	public void deleteKeyValueItem(KeyValueItem keyValueItem) {
		keyValueItemMapper.updateByPrimaryKeySelective(keyValueItem);
		
	}
	@Override
	public KeyValueItem getKeyValueItem(String id) {
		return keyValueItemMapper.selectByPrimaryKey(id);
		
	}
	
	@Override
	public List<KeyValueCategory> getKeyValueItemByCode(String categoryCode) {
		List<KeyValueCategory> list = new ArrayList<KeyValueCategory>();
		String[] strs = categoryCode.split("_");
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("categoryStatus", BaseConstant.BASE_TRUE);
		for (String code : strs) {
			map.put("code", code);
			KeyValueCategory keyValueCategory = keyValueCategoryMapper.getItemByCategoryCode(map);
			list.add(keyValueCategory);
		}
		return list;
	}
	
	@Override
	public List<KeyValueItem> conditionSearch(Map map) {
		return keyValueItemMapper.selectByCondition(map);
	}
	@Override
	public int selectMaxCode(Map map) {
		return keyValueItemMapper.selectMaxCode(map);
		
	}
	@Override
	public KeyValueItem getKeyValueItem(String name, short status,String keyValueCategotyId) {
		KeyValueItemExample example = new KeyValueItemExample();
		example.createCriteria().andNameEqualTo(name).andStatusEqualTo(status).andKeyValueCategotyIdEqualTo(keyValueCategotyId);
		List<KeyValueItem> items = keyValueItemMapper.selectByExample(example);
		if(items!=null&&items.size()>0){
			return items.get(0);
		}
		return null;
	}
	@Override
	public String getKeyValueItemByCode(Short itemCode, String categoryCode) {
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("itemCode", itemCode);
		map.put("categoryCode", categoryCode);
		String str = keyValueItemMapper.getKeyValueItemByCode(map);
		if(str==null){
			return "";
		}
		return str;
	}

}
