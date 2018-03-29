package com.gsralex.gdata.query;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author gsralex
 * @version 2018/3/22
 */
public class Queryable {

    public  static class dentity{
        private int d1;
        private int d2;

        public int getD1() {
            return d1;
        }

        public int getD2() {
            return d2;
        }
    }

    public static void d(){
        List<dentity> list=new ArrayList<>();
        List<Integer> names= list.stream().map(dentity::getD1).collect(Collectors.toList());
    }
}
