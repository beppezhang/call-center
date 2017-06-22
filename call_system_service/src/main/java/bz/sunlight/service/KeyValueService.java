package bz.sunlight.service;

import java.util.List;
import java.util.Map;

import bz.sunlight.entity.KeyValueCategory;
import bz.sunlight.entity.KeyValueItem;

public interface KeyValueService {

	List<KeyValueCategory> getKeyValueCategory(short isAdminBuild,short categoryStatus,short itemStatus);
	
	void insert(KeyValueItem keyValueItem);
	
	void update(KeyValueItem keyValueItem);
	
	List<KeyValueCategory> getKeyValueCategory(short isAdminBuild,short categoryStatus);
	
	KeyValueItem getKeyValueItem(String id);
	
	void deleteKeyValueItem(KeyValueItem keyValueItem);
	
	List<KeyValueCategory> getKeyValueItemByCode(String categoryCode);
	
	List<KeyValueItem>  conditionSearch(Map map);
	
	int selectMaxCode(Map map);
	
	KeyValueItem getKeyValueItem(String name, short status,String keyValueCategotyId);
	
	String getKeyValueItemByCode(Short code,String categoryCode);
	
	
}
