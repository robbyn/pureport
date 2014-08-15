package org.librebiz.pureport.definition;

import java.util.ArrayList;
import java.util.List;

public class BoxContainer extends Box {
    private final List<Box> boxes = new ArrayList<Box>();

    public int getBoxCount() {
        return boxes.size();
    }

    public Box getBox(int i) {
        return boxes.get(i);
    }

    public Box[] getBoxes() {
        return boxes.toArray(new Box[boxes.size()]);
    }

    public void addBox(Box box) {
        boxes.add(box);
    }
}
