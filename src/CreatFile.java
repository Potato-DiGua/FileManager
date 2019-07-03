import javax.swing.*;

public class CreatFile {
    public JPanel mpanel;
    private JTextArea textArea1;
    private JScrollPane jsp;
    public CreatFile(String content)
    {
        textArea1.setText(content);
    }
    public String getContent()
    {
        return textArea1.getText();
    }
}
