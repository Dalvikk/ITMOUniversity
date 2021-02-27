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

using namespace std;

inline int nxt() {
    int a;
    cin >> a;
    return a;
}

const int MAXN = 1e5;
int n, k;
int a[MAXN];

bool f(ll sum) {
    int k2 = 0;
    ll cur_sum = 0;
    int i = -1;
    while (i < n) {
        while (i < n && cur_sum <= sum) {
            i++;
            cur_sum += a[i];
        }
        k2++;
        cur_sum = a[i];
    }
    return k2 <= k;
}

int main() {
    fastIO;
    n = nxt(), k = nxt();
    forn(i, 0, n) {
        a[i] = nxt();
    }
    ll l = (*max_element(a, a + n)) - 1;
    ll r = 1e9 * 1e5;
    while (r - l > 1) {
        ll m = l + r >> 1;
        if (f(m)) {
            r = m;
        } else {
            l = m;
        }
    }
    cout << r << endl;
    return 0;
}
