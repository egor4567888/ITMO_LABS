import re

test1 = "Студент Вася вспомнил, что на своей лекции Балакшин П.-В.А. упоминал про старшекурсников, которые будут ему помогать: Анищенко А., Машина Е-К.А. и Голованова-Иванова Д.В." #Анищенко Балакшин Голованова-Иванова Машина
test2 = "Отец мой Лондоньо Гринев А.П. в молодости своей служил при графе Минихе и вышел в отставку премьер-майором в 17.. году." #Гринёв
test3 = "Часть своего времени в эти годы я посвящал именно изучению истории науки. С особым интересом я продолжал изучать работы Койре А. и впервые обнаружил работы Мейерсона Э., Мецгер Е. и  Майер А." #Койре Майер Мейерсона Мецгер
test4 = "In particular I continued to study the writings of Koyre A. and first encountered those of Meyerson E., Metzger H., and Maier A.." #Koyre Maier Metzger Meyerson
test5 = ""
test = ""
print("Список тестов:")
print("1)"+test1)
print("2)"+test2)
print("3)"+test3)
print("4)"+test4)
print("5)"+test5)
print("Введите номер теста")
n = input()
if (len(n)!=1 or n[0]>"5" or n[0]<"1"):
    print("Ошибка ввода. Введите число от 1 до 5")
    SystemExit
n = int(n)
if n==1:
    test=test1
if n==2:
    test=test2
if n==3:
    test=test3
if n==4:
    test=test4
if n==5:
    test=test5
if len(re.findall("([А-ЯA-Z][а-яa-z]+-?([А-ЯA-Z][а-яa-z]*)?\s[А-ЯA-Z]\.-?([А-ЯA-Z]\.)*)",test))==0:
    print("Фамилии не найдены")
else:
    print("Найденные фамилии:")
    for a in (sorted(re.findall("[А-ЯA-Z]{1}[а-яa-z]+-?[А-ЯA-Z]?[а-яa-z]+\s",str(re.findall("([А-ЯA-Z][а-яa-z]+-?([А-ЯA-Z][а-яa-z]*)?\s[А-ЯA-Z]\.-?([А-ЯA-Z]\.)*)",test))))):
        print (a)
