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

void preCalc() {
    fact[0] = 1;
    for (int i = 1; i < 19; ++i) {
        fact[i] = fact[i - 1] * i;
    }
}

int main() {
    freopen("perm2num.in", "r", stdin);
    freopen("perm2num.out", "w", stdout);
    preCalc();
    int n = nxt();
    vector<int> a(n);
    for (auto &item : a) {
        cin >> item;
    }
    vector<int> b = a;
    sort(b.begin(), b.end());
    long long k = 0;
    for (int i = 0; i < a.size(); i++) {
        int pos = find(b.begin(), b.end(), a[i]) - b.begin();
        k += pos * fact[a.size() - 1 - i];
        b.erase(b.begin() + pos);
    }
    cout << k << "\n";
    return 0;
}
