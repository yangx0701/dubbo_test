package com.yx.api;

import java.util.*;

public class TestMain {
    public static void main(String[] args){
        ArrayList<String> li =new ArrayList<>();
        li.add("bbb");
        li.add("aaa");
        li.add("ccc");
//        li.forEach(System.out::println);
        li.forEach(litmp->System.out.println(litmp));

        new Thread(()->{
            System.out.println("hi");
        }).start();

        Runnable r = ()->{
            System.out.println("hi2");
        };
        new Thread(r).start();

        System.out.println("----------------------------");

        String players[] = {"ccc","bbb","aaa"};
        Arrays.sort(players, (String s1, String s2) -> (s1.compareTo(s2)));

        Comparator<String> sortByName = (String s1, String s2) -> (s1.compareTo(s2));
        Arrays.sort(players, sortByName);

        Random random = new Random();
        random.ints().limit(10).forEach(System.out::println);

        List<Integer> numbers = Arrays.asList(3, 2, 2, 3, 7, 3, 5);

        IntSummaryStatistics stats = numbers.stream().mapToInt((x) -> x)
                .summaryStatistics();

        System.out.println("列表中最大的数 : " + stats.getMax());
        System.out.println("列表中最小的数 : " + stats.getMin());
        System.out.println("所有数之和 : " + stats.getSum());
        System.out.println("平均数 : " + stats.getAverage());
    }

}
