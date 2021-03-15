import java.util.Scanner;

public class H {

    private static String NOT(String x) {
        return String.format("(%s|%s)", x, x);
    }


    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        String[] f = new String[101];
        int n = in.nextInt();
        f[0] = "((A0|B0)|(A0|B0))";
        for (int i = 1; i < n; i++) {
            f[i] = String.format("((%s|(%s|%s))|(%s|%s))", f[i-1], NOT("A" + i), NOT("B" + i), "A" + i, "B" + i);
        }
        System.out.println(f[n-1]);
    //    System.out.println(f[n-1].length());
    }
}
