package TemperatureConverter;

import java.awt.Dimension;
import java.awt.GridBagLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

public class View extends JPanel implements Subscriber {
    private String title;
    private JMenuBar menuBarRemote;
    private JLabel celLabelRemote, fahLabelRemote;
    private JButton celButtonRemote, fahButtonRemote;
    private CommandProcessor controllerRemote;
    private Model modelRemote;

    public View() {
        setLayout(new GridBagLayout());
        setPreferredSize(new Dimension(500, 30));
        setMinimumSize(new Dimension(500, 30));
        controllerRemote = new CommandProcessor(this);
        setSubject(new Model());
        modelRemote.attach(this);
        controllerRemote.setModelRemote(modelRemote);
        title = "Temperature Converter";
        celLabelRemote = new JLabel("Celcius: ");
        fahLabelRemote = new JLabel("Fahrenheit: ");
        celButtonRemote = new JButton();
        fahButtonRemote = new JButton();
        celButtonRemote.setText("0.0");
        fahButtonRemote.setText("32.0");
        celButtonRemote.addActionListener(controllerRemote);
        fahButtonRemote.addActionListener(controllerRemote);
        add(celLabelRemote);
        add(celButtonRemote);
        add(new JLabel("   "));
        add(fahLabelRemote);
        add(fahButtonRemote);
    }

    public void display() {
        JFrame frame = new JFrame(title);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        menuBarRemote = new JMenuBar();
        JMenu fileMenu = new JMenu("File", false);
        JMenu editMenu = new JMenu("Edit", false);
        JMenuItem saveMenuItem = new JMenuItem("Save");
        JMenuItem saveAsMenuItem = new JMenuItem("Save As...");
        JMenuItem openMenuItem = new JMenuItem("Open");
        JMenuItem exitMenuItem = new JMenuItem("Exit");
        JMenuItem c2fMenuItem = new JMenuItem("Celcius to Fahrenheit");
        JMenuItem f2cMenuItem = new JMenuItem("Fahrenheit to Celcius");
        JMenuItem undoMenuItem = new JMenuItem("Undo");
        JMenuItem redoMenuItem = new JMenuItem("Redo");
        c2fMenuItem.addActionListener(controllerRemote);
        f2cMenuItem.addActionListener(controllerRemote);
        saveMenuItem.addActionListener(controllerRemote);
        saveAsMenuItem.addActionListener(controllerRemote);
        openMenuItem.addActionListener(controllerRemote);
        undoMenuItem.addActionListener(controllerRemote);
        redoMenuItem.addActionListener(controllerRemote);
        exitMenuItem.addActionListener(controllerRemote);
        fileMenu.add(saveMenuItem);
        fileMenu.add(saveAsMenuItem);
        fileMenu.add(openMenuItem);
        fileMenu.add(exitMenuItem);
        editMenu.add(c2fMenuItem);
        editMenu.add(f2cMenuItem);
        editMenu.add(undoMenuItem);
        editMenu.add(redoMenuItem);
        menuBarRemote.add(fileMenu);
        menuBarRemote.add(editMenu);
        frame.setJMenuBar(menuBarRemote);
        frame.add(this);
        frame.pack();
        frame.setLocationRelativeTo(frame);
        frame.setVisible(true);
    }

    public JButton getCelButtonRemote() {
        return celButtonRemote;
    }

    public JButton getFahButtonRemote() {
        return fahButtonRemote;
    }

    @Override
    public void update(double cel, double fah) {
        String celcius = String.valueOf(cel);
        String fahrenheit = String.valueOf(fah);
        celButtonRemote.setText(celcius);
        fahButtonRemote.setText(fahrenheit);
    }

    @Override
    public void setSubject(Publisher subject) {
        modelRemote = (Model) subject;
    }
}
