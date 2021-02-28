#include <iostream>
#include <algorithm>
#include <cmath>
#pragma GCC optimize("unroll-loops")
#pragma GCC optimize("Ofast")
#pragma GCC optimize("-O3")
#pragma GCC target("sse,sse2,sse3,ssse3,sse4,popcnt,abm,mmx,avx,avx2,tune=native")
#define fastIO ios_base::sync_with_stdio(0); cin.tie(0);
#define mp make_pair
#define endl '\n'
using namespace std;

//[cringe]

const int MAX_MEM = 60 * 1e6;
int mpos = 0;
char mem[MAX_MEM];

inline void *operator new(size_t n) {
    char *res = mem + mpos;
    mpos += n;
    return (void *) res;
}

inline void operator delete(void *) {}

//[\cringe]

inline int nxt() {
    int a;
    cin >> a;
    return a;
}

const int MAXN = 300001;
int sz[MAXN], points[MAXN], parent[MAXN];


inline pair<int, int> findPar(int v) {
    if (v == parent[v]) {
        return {v, 0};
    }
    auto a = findPar(parent[v]);
    return {parent[v] = a.first, points[v] = a.second + points[v]};
}

inline void unionSets(int a, int b) {
    a = findPar(a).first;
    b = findPar(b).first;
    if (sz[b] > sz[a]) {
        swap(a, b);
    }
    if (a != b) {
        parent[b] = a;
        sz[a] += sz[b];
        points[b] -= points[a];
    }
}

int main() {
    fastIO;
    int n = nxt(), m = nxt();
    for (int i = 0; i < n + 1; ++i) {
        parent[i] = i, sz[i] = 1;
    }
    for (int i = 0; i < m; ++i) {
        string s;
        cin >> s;
        if (s[0] == 'j') {
            int x = nxt(), y = nxt();
            unionSets(x, y);
        } else if (s[0] == 'a') {
            int x = nxt(), v = nxt();
            points[findPar(x).first] += v;
        } else {
            int x = nxt();
            if (x == parent[x]) {
                cout << points[x] << endl;
            } else {
                cout << points[findPar(x).first] + points[x] << endl;
            }
        }
    }
    return 0;
}
