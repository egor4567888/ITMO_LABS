import math

def f(x):
    return x**2 - 2*x + math.exp(-x)

def f_prime(x):
    return 2*x - 2 - math.exp(-x)

def f_double_prime(x):
    return 2 + math.exp(-x)

# 1. Метод половинного деления (дихотомии)
def dichotomy_method(a, b, eps):
    delta = eps / 2  # малое значение для деления интервала
    while (b - a) > 2*eps:
        mid = (a + b) / 2
        div = b-a
        x1 = mid - delta
        x2 = mid + delta
        if f(x1) < f(x2):
            b = x2
        else:
            a = x1
    return (a + b) / 2

# 2. Метод Ньютона
def newton_method(x0, eps):
    x = x0
    while True:
        d1 = f_prime(x)
        d2 = f_double_prime(x)
        if d2 == 0:
            break
        x_new = x - d1 / d2
        if abs(x_new - x) < eps:
            break
        x = x_new
        
    return x

# 3. Метод золотого сечения
def golden_section_method(a, b, eps):
    phi = (math.sqrt(5) - 1) / 2  # 0.618...
    # Инициализация точек внутри отрезка
    x1 = a + (1 - phi) * (b - a)
    x2 = a + phi * (b - a)
    f1 = f(x1)
    f2 = f(x2)
    while (b - a) > eps:
        if f1 < f2:
            b = x2
            x2 = x1
            f2 = f1
            x1 = a + (1 - phi) * (b - a)
            f1 = f(x1)
        else:
            a = x1
            x1 = x2
            f1 = f2
            x2 = a + phi * (b - a)
            f2 = f(x2)
    return (a + b) / 2

# 4. Метод хорд 
def chord_method(x0, x1, eps):
    # Ищем корень уравнения f'(x) = 0 методом хорд (секущих)
    fprime_x0 = f_prime(x0)
    fprime_x1 = f_prime(x1)
    while abs(x1 - x0) > eps:
        # Предотвращаем деление на ноль
        if (fprime_x1 - fprime_x0) == 0:
            break
        x_new = x1 - f_prime(x1) * (x1 - x0) / (f_prime(x1) - f_prime(x0))
        x0, x1 = x1, x_new
        fprime_x0 = f_prime(x0)
        fprime_x1 = f_prime(x1)
    return x1

# 5. Метод квадратичной апроксимации
def quadratic_approximation(a, b, eps):
    x1 = a
    delta = (b-a)/2
    while True:
        x2 = x1+delta
        if(f(x1)>f(x2)):
            x3 = x1+2*delta
        else:
            x3 = x1-delta
        while True:
            f1 = f(x1)
            f2 = f(x2)
            f3 = f(x3)
            fmin = min(f1,min(f2,f3))
            if f1==fmin:
                xmin = x1
            if f2==fmin:
                xmin = x2
            if f3==fmin:
                xmin=x3

            if ((x2-x3)*f1+(x3-x1)*f2+(x1-x2)*f3) == 0:
                x1 = xmin
                break
            xline = 1/2*((x2**2-x3**2)*f1+(x3**2-x1**2)*f2+(x1**2-x2**2)*f3)/((x2-x3)*f1+(x3-x1)*f2+(x1-x2)*f3)
            fline = f(xline)
            
            if abs((fmin-fline)/fline) < 1e-5 and abs((xmin-xline)/xline)<eps:
                return xline
            
            if xline<x1 or xline>x3:
                x1 = xline
                break
            points = [x1, x2, x3, xline]
            points.sort()
            idx = points.index(xmin)
            new_x1 = points[idx - 1] if idx > 0 else points[idx]
            new_x3 = points[idx + 1] if idx < len(points) - 1 else points[idx]
            x1, x2, x3 = new_x1, xmin, new_x3
            
            


if __name__ == "__main__":
    a = 1
    b = 1.5
    eps = 0.05

    result_dichotomy = dichotomy_method(a, b, eps)
    result_newton = newton_method((a+b)/2, eps)
    result_golden = golden_section_method(a, b, eps)
    result_chord = chord_method(a, b, eps)
    result_quadratic = quadratic_approximation(a, b, eps)

    print("Метод половинного деления:", result_dichotomy, "f(x) =", f(result_dichotomy))
    print("Метод Ньютона:", result_newton, "f(x) =", f(result_newton))
    print("Метод золотого сечения:", result_golden, "f(x) =", f(result_golden))
    print("Метод хорд:", result_chord, "f(x) =", f(result_chord))
    print("Метод квадратичной апроксимации:", result_quadratic, "f(x) =", f(result_quadratic))