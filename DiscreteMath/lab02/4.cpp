//
// Created by Vladislav Kovalchuk on 23.11.2020.
//

#include <iostream>
#include <vector>
#include <algorithm>

using namespace std;

int main() {
    freopen("chaincode.in", "r", stdin);
    freopen("chaincode.out", "w", stdout);
    int n; cin >> n;
    int current = 0;
    int mask = (1 << n) - 1;
    vector<bool> have(1 << n, false);
    vector<int> result;
    result.push_back(current);
    have[current] = true;

    while (true) {
        current = (current << 1) & mask;
        if (!have[current + 1]) {
            current += 1;
        } else if (!have[current]) {
            //for non breaking
        } else {
            break;
        }
        have[current] = true;
        result.push_back(current);
    }

    for (int i = 0; i < result.size(); i++) {
        for (int j = n - 1; j >= 0; j--) { // print_mask
            cout << (result[i] >> j & 1);
        }
        cout << endl;
    }
    return 0;
}