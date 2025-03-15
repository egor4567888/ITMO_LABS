#include <algorithm>
#include <iostream>
#include <string>
#include <unordered_map>
#include <vector>

// int getElement(
//     const std::unordered_map<std::string, std::vector<int>>& hashMap,
//     const std::string& key,
//     size_t index
// ) {
//   auto it = hashMap.find(key);
//   if (it != hashMap.end() && !it->second.empty()) {
//     const std::vector<int>& vec = it->second;
//     return (index < vec.size()) ? vec[index] : vec.back();
//   }

//   return 0;
// }
// void addElement(
//     std::unordered_map<std::string, std::vector<int>>& hashMap,
//     const std::string& key,
//     size_t index,
//     int value
// ) {
//   auto& vec = hashMap[key];
//   int fillVal = 0;
//   if (!vec.empty()) {
//     fillVal = vec.back();
//   }
//   if (vec.size() <= index) {
//     vec.resize(index + 1, fillVal);
//   }
//   vec[index] = value;
// }

bool isNumber(const std::string& str) {
  if (str.empty())
    return false;

  if (str.size() == 0)
    return false;
  size_t start = 0;
  if (str[0] == '-') {
    start++;
  }

  return std::all_of(str.begin() + start, str.end(), ::isdigit);
}

struct StackRecord {
  std::string var;
  int oldVal;
};

int main() {
  std::unordered_map<std::string, int> vars;
  std::vector<StackRecord> history;
  std::vector<size_t> blockMarkers;

  std::string line;

  std::string firstPart;
  std::string secondPart;
  while (std::getline(std::cin, line)) {
    if (line == "{") {
      blockMarkers.push_back(history.size());
      continue;
    }

    if (line == "}") {
      if (!blockMarkers.empty()) {
        size_t indx = blockMarkers.back();
        blockMarkers.pop_back();
        while (history.size() > indx) {
          StackRecord rec = history.back();
          history.pop_back();
          vars[rec.var] = rec.oldVal;
        }
      }

      continue;
    }

    size_t pos = line.find('=');
    std::string firstPart = line.substr(0, pos);
    std::string secondPart = line.substr(pos + 1);
    int current = vars.count(firstPart) ? vars[firstPart] : 0;
    if (isNumber(secondPart)) {
      history.push_back({firstPart, current});
      vars[firstPart] = std::stoi(secondPart);
    } else {
      history.push_back({firstPart, current});
      int newVal = vars.count(secondPart) ? vars[secondPart] : 0;
      vars[firstPart] = newVal;
      std::cout << newVal << "\n";
    }
  }

  return 0;
}