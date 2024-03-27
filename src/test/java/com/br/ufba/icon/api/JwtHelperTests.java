package com.br.ufba.icon.api;

import com.br.ufba.icon.api.helper.JwtHelper;

import java.util.Scanner;

public class JwtHelperTests {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        String token = JwtHelper.generateToken("Edu");
        System.out.println("JWT Token: " + token);

        String username = JwtHelper.extractUsername(token);
        System.out.println(username);

    }
}
