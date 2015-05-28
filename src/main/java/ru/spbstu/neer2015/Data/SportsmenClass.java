package ru.spbstu.neer2015.data;

/**
 * Created by tseyler on 28.05.15.
 */
public enum SportsmenClass {
    FIRST(0, 4, 1){
        @Override
        public int getSportsmenClass(){
            return 1;
        }
        @Override
        public String getText(){
            return "с 1 по 3";
        }
    },
    SECOND(4, 8, 2){
        @Override
        public int getSportsmenClass(){
            return 2;
        }
        @Override
        public String getText(){
            return "с 4 по 8";
        }
    },
    THIRD(8, 16, 3){
        @Override
        public int getSportsmenClass(){
            return 3;
        }
        @Override
        public String getText(){
            return "с 8 по 16";
        }
    },
    FOURTH(16, 32, 4){
        @Override
        public int getSportsmenClass(){
            return 4;
        }
        @Override
        public String getText(){
            return "с 16 по 32";
        }
    },
    FIFTH(32, Integer.MAX_VALUE, 5){
        @Override
        public int getSportsmenClass(){
            return 5;
        }
        @Override
        public String getText(){
            return "выше";
        }
    };
    private int low, high, clazz;
    private SportsmenClass(int low, int high, int clazz){
        this.low = low;
        this.high = high;
        this.clazz = clazz;
    }
    public static SportsmenClass getClass(double place){
        for (SportsmenClass sportsmenClass : SportsmenClass.values()){
            if (place<=sportsmenClass.high && place>sportsmenClass.low){
                return sportsmenClass;
            }
        }
        return null;
    }
    public static SportsmenClass getInstanceByClass(int clazz){
        for (SportsmenClass sportsmenClass : SportsmenClass.values()){
            if (clazz == sportsmenClass.clazz){
                return sportsmenClass;
            }
        }
        return null;
    }
    public abstract int getSportsmenClass();
    public abstract String getText();
}
