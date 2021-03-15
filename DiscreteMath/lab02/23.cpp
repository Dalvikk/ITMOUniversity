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

string next_vector(string vector) {
    int last = -1;
    for (int i = vector.size() - 1; i >= 0; i--) {
        if (vector[i] == '0') {
            last = i;
            break;
        }
    }
    if (last == -1) {
        return "-";
    }
    vector[last] = '1';
    for (int i = last + 1; i < vector.size(); i++) {
        vector[i] = '0';
    }
    return vector;
}

void invert_vector(string& vector) {
    for (auto &item : vector) {
        if (item == '1') {
            item = '0';
        } else if (item == '0') {
            item = '1';
        }
    }
}

string prev_vector(string vector) {
    invert_vector(vector);
    vector = next_vector(vector);
    invert_vector(vector);
    return vector;
}

int main() {
    freopen("nextvector.in", "r", stdin);
    freopen("nextvector.out", "w", stdout);
    string s;
    cin >> s;
    cout << prev_vector(s) << "\n";
    cout << next_vector(s) << "\n";
    return 0;
}
