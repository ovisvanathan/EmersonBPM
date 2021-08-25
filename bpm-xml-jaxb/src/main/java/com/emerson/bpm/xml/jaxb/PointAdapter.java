package com.emerson.bpm.xml.jaxb;

import java.awt.Point;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class PointAdapter extends XmlAdapter<String, Point> {

    @Override
    public Point unmarshal(String v) throws Exception {
        String[] coords = v.split(",");
        return new Point(Integer.parseInt(coords[0]), Integer.parseInt(coords[1]));
    }

    @Override
    public String marshal(Point v) throws Exception {
        return String.format("%d,%d", v.x, v.y);
    }
}