import re
test1 = "students.@spam.com@yandex.ru" #yandex.ru
test2 = "example@example.." #Fail!
test3 = "....@gm_ail.com" #Fail!
test4 = "google@niu.i.t.m.o.ru" #niuitmo.ru
test5 = "" #Fail!
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
if len(re.findall(r"^[\w\.]+[\w]+@[a-zA-Z\.]+[a-zA-Z]+$",test)) ==0:
    print("Fail!")
else:
    print(str(re.findall(r"@[a-zA-Z\.]+$",test)[0])[1:])

