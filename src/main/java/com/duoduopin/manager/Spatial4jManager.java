package com.duoduopin.manager;

/**
 * Spatial4j管理类
 *
 * @author z217
 * @date 2020/08/13
 * @see com.duoduopin.manager.impl.Spatial4jMangerImpl
 */
public interface Spatial4jManager {
  /**
   * 通过Spatial4j获取geohash
   *
   * @param longitude 经度
   * @param latitude  纬度
   * @return geohash串
   */
  public String getGeohash(double longitude, double latitude);
  
  /**
   * 获取坐标间距离，单位km
   *
   * @param lon1 经度1
   * @param lat1 纬度1
   * @param lon2 经度2
   * @param lat2 纬度2
   * @return 距离
   */
  public double getDistance(double lon1, double lat1, double lon2, double lat2);
}
