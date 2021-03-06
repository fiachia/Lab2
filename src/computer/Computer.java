package computer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Computer {
    private JButton quitButton;
    private JButton cButton;
    private JButton mButton2;
    private JButton mButton;
    private JButton startButton;
    private JButton lastButton;
    private JButton opExpButton;
    private JPanel computer;
    private JTextField inputTextField;
    private JTextField outputTextField;
    private JButton ACButton;
    private JButton mButton3;
    private JButton finalButton;
    private JButton nextButton;

    public Computer() {
        opExpButton.addActionListener(e -> show_Result());
        inputTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    show_Result();
                }
            }
        });
        startButton.addActionListener(e -> {
            index = start_Index;
            show_Old(index);
        });
        finalButton.addActionListener(e -> {
            if (length != 0) {
                index = (start_Index - 1 + length) % length;
                show_Old(index);
            }
        });
        lastButton.addActionListener(e -> {
            if (index != start_Index & length != 0) {
                index = (index - 1 + length) % length;
            }
            show_Old(index);
        });
        nextButton.addActionListener(e -> {
            if (length != 0) {
                if (index != (start_Index - 1 + length) % length) {
                    index = (index + 1) % length;
                }
                show_Old(index);
            }
        });
        cButton.addActionListener(e -> {
            inputTextField.setText("");
            outputTextField.setText("");
        });
        ACButton.addActionListener(e -> init_All());
        quitButton.addActionListener(e -> System.exit(0));
        mButton.addActionListener(e -> outputTextField.setText(String.valueOf(M)));
        mButton3.addActionListener(e -> {
            String str = outputTextField.getText();
            if (!str.equals("")) {
                if (is_Number(str)) {
                    M = M + Double.parseDouble(str);
                }
            }
        });
        mButton2.addActionListener(e -> {
            String str = outputTextField.getText();
            if (!str.equals("")) {
                if (is_Number(str)) {
                    M = M - Double.parseDouble(str);
                }
            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Computer");
        frame.setContentPane(new Computer().computer);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600,500);
        frame.setPreferredSize(new Dimension(600,500));
        frame.pack();
        frame.setVisible(true);
    }
    public void init_All() {//初始化参数
        len = 10;
        is_Zero_Wrong = false;
        oldExp = new String[len];
        oldResult = new String[len];
        length = 0;
        index = 0;
        start_Index = 0;
        inputTextField.setText("");
        outputTextField.setText("");
    }
    private void add_New() {//添加新纪录
        if (!inputTextField.getText().equals("")) {
            if (length < len) {
                oldExp[length] = inputTextField.getText();
                oldResult[length] = outputTextField.getText();
                index++;
                length++;
            } else {
                oldExp[start_Index] = inputTextField.getText();
                oldResult[start_Index] = outputTextField.getText();
                index = start_Index;
                start_Index = (start_Index + 1)%len;
            }
        }
    }
    private void show_Old(int index) {//展示旧记录
        inputTextField.setText(oldExp[index]);
        outputTextField.setText(oldResult[index]);
    }
    private void show_Result() {//生成结果
        String input = inputTextField.getText();
        if (!input.equals("")){
            if (is_Illegal(input)){
                double output = op_All(input);
                if (!is_Zero_Wrong) {
                    outputTextField.setText(String.valueOf(output));
                    add_New();
                } else {
                    outputTextField.setText("The Divisor can not be Zero");
                    is_Zero_Wrong = false;
                }
            } else {
                outputTextField.setText("The Expression is Wrong!");
            }
        } else {
            outputTextField.setText("No Input!");
        }
    }
    private int len = 10;//记录最大长度
    private boolean is_Zero_Wrong = false;//除数是否为零
    private double M = 0;//M记录值
    private String[] oldExp = new String[len];//表达式记录
    private String[] oldResult = new String[len];//结果记录
    private int length = 0;//记录长度
    private int index = 0;//记录指针
    private int start_Index = 0;//记录起始点
    //private Pattern op_sign = Pattern.compile("[-+*/]{2,}");//运算符重复
    //private Pattern op_start = Pattern.compile("^\\(|(([1-9]\\d+|[0-9])[-+*/].*)");//表达式开头
    //private Pattern op_end = Pattern.compile("(.*[-+*/][1-9]\\d+|[0-9])|\\)$");//表达式结尾
    private Pattern op_mm = Pattern.compile("(([1-9]\\d+|[0-9])(\\.[0-9]+)?)([-+*/](([1-9]\\d+|[0-9])(\\.[0-9]+)?))*");//无括号表达式匹配
    private Pattern op_kk = Pattern.compile("\\([^(]*?\\)");//最小括号表达式匹配
    //private Pattern op_as = Pattern.compile("[-+]");//+-
    private Pattern is_num = Pattern.compile("-?[0-9]+(\\.[0-9]+)?");//+-
    private boolean is_Illegal(String input) {//是否非法
        //按照括号划分，从最小的括号开始
        //如果括号内满足条件，括号内（包括括号）改成1
        //直到有任一不满足返回fales
        Matcher m0 = op_kk.matcher(input);
        String Expmin;
        while (m0.find()) {//括号在表达式里
            Expmin = m0.group();
            //匹配最小括号：第一个)且(最近
            if (Exp_Is_Illegal(Expmin.replace("(", "").replace(")", ""))) {//匹配最小括号里的表达式
                //匹配失败，退出返回false
                return false;
            } else {
                //替换刚刚的表达式为1
                input = input.replace(Expmin,"1");
                m0 = op_kk.matcher(input);
            }
        }
        //匹配最后的无括号表达式
        //匹配失败，退出返回false
        return !Exp_Is_Illegal(input);
    }
    private boolean Exp_Is_Illegal(String input) {//表达式是否非法
        
        Matcher m1 = op_mm.matcher(input);
        return !m1.matches();
    }
    private boolean is_Number(String input) {//是否是数字
        Matcher m2 = is_num.matcher(input);
        return m2.matches();
    }
    private double op_All(String input) {//运算输入的表达式
        //input = Pattern.compile("^-").matcher(input).replaceAll("0-");
        Matcher m0 = op_kk.matcher(input);
        String Expmin;
        while (m0.find()) {//括号在表达式里
            Expmin = m0.group();
            //匹配最小括号：第一个)且(最近
            //替换刚刚的表达式为值
            input = input.replace(Expmin,String.valueOf(op_Exp(Expmin.replace("(", "").replace(")", ""))));
            //System.out.println("input: " + input);
            m0 = op_kk.matcher(input);
        }
        //匹配最后的无括号表达式
        //匹配失败，退出返回false
        return op_Exp(input);
    }
    private double op_Exp(String input) {//无括号运算，上限200目前
        //先根据加减划分表达式，分成乘法表达式和符号两个列表
        //计算每个n的值
        //将每个n的值运算
        String[] input0 = input.split("[-+]");//以加减划分的表达式
        String temp = Pattern.compile("[1-9]\\d+|[0-9]|[*/]|\\.").matcher(input).replaceAll("1");//中间变量，方便分解出符号
        String[] input1 = temp.split("1+");//符号，首位为空
        double[] result0 = new double[200];//定义最高录入数据（暂无非定长解决方法）
        for (int i = 0; i < input0.length; i++) {//所有划分的表达式运算
            result0[i] = opExp_Mult_Div(input0[i]);
            //System.out.println("result " + result0[i]);
        }
        double result = result0[0];
        for (int j = 1; j < input1.length; j++) {//运算剩余的符号
            if (input1[j].equals("+")) {
                result = result + result0[j];
            } else {
                result = result - result0[j];
            }
        }
        return result;
    }
    private double opExp_Mult_Div(String input) {//单乘除表达式运算
        //计算乘除    a*b*c/d/e
        System.out.println("*/" + input);
        String[] input0 = input.split("[*/]");
        String[] input1 = input.split("[0-9]+(\\.[0-9]+)?");
        if (input1.length == 0) {
            //System.out.println("*/sum" + Double.parseDouble(input0[0]));
            return Double.parseDouble(input0[0]);
        }
        double sum = Double.parseDouble(input0[0]);
        for (int i = 1; i < input1.length; i++) {
            if (input1[i].equals("*")) {
                sum = sum * Double.parseDouble(input0[i]);
            } else {
                if (Double.parseDouble(input0[i]) != 0) {
                    sum = sum / Double.parseDouble(input0[i]);
                } else {
                    is_Zero_Wrong = true;
                    break;
                }
            }
        }
        //System.out.println("*/sum" + sum);
        return sum;
    }
}