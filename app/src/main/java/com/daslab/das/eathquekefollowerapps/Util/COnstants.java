package com.daslab.das.eathquekefollowerapps.Util;

import java.lang.ref.PhantomReference;
import java.util.Random;

/**
 * Created by User on 12/18/2017.
 */

public class COnstants {
   public static String URL="https://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/all_week.geojson";
   public static final int LIMIT=50;

   public static int randomInt(int max, int  min)
   {
      return new Random().nextInt(max - min) + min;
   }


}
