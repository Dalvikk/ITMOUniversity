//
// Created by Vladislav Kovalchuk on 23.11.2020.
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
#include <numeric>

#define fastIO ios_base::sync_with_stdio(0); cin.tie(0);
#define INF (int)1e9
#define INFL (long long)1e18
#define ll long long
#define endl '\n'
using namespace std;

inline int nxt() {
    int a;
    cin >> a;
    return a;
}

long long fact[19];

void gen(int n, long long k, int len_left, vector<int> &a) {
    if (len_left == 0) {
        return;
    }
    int num = -1;
    int intIdx = 0;
    for (int i = 0; i < a.size(); i++) {
        if (k < (i + 1) * fact[len_left - 1]) {
            num = a[i];
            intIdx = i;
            break;
        }
    }
    cout << num << " ";
    a.erase(find(a.begin(), a.end(), num));
    gen(n, k - intIdx * fact[len_left - 1], len_left - 1, a);
}

void precalc() {
    fact[0] = 1;
    for (int i = 1; i < 19; ++i) {
        fact[i] = fact[i - 1] * i;
    }
}

int main() {
    freopen("num2perm.in", "r", stdin);
    freopen("num2perm.out", "w", stdout);
    precalc();
    int n = nxt();
    long long k;
    cin >> k;
    vector<int> a(n);
    iota(a.begin(), a.end(), 1);
    gen(n, k, n, a);
    return 0;
}
