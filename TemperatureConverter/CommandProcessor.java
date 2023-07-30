package TemperatureConverter;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Stack;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;

public class CommandProcessor implements ActionListener, Serializable {
    private View viewRemote;
    private JFileChooser save;
    private Model modelRemote;

    private Stack<Memento> undoStack = new Stack<Memento>();
    private Stack<Memento> redoStack = new Stack<Memento>();

    public CommandProcessor(View viewRemote) {
        this.viewRemote = viewRemote;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == viewRemote.getCelButtonRemote())
            execute(new C2FCommand(modelRemote));
        else if (e.getSource() == viewRemote.getFahButtonRemote()) {
            execute(new F2CCommand(modelRemote));
        } else
            switch (((JMenuItem) e.getSource()).getText()) {
                case "Celcius to Fahrenheit":
                    execute(new C2FCommand(modelRemote));
                    break;
                case "Fahrenheit to Celcius":
                    execute(new F2CCommand(modelRemote));
                    break;
                case "Undo":
                    undo();
                    break;
                case "Redo":
                    redo();
                    break;
                case "Save":
                    save();
                    break;
                case "Save As...":
                    saveAs();
                    break;
                case "Open": {
                    try {
                        confirmSaving();
                    } catch (ClassNotFoundException | IOException e1) {
                        e1.printStackTrace();
                    }
                    try {
                        open();
                    } catch (IOException e1) {
                    } catch (ClassNotFoundException e1) {
                    }
                    break;
                }
                case "Exit": {
                    try {
                        confirmSaving();
                    } catch (ClassNotFoundException | IOException e1) {
                        e1.printStackTrace();
                    }
                    System.exit(0);
                }
            }
    }

    public void setModelRemote(Model modelRemote) {
        this.modelRemote = modelRemote;
    }

    private void execute(Command command) {
        double currentCel = modelRemote.getCel();
        command.execute();
        if (currentCel != modelRemote.getCel()) {
            undoStack.push(modelRemote.saveToMemento());
            redoStack.clear();
        }
    }

    private void undo() {
        if (!undoStack.isEmpty()) {
            Memento memento = undoStack.pop();
            redoStack.push(memento);
            if (!undoStack.isEmpty()) {
                Memento undo = undoStack.peek();
                modelRemote.getFromMemento(undo);
                modelRemote.notifyObservers();
            } else {
                modelRemote.setCel(0);
                modelRemote.setFah(modelRemote.c2f());
            }
        }
    }

    private void redo() {
        if (!redoStack.isEmpty()) {
            Memento memento = redoStack.pop();
            modelRemote.getFromMemento(memento);
            modelRemote.notifyObservers();
            undoStack.push(memento);
        }
    }

    private void save() {
        if (save != null) {
            try {
                String file = save.getSelectedFile().toString();
                if (!file.endsWith(".bin"))
                    file += ".bin";
                FileOutputStream fileOutputStream = new FileOutputStream(file, false);
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
                objectOutputStream.writeObject(this.modelRemote);
                objectOutputStream.close();
                JOptionPane.showMessageDialog(save, "Saved as file: " + save.getSelectedFile());
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else
            saveAs();
    }

    private void saveAs() {
        save = new JFileChooser();
        save.setCurrentDirectory(new File("Document"));
        save.setDialogTitle("Choose a file to save as");
        int userSelection = save.showSaveDialog(null);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            try {
                String file = save.getSelectedFile().toString();
                if (!file.endsWith(".bin"))
                    file += ".bin";
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
                objectOutputStream.writeObject(this.modelRemote);
                objectOutputStream.close();
                JOptionPane.showMessageDialog(save, "Saved as file: " + save.getSelectedFile());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void open() throws IOException, ClassNotFoundException {
        JFileChooser open = new JFileChooser();
        open.setCurrentDirectory(new File("Document"));
        open.setDialogTitle("Choose a file to open");
        int userSelection = open.showOpenDialog(open);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            try {
                String file = open.getSelectedFile().toString();
                if (!file.endsWith(".bin"))
                    file += ".bin";
                ObjectInputStream fileInputStream = new ObjectInputStream(new FileInputStream(file));
                setModelRemote((Model) fileInputStream.readObject());
                boolean contains = false;
                for (View view : modelRemote.observers) {
                    if (view.equals(viewRemote)) {
                        contains = true;
                        break;
                    }
                }
                if (!contains)
                    viewRemote = modelRemote.observers.get(modelRemote.observers.size() - 1);
                try {
                    fileInputStream.close();
                } catch (Exception e) {
                }
                JOptionPane.showMessageDialog(open, "File opened: " +
                        open.getSelectedFile());
                for (Frame frame : JFrame.getFrames()) {
                    frame.dispose();
                }
                modelRemote.observers.get(modelRemote.observers.size() - 1).display();
            } catch (InvalidClassException e) {
                JOptionPane.showMessageDialog(open, "File invalid!", "ERROR!", JOptionPane.WARNING_MESSAGE);
                open();
            }

        }
    }

    private void confirmSaving() throws ClassNotFoundException, IOException {
        boolean isAltered = false;
        if (save != null) {
            ObjectInputStream fileInputStream = new ObjectInputStream(new FileInputStream(save.getSelectedFile()));
            fileInputStream.close();
            Model model = (Model) fileInputStream.readObject();
            if (modelRemote.equals(model)) {
                isAltered = false;
            }
        }
        if (isAltered || ((save == null) && (!undoStack.empty() || !redoStack.empty()))) {
            int option = JOptionPane.showConfirmDialog(save, "Save changes before exit?", "Save?",
                    JOptionPane.YES_NO_OPTION);
            if (option == 0) {
                save();
            } else
                return;
        }
    }
}
