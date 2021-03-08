package search;

public class BinarySearchMax {
    /*
    Inside(k, l, r, a) =
        Exists k in [l, r):
        for all i in [0, k): a[i] < a[i + 1] && for all j in [k + 1, a.length - 1): a[j] > a[j + 1]
     */


    // Pred: Inside(k, 0, a.length, a) && a.length > 0
    // Post: R == a[k]
    public static int binarySearchMaxIterative(final int[] a) {
        // Inside(k, 0, a.length, a) && a.length > 0
        int l = 0;
        int r = a.length;
        // Inv: Inside(k, l, r, a) && 0 <= l < r <= a.length
        while (l + 1 != r) {
            // Inv && (l + 1 != r) -> (l + 1 < r)
            final int m = (l + r) / 2;
            // Inv && (l + 1 < r) -> (0 <= l < m < r <= a.length)
            if (a[m - 1] < a[m]) {
                // Inv &&
                // (a[m-1] < a[m]) -> !Inside(k, l, m, a) -> Inside(k, m, r, a) &&
                // 0 < l < m < r <= a.length
                l = m;
                // Inside(k, l, r, a) && 0 < l < r <= a.length
            } else {
                // Inv &&
                // a[m-1] >= a[m] -> !Inside(k, m, r, a) -> Inside(k, l, m, a) &&
                // 0 <= l < m < r <= a.length
                r = m;
                // Inside(k, l, r, a) && 0 <= l < r < a.length
            }
        }
        // Inside(k, l, l + 1, a) -> (k == l)
        return a[l];
        // R == a[k]
    }

    // Imm: Inside(k, l, r, a) && a.length > 0, 0 <= l < r <= a.length

    // Pred: Imm
    // Post: R == a[k]
    private static int binarySearchMaxRecursive(final int[] a, final int l, final int r) {
        if (l + 1 == r) {
            // Inside(k, l, l+1, a) -> (k == l)
            return a[l];
            // R == a[k]
        }
        // Imm && (l + 1 != r)
        final int m = (l + r) / 2;
        // Imm &&
        // (l + 1 != r && l < r) -> (l + 1 < r) -> (0 <= l < m < r <= a.length)
        if (a[m - 1] < a[m]) {
            // Imm &&
            // a[m-1] < a[m] -> !Inside(k, l, m, a) -> Inside(k, m, r, a) &&
            // l < m < r
            return binarySearchMaxRecursive(a, m, r);
            // R == a[k]
        } else {
            // Imm &&
            // a[m-1] >= a[m] -> !Inside(k, m, r, a) -> Inside(k, l, m, a) &&
            // 0 <= l < m < r
            return binarySearchMaxRecursive(a, l, m);
            // R == a[k]
        }
    }

    // Pred: Inside(k, 0, a.length, a) && a.length > 0
    // Post: R == a[k]
    public static int binarySearchMaxRecursive(final int[] a) {
        return binarySearchMaxRecursive(a, 0, a.length);
        // R == a[k]
    }


    // Pred: String[] args, parsable to int[] && Inside(k, 0, args.length, args)
    // Post: print(args[k])
    public static void main(final String[] args) {
        if (args.length == 0) {
            System.out.println("Sorry, you violated contract's terms. Array cannot be empty");
            return;
        }
        // String[] args, parsable to int[] && Inside(k, 0, args.length, args) && args.length > 0
        final int[] a = new int[args.length];
        // String[] args, parsable to int[] && Inside(k, 0, args.length, args) && args.length > 0
        // && a.length == args.length
        for (int i = 0; i < a.length; i++) {
            try {
                a[i] = Integer.parseInt(args[i]);
                // String[] args, parsable to int[] && Inside(k, 0, args.length, args)
                // && a.length == args.length && a[i] = int(args[i])
            } catch (final NumberFormatException e) {
                // String[] args not parsable to int[]
                System.out.println("Sorry, you violated contract's terms. " + args[i] + " isn't an integer");
                // print error message
                return;
            }
        }
        // int[] a && Inside(k, 0, a.length, a) && a.length > 0
        System.out.println(binarySearchMaxRecursive(a));
        // print(a[k]
    }
}
