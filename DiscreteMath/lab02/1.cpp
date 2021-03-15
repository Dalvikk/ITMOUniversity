//
// Created by Vladislav Kovalchuk on 17.11.2020.
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
#define    INF (int)1e9
#define    INFL (long long)1e18
#define    ll long long
#define    endl '\n'
using namespace std;

inline int nxt() {
    int a;
    cin >> a;
    return a;
}

int main() {
    fastIO;
    freopen("allvectors.in", "r", stdin);
    freopen("allvectors.out", "w", stdout);
    int n = nxt();
    for (int i = 0; i < (1 << n); i++) {
        for (int j = n - 1; j >= 0; j--) {
            cout << (i >> j & 1);
        }
        cout << "\n";
    }
    return 0;
}
