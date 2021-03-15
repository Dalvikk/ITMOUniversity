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

bool next_correct_bracket_sequence(string &s) {
    int open_count = 0;
    int close_count = 0;
    for (int i = s.size() - 1; i >= 0; i--) {
        if (s[i] == '(') {
            open_count++;
            if (close_count > open_count) {
                break;
            }
        } else {
            close_count++;
        }
    }
    if (open_count + close_count == s.size()) {
        return false;
    }
    int pointer = s.size() - open_count - close_count;
    s[pointer++] = ')';
    for (int i = 0; i < open_count; i++) {
        s[pointer++] = '(';
    }
    for (int i = 0; i < close_count; ++i) {
        s[pointer++] = ')';
    }
    return true;
}

int main() {
    freopen("nextbrackets.in", "r", stdin);
    freopen("nextbrackets.out", "w", stdout);
    string s;
    cin >> s;
    if (next_correct_bracket_sequence(s)) {
        cout << s << "\n";
    } else {
        cout << "-\n";
    }
    return 0;
}
