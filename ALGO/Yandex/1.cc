#include <iostream>
#include <string>

int main() {
  long n = 0;
  std::cin >> n;
  long a;
  long prev = 0;
  long cur_start = 0;
  long max_start = 0;
  long max_end = 0;
  bool eqToPrev = 0;

  for (int i = 0; i < n; i++) {
    std::cin >> a;
    if (i + 1 == n && (a != prev || !eqToPrev)) {
      if (i - cur_start > max_end - max_start) {
        max_start = cur_start;
        max_end = i;
      }
    }

    if (a == prev) {
      if (eqToPrev) {
        if (i - 1 - cur_start > max_end - max_start) {
          max_start = cur_start;
          max_end = i - 1;
        }
        cur_start = i - 1;
      } else {
        eqToPrev = 1;
      }
    } else {
      eqToPrev = 0;
    }

    prev = a;
  }
  std::cout << max_start + 1 << " " << max_end + 1 << std::endl;

  return 0;
}