//package HW;

import java.lang.*;
import java.io.*;
import java.nio.charset.Charset;
import java.util.*;

public class F {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int k = scanner.nextInt();

        List<List<Integer>>[] f = new LinkedList[2];
        for (int i = 0; i < f.length; i++) {
            f[i] = new LinkedList<>();
        }
        for (int i = 0; i < k; i++) {
            List<Integer>[] dis = new LinkedList[2];
            for (int j = 0; j < dis.length; j++) {
                dis[j] = new LinkedList<>();
            }
            for (int j = 0; j < n; j++) {
                int in = scanner.nextInt();
                if (in != -1) {
                    dis[in].add(j + 1);
                }
            }
            f[1].add(dis[1]);
            f[0].add(dis[0]);
        }
        while (true) {
            int simpleX = -1;
            int idx = -1;
            for (int i = 0; i < k; i++) {
                if (f[0].get(i).size() + f[1].get(i).size() == 1) {
                    idx = f[0].get(i).size() == 1 ? 0 : 1;
                    simpleX = f[idx].get(i).get(0);
                    break;
                }
            }
            if (idx == -1) {
                System.out.println("NO");
                return;
            } else {
                for (int j = 0; j < k; j++) {
                    for (Integer integer : f[idx].get(j)) {
                        if (integer.intValue() == simpleX) {
                            f[idx].get(j).clear();
                            f[1 - idx].get(j).clear();
                        }
                    }
                    // for (Integer integer : f[1 - idx].get(j)) {
                    List<List<Integer>> ls = f[1 - idx];
                    List<Integer> lss = ls.get(j);
                    for (int z = 0; z < lss.size(); z++) {
                        Integer integer = lss.get(z);
                        if (integer.intValue() == simpleX) {
                            boolean check = f[1 - idx].get(j).remove(integer);
                            if (f[1 - idx].get(j).isEmpty() && f[idx].get(j).isEmpty()) {
                                System.out.println("YES");
                                return;
                            }
                        }
                    }


                }
            }
        }
    }
}