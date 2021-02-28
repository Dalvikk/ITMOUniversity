//
// Created by Vladislav Kovalchuk on 07.11.2020.
//

#include <iostream>
#include <vector>
#include <algorithm>
#include <map>
#include <set>
#include <stack>
#include <queue>
#include <deque>
#include <cmath>

#define fastIO ios_base::sync_with_stdio(0); cin.tie(0);
#define  forn(i, a, b) for (int i = (a); i < (b); i++)
#define  ford(i, a, b) for (int i = (b)-1; i >= (a); i--)
#define   all(x) (x).begin(),(x).end()
#define   fi first
#define   se second
#define   szof(x) ((int)(x).size())
#define   rsz resize
#define   lb lower_bound
#define   ub upper_bound
#define   INF (int)1e9
#define   INFL (long long)1e18
#define   re return
#define   pb push_back
#define   mp make_pair
#define   ll long long
#define   ld long double
#define   PII pair<int,int>
#define   VI vector<int>
#define   VVI vector<vector <int> >
#define   VVLL vector<vector <long long> >
#define   VLL vector <long long>
#define   mt make_tuple
#define   endl '\n'
#define  debug(x) cout << #x << " is " << x << endl;
#define   debug2(x) cout << #x << " is "; for (auto elem : x) {cout << elem << " ";} cout << endl;

using namespace std;

inline int nxt() {
    int a;
    cin >> a;
    return a;
}

const int MAXN = 300001;
int size[MAXN], mn[MAXN], mx[MAXN], p[MAXN];

int makeSet(int v) {
    p[v] = v;
    mn[v] = mx[v] = v;
    size[v] = 1;
}

int findPar(int v) {
    return v == p[v] ? v : p[v] = findPar(p[v]);
}

void unionSets(int a, int b) {
    a = findPar(a);
    b = findPar(b);
    if (size[b] > size[a]) {
        swap(a, b);
    }
    if (a != b) {
        p[b] = a;
        size[a] += size[b];
        mn[a] = min(mn[a], mn[b]);
        mx[a] = max(mx[a], mx[b]);
    }
}

int main() {
    fastIO;
    int n = nxt();
    for (int i = 1; i <= n; i++) {
        makeSet(i);
    }
    string s;
    while (cin >> s) {
        if (s == "union") {
            unionSets(nxt(), nxt());
        } else {
            int v = nxt();
            int p = findPar(v);
            cout << mn[p] << " " << mx[p] << " " << size[p] << endl;
        }
    }
    return 0;
}    
