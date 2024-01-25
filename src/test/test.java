package test;

import java.util.Scanner;

import static java.lang.Double.parseDouble;

public class test {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Insert a number or letter: ");
        String string = sc.nextLine();

        if (string.matches("[0-9]+")) {
            System.out.println("A String contém apenas numeros.");
        } else {
            System.out.println("A String contém carcteres diferente de numeros");
        }
    }
}
