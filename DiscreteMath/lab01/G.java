//package HW;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class G {

    private static int getBit(long x, int i) {
        return (int) (x >> i & 1);
    }

    private static int setBit(int mask, int i, int val) {
        return mask | (val << i);
    }

    private static void solution(int n, long[] a, long s) {
        List<List<String>> SDNF = new ArrayList<>();

        int length = 31;
        int[] res = new int[33];
        Arrays.fill(res, -1);
        res[0] = 0;
        for (int i = length; i >= 0; i--) {
            int mask = 0;
            for (int j = 0; j < n; j++) {
                mask = setBit(mask, j, getBit(a[j], i));
            }
            int currentRes = getBit(s, i);
            if (res[mask] != -1 && res[mask] != currentRes) { // have the same vectors and different values
                System.out.println("Impossible");
                return;
            }
            res[mask] = currentRes;
            if (res[mask] == 1) { // add to SDNF
                SDNF.add(new ArrayList<String>());
                int idx = SDNF.size() - 1;
                for (int j = 0; j < n; j++) {
                    if (getBit(mask, j) == 1) {
                        SDNF.get(idx).add(String.valueOf(j+1));
                    } else {
                        SDNF.get(idx).add("~" + (j + 1));
                    }
                }
            }
        }
        outSDNF(SDNF);
    }

    private static void outSDNF(List<List<String>> f) {
        for (int i = 0; i < f.size(); i++) {
            if (i != 0) {
                System.out.print("|");
            }
            System.out.print("(");
            for (int j = 0; j < f.get(i).size(); j++) {
                if (j != 0) {
                    System.out.print("&");
                }
                System.out.print("(" + f.get(i).get(j) + ")");
            }
            System.out.print(")");
        }
        System.out.println();
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        long[] a = new long[n];
        for (int i = 0; i < n; i++) {
            a[i] = in.nextLong();
        }
        long s = in.nextLong();
        if (s == 0) {
            System.out.println("1&~1");
        } else {
            solution(n, a, s);
        }
    }
}
