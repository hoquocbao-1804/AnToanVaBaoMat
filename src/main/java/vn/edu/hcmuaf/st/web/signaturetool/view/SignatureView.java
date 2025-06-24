package vn.edu.hcmuaf.st.web.signaturetool.view;

import javax.swing.*;
import java.awt.*;

public class SignatureView extends JFrame {
    public JButton btnGenerateKey = new JButton("Tạo Khóa");
    public JButton btnReportLostKey = new JButton("Báo Mất Khóa");
    public JButton btnSign = new JButton("Ký Hóa Đơn");
    public JButton btnVerify = new JButton("Xác Thực");
    public JTextArea txtInvoice = new JTextArea(10, 40);
    public JTextField txtSignature = new JTextField(40);
    public JLabel lblStatus = new JLabel("Trạng thái: ");

    public SignatureView() {
        setTitle("Ứng Dụng Chữ Ký Điện Tử");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new BorderLayout());
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(btnGenerateKey);
        buttonPanel.add(btnReportLostKey);
        buttonPanel.add(btnSign);
        buttonPanel.add(btnVerify);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(new JScrollPane(txtInvoice), BorderLayout.CENTER);

        JPanel signaturePanel = new JPanel(new BorderLayout());
        signaturePanel.add(new JLabel("Chữ ký: "), BorderLayout.WEST);
        signaturePanel.add(txtSignature, BorderLayout.CENTER);

        JPanel statusPanel = new JPanel(new BorderLayout());
        statusPanel.add(lblStatus, BorderLayout.CENTER);

        panel.add(buttonPanel, BorderLayout.NORTH);
        panel.add(centerPanel, BorderLayout.CENTER);
        panel.add(signaturePanel, BorderLayout.SOUTH);
        panel.add(statusPanel, BorderLayout.PAGE_END);

        add(panel);
        setVisible(true);
    }
}

