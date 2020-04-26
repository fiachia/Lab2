package computer;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Computer {
    private JButton ACButton1;
    private JButton cButton;
    private JButton mButton2;
    private JButton mButton3;
    private JButton mButton1;
    private JButton lastButton;
    private JButton quitButton;
    private JButton button8;
    private JPanel computer;
    private JTextField inputTextField;
    private JTextField outputTextField;

    public Computer() {
        button8.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                show_Result();
            }
        });
        inputTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    show_Result();
                }
            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Computer");
        frame.setContentPane(new Computer().computer);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
    private void show_Result() {
        String input = inputTextField.getText();
        if (!input.equals("")){
            if (is_Illegal(input)){
                double output = op_All(input);
                if (!is_Zero_Wrong) {
                    outputTextField.setText(String.valueOf(output));
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
    private boolean is_Zero_Wrong = false;
    //private Pattern op_sign = Pattern.compile("[-+*/]{2,}");//运算符重复
    //private Pattern op_start = Pattern.compile("^\\(|(([1-9]\\d+|[0-9])[-+*/].*)");//表达式开头
    //private Pattern op_end = Pattern.compile("(.*[-+*/][1-9]\\d+|[0-9])|\\)$");//表达式结尾
    private Pattern op_mm = Pattern.compile("([1-9]\\d+|[0-9])([-+*/]([1-9]\\d+|[0-9]))*");//无括号表达式匹配
    private Pattern op_kk = Pattern.compile("\\(.*?\\)");//最小括号表达式匹配
    private Pattern op_as = Pattern.compile("[-+]");//+-
    private boolean is_Illegal(String input) {
        //按照括号划分，从最小的括号开始
        //如果括号内满足条件，括号内（包括括号）改成1
        //直到有任一不满足返回fales
        Matcher m0 = op_kk.matcher(input);
        String Expmin;
        while (m0.find()) {//括号在表达式里
            Expmin = m0.group(0);
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
    private boolean Exp_Is_Illegal(String input) {
        Matcher m1 = op_mm.matcher(input);
        return !m1.matches();
    }
    private double op_All(String input) {
        Matcher m0 = op_kk.matcher(input);
        String Expmin;
        while (m0.find()) {//括号在表达式里
            Expmin = m0.group(0);
            //匹配最小括号：第一个)且(最近
            //替换刚刚的表达式为值
            input = input.replace(Expmin,String.valueOf(op_Exp(Expmin.replace("(", "").replace(")", ""))));
            m0 = op_kk.matcher(input);
        }
        //匹配最后的无括号表达式
        //匹配失败，退出返回false
        return op_Exp(input);
    }
    private double op_Exp(String input) {//无括号运算
        //先根据加减划分表达式，分成n和n-1两个列表
        //计算每个n的值
        //将每个n的值运算
        String[] input0 = input.split("[-+]");
        String temp = Pattern.compile("[1-9]\\d+|[0-9]|[*/]|\\.").matcher(input).replaceAll("1");
        String[] input1 = temp.split("1+");
        double[] result0 = new double[200];
        for (int i = 0; i < input0.length; i++) {
            result0[i] = opExp_Mult_Div(input0[i]);
        }
        double result = result0[0];
        for (int j = 1; j < input1.length; j++) {
            if (input1[j].equals("+")) {
                result = result + result0[j];
            } else {
                result = result - result0[j];
            }
        }
        return result;
    }
    private double opExp_Mult_Div(String input) {
        //计算乘除    a*b*c/d/e
        String[] input0 = input.split("[*/]");
        String[] input1 = input.split("[1-9]\\d+|[0-9]|\\.");
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
        return sum;
    }
}