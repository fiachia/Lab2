package computer;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
                String input = inputTextField.getText();
                if (!input.equals("")){
                    if (is_Illegal(input)){
                        String output = input;
                        outputTextField.setText(output);
                    } else {
                        outputTextField.setText("The Expression is Wrong!");
                    }
                } else {
                    outputTextField.setText("No Input!");
                }
            }
            private Pattern op_sign = Pattern.compile("[-+*/]{2,}");//运算符重复
            private Pattern op_start = Pattern.compile("^\\(|(([1-9]\\d+|[0-9])[-+*/].*)");//表达式开头
            private Pattern op_end = Pattern.compile("(.*[-+*/][1-9]\\d+|[0-9])|\\)$");//表达式结尾
            private Pattern op_mm = Pattern.compile("([1-9]\\d+|[0-9])([-+*/]([1-9]\\d+|[0-9]))*");//无括号表达式匹配
            private Pattern op_kk = Pattern.compile("\\(.*?\\)");//最小括号表达式匹配
            private boolean is_Illegal(String input) {
                //按照括号划分，从最小的括号开始
                //如果括号内满足条件，括号内（包括括号）改成1
                //直到有任一不满足返回fales
                Matcher m0 = op_kk.matcher(input);
                while (m0.find()) {//括号在表达式里

                    //匹配最小括号：第一个)且(最近
                    if (!Exp_Is_Illegal(input)) {//匹配最小括号里的表达式
                        //匹配失败，退出返回false
                        return false;
                    } else {
                        //替换刚刚的表达式为1
                        input = "1";
                        m0 = op_kk.matcher(input);
                    }
                }
                if (!Exp_Is_Illegal(input)) {//匹配最后的无括号表达式
                    //匹配失败，退出返回false
                    return false;
                } else {
                    return true;
                }
            }
            private boolean sign_Is_Illegal(String input) {
                Matcher m1 = op_sign.matcher(input);
                if (m1.find()){
                    return false;
                }
                return true;
            }
            private boolean start_Is_Illegal(String input) {
                Matcher m2 = op_start.matcher(input);
                if (m2.find()) {
                    return true;
                }
                return false;
            }
            private boolean end_Is_Illegal(String input) {
                Matcher m3 = op_end.matcher(input);
                if (m3.find()) {
                    return true;
                }
                return false;
            }
            private boolean Exp_Is_Illegal(String input) {
                //按照运算符拆分，剩下的都能转成数字
                //满足开始与结束
                if (start_Is_Illegal(input)&end_Is_Illegal(input)&sign_Is_Illegal(input)) {
                    return true;
                }
                return false;
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
}