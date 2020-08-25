package com.dean.proxy.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.common.collect.Lists;

public class Test {

    public static void main(String[] args) {
        int[] price_list = new int[]{10, 20, 40, 80, 160};

        int[] array = new int[price_list.length];
        // 第0个加入
        array[0] = price_list[0];

        for (int i =1; i< array.length; ++i) {
            array[i] = price_list[i -1];
        }
        String[] result = new String[price_list.length];
        for (int i = 0; i < array.length; ++i) {
            result[i] = String.valueOf(Math.log(price_list[i] / array[i]));
        }

        Arrays.stream(result).forEach(i -> {
            System.out.println(i);
        });
        System.out.println("-----");


        List<Double> records = new ArrayList();
        records.add(10D);
        records.add(20D);
        records.add(40D);
        records.add(80D);
        records.add(160D);

        final List<Double> series = Lists.newArrayListWithCapacity(records.size());
        series.add(Math.log(records.get(0) / records.get(0)));

        for (int i = 1; i < records.size(); i++) {
            series.add(Math.log(records.get(i) / records.get(i - 1)));
        }

        series.stream().forEach(i -> {
            System.out.println(i);
        });
    }
}
