package lab3;

import javax.swing.table.AbstractTableModel;

@SuppressWarnings("serial")
public class GornerTableModel extends AbstractTableModel {
    private Double[] coefficients;
    private Double from;
    private Double to;
    private Double step;

    public GornerTableModel(Double from, Double to, Double step, Double[] coefficients) {
        this.from = from;
        this.to = to;
        this.step = step;
        this.coefficients = coefficients;
    }

    public Double getFrom() {
        return from;
    }

    public Double getTo() {
        return to;
    }

    public Double getStep() {
        return step;
    }

    public int getColumnCount() {
        // В данной модели три столбца
        return 3;
    }

    public int getRowCount() {
        // Вычислить количество точек между началом и концом отрезка
        // исходя из шага табулирования
        return new Double(Math.ceil((to - from) / step)).intValue() + 1;
    }

    public Object getValueAt(int row, int col) {
        // Вычислить значение X как НАЧАЛО_ОТРЕЗКА + ШАГ * НОМЕР_СТРОКИ
        double x = from + step * row;
        if (col == 0) {
            // Если запрашивается значение 1-го столбца, то это X
            return x;
        } else {
            // Если запрашивается значение 2-го столбца, то это значение многочлена
            Double result = coefficients[0]; // Начинаем с первого коэффициента
            for (int i = 1; i < coefficients.length; i++) {
                result = result * x + coefficients[i]; // Применяем схему Горнера
            }
            if (col == 1) {
                return result; // Возвращаем значение многочлена
            } else {
                // Если запрашивается значение 3-го столбца, проверяем, является ли целая часть квадратом
                int intPart = (int) Math.floor(result);
                return isPerfectSquare(intPart); // Возвращаем true или false
            }
        }
    }

    public String getColumnName(int col) {
        switch (col) {
            case 0:
                // Название 1-го столбца
                return "Значение X";
            case 1:
                // Название 2-го столбца
                return "Значение многочлена";
            default:
                // Название 3-го столбца
                return "Целая часть является квадратом?";
        }
    }

    public Class<?> getColumnClass(int col) {
        if (col == 2) {
            // Третий столбец содержит булевские значения
            return Boolean.class;
        }
        // И в 1-ом и во 2-ом столбце находятся значения типа Double
        return Double.class;
    }

    // Метод для проверки, является ли число квадратом
    private boolean isPerfectSquare(int number) {
        if (number < 0) {
            return false; // Отрицательные числа не могут быть квадратами
        }
        int sqrt = (int) Math.sqrt(number);
        return (sqrt * sqrt == number);
    }
}
