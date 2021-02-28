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
    string s, t;
    cin >> s >> t;
    int n = s.size();
    int m = t.size();
    vector<vector<int> > dp(n + 1, vector<int>(m + 1, INF));
    dp[0][0] = 0;
    for (int i = 1; i <= n; i++) {
        dp[i][0] = i;
    }
    for (int i = 1; i <= m; i++) {
        dp[0][i] = i;
    }
    for (int i = 1; i <= n; i++) {
        for (int j = 1; j <= m; j++) {
            dp[i][j] = min({
                                   dp[i - 1][j - 1] + !(s[i - 1] == t[j - 1]),
                                   dp[i][j - 1] + 1,
                                   dp[i - 1][j] + 1
                           });
        }
    }
    cout << dp[n][m] << endl;
    return 0;
}
