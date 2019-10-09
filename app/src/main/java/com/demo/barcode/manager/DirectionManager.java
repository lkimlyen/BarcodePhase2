package com.demo.barcode.manager;

import com.demo.barcode.R;
import com.demo.barcode.app.CoreApplication;

import java.util.ArrayList;

public class DirectionManager {
    private ArrayList<Direction> directions;

    private static DirectionManager instance;

    public static DirectionManager getInstance() {
        if (instance == null) {
            instance = new DirectionManager();
        }
        return instance;
    }

    public ArrayList<Direction> getListType() {
        directions = new ArrayList<>();
        directions.add(new Direction(CoreApplication.getInstance().getString(R.string.text_no), 0));
        directions.add(new Direction(CoreApplication.getInstance().getString(R.string.text_left), 1));
        directions.add(new Direction(CoreApplication.getInstance().getString(R.string.text_right), 2));
        return directions;
    }

    public int getValueByPositon(int position) {
        Direction typeSO = directions.get(position);
        return typeSO.getValue();
    }

    public class Direction {
        private String name;
        private int value;

        public Direction(String name, int value) {
            this.name = name;
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public int getValue() {
            return value;
        }

        @Override
        public String toString() {
            return name;
        }
    }
}
