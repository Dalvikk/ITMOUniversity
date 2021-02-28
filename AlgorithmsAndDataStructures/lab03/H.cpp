//
// Created by Vladislav Kovalchuk on 06.12.2020.
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
#define endl '\n'
using namespace std;

inline int nxt() {
    int a;
    cin >> a;
    return a;
}

int main() {
    int n = nxt();
    vector<vector<int> > a = vector<vector<int>>(n, vector<int>(n));
    for (auto &item : a) {
        for (auto &item1 : item) {
            item1 = nxt();
        }
    }
    vector<vector<int>> dp(1 << n, vector<int>(n, INF));
    vector<vector<int>> prev(1 << n, vector<int>(n, -1));
    for (int i = 0; i < n; i++) {
        dp[1 << i][i] = a[i][i];
    }

    for (int mask = 0; mask < (1 << n); mask++) {
        for (int i = 0; i < n; i++) {
            if (dp[mask][i] == INF) {
                continue;
            }
            for (int k = 0; k < n; k++) {
                if (!(mask >> k & 1)) {
                    if (dp[mask][i] + a[i][k] < dp[mask | (1 << k)][k]) {
                        dp[mask | (1 << k)][k] = dp[mask][i] + a[i][k];
                        prev[mask | (1 << k)][k] = i;
                    }
                }
            }
        }
    }
    int ans_idx = 0;
    for (int i = 0; i < n; i++) {
        if (dp[(1 << n) - 1][i] < dp[(1 << n) - 1][ans_idx]) {
            ans_idx = i;
        }
    }
    vector<int> path;
    int mask = (1 << n) - 1;
    int last = ans_idx;
    while (mask != 0) {
        path.push_back(last + 1);
        int tmp = prev[mask][last];
        mask = mask ^ (1 << last);
        last = tmp;
    }

    cout << dp[(1 << n) - 1][ans_idx] << "\n";
    reverse(path.begin(), path.end());
    for (const auto &item : path) {
        cout << item << " ";
    }
    cout << "\n";
    return 0;
}
