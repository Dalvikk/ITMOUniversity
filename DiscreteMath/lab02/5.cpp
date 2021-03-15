//
// Created by Vladislav Kovalchuk on 28.11.2020.
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
    freopen("telemetry.in", "r", stdin);
    freopen("telemetry.out", "w", stdout);
    int n = nxt(), k = nxt();
    vector<string> ans = {""};
    for (int i = 0; i < n; i++) {
        vector<string> new_ans;
        for (int j = 0; j < k; j++) {
            for (const auto &item : ans) {
                new_ans.push_back(item + char(j + '0'));
            }
            reverse(ans.begin(), ans.end());
        }
        swap(new_ans, ans);
    }
    for (const auto &item : ans) {
        cout << item << "\n";
    }
    return 0;
}
