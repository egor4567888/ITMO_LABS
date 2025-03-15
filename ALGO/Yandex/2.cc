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
  std::string imp = "Impossible", pos = "Possible";
  std::unordered_map<char, int> open;
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
    if (open[shift(s[i])] == 0) {
      open[s[i]]++;
      st.push(std::make_pair(s[i], std::islower(s[i]) ? lowCount : upCount));
    } else {
      if (!st.empty() && st.top().first == shift(s[i])) {
        answer[std::isupper(s[i]) ? upCount - 1 : st.top().second - 1] =
            std::isupper(s[i]) ? st.top().second : lowCount;
        st.pop();
        open[shift(s[i])]--;
      } else {
        std::cout << imp << "\n";
        return 0;
      }
    }
  }
  if (!st.empty()) {
    std::cout << imp << "\n";
    return 0;
  }

  std::cout << pos << "\n";
  for (int in : answer) {
    std::cout << in << " ";
  }
  std::cout << std::endl;
  return 0;
}
// ABcCDAAAaaaaAdba
// ADAaaAda