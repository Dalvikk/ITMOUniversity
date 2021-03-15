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

void gen(string str, int n, int mx) {
    if (n == 0) {
        for (int i = 1; i < str.size(); ++i) {
            cout << str[i];
        }
        cout << "\n";
        return;
    }
    for (int i = mx; i <= n; i++) {
        gen(str + "+" + to_string(i), n - i, i);
    }
}


int main() {
    freopen("partition.in", "r", stdin);
    freopen("partition.out", "w", stdout);
    int n = nxt();
    gen("", n, 1);
    return 0;
}
