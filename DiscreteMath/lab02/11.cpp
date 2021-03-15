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

void gen(string str, int n, int last) {
    cout << str << endl;
    if (last == n) {
        return;
    }
    for (int i = last + 1; i <= n; i++) {
        gen(str + to_string(i) + " ", n, i);
    }
}


int main() {
    freopen("subsets.in", "r", stdin);
    freopen("subsets.out", "w", stdout);
    int n = nxt();
    gen("", n, 0);
    return 0;
}
