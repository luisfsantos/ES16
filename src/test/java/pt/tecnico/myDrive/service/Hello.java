package pt.tecnico.myDrive.service;

public class Hello {

    public static void main(String[] args) {
        System.out.println("Main: Hello myDrive!");
    }

    public static void hello(String[] args) {
        System.out.println("hello there:");
        for (String s: args) {
            System.out.println(s + "!");
        }
    }

    public static void helloEmpty(String[] args) {
        System.out.println("helloEmpty: Hello World!");
    }

}
