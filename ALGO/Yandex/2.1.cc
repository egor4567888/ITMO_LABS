#include <algorithm>
#include <iostream>
#include <stack>
#include <string>
#include <unordered_map>
#include <vector>

bool isMatching(char a, char b) {
  return ((std::isupper(a) && std::tolower(a) == b) || (std::islower(a) && std::toupper(a) == b));
}
char shift(char a) {
  if (std::isupper(a))
    return std::tolower(a);
  else
    return std::toupper(a);
}
int main() {
  std::stack<std::pair<char, int>> st;

  std::string s;
  std::cin >> s;
  int n = s.size();
  std::vector<int> answer(n / 2);
  int upCount = 0;
  int lowCount = 0;
  for (int i = 0; i < n; i++) {
    if (std::islower(s[i]))
      lowCount++;
    else
      upCount++;

    if (!st.empty() && st.top().first == shift(s[i])) {
      answer[std::isupper(s[i]) ? upCount - 1 : st.top().second - 1] =
          std::isupper(s[i]) ? st.top().second : lowCount;
      st.pop();
    } else {
      if (std::islower(s[i])) {
        st.push(std::make_pair(s[i], lowCount));
      } else {
        st.push(std::make_pair(s[i], upCount));
      }
    }
  }
  if (!st.empty()) {
    std::cout << "Impossible\n";
    return 0;
  }

  std::cout << "Possible\n";
  for (int in : answer) {
    std::cout << in << " ";
  }
  std::cout << std::endl;
  return 0;
}
