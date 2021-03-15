//
// Created by Vladislav Kovalchuk on 04.12.2020.
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

int n, k;
vector<int> sets;

void gen(int current_num, int num_sets) {
    if (current_num == n) {
        if (num_sets != k) {
            return;
        }
        vector<vector<int>> unique(k);
        for (int i = 0; i < n; i++) {
            unique[sets[i]].push_back(i + 1);
        }
        for (int i = 0; i < k; i++) {
            for (int j = 0; j < unique[i].size(); j++) {
                cout << unique[i][j] << " ";
            }
            cout << "\n";
        }
        cout << "\n";
    } else {
        for (int i = 0; i <= num_sets; i++) {
            sets[current_num] = i;
            if (i == num_sets) {
                gen(current_num + 1, num_sets + 1);
            } else {
                gen(current_num + 1, num_sets);
            }
        }
    }
}

void solve() {
    cin >> n >> k;
    sets.resize(n);
    gen(0, 0);
}

int main() {
    freopen("part2sets.in", "r", stdin);
    freopen("part2sets.out", "w", stdout);
    solve();
    return 0;
}
