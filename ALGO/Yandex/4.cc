#include <iostream>
#include <string>

int main() {
  long a, b, c, d, k, prev;
  std::cin >> a >> b >> c >> d >> k;
  long cur = a;
  if ((a * b - c > 0 && a * b - c >= d && d * b - c >= d)) {
    std::cout << d << "\n";
    return 0;
  }
  for (int i = 0; i < k; i++) {
    prev = cur;
    cur *= b;
    if (cur <= c) {
      std::cout << 0 << "\n";
      return 0;
    }
    cur -= c;
    if (cur > d)
      cur = d;

    if (cur == prev) {
      std::cout << cur << "\n";
      return 0;
    }
  }

  std::cout << cur << "\n";

  return 0;
}