package bz.sunlight.dao;

import bz.sunlight.entity.KeyValueCategory;
import bz.sunlight.entity.KeyValueCategoryExample;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface KeyValueCategoryMapper {

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table key_value_category
	 * @mbggenerated
	 */
	int countByExample(KeyValueCategoryExample example);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table key_value_category
	 * @mbggenerated
	 */
	int deleteByExample(KeyValueCategoryExample example);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table key_value_category
	 * @mbggenerated
	 */
	int deleteByPrimaryKey(String id);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table key_value_category
	 * @mbggenerated
	 */
	int insert(KeyValueCategory record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table key_value_category
	 * @mbggenerated
	 */
	int insertSelective(KeyValueCategory record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table key_value_category
	 * @mbggenerated
	 */
	List<KeyValueCategory> selectByExample(KeyValueCategoryExample example);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table key_value_category
	 * @mbggenerated
	 */
	KeyValueCategory selectByPrimaryKey(String id);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table key_value_category
	 * @mbggenerated
	 */
	int updateByExampleSelective(@Param("record") KeyValueCategory record,
			@Param("example") KeyValueCategoryExample example);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table key_value_category
	 * @mbggenerated
	 */
	int updateByExample(@Param("record") KeyValueCategory record,
			@Param("example") KeyValueCategoryExample example);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table key_value_category
	 * @mbggenerated
	 */
	int updateByPrimaryKeySelective(KeyValueCategory record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table key_value_category
	 * @mbggenerated
	 */
	int updateByPrimaryKey(KeyValueCategory record);

	List<KeyValueCategory> getAllKeyValueCategory(short isAdminBuild,short categoryStatus,short itemStatus);

	KeyValueCategory getItemByCategoryCode(Map map);
}