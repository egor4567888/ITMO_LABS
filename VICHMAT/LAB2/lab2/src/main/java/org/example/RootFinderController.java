package org.example;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.io.File;
import java.net.MalformedURLException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

@Controller
public class RootFinderController {

    // Отображение главной страницы с формой.
    @GetMapping("/")
public String index(Model model) {
    model.addAttribute("functions", Arrays.asList(MathFunction.values()));
    model.addAttribute("methods", Arrays.asList(MethodEnum.values()));
    model.addAttribute("systems", Arrays.asList(MathFunctionSystem.values()));
    return "index";
}

    // Обработка отправки формы.
    @PostMapping("/solve")
    public String solve(
            @RequestParam("function") int functionChoice,
            @RequestParam("method") int methodChoice,
            @RequestParam("a") String aStr,
            @RequestParam("b") String bStr,
            @RequestParam(value = "x0", required = false) String x0Str,
            @RequestParam(value = "x1", required = false) String x1Str,
            @RequestParam("epsilon") String epsilonStr,
            Model model
    ) {
        try {
            MathFunction chosenFunction = MathFunction.fromChoice(functionChoice);
            MethodEnum chosenMethod = MethodEnum.fromChoice(methodChoice);

            BigDecimal a = new BigDecimal(aStr);
            BigDecimal b = new BigDecimal(bStr);
            UserInputHandler.validateInterval(a, b, chosenFunction);
            BigDecimal epsilon = new BigDecimal(epsilonStr);

            // Для методов, где x0 вычисляется автоматически (например, простых итераций)
            BigDecimal x0 = null;
            BigDecimal x1 = null;

            if (chosenMethod == MethodEnum.NEWTON || chosenMethod == MethodEnum.SECANT) {
                if (x0Str == null || x0Str.isEmpty()) {
                    throw new IllegalArgumentException("Необходимо задать x0 для выбранного метода.");
                }
                x0 = new BigDecimal(x0Str);
            }
            if (chosenMethod == MethodEnum.SECANT) {
                if (x1Str == null || x1Str.isEmpty()) {
                    throw new IllegalArgumentException("Необходимо задать x1 для метода секущих.");
                }
                x1 = new BigDecimal(x1Str);
            }
            if (chosenMethod == MethodEnum.SIMPLE_ITERATION) {
                // x0 вычисляется как (a+b)/2
                x0 = a.add(b).divide(new BigDecimal("2"), new MathContext(10, RoundingMode.HALF_UP));
            }

            UserInputData inputData = new UserInputData(chosenFunction, chosenMethod, a, b, x0, x1, epsilon);
            MethodResult result = RootFinder.solve(inputData);

            // Сохранение графика. Вычислим шаг как (b-a)/100.
            BigDecimal step = b.subtract(a).abs().divide(new BigDecimal("100"), new MathContext(10, RoundingMode.HALF_UP));
            String chartFile = "chart.png";
            GraphBuilder.saveFunctionPlot(
                    chosenFunction.getFunction(),
                    chosenFunction.getDescription(),
                    a,
                    b,
                    step,
                    chartFile,
                    800,
                    600,
                    result.getRoot()
            );

            model.addAttribute("result", result);
            model.addAttribute("chartFile", chartFile);
            model.addAttribute("error", null);
            model.addAttribute("chartUrl", "/chart?filename=" + chartFile);

        } catch (Exception ex) {
            model.addAttribute("error", ex.getMessage());
        }

        model.addAttribute("functions", Arrays.asList(MathFunction.values()));
        model.addAttribute("methods", Arrays.asList(MethodEnum.values()));
        model.addAttribute("systems", Arrays.asList(MathFunctionSystem.values()));
        return "index";
    }

 @PostMapping("/solveSystem")
public String solveSystem(
        @RequestParam("system") int systemChoice,
        @RequestParam("x0sys") String x0SysStr,
        @RequestParam("y0sys") String y0SysStr,
        @RequestParam("epsSys") String epsSysStr,
        Model model
) {
    try {
        MathFunctionSystem chosenSystem = MathFunctionSystem.fromChoice(systemChoice);
        BigDecimal x0 = new BigDecimal(x0SysStr);
        BigDecimal y0 = new BigDecimal(y0SysStr);
        BigDecimal epsilon = new BigDecimal(epsSysStr);

        // Проверка достаточного условия сходимости:
        // Абсолютные значения суммарных производных функций должны быть меньше 1
        BigDecimal f1DerivSum = chosenSystem.getF1DerivativeSum().apply(x0, y0).abs();
        BigDecimal f2DerivSum = chosenSystem.getF2DerivativeSum().apply(x0, y0).abs();
//        if (f1DerivSum.compareTo(BigDecimal.ONE) >= 0 || f2DerivSum.compareTo(BigDecimal.ONE) >= 0) {
//            throw new IllegalArgumentException("Достаточное условие сходимости не выполнено: " +
//                    "абсолютные значения суммарных производных должны быть меньше 1. " +
//                    "f1_derivative = " + f1DerivSum + ", f2_derivative = " + f2DerivSum);
//        }

        // Используем итерационные функции из выбранной системы.
        SystemInputData inputData = new SystemInputData(
                chosenSystem.getxPhi(),  // phi1
                chosenSystem.getyPhi(),  // phi2
                x0, y0, epsilon
        );

        SystemMethodResult sysResult = RootFinder.solveSystem(inputData);
        // Определяем график в зависимости от выбранной системы:
        String chart3d;
        switch (chosenSystem) {
            case FIRST:
                chart3d = "chart3d_1.png";
                break;
            case SECOND:
                chart3d = "chart3d_2.png";
                break;
            case THIRD:
                chart3d = "chart3d_3.png";
                break;
            default:
                chart3d = "chart3d_1.png";
        }

        model.addAttribute("systemResult", sysResult);
        model.addAttribute("chart3dUrl", "/chart?filename=" + chart3d);
        model.addAttribute("errorSystem", null);
    } catch (Exception ex) {
        model.addAttribute("errorSystem", ex.getMessage());
    }
    model.addAttribute("functions", Arrays.asList(MathFunction.values()));
    model.addAttribute("methods", Arrays.asList(MethodEnum.values()));
    model.addAttribute("systems", Arrays.asList(MathFunctionSystem.values()));
    return "index";
}

    @GetMapping("/chart")
    @ResponseBody
    public Resource getChart(@RequestParam("filename") String filename) throws MalformedURLException {
        Path path = Paths.get(filename);
        Resource resource = new UrlResource(path.toUri());
        return resource;
    }
}