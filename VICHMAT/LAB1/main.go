package main

import (
	"bufio"
	"fmt"
	"math/big"
	"math/rand"
	"os"
	"strconv"
	"strings"
	"time"

	"gonum.org/v1/gonum/mat"
)

func newBigFloat(value float64) *big.Float {
    return new(big.Float).SetPrec(256).SetFloat64(value)
}

func toTriangle(matrix *[][]*big.Float) (bool, bool) {
    var flag bool = false
    var max *big.Float = newBigFloat(0)
    var maxRow int = -1
    for n := 0; n < len(*matrix)-1; n++ {
        max.SetFloat64(0)
        for i := n; i < len(*matrix); i++ {
            absVal := new(big.Float).SetPrec(256).Abs((*matrix)[i][n])
            if absVal.Cmp(max) > 0 {
                max.Set(absVal)
                maxRow = i
            }
        }

        if max.Cmp(newBigFloat(0)) == 0 {
            return false, flag // Матрица вырождена, система не имеет единственного решения
        }
        if n != maxRow {
            flag = !flag
            (*matrix)[n], (*matrix)[maxRow] = (*matrix)[maxRow], (*matrix)[n]
        }

        for i := n + 1; i < len(*matrix); i++ {
            for j := len(*matrix); j >= n; j-- {
                x := new(big.Float).SetPrec(256).Quo(new(big.Float).SetPrec(256).Mul((*matrix)[i][n], (*matrix)[n][j]), (*matrix)[n][n])
                (*matrix)[i][j].Sub((*matrix)[i][j], x)
            }
        }
    }
    return true, flag
}

func solve(matrix *[][]*big.Float) ([]*big.Float, bool) {
    n := len(*matrix)
    x := make([]*big.Float, n)

    // Обратный ход метода Гаусса
    for i := n - 1; i >= 0; i-- {
        if (*matrix)[i][i].Cmp(newBigFloat(0)) == 0 {
            return nil, false // Система не имеет единственного решения
        }
        x[i] = new(big.Float).SetPrec(256).Set((*matrix)[i][n]) // Начинаем с правой части уравнения
        for j := i + 1; j < n; j++ {
            x[i].Sub(x[i], new(big.Float).SetPrec(256).Mul((*matrix)[i][j], x[j])) // Вычитаем известные переменные
        }
        x[i].Quo(x[i], (*matrix)[i][i]) // Делим на коэффициент при текущей переменной
    }

    return x, true
}

func main() {
    fmt.Println("Введите 0 для ввода из консоли, 1 для использования файла или 2 для генерации случайной матрицы")
    var flag int
    _, err := fmt.Scan(&flag)
    if err != nil || (flag != 0 && flag != 1 && flag != 2) {
        fmt.Println("Ошибка ввода")
        return
    }

    var n int
    var useFile bool
    var fileName string
    if flag == 1 {
        useFile = true
        fmt.Println("Введите имя файла")
        _, err := fmt.Scan(&fileName)
        if err != nil {
            fmt.Println("Ошибка ввода имени файла")
            return
        }
        file, err := os.Open(fileName)
        if err != nil {
            fmt.Println("Ошибка открытия файла")
            return
        }
        defer file.Close()
        scanner := bufio.NewScanner(file)
        if !scanner.Scan() {
            fmt.Println("Ошибка чтения файла")
            return
        }
        n, err = strconv.Atoi(scanner.Text())
        if err != nil || n > 20 || n < 1 {
            fmt.Println("Ошибка ввода размера матрицы")
            return
        }
    } else if flag == 0 {
        useFile = false
        fmt.Println("Введите размер матрицы, не более 20")
        _, err := fmt.Scan(&n)
        if err != nil || n > 20 || n < 1 {
            fmt.Println("Ошибка ввода")
            return
        }
    } else if flag == 2 {
        useFile = false
        fmt.Println("Введите размер матрицы, не более 20")
        _, err := fmt.Scan(&n)
        if err != nil || n > 20 || n < 1 {
            fmt.Println("Ошибка ввода")
            return
        }
    }

    matrix := make([][]*big.Float, n)
    for i := range matrix {
        matrix[i] = make([]*big.Float, n+1)
    }

    if useFile {
        file, err := os.Open(fileName)
        if err != nil {
            fmt.Println("Ошибка открытия файла")
            return
        }
        defer file.Close()
        scanner := bufio.NewScanner(file)
        scanner.Scan() // Пропускаем первую строку с размером матрицы
        for i := 0; i < n; i++ {
            if !scanner.Scan() {
                fmt.Println("Ошибка чтения файла")
                return
            }
            line := scanner.Text()
            values := strings.Split(line, " ")
            for j := 0; j <= n; j++ {
                value := strings.Replace(values[j], ",", ".", -1)
                var ok bool
                matrix[i][j], ok = new(big.Float).SetPrec(256).SetString(value)
                if !ok {
                    fmt.Println("Ошибка преобразования числа")
                    return
                }
            }
        }
    } else if flag == 0 {
        scanner := bufio.NewScanner(os.Stdin)
        for i := 0; i < n; i++ {
            if !scanner.Scan() {
                fmt.Println("Ошибка чтения ввода")
                return
            }
            line := scanner.Text()
            values := strings.Split(line, " ")

    if len(values) != n+1 {
        fmt.Println("Ошибка: неверное количество элементов в строке")
        return
    }
            for j := 0; j <= n; j++ {
                value := strings.Replace(values[j], ",", ".", -1)
                var ok bool
                matrix[i][j], ok = new(big.Float).SetPrec(256).SetString(value)
                if !ok {
                    fmt.Println("Ошибка преобразования числа")
                    return
                }
            }
        }
    } else if flag == 2 {
        rand.Seed(time.Now().UnixNano())
        for i := 0; i < n; i++ {
            for j := 0; j <= n; j++ {
                matrix[i][j] = new(big.Float).SetPrec(256).SetFloat64(rand.Float64() * 100)
            }
    
        }
        fmt.Println("Матрица:")
        for _, row := range matrix {
            for _, val := range row {
                fmt.Print(val, " ")
            }
            fmt.Println()
        }
    }

    originalMatrix := make([][]*big.Float, n)
    for i := range matrix {
        originalMatrix[i] = make([]*big.Float, n+1)
        for j := range matrix[i] {
            originalMatrix[i][j] = new(big.Float).SetPrec(256).Set(matrix[i][j])
        }
    }
    var e bool
    var negDet bool

    e, negDet = toTriangle(&matrix)
    if !e {
        fmt.Println("Система не имеет единственного решения (матрица вырождена)")
        return
    }
    fmt.Println("Матрица в треугольном виде:")
    for _, row := range matrix {
        for _, val := range row {
            fmt.Print(val, " ")
        }
        fmt.Println()
    }
    solution, ok := solve(&matrix)
    if !ok {
        fmt.Println("Система не имеет единственного решения")
        return
    }
    fmt.Println("x: ")
    for _, val := range solution {
        fmt.Println(val)
    }

    residuals := make([]*big.Float, n)
    for i := 0; i < n; i++ {
        residuals[i] = new(big.Float).SetPrec(256).Neg(originalMatrix[i][n])
        for j := 0; j < n; j++ {
            residuals[i].Add(residuals[i], new(big.Float).SetPrec(256).Mul(originalMatrix[i][j], solution[j]))
        }
    }

    fmt.Println("Вектор невязок для метода Гаусса:")
    for i, val := range residuals {
        fmt.Println("Невязка для уравнения " + strconv.Itoa(i+1) + ":", val)
    }

    // Использование библиотеки gonum/mat для решения системы и вычисления определителя
    data := make([]float64, n*n)
    b := make([]float64, n)
    for i := 0; i < n; i++ {
        for j := 0; j < n; j++ {
            data[i*n+j], _ = originalMatrix[i][j].Float64()
        }
        b[i], _ = originalMatrix[i][n].Float64()
    }

    A := mat.NewDense(n, n, data)
    B := mat.NewVecDense(n, b)
    var X mat.VecDense

    err = X.SolveVec(A, B)
    if err != nil {
        fmt.Println("Ошибка при решении системы:", err)
        return
    }

    fmt.Println("Решение с использованием библиотеки gonum/mat:")
    for i := 0; i < n; i++ {
        fmt.Println("x" + strconv.Itoa(i+1) + " =", X.AtVec(i))
    }

    // Вычисление определителя
    det := new(big.Float).SetPrec(256).SetFloat64(1)
    if negDet {
        det.Neg(det)
    }
    for i := range matrix {
        det.Mul(det, matrix[i][i])
    }
    fmt.Println("Определитель матрицы:", det)
    det_gonum := mat.Det(A)
    fmt.Println("Определитель матрицы gonum/mat:", det_gonum)

    // Сравнение решений
    fmt.Println("Сравнение решений:")
    for i := 0; i < n; i++ {
        fmt.Println("x" + strconv.Itoa(i+1) + ": метод Гаусса =", solution[i], ", gonum/mat =", X.AtVec(i))
    }
}