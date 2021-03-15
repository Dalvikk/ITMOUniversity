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

void gen(vector<int> cur, int n, int k) {
    if (k == 0) {
        for (const auto &item : cur) {
            cout << item << " ";
        }
        cout << "\n";
        return;
    }
    for (int i = cur.empty() ? 1 : cur.back() + 1; i <= n; ++i) {
        vector<int> nxt = cur;
        nxt.push_back(i);
        gen(nxt, n, k - 1);
    }
}


int main() {
    freopen("choose.in", "r", stdin);
    freopen("choose.out", "w", stdout);
    int n = nxt(), k = nxt();
    gen(vector<int>(), n, k);
    return 0;
}