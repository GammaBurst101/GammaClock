
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Calendar;
import javax.sound.sampled.*;
import java.io.File;

public class AlarmClock 
{
    private JFrame frame;
    private JTextField time, alarmHour, alarmMin;
    private JPanel panel;
    private JButton b;
    private JLabel l;
    private String alarm;
    private Timer t, t2;
    private static boolean alarmRung = false;
    
    public AlarmClock()
    {
        //Setting JFrame
        frame = new JFrame ("Clock");
        frame.setSize(500, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setResizable(true);
        frame.setLocationRelativeTo(null);
        
        //initialisations and setting them
        panel = new JPanel();
        panel.setLayout(new FlowLayout());
        panel.setBounds(0 ,0, 500, 400);
        
        time = new JTextField(10);
        time.setEditable(false);
        time.setFont(new Font("Arial", Font.PLAIN, 48));
        
        alarmHour = new JTextField("HH");
        alarmHour.setBounds(250, 200, 40, 40);
        
        alarmMin = new JTextField("MM");
        alarmMin.setBounds(350, 200, 40, 40);
        
        b = new JButton("Set Alarm");
        b.setBounds(250, 350, 100, 40);
        b.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                if((b.getText().equals("Stop Alarm"))==false)
                {
                    alarm = alarmHour.getText()+":" + alarmMin.getText() +":0";
                    b.setText("Stop Alarm");
                }
                else
                {
                    alarm = "";
                    b.setText("Set Alarm");
                }
            }
        });
        
        l = new JLabel("Enter the time to set alarm at here");
        l.setBounds(100, 200, 100, 40);
        
        t = new Timer(1000,new Listener());
        t2 = new Timer(1000, new ActionListener(){
            public void actionPerformed(ActionEvent e)
            {
                if(time.getText().equals(alarm))
                ringAlarm();
                
                if(alarmRung==true && b.getText().equals("Stop Alarm"))
                {
                    b.setText("Set Alarm");
                    alarmRung = false;
                }
            }
        });
        
        //Add components
        panel.add(time);
        panel.add(l);
        panel.add(alarmHour);
        panel.add(alarmMin);
        panel.add(b);
        frame.add(panel);
        
        //Starting the timers
        t.start();
        t2.start();
    }
    
    class Listener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            Calendar cal = Calendar.getInstance();
            
            int hour = cal.get(Calendar.HOUR_OF_DAY);
            int min = cal.get(Calendar.MINUTE);
            int sec = cal.get(Calendar.SECOND);
            
            time.setText(hour+":"+min+":"+sec);
        }
    }
    
    private static void ringAlarm()
    {
        try{
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("software\\sounds\\alarm.wav").getAbsoluteFile());
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