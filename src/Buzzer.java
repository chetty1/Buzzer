import org.ardulink.core.Link;
import org.ardulink.core.Pin;
import org.ardulink.core.convenience.Links;
import org.ardulink.util.URIs;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.text.SimpleDateFormat;

public class Buzzer {


    Link link;

    public Buzzer() {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
                    ex.printStackTrace();
                }

                ContentPane periodPane = new ContentPane();
                ContentPane breakpane = new ContentPane();
                ContentPane pane = new ContentPane();
                periodPane.testPane.connectToArduino();
                periodPane.panel.getButton().addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {

                        int min1;
                        int sec;
                        if(periodPane.panel.getSec().getText().equals("")&& periodPane.panel.getMin().getText().equals("")){
                            sec=0;
                            min1=0;

                        }
                        else if (periodPane.panel.getMin().getText().equals("")){
                            min1=0;
                            sec = Integer.parseInt(periodPane.panel.getSec().getText());


                        }
                        else if(periodPane.panel.getSec().getText().equals("")){
                            sec=0;
                            min1= Integer.parseInt(periodPane.panel.getMin().getText());

                        }


                        else{
                            min1= Integer.parseInt(periodPane.panel.getMin().getText());
                            sec = Integer.parseInt(periodPane.panel.getSec().getText());
                        }
                        periodPane.testPane.duration = (min1 * 60 * 1000) + (sec * 1000);
                        periodPane.testPane.startTime = -1;
                        periodPane.testPane.timer.start();

                    }
                });

                breakpane.panel.getButton().addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {



                        int min;
                        int sec1;
                    if(breakpane.panel.getSec().getText().equals("")&& breakpane.panel.getMin().getText().equals("")){
                            min=0;
                            sec1=0;

                        }
                       else if (breakpane.panel.getMin().getText().equals("")){
                            min=0;
                            sec1 = Integer.parseInt(breakpane.panel.getSec().getText());


                        }
                        else if(breakpane.panel.getSec().getText().equals("")){
                            sec1=0;
                            min= Integer.parseInt(breakpane.panel.getMin().getText());

                        }

                        else{
                            min= Integer.parseInt(breakpane.panel.getMin().getText());
                            sec1 = Integer.parseInt(breakpane.panel.getSec().getText());
                        }


                        breakpane.testPane.duration = (min * 60 * 1000) + (sec1 * 1000);
                        breakpane.testPane.startTime = -1;
breakpane.testPane.timer.start();

                    }
                });




                pane.panel.getButton().addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        int min;
                        int sec1;

                        if (pane.panel.getMin().getText().equals("")){
                            min=0;
                            sec1 = Integer.parseInt(pane.panel.getSec().getText());


                        }
                        else if(pane.panel.getSec().getText().equals("")){
                            sec1=0;
                            min= Integer.parseInt(pane.panel.getMin().getText());

                        }
                        else if(pane.panel.getSec().getText().equals("")&& pane.panel.getMin().getText().equals("")){
                            min=0;
                            sec1=0;

                        }
                        else{
                            min= Integer.parseInt(pane.panel.getMin().getText());
                            sec1 = Integer.parseInt(pane.panel.getSec().getText());
                        }
                        pane.testPane.duration = (min * 60 * 1000) + (sec1 * 1000);
                        pane.testPane.startTime = -1;
                        pane.testPane.timer.start();

                    }
                });

                JFrame frame = new JFrame("Testing");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setLayout(new FlowLayout());
                frame.add(periodPane);
                frame.add(breakpane);
                frame.add(pane);
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        });
    }

    public static void main(String[] args) {
        new Buzzer();
    }

    public class ContentPane extends JPanel {

        public ButtonPanel panel;
        public TestPane testPane;

        public ContentPane() {
            setLayout(new BorderLayout());

            panel = new ButtonPanel();
            testPane = new TestPane();
            add(panel, BorderLayout.NORTH);
            add(testPane, BorderLayout.CENTER);
        }
    }


    public class TestPane extends JPanel {


        public Timer timer;
        public long startTime = -1;
        public long duration = 9000;
        public boolean isDone = false;


        private JLabel label;


        public TestPane() {
            setLayout(new GridBagLayout());
            timer = new Timer(1000, new ActionListener() {


                @Override
                public void actionPerformed(ActionEvent e) {


                    if (startTime < 0) {
                        startTime = System.currentTimeMillis();
                    }
                    long now = System.currentTimeMillis();
                    long clockTime = now - startTime;
                    if (clockTime >= duration) {
                        clockTime = duration;
                        timer.stop();
                    }

                    SimpleDateFormat df = new SimpleDateFormat("mm:ss");

                    if (duration - clockTime == 0) {
                        isDone = true;

                        onBuzzer();
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e1) {
                            e1.printStackTrace();
                        }
                        offBuzzer();

                    }


                    label.setText(df.format(duration - clockTime));
                    label.setFont(new Font(label.getFont().getName(), Font.PLAIN, 100));
                }

            });


            timer.setInitialDelay(0);
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (!timer.isRunning()) {
                        timer.start();

                    } else {

                        timer.stop();
                    }
                }
            });
            label = new JLabel("...");
            add(label);

        }

        public void setTime(long time) {
            duration = time;
        }

        public boolean getDone() {
            return isDone;
        }

        public void connectToArduino() {

            link = Links.getLink(URIs.newURI("ardulink://serial-jssc?port=COM3"));

        }

        public void onBuzzer() {
           try {
                link.switchDigitalPin(Pin.digitalPin(3), true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void offBuzzer() {
           try {
                link.switchDigitalPin(Pin.digitalPin(3), false);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        @Override
        public Dimension getPreferredSize() {
            return new Dimension(350, 300);
        }

    }

}