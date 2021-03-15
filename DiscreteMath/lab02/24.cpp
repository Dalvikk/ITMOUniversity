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

vector<int> next_perm(vector<int> vector) {
    int pointer = vector.size() - 2;
    while (pointer >= 0 && vector[pointer] >= vector[pointer + 1]) {
        pointer--;
    }
    if (pointer == -1) {
        for (auto &item : vector) {
            item = 0;
        }
    } else {
        int mnIdx = pointer + 1;
        while (mnIdx < vector.size() - 1 && vector[mnIdx + 1] > vector[pointer]) {
            mnIdx++;
        }
        swap(vector[pointer], vector[mnIdx]);
        reverse(vector.begin() + pointer + 1, vector.end());
    }
    return vector;
}

void invert_perm(vector<int>& vector) {
    int n = vector.size() + 1;
    for (auto &item : vector) {
        if (item != 0) {
            item = n - item;
        }
    }
}

vector<int> prev_perm(vector<int> vector) {
    invert_perm(vector);
    vector = next_perm(vector);
    invert_perm(vector);
    return vector;
}

void print_vector(vector<int> a) {
    for (const auto &item : a) {
        cout << item << " ";
    }
    cout << "\n";
}

int main() {
    freopen("nextperm.in", "r", stdin);
    freopen("nextperm.out", "w", stdout);
    int n;
    cin >> n;
    vector<int> a(n);
    for (auto &item : a) {
        cin >> item;
    }
    print_vector(prev_perm(a));
    print_vector(next_perm(a));
    return 0;
}
