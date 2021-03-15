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

void gen(string s, int open, int closed) {
    if (open == 0 && closed == 0) {
        cout << s << "\n";
        return;
    }
    if (open != 0) {
        gen(s + "(", open - 1, closed);
    }
    if (closed != 0 && open < closed) {
        gen(s + ")", open, closed - 1);
    }
}


int main() {
    freopen("brackets.in", "r", stdin);
    freopen("brackets.out", "w", stdout);
    int n = nxt();
    gen("", n, n);
    return 0;
}