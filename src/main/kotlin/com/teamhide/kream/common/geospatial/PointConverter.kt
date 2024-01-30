package com.teamhide.kream.common.geospatial

import org.locationtech.jts.geom.Point
import org.locationtech.jts.io.WKTReader

class PointConverter private constructor() {
    companion object {
        fun from(lat: Double, lng: Double): Point {
            return WKTReader().read("POINT ($lat $lng)") as Point
        }
    }
}
