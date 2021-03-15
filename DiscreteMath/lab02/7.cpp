//
// Created by Vladislav Kovalchuk on 23.11.2020.
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

vector<string> ans;

void gen(string s, int n) {
    if (s.size() == n) {
        ans.push_back(s);
        return;
    }
    for (int i = 1; i <= n; i++) {
        bool can = true;
        for (int j = 0; j < s.size(); j++) {
            if ((s[j] - '0') == i) {
                can = false;
                break;
            }
        }
        if (can) {
            gen(s + (char)('0' + i), n);
        }
    }
}



int main() {
    freopen("permutations.in", "r", stdin);
    freopen("permutations.out", "w", stdout);
    int n = nxt();
    gen("", n);
    for (const auto &item : ans) {
        for (const auto &item2 : item) {
            cout << item2 << " ";
        }
        cout << "\n";
    }
    return 0;
}
