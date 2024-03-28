package com.br.ufba.icon.api;

import java.sql.Timestamp;

public class Test {

    public static void main(String[] args) {
        System.out.println(1000*60*60*3);
        System.out.println(new Timestamp(1000*60*60*3));

        String time = "02:45:12";

        int hour = Integer.parseInt(time.substring(0, 2));
        int minutes = Integer.parseInt(time.substring(3,5));
        int seconds = Integer.parseInt(time.substring(6,8));

        long total = ((long) hour * 60 * 60 * 1000) + ((long) minutes * 60 * 1000) + (seconds * 1000L);

        System.out.println(hour);
        System.out.println(minutes);
        System.out.println(seconds);
        System.out.println(total);

        Timestamp zero = new Timestamp(10800000);
        System.out.println(new Timestamp(zero.getTime() + total));
    }
}
