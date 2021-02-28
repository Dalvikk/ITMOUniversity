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
    int n = nxt();
    vector<int> a(n);
    vector<int> dp(n, 1);
    vector<int> p(n, -1);
    for (auto &item : a) {
        item = nxt();
    }
    int max_length = 1;
    int ans_idx = 0;
    for (int i = 0; i < n; i++) {
        for (int j = i - 1; j >= 0; j--) {
            if (a[i] > a[j] && dp[j] + 1 > dp[i]) {
                dp[i] = dp[j] + 1;
                p[i] = j;
                if (dp[i] > max_length) {
                    max_length = dp[i];
                    ans_idx = i;
                }
            }
        }
    }
    vector<int> path;
    while (ans_idx != -1) {
        path.push_back(a[ans_idx]);
        ans_idx = p[ans_idx];
    }
    reverse(path.begin(), path.end());
    cout << max_length << endl;
    for (const auto &item : path) {
        cout << item << " ";
    }
    cout << "\n";
    return 0;
}
