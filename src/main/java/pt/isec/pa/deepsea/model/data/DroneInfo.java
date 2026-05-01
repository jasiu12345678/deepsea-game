package pt.isec.pa.deepsea.model.data;

import java.util.Comparator;

public record DroneInfo(int id, int hp, int maxHp, int fuel, int maxFuel) {
    public static class SortByFuel implements Comparator<DroneInfo> {
        @Override
        public int compare(DroneInfo a, DroneInfo b) {
            return Integer.compare(b.fuel(), a.fuel());
        }
    }

    public static class SortByHullIntegrity implements Comparator<DroneInfo> {
        @Override
        public int compare(DroneInfo a, DroneInfo b) {
            return Integer.compare(b.hp(), a.hp());
        }
    }
}