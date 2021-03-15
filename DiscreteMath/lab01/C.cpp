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

inline int getBit(int val, int idx) {
    return val >> idx & 1;
}


void findMaxDepth(int v, vector<int> &d, vector<bool> &u, vector<vector<int>> &g) {
    u[v] = true;
    for (auto to : g[v]) {
        if (!u[to]) {
            findMaxDepth(to, d, u, g);
        }
        d[v] = max(d[v], d[to] + 1);
    }
}

void calc(int v, vector<int> &res, vector<bool> &u, vector<int> &f, vector<vector<int>> &g) {
    u[v] = true;
    int mask = 0;
    for (int i = 0; i < g[v].size(); i++) {
        int to = g[v][i];
        if (!u[to]) {
            calc(to, res, u, f, g);
        }
        mask |= res[to] << (g[v].size() - 1 - i);
    }
//    debug(v);
//    debug(mask);
    if (g[v].size() != 0) {
        res[v] = getBit(f[v], mask);
    }
//    debug(res[v]);
}


int main() {
    fastIO;
    int n = nxt();
    vector<vector<int>> g(n + 1);
    vector<int> f(n + 1, 0);
    vector<int> res(n + 1, 0);
    vector<int> lists;

    for (int i = 1; i <= n; ++i) {
        int m = nxt();
        for (int j = 0; j < m; ++j) {
            int v = nxt();
            g[i].push_back(v);
        }
        for (int j = 0; m != 0 && j < (1 << m); ++j) {
            int bit = nxt();
            f[i] |= (bit << j);
        }
        if (m == 0) {
            lists.push_back(i);
        }
    }
    vector<bool> u(n, false);
    vector<int> d(n + 1, 0);
    findMaxDepth(n, d, u, g);
    cout << d[n] << endl;
    for (int i = 0; i < (1 << (lists.size())); ++i) {
        for (int j = 0; j < lists.size(); j++) {
            res[lists[j]] = getBit(i, lists.size() - 1 - j);
        }
        u.assign(n + 1, false);
        calc(n, res, u, f, g);
        cout << res[n];
    }
    cout << endl;
    return 0;
}