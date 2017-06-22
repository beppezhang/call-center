package bz.sunlight.dao;

import bz.sunlight.entity.CustomerAd;
import bz.sunlight.entity.CustomerAdExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface CustomerAdMapper {

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table customer_ad
	 * @mbggenerated
	 */
	int countByExample(CustomerAdExample example);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table customer_ad
	 * @mbggenerated
	 */
	int deleteByExample(CustomerAdExample example);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table customer_ad
	 * @mbggenerated
	 */
	int deleteByPrimaryKey(String id);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table customer_ad
	 * @mbggenerated
	 */
	int insert(CustomerAd record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table customer_ad
	 * @mbggenerated
	 */
	int insertSelective(CustomerAd record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table customer_ad
	 * @mbggenerated
	 */
	List<CustomerAd> selectByExample(CustomerAdExample example);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table customer_ad
	 * @mbggenerated
	 */
	CustomerAd selectByPrimaryKey(String id);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table customer_ad
	 * @mbggenerated
	 */
	int updateByExampleSelective(@Param("record") CustomerAd record,
			@Param("example") CustomerAdExample example);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table customer_ad
	 * @mbggenerated
	 */
	int updateByExample(@Param("record") CustomerAd record,
			@Param("example") CustomerAdExample example);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table customer_ad
	 * @mbggenerated
	 */
	int updateByPrimaryKeySelective(CustomerAd record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table customer_ad
	 * @mbggenerated
	 */
	int updateByPrimaryKey(CustomerAd record);
}