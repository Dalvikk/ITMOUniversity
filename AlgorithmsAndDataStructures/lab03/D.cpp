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
#define INFL (long long)1e18
#define ll long long
#define endl '\n'
using namespace std;

inline int nxt() {
    int a;
    cin >> a;
    return a;
}

vector<vector<int> > to(10);
const int MOD = 1e9;

void preCalc() {
    to[0] = {4, 6};
    to[1] = {6, 8};
    to[2] = {7, 9};
    to[3] = {4, 8};
    to[4] = {3, 0, 9};
    to[6] = {1, 7, 0};
    to[7] = {2, 6};
    to[8] = {1, 3};
    to[9] = {4, 2};
}

int main() {
    preCalc();
    int n = nxt();
    vector<vector<int> > dp(n, vector<int>(10, 0));
    for (int i = 0; i < 10; i++) {
        dp[0][i] = 1;
    }
    dp[0][0] = dp[0][8] = 0;
    for (int i = 0; i + 1 < n; i++) {
        for (int j = 0; j < 10; j++) {
            for (const auto &item : to[j]) {
                dp[i + 1][item] += dp[i][j];
                dp[i + 1][item] %= MOD;
            }
        }
    }
    int ans = 0;
    for (int i = 0; i < 10; i++) {
        ans += dp[n - 1][i];
        ans %= MOD;
    }
    cout << ans << "\n";
    return 0;
}
