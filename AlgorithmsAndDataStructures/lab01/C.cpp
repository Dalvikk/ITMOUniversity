#include <iostream>
#include <vector>
#include <algorithm>
#include <map>
#include <set>
#include <stack>
#include <queue>
#include <deque>

#define fastIO ios_base::sync_with_stdio(0); cin.tie(0);
#pragma GCC optimize("unroll-loops")
#pragma GCC optimize("Ofast")
#pragma GCC optimize("-O3")
#pragma GCC target("sse,sse2,sse3,ssse3,sse4,popcnt,abm,mmx,avx,tune=native")
#define forn(i, a, b) for (int i = (a); i < (b); i++)
#define ford(i, a, b) for (int i = (b)-1; i >= (a); i--)
#define  all(x) (x).begin(),(x).end()
#define  fi first
#define  se second
#define  szof(x) ((int)(x).size())
#define  rsz resize
#define  lb lower_bound
#define  ub upper_bound
#define  INF (int)1e9
#define  INFL (long long)1e18
#define  re return
#define  pb push_back
#define  mp make_pair
#define  ll long long
#define  ld long double
#define  PII pair<int,int>
#define  VI vector<int>
#define  VVI vector<vector <int> >
#define  VVLL vector<vector <long long> >
#define  VLL vector <long long>
#define  mt make_tuple

using namespace std;

inline int nxt() {
    int a;
    cin >> a;
    return a;
}

ll ans = 0;

ll cnt_inv(int *a, int n, int *b, int m) {
    ll res = 0;
    int j = 0;
    for (int i = 0; i < n; i++) {
        while(j < m && a[i] > b[j]) {
            j++;
        }
        res += j;
    }
    return res;
}

int *merge(int *a, int l, int r) {
    if (l == r - 1) {
        return new int[1]{a[l]};
    }
    int m = l + r >> 1;
    int *b = merge(a, l, m);
    int *c = merge(a, m, r);
    int *res = new int[r - l];
    int i = 0, j = 0, k = 0;
    while (k < r - l) {
        if ((j == r - m) || (i < m - l && b[i] < c[j])) {
            res[k++] = b[i++];
        } else {
            res[k++] = c[j++];
        }
    }
    ans += cnt_inv(b, m - l, c, r - m);
    delete[] b;
    delete[] c;
    return res;
}

int main() {
    fastIO;
    int n = nxt();
    int *a = new int[n];
    forn(i, 0, n) {
        a[i] = nxt();
    }
    a = merge(a, 0, n);
    cout << ans << endl;
    return 0;
}
