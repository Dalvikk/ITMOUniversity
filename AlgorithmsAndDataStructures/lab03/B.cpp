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

int main() {
    int n = nxt(), m = nxt();
    vector<vector<int> > a(n + 1, vector<int>(m + 1, 0));
    vector<vector<int> > dp(n + 1, vector<int>(m + 1, -INF));
    vector<vector<pair<int, int>>> p(n + 1, vector<pair<int, int>>(m + 1, {-1, -1}));
    dp[1][0] = 0;
    dp[0][1] = 0;
    for (int i = 1; i <= n; i++) {
        for (int j = 1; j <= m; j++) {
            a[i][j] = nxt();
        }
    }
    for (int i = 1; i <= n; i++) {
        for (int j = 1; j <= m; j++) {
            if (dp[i - 1][j] + a[i][j] > dp[i][j]) {
                dp[i][j] = dp[i - 1][j] + a[i][j];
                p[i][j] = {i - 1, j};
            }
            if (dp[i][j - 1] + a[i][j] > dp[i][j]) {
                dp[i][j] = dp[i][j - 1] + a[i][j];
                p[i][j] = {i, j - 1};
            }
        }
    }

    string path;
    int r = n, c = m;
    while (r != 1 || c != 1) {
        if (p[r][c] == make_pair(r - 1, c)) {
            path.push_back('D');
            r--;
        } else {
            path.push_back('R');
            c--;
        }
    }
    reverse(path.begin(), path.end());
    cout << dp[n][m] << "\n";
    cout << path << "\n";
    return 0;
}
