//
// Created by Vladislav Kovalchuk on 17.11.2020.
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
#define    INF (int)1e9
#define    INFL (long long)1e18
#define    ll long long
#define    endl '\n'
using namespace std;

inline int nxt() {
    int a;
    cin >> a;
    return a;
}

void print_mask(int mask, int sz) {
    for (int i = sz - 1; i >= 0; i--) {
        cout << (mask >> i & 1);
    }
    cout << endl;
}

vector<string> anti_gray(int n) {
    vector<string> ans;
    string num[] = {"0", "1", "2"};
    if (n == 1) {
        ans = {"0", "1", "2"};
    } else {
        vector<string> gn = anti_gray(n - 1);
        for (int i = 0; i < 3; i++) {
            int cnt = i;
            for (const auto &item : gn) {
                ans.push_back(num[cnt % 3] + item);
                cnt++;
            }
        }
    }
    return ans;
}

int main() {
    freopen("antigray.in", "r", stdin);
    freopen("antigray.out", "w", stdout);
    int n = nxt();
    vector<string> ans = anti_gray(n);
    for (const auto &item : ans) {
        cout << item << "\n";
    }
    return 0;
}
