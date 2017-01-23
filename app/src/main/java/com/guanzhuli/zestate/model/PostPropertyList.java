package com.guanzhuli.zestate.model;

import java.util.ArrayList;

/**
 * Created by Guanzhu Li on 1/22/2017.
 */

public class PostPropertyList extends ArrayList<Property> {
    private static PostPropertyList mInstance = new PostPropertyList();

    public static PostPropertyList getInstance() {
        return mInstance;
    }

    private PostPropertyList() {
    }
}
