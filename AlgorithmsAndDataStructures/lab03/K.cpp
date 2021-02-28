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

const int MAXN = 18;
int n, s;
int w[MAXN];
pair<int, int> dp[1 << MAXN];
int par[1 << MAXN];

/*
 * dp[mask] --- пара (a,b): минимальное количество полных рюкзаков 'a' и
 * на 'b' заполненного последнего рюкзака, заполняем предметами из 'mask'
 */

int main() {
    n = nxt(), s = nxt();
    for (int i = 0; i < n; i++) {
        cin >> w[i];
    }
    dp[0] = make_pair(0, 0);
    for (int mask = 1; mask < (1 << n); mask++) {
        dp[mask] = make_pair(INF, INF);
        for (int i = 0; i < n; i++) {
            if (mask >> i & 1) {
                int prev_mask = mask ^ (1 << i);
                int a = dp[prev_mask].first;
                int b = dp[prev_mask].second;
                b += w[i];
                if (b > s) {
                    a++;
                    b = w[i];
                }
                if (make_pair(a, b) < dp[mask]) {
                    dp[mask] = make_pair(a, b);
                    par[mask] = i;
                }
            }
        }
    }
    vector<vector<int> > backpacks;
    backpacks.push_back(vector<int>());
    int mask = (1 << n) - 1;
    while (mask != 0) {
        int prev_mask = mask ^ (1 << par[mask]);
        backpacks.back().push_back(par[mask] + 1);
        if (dp[mask].first != dp[prev_mask].first) {
            backpacks.push_back(vector<int>());
        }
        mask = prev_mask;
    }
    cout << backpacks.size() << "\n";
    for (const auto &item : backpacks) {
        cout << item.size() << " ";
        for (const auto &item1 : item) {
            cout << item1 << " ";
        }
        cout << "\n";
    }
    return 0;
}
