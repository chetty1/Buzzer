import javax.swing.*;
import java.awt.*;

/**
 * Created by chett_000 on 2017/06/07.
 */
public class ButtonPanel extends JPanel {

   public JLabel Period ;
   public  JTextField field ;
   public  JLabel Break ;
    public JLabel min1;
    JButton button;
    JTextField field1;
    public ButtonPanel(){
         Period = new JLabel("Period Time:");
         field = new JTextField();
         min1 = new JLabel(":");

setLayout(new FlowLayout());

       field1 = new JTextField();




        button = new JButton("Start");

        field.setPreferredSize(new Dimension(40,30));
        field1.setPreferredSize(new Dimension(40,30));

        add(Period);
        add(field);
        add(min1);
        add(field1);
        add(button);
    }
    public JButton getButton(){
        return button;
    }
    public void setButtonText(String text){
        button.setText(text);
    }
    public JTextField getMin(){
        return field;
    }

    public JTextField getSec(){
        return field1;

    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(100, 100);
    }
}
