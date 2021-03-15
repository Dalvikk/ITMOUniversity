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
    gen(s + "0", n);
    if (s.empty() || s.back() == '0') {
        gen(s + "1", n);
    }
}


int main() {
    freopen("vectors.in", "r", stdin);
    freopen("vectors.out", "w", stdout);
    int n = nxt();
    gen("", n);
    cout << ans.size() << "\n";
    for (const auto &item : ans) {
        cout << item << "\n";
    }
    return 0;
}
