package com.br.ufba.icon.api;

import java.sql.Timestamp;

public class TimestampTest {

    public static void main(String[] args) {
        String _in = "2024-03-29 20:40:30";
        String _out = "2024-03-29 21:05:02";

        Timestamp in = Timestamp.valueOf(_in);
        Timestamp out = Timestamp.valueOf(_out);

        Timestamp diff = new Timestamp((out.getTime() - in.getTime()));
        Timestamp initial = new Timestamp(10800000);

        Timestamp _final = new Timestamp((initial.getTime() + diff.getTime()));

        System.out.println(in);
        System.out.println(out);
        System.out.println(diff);


        System.out.println(initial);
        System.out.println(_final);

    }
}
