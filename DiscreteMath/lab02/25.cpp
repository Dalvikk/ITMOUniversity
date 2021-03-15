//
// Created by Vladislav Kovalchuk on 26.11.2020.
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

bool next_choose(vector<int>& a, int n, int k) {
    a.push_back(n + 1);
    int pointer = k - 1;
    while (pointer >= 0 && a[pointer + 1] - a[pointer] < 2) {
        pointer--;
    }
    if (pointer == -1) {
        return false;
    }
    a[pointer]++;
    for (int i = pointer + 1; i + 1 < a.size(); i++) {
        a[i] = a[i-1] + 1;
    }
    a.pop_back();
    return true;
}

int main() {
    freopen("nextchoose.in", "r", stdin);
    freopen("nextchoose.out", "w", stdout);
    int n = nxt(), k = nxt();
    vector<int> a(k);
    for (auto &item : a) {
        cin >> item;
    }
    if (next_choose(a, n, k)) {
        for (const auto &item : a) {
            cout << item << " ";
            cout << "\n";
        }
    } else {
        cout << -1 << "\n";
    }
    return 0;
}
