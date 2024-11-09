/* Задание 1. Напишите программу-калькулятор арифметических
выражений записанных в обратной польской нотации (RPN-калькулятор) */
import java.util.Scanner;
import java.util.Stack;

public class RPNCalculator {

    public static double calculateRPN(String expression) {
        /* Создаём пустой стек типа Double, используем для этого встроенный Stack
        для хранения операндов */
        Stack<Double> stack = new Stack<>();
        /* Передаваемое значение разбиваем на токены через квантификатор пробела */
        String[] tokens = expression.split("\\s+");
        // Проходимся по каждому токену
        for (String token : tokens) {
            if (isOperator(token)) { // Если токен - операция
                // Извлекаются два последних числа из стека
                double operand2 = stack.pop();
                double operand1 = stack.pop();
                // Передаем параметры в метод вычисления значения и передаем это значение в стек
                stack.push(performOperation(token, operand1, operand2));
            } else {
                // Если токен - число, преобразуем в тип Double и добавляем в стек
                stack.push(Double.parseDouble(token));
            }
        }
        /* После обработки всех токенов возвращаем значение,
        которое находится на вершине стека (результат вычислений) */
        return stack.pop();
    }

    // Булев метод, является ли токен операцией
    private static boolean isOperator(String token) {
        // Сравниваем переданный токен с возможными операциями
        return token.equals("+") || token.equals("-") || token.equals("*") || token.equals("/");
    }

    // Метод для выполнения переданной операции над переданными значениями
    private static double performOperation(String operator, double operand1, double operand2) {
        switch (operator) {
            case "+":
                return operand1 + operand2;
            case "-":
                return operand1 - operand2;
            case "*":
                return operand1 * operand2;
            case "/":
                if (operand2 == 0) {
                    throw new ArithmeticException("Деление на ноль");
                }
                return operand1 / operand2;
        }
        throw new IllegalArgumentException("Неизвестный оператор: " + operator);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите выражение в обратной польской нотации (RPN):");
        String expression = scanner.nextLine();
        System.out.println("Результат: " + calculateRPN(expression));
    }
}
