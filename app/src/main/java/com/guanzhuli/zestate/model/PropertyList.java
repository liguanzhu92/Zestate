package com.guanzhuli.zestate.model;

import java.util.ArrayList;

/**
 * Created by Guanzhu Li on 1/22/2017.
 */

public class PropertyList extends ArrayList<Property> {
    private static PropertyList mInstance = new PropertyList();

    public static PropertyList getInstance() {
        return mInstance;
    }

    private PropertyList() {
    }
}
