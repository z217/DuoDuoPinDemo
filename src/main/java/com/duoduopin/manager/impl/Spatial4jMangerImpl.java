package com.duoduopin.manager.impl;

import com.duoduopin.manager.Spatial4jManager;
import com.spatial4j.core.context.SpatialContext;
import com.spatial4j.core.distance.DistanceUtils;
import com.spatial4j.core.io.GeohashUtils;
import org.springframework.stereotype.Component;

/**
 * Spatial4j管理实现类
 *
 * @author z217
 * @date 2020/08/13
 * @see com.duoduopin.manager.Spatial4jManager
 */
@Component
public class Spatial4jMangerImpl implements Spatial4jManager {
  private static SpatialContext geo = SpatialContext.GEO;

  @Override
  public String getGeohash(double longitude, double latitude) {
    return GeohashUtils.encodeLatLon(latitude, longitude);
  }

  @Override
  public double getDistance(double lon1, double lat1, double lon2, double lat2) {
    return geo.calcDistance(geo.makePoint(lon1, lat1), geo.makePoint(lon2, lat2))
        * DistanceUtils.DEG_TO_KM;
  }
}
