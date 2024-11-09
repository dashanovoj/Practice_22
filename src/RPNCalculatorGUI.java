import javax.swing.*; // Импортируем для создания графического интерфейса
import java.awt.*; // Импортируем для работы с графикой
import java.awt.event.ActionEvent; // Импортируем для обработки событий
import java.awt.event.ActionListener; // Импортируем для обработки действий
import java.util.Stack; // Импортируем для работы со стеком

// Реализуем класс для разработки графического интерфейса калькулятора
public class RPNCalculatorGUI {

    // Поля данных
    private JFrame frame; // главное окно приложения
    private JTextField display; // поле для отображения ввода и результата
    private JPanel buttonPanel; // панель для размещения кнопок
    private Model model; // модель, которая будет хранить данные
    private Controller controller; // контроллер, который управляет взаимодействием между моделью и представлением

    // Конструктор
    public RPNCalculatorGUI() {
        model = new Model(); // создаём экземпляр класса Model
        controller = new Controller(model, this); // создаём экземпляр класса Controller, передаём в него модель и GUI калькулятора

        frame = new JFrame("Калькулятор");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 300);
        frame.setLayout(new BorderLayout()); // установка компоновки для окна

        display = new JTextField();
        display.setEditable(false); // запрещаем редактирование текстового поля
        display.setHorizontalAlignment(JTextField.RIGHT); // выравнивание текста по правому краю
        frame.add(display, BorderLayout.NORTH); // добавляем панель текстового поля в верхнюю часть окна

        buttonPanel = new JPanel(new GridLayout(5, 4, 5, 5)); // табличная компоновка
        createButtons(); // вызываем метод создания кнопок
        frame.add(buttonPanel, BorderLayout.CENTER); // располагаем панель кнопок по центру окна

        frame.setVisible(true); // отображение окна
    }

    // Метод для создания кнопок
    private void createButtons() {
        // Определяем массив меток для кнопок
        String[] buttonLabels = {
                "7", "8", "9", "/",
                "4", "5", "6", "*",
                "1", "2", "3", "-",
                "0", ".", "+", "=",
                "C", // Кнопка очистки
        };
        // Проходимся по массиву
        for (String label : buttonLabels) {
            JButton button = new JButton(label); // создаём кнопку с соответствующей меткой
            button.addActionListener(controller); // добавляем слушателя события к кнопке
            buttonPanel.add(button); // добавляем кнопку на панель
        }
    }

    // Метод для обновления дисплея
    public void updateDisplay(String text) {
        display.setText(text);
    }

    // Вложенный класс для модели данных калькулятора
    private static class Model {
        // Создаём стек для хранения данных
        private Stack<Double> stack = new Stack<>();

        // Метод для добавления значения в стек
        public void push(double value) {
            stack.push(value);
        }

        // Метод для извлечения значения из стека
        public double pop() {
            if (stack.isEmpty()) {
                throw new IllegalStateException("Стек пуст");
            }
            return stack.pop();
        }

        // Метод для выполнения вычислений
        public double calculate(String operator) {
            switch (operator) {
                case "+":
                    return pop() + pop();
                case "-":
                    return pop() - pop();
                case "*":
                    return pop() * pop();
                case "/":
                    double operand2 = pop();
                    if (operand2 == 0) {
                        throw new ArithmeticException("Деление на ноль");
                    }
                    return pop() / operand2;
                default:
                    throw new IllegalArgumentException("Неизвестный оператор: " + operator);
            }
        }
    }

    // Вложенный класс для контроллера
    private static class Controller implements ActionListener {
        private Model model; // Ссылка на модель
        private RPNCalculatorGUI RPNCalculatorGUI; // Ссылка на графический интерфейс калькулятора

        // Конструктор
        public Controller(Model model, RPNCalculatorGUI RPNCalculatorGUI) {
            this.model = model;
            this.RPNCalculatorGUI = RPNCalculatorGUI;
        }

        // Метод обработки событий нажатия кнопок
        @Override
        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand(); // получаем команду, связанную с событием
            try {
                // Проверяем, является ли команда числом
                if (command.matches("\\d+\\.?\\d*")) {
                    model.push(Double.parseDouble(command)); // преобразуем строку в число и добавляем в стек
                    RPNCalculatorGUI.updateDisplay(RPNCalculatorGUI.display.getText() + command + " "); // обновляем дисплей
                } else if (command.equals("=")) { // Если нажата кнопка равно "="
                    // Проверяем, есть ли как минимум два числа в стеке
                    if (model.stack.size() >= 2) {
                        // Вычисляем результат
                        double result = model.calculate(RPNCalculatorGUI.display.getText().trim().split(" ")[RPNCalculatorGUI.display.getText().trim().split(" ").length - 1]);
                        RPNCalculatorGUI.updateDisplay(RPNCalculatorGUI.display.getText() + "= " + result + " ");
                        model.push(result); // Сохраняем результат в стеке для дальнейших операций
                    }
                } else if (command.equals("C")) { // Если нажата кнопка очистки
                    model.stack.clear(); // Очистка стека
                    RPNCalculatorGUI.updateDisplay(""); // Очистка дисплея
                } else {
                    // При нажатии кнопки оператора добавляем его в дисплей
                    RPNCalculatorGUI.updateDisplay(RPNCalculatorGUI.display.getText() + command + " ");
                }
            } catch (Exception ex) { // Обрабатываем исключения
                RPNCalculatorGUI.updateDisplay(ex.getMessage()); // Обновляем дисплей с сообщением об ошибке
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(RPNCalculatorGUI::new); // Запускаем GUI в потоке обработки событий Swing
    }
}

