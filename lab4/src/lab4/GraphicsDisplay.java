package lab4;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Stroke;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.font.FontRenderContext;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import javax.swing.JPanel;
import javax.swing.ToolTipManager;
@SuppressWarnings("serial")
public class GraphicsDisplay extends JPanel {
// Список координат точек для построения графика
private Double[][] graphicsData;
// Флаговые переменные, задающие правила отображения графика
private boolean showAxis = true;
private boolean showMarkers = true;
// Границы диапазона пространства, подлежащего отображению
private double minX;
private double maxX;
private double minY;
private double maxY;
private boolean isDragging = false;
private Point2D.Double dragStart = null;
private Point2D.Double dragEnd = null;
// Используемый масштаб отображения
private double scale;
// Различные стили черчения линий
private BasicStroke graphicsStroke;
private BasicStroke axisStroke;
private BasicStroke markerStroke;
// Различные шрифты отображения надписей
private Font axisFont;
public GraphicsDisplay() {
// Цвет заднего фона области отображения - белый
setBackground(Color.WHITE);
// Сконструировать необходимые объекты, используемые в рисовании
// Перо для рисования графика
graphicsStroke = new BasicStroke(2.0f, BasicStroke.CAP_BUTT,
BasicStroke.JOIN_ROUND, 10.0f, null, 0.0f);
// Перо для рисования осей координат
axisStroke = new BasicStroke(2.0f, BasicStroke.CAP_BUTT,
BasicStroke.JOIN_MITER, 10.0f, null, 0.0f);
// Перо для рисования контуров маркеров
markerStroke = new BasicStroke(1.0f, BasicStroke.CAP_BUTT,
BasicStroke.JOIN_MITER, 10.0f, null, 0.0f);
// Шрифт для подписей осей координат
axisFont = new Font("Serif", Font.BOLD, 36);
//Добавляем обработчик мыши
addMouseListener(new MouseAdapter() {
    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) { // Левая кнопка мыши
            dragStart = new Point2D.Double(e.getX(), e.getY());
            isDragging = true;
        } else if (e.getButton() == MouseEvent.BUTTON3) { // Правая кнопка мыши
            resetScale();
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (isDragging) {
            dragEnd = new Point2D.Double(e.getX(), e.getY());
            zoomToSelection(dragStart, dragEnd);
            isDragging = false;
            repaint();
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        // Проверка, находится ли курсор над маркером
        for (Double[] point : graphicsData) {
            Point2D.Double center = xyToPoint(point[0], point[1]);
            if (center.distance(e.getX(), e.getY()) < 5) { // 5 - радиус для проверки
                setToolTipText("X: " + point[0] + ", Y: " + point[1]);
                ToolTipManager.sharedInstance().setInitialDelay(0);
                ToolTipManager.sharedInstance().setDismissDelay(10000);
                break;
            } else {
                setToolTipText(null);
            }
        }
    }
});
}

// Метод для масштабирования выделенной области
private void zoomToSelection(Point2D.Double start, Point2D.Double end) {
double minX = Math.min(start.getX(), end.getX());
double maxX = Math.max(start.getX(), end.getX());
double minY = Math.min(start.getY(), end.getY());
double maxY = Math.max(start.getY(), end.getY());

// Преобразование координат в значения графика
this.minX = pointToX(minX);
this.maxX = pointToX(maxX);
this.minY = pointToY(maxY);
this.maxY = pointToY(minY);
repaint();
}

// Метод для сброса масштаба
private void resetScale() {
// Установите minX, maxX, minY, maxY в исходные значения
// Например, можно использовать значения, которые были при первой загрузке данных
// minX = ...; maxX = ...; minY = ...; maxY = ...;
repaint();
}

// Преобразование точки в значение X
private double pointToX(double x) {
return minX + (x / scale);
}

// Преобразование точки в значение Y
private double pointToY(double y) {
return maxY - (y / scale);
}
// главного окна приложения в случае успешной загрузки данных
public void showGraphics(Double[][] graphicsData) {
// Сохранить массив точек во внутреннем поле класса
this.graphicsData = graphicsData;
// Запросить перерисовку компонента, т.е. неявно вызвать
paintComponent(null);
repaint();
}
// Методы-модификаторы для изменения параметров отображения графика
// Изменение любого параметра приводит к перерисовке области
public void setShowAxis(boolean showAxis) {
this.showAxis = showAxis;
repaint();
}
public void setShowMarkers(boolean showMarkers) {
this.showMarkers = showMarkers;
repaint();
}
// Метод отображения всего компонента, содержащего график
public void paintComponent(Graphics g) {
/* Шаг 1 - Вызвать метод предка для заливки области цветом заднего фона
* Эта функциональность - единственное, что осталось в наследство от
* paintComponent класса JPanel
*/
super.paintComponent(g);
if (graphicsData==null || graphicsData.length==0) return;
minX = graphicsData[0][0];
maxX = graphicsData[graphicsData.length-1][0];
minY = graphicsData[0][1];
maxY = minY;
// Найти минимальное и максимальное значение функции
for (int i = 1; i<graphicsData.length; i++) {
if (graphicsData[i][1]<minY) {
minY = graphicsData[i][1];
}
if (graphicsData[i][1]>maxY) {
maxY = graphicsData[i][1];
}
}
/* Шаг 4 - Определить (исходя из размеров окна) масштабы по осям X
и Y - сколько пикселов
* приходится на единицу длины по X и по Y
*/
double scaleX = getSize().getWidth() / (maxX - minX);
double scaleY = getSize().getHeight() / (maxY - minY);
// Выбираем за основу минимальный
scale = Math.min(scaleX, scaleY);
if (scale==scaleX) {
/* Если за основу был взят масштаб по оси X, значит по оси Y
делений меньше,
* т.е. подлежащий визуализации диапазон по Y будет меньше
высоты окна.
* Значит необходимо добавить делений, сделаем это так:
* 1) Вычислим, сколько делений влезет по Y при выбранном
масштабе - getSize().getHeight()/scale
* 2) Вычтем из этого сколько делений требовалось изначально
* 3) Набросим по половине недостающего расстояния на maxY и
minY
*/
double yIncrement = (getSize().getHeight()/scale - (maxY -
minY))/2;
maxY += yIncrement;
minY -= yIncrement;
}
if (scale==scaleY) {
double xIncrement = (getSize().getWidth()/scale - (maxX -
minX))/2;
maxX += xIncrement;
minX -= xIncrement;
}
//Шаг 7 - Сохранить текущие настройки холста
Graphics2D canvas = (Graphics2D) g;
Stroke oldStroke = canvas.getStroke();
Color oldColor = canvas.getColor();
Paint oldPaint = canvas.getPaint();
Font oldFont = canvas.getFont();
//Первыми (если нужно) отрисовываются оси координат.
if (showAxis) paintAxis(canvas);
//Затем отображается сам график
paintGraphics(canvas);
if (showMarkers) paintMarkers(canvas);
//Шаг 9 - Восстановить старые настройки холста
canvas.setFont(oldFont);
canvas.setPaint(oldPaint);
canvas.setColor(oldColor);
canvas.setStroke(oldStroke);
}
//Отрисовка графика по прочитанным координатам
//Отрисовка графика по прочитанным координатам
protected void paintGraphics(Graphics2D canvas) {
 // Установить штрих-пунктирный стиль
 float[] dashPattern = {10.0f, 5.0f}; // Длина штриха и пробела
 BasicStroke dashedStroke = new BasicStroke(2.0f, BasicStroke.CAP_BUTT,
         BasicStroke.JOIN_ROUND, 10.0f, dashPattern, 0.0f);
 canvas.setStroke(dashedStroke);
 
 // Выбрать цвет линии
 canvas.setColor(Color.RED);
 
 // Начало пути устанавливается в первую точку графика
 GeneralPath graphics = new GeneralPath();
 for (int i = 0; i < graphicsData.length; i++) {
     // Преобразовать значения (x,y) в точку на экране point
     Point2D.Double point = xyToPoint(graphicsData[i][0], graphicsData[i][1]);
     if (i > 0) {
         graphics.lineTo(point.getX(), point.getY());
     } else {
         graphics.moveTo(point.getX(), point.getY());
     }
 }
 // Отобразить график
 canvas.draw(graphics);
}
// Отображение маркеров точек, по которым рисовался график
//Отображение маркеров точек, по которым рисовался график
protected void paintMarkers(Graphics2D canvas) {
 canvas.setStroke(markerStroke);
 
 // Шаг 2 - Организовать цикл по всем точкам графика
 for (Double[] point : graphicsData) {
     Point2D.Double center = xyToPoint(point[0], point[1]);
     
     // Проверка, находится ли значение функции в диапазоне 0.1 от целого числа
     double value = point[1];
     boolean isHighlighted = (value % 1) <= 0.1 || (value % 1) >= 0.9;

     // Установка цвета маркера в зависимости от условия
     if (isHighlighted) {
         canvas.setColor(Color.GREEN); // Цвет для выделенных точек
     } else {
         canvas.setColor(Color.RED); // Цвет для обычных точек
     }
     
     // Создаем треугольник
     GeneralPath triangle = new GeneralPath();
     triangle.moveTo(center.getX(), center.getY() - 5); // верхняя точка
     triangle.lineTo(center.getX() - 5, center.getY() + 5); // нижний левый
     triangle.lineTo(center.getX() + 5, center.getY() + 5); // нижний правый
     triangle.closePath(); // Замкнуть треугольник
     
     canvas.draw(triangle); // Начертить контур маркера
     canvas.fill(triangle); // Залить внутреннюю область маркера
 }
}
// Метод, обеспечивающий отображение осей координат
protected void paintAxis(Graphics2D canvas) {
// Установить особое начертание для осей
canvas.setStroke(axisStroke);
// Оси рисуются чѐрным цветом
canvas.setColor(Color.BLACK);
// Стрелки заливаются чѐрным цветом
canvas.setPaint(Color.BLACK);
// Подписи к координатным осям делаются специальным шрифтом
canvas.setFont(axisFont);
FontRenderContext context = canvas.getFontRenderContext();
// Определить, должна ли быть видна ось Y на графике
if (minX<=0.0 && maxX>=0.0) {
//а правая (maxX) >= 0.0
//Сама ось - это линия между точками (0, maxY) и (0, minY)
canvas.draw(new Line2D.Double(xyToPoint(0, maxY),
xyToPoint(0, minY)));
//Стрелка оси Y
GeneralPath arrow = new GeneralPath();
Point2D.Double lineEnd = xyToPoint(0, maxY);
arrow.moveTo(lineEnd.getX(), lineEnd.getY());
arrow.lineTo(arrow.getCurrentPoint().getX()+5,
arrow.getCurrentPoint().getY()+20);

arrow.lineTo(arrow.getCurrentPoint().getX()-10,
arrow.getCurrentPoint().getY());
//Замкнуть треугольник стрелки
arrow.closePath();
canvas.draw(arrow); // Нарисовать стрелку
canvas.fill(arrow); // Закрасить стрелку
//Нарисовать подпись к оси Y
//Определить, сколько места понадобится для надписи "y"
Rectangle2D bounds = axisFont.getStringBounds("y", context);
Point2D.Double labelPos = xyToPoint(0, maxY);
//Вывести надпись в точке с вычисленными координатами
canvas.drawString("y", (float)labelPos.getX() + 10,
(float)(labelPos.getY() - bounds.getY()));
}
//Определить, должна ли быть видна ось X на графике
if (minY<=0.0 && maxY>=0.0) {
//а нижняя (minY) <= 0.0
canvas.draw(new Line2D.Double(xyToPoint(minX, 0),
xyToPoint(maxX, 0)));
//Стрелка оси X
GeneralPath arrow = new GeneralPath();
Point2D.Double lineEnd = xyToPoint(maxX, 0);
arrow.moveTo(lineEnd.getX(), lineEnd.getY());

arrow.lineTo(arrow.getCurrentPoint().getX()-20,
arrow.getCurrentPoint().getY()-5);

arrow.lineTo(arrow.getCurrentPoint().getX(),
arrow.getCurrentPoint().getY()+10);
//Замкнуть треугольник стрелки
arrow.closePath();
canvas.draw(arrow); // Нарисовать стрелку
canvas.fill(arrow); // Закрасить стрелку
//Нарисовать подпись к оси X
//Определить, сколько места понадобится для надписи "x"
Rectangle2D bounds = axisFont.getStringBounds("x", context);
Point2D.Double labelPos = xyToPoint(maxX, 0);
//Вывести надпись в точке с вычисленными координатами
canvas.drawString("x", (float)(labelPos.getX() -
bounds.getWidth() - 10), (float)(labelPos.getY() + bounds.getY()));
}
}
/* Метод-помощник, осуществляющий преобразование координат.
* Оно необходимо, т.к. верхнему левому углу холста с координатами
* (0.0, 0.0) соответствует точка графика с координатами (minX, maxY),
где
* minX - это самое "левое" значение X, а
* maxY - самое "верхнее" значение Y.
*/
protected Point2D.Double xyToPoint(double x, double y) {
// Вычисляем смещение X от самой левой точки (minX)
double deltaX = x - minX;
// Вычисляем смещение Y от точки верхней точки (maxY)
double deltaY = maxY - y;
return new Point2D.Double(deltaX*scale, deltaY*scale);
}
/* Метод-помощник, возвращающий экземпляр класса Point2D.Double
* смещѐнный по отношению к исходному на deltaX, deltaY
* К сожалению, стандартного метода, выполняющего такую задачу, нет.
*/
protected Point2D.Double shiftPoint(Point2D.Double src, double deltaX,
double deltaY) {
// Инициализировать новый экземпляр точки
Point2D.Double dest = new Point2D.Double();
dest.setLocation(src.getX() + deltaX, src.getY() + deltaY);
return dest;
}
}
