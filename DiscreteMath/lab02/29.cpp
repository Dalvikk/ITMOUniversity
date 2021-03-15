//
// Created by Vladislav Kovalchuk on 26.11.2020.
//

#include <iostream>
#include <vector>
#include <algorithm>
#include <string.h>
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

int n;
vector<int> a;

bool next_partition(vector<int> &a) {
    if (a.size() == 1) {
        return false;
    }
    a.back()--;
    a[a.size() - 2]++;
    if (a[a.size() - 2] > a.back()) {
        a[a.size() - 2] += a.back();
        a.pop_back();
    } else {
        while (a[a.size() - 2] * 2 <= a.back()) {
            a.push_back(a.back() - a[a.size() - 2]);
            a[a.size() - 2] = a[a.size() - 3];
        }
    }
    return true;
}


void print_vector(vector<int> a) {
    for (const auto &item : a) {
        cout << item << " ";
    }
    cout << "\n";
}

void in() {
    freopen("nextpartition.in", "r", stdin);
    freopen("nextpartition.out", "w", stdout);
    char str[200001];
    scanf("%d=%s", &n, str);
    char * pch = strtok(str, "+");
    while (pch != NULL) {
        a.push_back(atoi(pch));
        pch = strtok(NULL, "+");
    }
}

int main() {
    in();
    if (next_partition(a)) {
        cout << n << "=" << a[0];
        for (int i = 1; i < a.size(); i++) {
            cout << "+" << a[i];
        }
        cout << endl;
    } else {
        cout << "No solution\n";
    }
    return 0;
}
