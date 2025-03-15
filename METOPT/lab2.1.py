import math

def f(x):
    return x**2 - 2*x + math.exp(-x)

def f_prime(x):
    return 2*x - 2 - math.exp(-x)

def f_double_prime(x):
    return 2 + math.exp(-x)

# 1. Метод половинного деления (дихотомии) с выводом первых 3 итераций
def dichotomy_method_iterations(a, b, eps, iterations=3):
    delta = eps / 2
    print("Метод половинного деления:")
    for i in range(1, iterations+1):
        mid = (a + b) / 2
        x1 = mid - delta
        x2 = mid + delta
        print(f" Итерация {i}: x1 = {x1:.3f}, x2 = {x2:.3f}, y1 = {f(x1):.3f}, y2 = {f(x2):.3f}, b-a = {b-a:.3f}")
        if f(x1) < f(x2):
            b = x2
        else:
            a = x1
    return (a + b) / 2

# 2. Метод Ньютона с выводом первых 3 итераций
def newton_method_iterations(x0, eps, iterations=3):
    print("Метод Ньютона:")
    x = x0
    for i in range(1, iterations+1):
        d1 = f_prime(x)
        d2 = f_double_prime(x) 
        if d2 == 0:
            print(f" Итерация {i}: Производная второго порядка равна нулю, остановка.")
            break
        x_new = x - d1/d2
        print(f"производная {i} {d1} {d2}")
        print(f" Итерация {i}: x = {x:.3f}, f(x) = {f(x):.3f}")
        if abs(x_new - x) < eps:
            x = x_new
            break
        x = x_new
    return x

# 3. Метод золотого сечения с выводом первых 3 итераций
def golden_section_method_iterations(a, b, eps, iterations=3):
    phi = (math.sqrt(5) - 1) / 2  # приблизительно 0.618
    x1 = a + (1 - phi) * (b - a)
    x2 = a + phi * (b - a)
    f1 = f(x1)
    f2 = f(x2)
    print("Метод золотого сечения:")
    for i in range(1, iterations+1):
        print(f" Итерация {i}: a = {a:.3f}, b = {b:.3f}, x1 = {x1:.3f}, f(x1) = {f1:.3f}, x2 = {x2:.3f}, f(x2) = {f2:.3f}")
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

# 4. Метод хорд с выводом первых 3 итераций
def chord_method_iterations(x0, x1, eps, iterations=3):
    print("Метод хорд:")
    for i in range(1, iterations+1):
        fprime_x0 = f_prime(x0)
        fprime_x1 = f_prime(x1)
        if (fprime_x1 - fprime_x0) == 0:
            print(f" Итерация {i}: Деление на ноль, остановка.")
            break
        x_new = x1 - f_prime(x1) * (x1 - x0) / (f_prime(x1) - f_prime(x0))
        print(f" Итерация {i}: x0 = {x0:.3f}, x1 = {x1:.3f}, x_new = {x_new:.3f}, f(x_new) = {f(x_new):.3f}")
        x0, x1 = x1, x_new
    return x1


# 5. Метод квадратичной апроксимации с выводом первых 3 итераций
def quadratic_approximation_iterations(a, b, eps, iterations=3):
    # Инициализируем первую точку и шаг delta
    x1 = a
    delta = (b - a) / 2
    x_new = None
    print("Метод квадратичной апроксимации:")
    for i in range(1, iterations+1):
        # Формируем дополнительные точки
        x2 = x1 + delta
        if f(x1) > f(x2):
            x3 = x1 + 2 * delta
        else:
            x3 = x1 - delta
        
        f1 = f(x1)
        f2 = f(x2)
        f3 = f(x3)
        
        # Определяем точку с минимальным значением функции
        if f1 <= f2 and f1 <= f3:
            xmin = x1
        elif f2 <= f1 and f2 <= f3:
            xmin = x2
        else:
            xmin = x3
        
        denom = (x2 - x3)*f1 + (x3 - x1)*f2 + (x1 - x2)*f3
        if abs(denom) < 1e-10:
            x_new = xmin
        else:
            x_new = 0.5 * (((x2**2 - x3**2)*f1 + (x3**2 - x1**2)*f2 + (x1**2 - x2**2)*f3) / denom)
        
        print(f" Итерация {i}: x1 = {x1:.3f}, x2 = {x2:.3f}, x3 = {x3:.3f}, x_new = {x_new:.3f}, f(x_new) = {f(x_new):.3f}")
        
        # Обновляем стартовую точку для следующей итерации
        if x_new < x1 or x_new > x3:
            x1 = x_new
        else:
            x1 = min(xmin, x_new)
    return x_new


if __name__ == "__main__":
    a = 1
    b = 1.5
    eps = 0.05

    print("Результаты первых 3 итераций каждого метода:\n")

    res_dichotomy = dichotomy_method_iterations(a, b, eps, iterations=3)
    print(" Промежуточный результат (Метод половинного деления):", res_dichotomy, "f(x) =", f(res_dichotomy))
    print()

    res_newton = newton_method_iterations((a + b) / 2, eps, iterations=3)
    print(" Промежуточный результат (Метод Ньютона):", res_newton, "f(x) =", f(res_newton))
    print()

    res_golden = golden_section_method_iterations(a, b, eps, iterations=3)
    print(" Промежуточный результат (Метод золотого сечения):", res_golden, "f(x) =", f(res_golden))
    print()

    res_chord = chord_method_iterations(a, b, eps, iterations=3)
    print(" Промежуточный результат (Метод хорд):", res_chord, "f(x) =", f(res_chord))
    print()

    res_quadratic = quadratic_approximation_iterations(a, b, eps, iterations=3)
    print(" Промежуточный результат (Метод квадратичной апроксимации):", res_quadratic, "f(x) =", f(res_quadratic))