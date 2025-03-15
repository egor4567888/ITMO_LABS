import math

# Определяем функции системы
def f(x, y):
    eq1 = x**2 - math.tan(x*y + 0.3)
    eq2 = 0.9*x**2 + 2*y**2 - 1
    return (eq1, eq2)

# Определяем якобиан системы
def jacobian(x, y):
    # Для eq1: f1 = x^2 - tan(x*y + 0.3)
    # f1_x = 2x - y * sec^2(x*y+0.3) ,  f1_y = - x * sec^2(x*y+0.3)
    cos_val = math.cos(x*y + 0.3)
    sec2 = 1/(cos_val**2)
    f1_x = 2*x - y * sec2
    f1_y = - x * sec2

    # Для eq2: f2 = 0.9x^2 + 2y^2 - 1
    f2_x = 1.8*x
    f2_y = 4*y

    return ((f1_x, f1_y), (f2_x, f2_y))

# Решаем систему методом Ньютона и сохраняем промежуточные результаты
def newton_system(x0, y0, tol=1e-2, max_iter=50):
    points = [(x0, y0)]
    x, y = x0, y0
    for i in range(max_iter):
        F1, F2 = f(x, y)
        (J11, J12), (J21, J22) = jacobian(x, y)

        # Определитель якобиана
        det = J11 * J22 - J12 * J21
        if abs(det) < 1e-12:
            print("Вычислительный детерминант слишком мал.")
            break

        # Вычисляем приращения dX и dY с помощью обратной матрицы
        dX = ( J22 * F1 - J12 * F2) / det
        dY = (-J21 * F1 + J11 * F2) / det

        # Обновляем приближения
        x_new = x - dX
        y_new = y - dY

        points.append((x_new, y_new))

        # Проверка условия останова
        if math.sqrt((x_new - x)**2 + (y_new - y)**2) < tol:
            return points

        x, y = x_new, y_new
    return points

if __name__ == '__main__':
    # Начальное приближение (подберите его при необходимости)
    x0, y0 = 0.7, 0.4
    iterations = newton_system(x0, y0)
    
    # Генерация LaTeX кода для таблицы
    table = []
    table.append(r"\begin{tabular}{|c|c|c|c|c|c|c|}")
    table.append(r"\hline")
    table.append(r"Iteration & $x$ & $y$ & $f_1$ & $f_2$ & $\Delta x$ & $\Delta y$ \\")
    table.append(r"\hline")

    x_old, y_old = None, None
    for idx, (x_val, y_val) in enumerate(iterations):
        eq1, eq2 = f(x_val, y_val)
        if idx == 0:
            dx, dy = 0, 0
        else:
            dx = abs(x_val - x_old)
            dy = abs(y_val - y_old)
        table.append(f"{idx} & {x_val:.6f} & {y_val:.6f} & {eq1:.6f} & {eq2:.6f} & {dx:.6f} & {dy:.6f} \\\\")
        table.append(r"\hline")
        x_old, y_old = x_val, y_val

    table.append(r"\end{tabular}")

    # Вывод полученной таблицы в виде LaTeX кода
    print("\n".join(table))