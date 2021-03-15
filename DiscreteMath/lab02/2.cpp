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

void print_mask(int mask, int sz) {
    for (int i = sz - 1; i >= 0; i--) {
        cout << (mask >> i & 1);
    }
    cout << endl;
}

int main() {
    freopen("gray.in", "r", stdin);
    freopen("gray.out", "w", stdout);
    int n = nxt();
    for (int i = 0; i < (1 << n); ++i) {
        int mask =  i ^ (i >> 1);
        print_mask(mask, n);
    }
    return 0;
}
