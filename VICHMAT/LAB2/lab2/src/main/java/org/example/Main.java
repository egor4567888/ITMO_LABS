package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.math.BigDecimal;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
      ApplicationContext context = SpringApplication.run(Main.class, args);
        //GraphBuilder.save3DPlot(MathFunctionSystem.FIRST.getF1(),MathFunctionSystem.FIRST.getF2(),BigDecimal.valueOf(0),BigDecimal.valueOf(10),BigDecimal.valueOf(0),BigDecimal.valueOf(10),BigDecimal.valueOf(0.1),"chart3d.png",500,500);
//        UserInputHandler inputHandler = context.getBean(UserInputHandler.class);
//        try {
//            GraphBuilder.saveFunctionPlot(MathFunction.SECOND.getFunction(),"test", BigDecimal.valueOf(-0.5) ,BigDecimal.valueOf(3),BigDecimal.valueOf(0.01),"test.png",500,500);
//            UserInputData inputData = inputHandler.processInput();
//            MethodResult result = RootFinder.solve(inputData);
//            System.out.println("Найденный корень: " + result.getRoot());
//            System.out.println("Значение функции в корне: " + result.getFunctionValue());
//            System.out.println("Количество итераций: " + result.getIterations());
//
//        } catch (Exception ex) {
//            System.err.println("Произошла ошибка: " + ex.getMessage());
//        }
    }
}