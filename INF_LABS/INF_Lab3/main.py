#8<O
import re
regexp = r"8<0"
test1 = "8<0" #1
test2 ="kja,/.421a8<0n8>0149-128<0" #2
test3 = "8<вршднгРГ1Е    238<0" #1
test4 = "0>8<0<<08>0<888<0>>00" #2
test5 = "" #0
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
    print("Найдено смайликов: " + str(len(re.findall(regexp, test1))))
elif n==2:
    print("Найдено смайликов: " + str(len(re.findall(regexp, test2))))
elif n==3:
    print("Найдено смайликов: " + str(len(re.findall(regexp, test3))))
elif(n==4):
    print("Найдено смайликов: " + str(len(re.findall(regexp, test4))))
elif(n==5):
    print("Найдено смайликов: " + str(len(re.findall(regexp, test5))))


