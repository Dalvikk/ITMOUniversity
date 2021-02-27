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
#define  endl '\n'
#define debug(x) cout << #x << " is " << x << endl;
#define  debug2(x) cout << #x << " is "; for (auto elem : x) {cout << elem << " ";} cout << endl;
#define int ll
using namespace std;

inline int nxt() {
    int a;
    cin >> a;
    return a;
}

bool f(int n, int x, int y, int m) {
    return m/x + m/y >= n;
}

signed main() {
    int n = nxt(), x = nxt(), y = nxt();
    n--;
    int l = -1;
    int r = min(x,y) * n;
    while (r-l > 1) {
        int m = l + r >> 1;
        if (f(n, x, y, m)) {
            r = m;
        } else {
            l = m;
        }
    }
    cout << min(x,y) + r << "\n";
    return 0;
}
