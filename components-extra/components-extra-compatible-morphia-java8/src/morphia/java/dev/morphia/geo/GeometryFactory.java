package dev.morphia.geo;

import java.util.List;

@SuppressWarnings("removal")
@Deprecated
interface GeometryFactory {
    Geometry createGeometry(List<?> geometries);
}
