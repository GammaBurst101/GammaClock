import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Calendar;
import javax.sound.sampled.*;
import java.io.*;

public class AlarmClock 
{
    private JFrame frame;
    private JTextField timeDisplay, alarmHour, alarmMin;
    private JPanel panel;
    private JButton btn;
    private JLabel txtL;
    private String alarm;
    private Timer time, alarmChecker;
    private static boolean alarmRung = false;
    private AlarmSystem alarmSystem;

    public AlarmClock()
    {
        //Setting JFrame
        frame = new JFrame ("Clock");
        frame.setSize(500, 140);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);

        //initialisations and setting them

        //alarm
        alarmSystem = new AlarmSystem();

        //background panel
        panel = new JPanel();
        panel.setLayout(new FlowLayout());

        //display for current time
        timeDisplay = new JTextField(10);
        timeDisplay.setEditable(false);
        timeDisplay.setFont(new Font("Arial", Font.PLAIN, 48));
        timeDisplay.setHorizontalAlignment(JTextField.CENTER);

        //input boxes for alarm time
        alarmHour = new JTextField("HH");
        alarmHour.setMargin(new Insets(2, 2, 2, 2));
        InputFieldListener inputFieldListener = new InputFieldListener();
        alarmHour.addMouseListener(inputFieldListener);
        alarmMin = new JTextField("MM");  
        alarmMin.setMargin(new Insets(2, 2, 2, 2));
        alarmMin.addMouseListener(inputFieldListener);

        //set/stop alarm button
        btn = new JButton("Set Alarm");
        btn.setFocusPainted(false);
        btn.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
                    if((btn.getText().equals("Stop Alarm"))==false)
                    {
                        alarm = alarmHour.getText()+":" + alarmMin.getText() +":0";
                        btn.setText("Stop Alarm");
                    }
                    else
                    {
                        alarm = "";
                        btn.setText("Set Alarm");
                    }
                }
            });

        //label to prompt users
        txtL = new JLabel("Enter the time to set alarm at here");

        //timers for managing the current time and checking it
        time = new Timer(1000,new TimeListener());
        alarmChecker = new Timer(1000, new ActionListener(){
                public void actionPerformed(ActionEvent e)
                {
                    if(timeDisplay.getText().equals(alarm))
                        alarmSystem.ringAlarm();

                    if(alarmRung==true && btn.getText().equals("Stop Alarm"))
                    {
                        btn.setText("Set Alarm");
                        alarmRung = false;
                    }
                }
            });

        //Add components
        
        panel.add(timeDisplay);
        panel.add(txtL);
        panel.add(alarmHour);
        panel.add(alarmMin);
        panel.add(btn);
        frame.add(panel);

        //Starting the timers
        time.start();
        alarmChecker.start();

        //setting frame to visible in last so that the frame doesn't need refresh to show all components
        frame.setVisible(true);
    }

    class TimeListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            Calendar cal = Calendar.getInstance();

            int hour = cal.get(Calendar.HOUR_OF_DAY);
            int min = cal.get(Calendar.MINUTE);
            int sec = cal.get(Calendar.SECOND);

            timeDisplay.setText(hour+":"+min+":"+sec);
        }
    }

    class AlarmSystem {
        private void ringAlarm()
        {
            try{
                InputStream audioSrc = getClass().getResourceAsStream("sounds\\alarm.wav");
                InputStream bufferedInput = new BufferedInputStream(audioSrc);
                AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(bufferedInput);
                Clip clip = AudioSystem.getClip();
                clip.open(audioInputStream);
                clip.start();
                alarmRung = true;
            }catch(Exception e) {
                System.out.println ("Error with playing sound.");
                e.printStackTrace();
            }
        }
    }
    
    class InputFieldListener implements MouseListener {
        public void mouseClicked(MouseEvent e) {
            ((JTextField)e.getSource()).setText("");
        }
        
        public void mouseExited(MouseEvent e) {}
        
        public void mouseEntered(MouseEvent e) {}
        
        public void mouseReleased(MouseEvent e) {}
        
        public void mousePressed(MouseEvent e) {}
    }
}