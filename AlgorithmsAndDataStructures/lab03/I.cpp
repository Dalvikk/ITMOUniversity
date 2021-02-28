//
// Created by Vladislav Kovalchuk on 06.12.2020.
//

#include <iostream>
#include <vector>
#include <algorithm>
#include <cassert>

#define INF (int)1e9
#define endl '\n'
using namespace std;

inline int nxt() {
    int a;
    cin >> a;
    return a;
}

const int MAXN = 12;
int n, m;
long long d[MAXN + 1][1 << MAXN];
char ch[MAXN + 1][MAXN + 1];

/*
 * Это динамика по профилю, детка
 * Надеюсь зайдет, не хватало еще на изломанный профиль переписывать
 */
void calc(int x = 0, int y = 0, int mask = 0, int next_mask = 0) {
    if (y >= n) {
        d[x + 1][next_mask] += d[x][mask];
    } else {
        if (mask >> y & 1) {
            calc(x, y + 1, mask, next_mask);
        } else {
            if (ch[y][x + 1] != 'X') {
                calc(x, y + 1, mask, next_mask | (1 << y));
            }

            if (y + 1 < n && !(mask & ((1 << y) << 1))) {
                calc(x, y + 2, mask, next_mask);
            }
        }
    }
}

int mask_with_fill_x(int x) {
    if (x >= m) {
        return 0;
    }
    int mask = 0;
    for (int i = 0; i < n; i++) {
        if (ch[i][x] == 'X') {
            mask = mask | (1 << i);
        }
    }
    return mask;
}

int main() {
    ios_base::sync_with_stdio(0);
    cin.tie(0);
    cout.tie(0);
    cin >> n >> m;
    for (int i = 0; i < n; i++) {
        for (int j = 0; j < m; j++) {
            cin >> ch[i][j];
        }
    }
    d[0][mask_with_fill_x(0)] = 1;
    for (int x = 0; x < m; ++x) {
        for (int mask = 0; mask < (1 << n); ++mask) {
            int good_mask = mask_with_fill_x(x);
            int next_mask = mask_with_fill_x(x + 1);
            if ((good_mask & mask) == good_mask) {
                calc(x, 0, mask, next_mask);
            }
        }
    }
    cout << d[m][0] << "\n";
    return 0;
}
