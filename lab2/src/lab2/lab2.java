package lab2;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

public class lab2 {
	// Импортируются классы, используемые в приложении
	

	@SuppressWarnings("serial")
	// Главный класс приложения, он же класс фрейма
	public class MainFrame extends JFrame {
	// Размеры окна приложения в виде констант
	private static final int WIDTH = 400;
	private static final int HEIGHT = 320;
	// Текстовые поля для считывания значений переменных,
	// как компоненты, совместно используемые в различных методах
	private JTextField textFieldX;
	private JTextField textFieldY;
	private JTextField textFieldZ;
	private static Double mem1 = 0.0;
    private static Double mem2 = 0.0;
    private static Double mem3 = 0.0;
    private static Double result;
	// Текстовое поле для отображения результата,
	// как компонент, совместно используемый в различных методах
	private static JTextField textFieldResult;
	// Группа радио-кнопок для обеспечения уникальности выделения в группе
	private ButtonGroup radioButtons = new ButtonGroup();
	// Контейнер для отображения радио-кнопок
	private Box hboxFormulaType = Box.createHorizontalBox();
	private int formulaId = 1;
	static JRadioButton mem1Button = new JRadioButton("Переменная 1");
    static JRadioButton mem2Button = new JRadioButton("Переменная 2");
    static JRadioButton mem3Button = new JRadioButton("Переменная 3");
	// Формула №1 для рассчѐта
	public Double calculate1(Double x, Double y, Double z) {
		return (Math.pow(4, Math.log(z) + Math.sin(Math.PI * Math.pow(y, 2)))) /
	               (Math.pow(y, Math.E) + Math.cos(x) + Math.sin(y));
	}
	// Формула №2 для рассчѐта
	public Double calculate2(Double x, Double y, Double z) {
		return Math.sqrt(z) * (3 - Math.pow(z, -x)) /
	               Math.sqrt(1 + Math.pow(y, 3));
	}
	public static void clearActiveMemory() {
        if (mem1Button.isSelected()) {
            mem1 = 0.0;
        } else if (mem2Button.isSelected()) {
            mem2 = 0.0;
        } else if (mem3Button.isSelected()) {
            mem3 = 0.0;
        }
    }

	public static void addToActiveMemory() {
        if (mem1Button.isSelected()) {
            mem1 += result;
            textFieldResult.setText(mem1.toString());
        } else if (mem2Button.isSelected()) {
            mem2 += result;
            textFieldResult.setText(mem2.toString());
        } else if (mem3Button.isSelected()) {
            mem3 += result;
            textFieldResult.setText(mem3.toString());
        }
        
    }
	// Вспомогательный метод для добавления кнопок на панель
	private void addRadioButton(String buttonName, final int formulaId) {
	JRadioButton button = new JRadioButton(buttonName);
	button.addActionListener(new ActionListener() {
	public void actionPerformed(ActionEvent ev) {
	MainFrame.this.formulaId = formulaId;
	}
	});
	radioButtons.add(button);
	hboxFormulaType.add(button);
	}
	// Конструктор класса
	public MainFrame() {
	super("Вычисление формулы");
	setSize(WIDTH, HEIGHT);
	Toolkit kit = Toolkit.getDefaultToolkit();
	// Отцентрировать окно приложения на экране
	setLocation((kit.getScreenSize().width - WIDTH)/2,
	(kit.getScreenSize().height - HEIGHT)/2);
	hboxFormulaType.add(Box.createHorizontalGlue());
	addRadioButton("Формула 1", 1);
	addRadioButton("Формула 2", 2);
	radioButtons.setSelected(
	radioButtons.getElements().nextElement().getModel(), true);
	hboxFormulaType.add(Box.createHorizontalGlue());
	hboxFormulaType.setBorder(
	BorderFactory.createLineBorder(Color.YELLOW));
	// Создать область с полями ввода для X и Y
	JLabel labelForX = new JLabel("X:");
	textFieldX = new JTextField("0", 10);
	textFieldX.setMaximumSize(textFieldX.getPreferredSize());
	JLabel labelForY = new JLabel("Y:");
	textFieldY = new JTextField("0", 10);
	textFieldY.setMaximumSize(textFieldY.getPreferredSize());
	JLabel labelForZ = new JLabel("Z:");
	textFieldZ = new JTextField("0", 10);
	textFieldZ.setMaximumSize(textFieldZ.getPreferredSize());
	Box hboxVariables = Box.createHorizontalBox();
	hboxVariables.setBorder(
	BorderFactory.createLineBorder(Color.RED));
	hboxVariables.add(Box.createHorizontalGlue());
	hboxVariables.add(labelForX);
	hboxVariables.add(Box.createHorizontalStrut(10));
	hboxVariables.add(textFieldX);
	hboxVariables.add(Box.createHorizontalStrut(100));
	hboxVariables.add(labelForY);
	hboxVariables.add(Box.createHorizontalStrut(10));
	hboxVariables.add(textFieldY);
	hboxVariables.add(Box.createHorizontalStrut(100));
	hboxVariables.add(labelForZ);
	hboxVariables.add(Box.createHorizontalStrut(10));
	hboxVariables.add(textFieldZ);
	hboxVariables.add(Box.createHorizontalStrut(10));
	hboxVariables.add(Box.createHorizontalGlue());
	JButton mcButton = new JButton("MC");
    JButton mPlusButton = new JButton("M+");

    // Создание радио-кнопок для выбора переменной
    
    
    
    // Группировка радио-кнопок
    ButtonGroup group = new ButtonGroup();
    group.add(mem1Button);
    group.add(mem2Button);
    group.add(mem3Button);
    
    Box memBox = Box.createVerticalBox();
    memBox.add(mcButton);
    memBox.add(mPlusButton);
    memBox.add(mem1Button);
    memBox.add(mem2Button);
    memBox.add(mem3Button);
    
    mem1Button.setSelected(true);
	// Создать область для вывода результата
	JLabel labelForResult = new JLabel("Результат:");
	//labelResult = new JLabel("0");
	textFieldResult = new JTextField("0", 10);
	textFieldResult.setMaximumSize(
	textFieldResult.getPreferredSize());
	Box hboxResult = Box.createHorizontalBox();
	hboxResult.add(Box.createHorizontalGlue());
	hboxResult.add(labelForResult);
	hboxResult.add(Box.createHorizontalStrut(10));
	hboxResult.add(textFieldResult);
	hboxResult.add(Box.createHorizontalGlue());
	hboxResult.setBorder(BorderFactory.createLineBorder(Color.BLUE));
	// Создать область для кнопок
	JButton buttonCalc = new JButton("Вычислить");
	buttonCalc.addActionListener(new ActionListener() {
	public void actionPerformed(ActionEvent ev) {
	try {
	Double x = Double.parseDouble(textFieldX.getText());
	Double y = Double.parseDouble(textFieldY.getText());
	Double z = Double.parseDouble(textFieldZ.getText());
	
	if (formulaId==1)
	result = calculate1(x, y, z);
	else
	result = calculate2(x, y, z);
	textFieldResult.setText(result.toString());
	} catch (NumberFormatException ex) {
	JOptionPane.showMessageDialog(MainFrame.this,
	"Ошибка в формате записи числа с плавающей точкой", "Ошибочный формат числа",
	JOptionPane.WARNING_MESSAGE);
	}
	}
	});
	JButton buttonReset = new JButton("Очистить поля");
	buttonReset.addActionListener(new ActionListener() {
	public void actionPerformed(ActionEvent ev) {
	textFieldX.setText("0");
	textFieldY.setText("0");
	textFieldResult.setText("0");
	}
	
	});
	
	Box hboxButtons = Box.createHorizontalBox();
	hboxButtons.add(Box.createHorizontalGlue());
	hboxButtons.add(buttonCalc);
	hboxButtons.add(Box.createHorizontalStrut(30));
	hboxButtons.add(buttonReset);
	hboxButtons.add(Box.createHorizontalGlue());
	hboxButtons.setBorder(
	BorderFactory.createLineBorder(Color.GREEN));
	
	// Связать области воедино в компоновке BoxLayout
	Box contentBox = Box.createVerticalBox();
	contentBox.add(Box.createVerticalGlue());
	contentBox.add(hboxFormulaType);
	contentBox.add(hboxVariables);
	contentBox.add(hboxResult);
	contentBox.add(hboxButtons);
	contentBox.add(memBox);
	contentBox.add(Box.createVerticalGlue());
	
	getContentPane().add(contentBox, BorderLayout.CENTER);
	// Добавление слушателя для кнопки "MC"
    mcButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
        	MainFrame.clearActiveMemory();
        }
    });

    // Добавление слушателя для кнопки "M+"
    mPlusButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
        	MainFrame.addToActiveMemory();
        }
    });
    
	
	}
	
		
	}
	// Главный метод класса
	public static void main(String[] args) {
		lab2 lab = new lab2();
		MainFrame frame = lab.new MainFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
}

