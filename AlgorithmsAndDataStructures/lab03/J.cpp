#include <iostream>
#include <vector>

using namespace std;

int n,m;
vector <vector <bool> > d;
vector <vector <long long> > a;

int bit(int x, int i) {
    return ((x >> i) & 1);
}

bool check(int p, int p2) {
    for (int i = 0; i < n-1; i++) {
        int a = bit(p, i);
        int b = bit(p, i+1);
        int c = bit(p2, i);
        int d = bit(p2, i+1);
        if (a == b && b == c && c == d) {
            return false;
        }
    }
    return true;
}

int main() {
    cin >> n >> m;
    if (n > m) {
        swap(n,m);
    }
    d.resize(1 << n, vector <bool> (1 << n));
    a.resize(m+1, vector<long long> (1 << n));
    for (int i = 0; i < (1 << n); i++) {
        for (int j = 0; j < (1 << n); j++) {
            d[i][j] = check(i,j);
        }
    }
    for (int i = 0; i < (1 << n); i++) {
        a[1][i] = 1;
    }
    for (int i = 2; i <= m; i++) {
        for (int p = 0; p < (1 << n); p++) {
            for (int p2 = 0; p2 < (1 << n); p2++) {
                a[i][p] += a[i-1][p2] * d[p2][p];
            }
        }
    }
    long long ans = 0;
    for (int i = 0; i < (1 << n); i++) {
        ans += a[m][i];
    }
    cout << ans << "\n";
    return 0;
}
