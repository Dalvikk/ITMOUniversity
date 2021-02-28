//
// Created by Vladislav Kovalchuk on 06.12.2020.
//

#include <iostream>
#include <vector>
#include <algorithm>
#include <cassert>

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

const int MAXN = 100;
int n;


int main() {
    n = nxt();
    vector<int> a(n);
    for (auto &item : a) {
        item = nxt();
    }
    /*
     * dp[i][j] --- (минимально потраченная сумма Пети, сколько купонов он потратил) за первые i дней
     * если у него осталось j купонов
     */
    vector<vector<pair<int, int>>> dp(n + 1, vector<pair<int, int>>(n + 1, make_pair(INF, INF)));
    vector<vector<pair<int, int>>> parent(n + 1, vector<pair<int, int>>(n + 1, make_pair(-1, -1)));
    dp[0][0] = make_pair(0, 0);
    for (int i = 0; i < n; i++) {
        for (int j = 0; j < n; j++) {
            if (dp[i][j] == make_pair(INF, INF)) {
                continue;
            }
            // Покупаем за деньги
            int add = a[i] > 100 ? 1 : 0;
            if (j + add <= n && dp[i][j].first + a[i] < dp[i + 1][j + add].first) {
                dp[i + 1][j + add] = make_pair(dp[i][j].first + a[i], dp[i][j].second);
                parent[i + 1][j + add] = make_pair(i, j);
            }
            // Покупаем за купон
            if (j != 0 && dp[i][j].first < dp[i + 1][j - 1].first) {
                dp[i + 1][j - 1] = make_pair(dp[i][j].first, dp[i][j].second + 1);
                parent[i + 1][j - 1] = make_pair(i, j);
            }
        }
    }
    int ans_idx = 0;
    for (int i = 0; i <= n; i++) {
        if (dp[n][i].first <= dp[n][ans_idx].first) {
            ans_idx = i;
        }
    }
    vector<int> path;
    int r = n, c = ans_idx;
    while (parent[r][c] != make_pair(-1, -1)) {
        if (parent[r][c].second - 1 == c) { // Купили за купон
            path.push_back(r);
            r--;
            c++;
        } else {
            int add = a[r - 1] > 100 ? 1 : 0;
            r--;
            c -= add;
        }
    }

    cout << dp[n][ans_idx].first << "\n";
    cout << ans_idx << " " << dp[n][ans_idx].second << "\n";
    reverse(path.begin(), path.end());
    assert (path.size() == dp[n][ans_idx].second);
    for (auto &item : path) {
        cout << item << "\n";
    }
    return 0;
}
