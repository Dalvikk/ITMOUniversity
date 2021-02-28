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
    int n = nxt(), k = nxt();
    vector<int> a(n + 1, 0);
    for (int i = 1; i < n - 1; i++) {
        a[i] = nxt();
    }
    vector<int> dp(n + 1, -INF);
    vector<int> p(n + 1, -1);
    dp[0] = 0;
    for (int i = 1; i < n; i++) {
        for (int j = i - 1; j >= max(0, i - k); j--) {
            if (dp[j] + a[i] > dp[i]) {
                dp[i] = dp[j] + a[i];
                p[i] = j;
            }
        }
    }
    vector<int> path;
    path.push_back(n - 1);
    int last = n - 1;
    while (last != 0) {
        path.push_back(p[last]);
        last = p[last];
    }
    reverse(path.begin(), path.end());
    cout << dp[n - 1] << "\n";
    cout << path.size() - 1 << "\n";
    for (const auto &item : path) {
        cout << item + 1 << " ";
    }
    cout << "\n";
    return 0;
}
