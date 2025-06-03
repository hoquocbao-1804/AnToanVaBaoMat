package vn.edu.hcmuaf.st.web.signaturetool.controller;

import vn.edu.hcmuaf.st.web.signaturetool.model.SignatureModel;
import vn.edu.hcmuaf.st.web.signaturetool.view.SignatureView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.security.PublicKey;
import java.util.Base64;

public class SignatureController {
    private SignatureModel model;
    private SignatureView view;

    public SignatureController(SignatureModel model, SignatureView view) {
        this.model = model;
        this.view = view;

        view.btnGenerateKey.addActionListener(e -> handleGenerateKey());
        view.btnReportLostKey.addActionListener(e -> handleLostKey());
        view.btnSign.addActionListener(e -> handleSign());
        view.btnVerify.addActionListener(e -> handleVerify());
    }

    private void handleGenerateKey() {
        try {
            model.generateKeys();
            view.lblStatus.setText("Đã tạo khóa mới.");
        } catch (Exception ex) {
            view.lblStatus.setText("Lỗi tạo khóa: " + ex.getMessage());
        }
    }

    private void handleLostKey() {
        model.reportLostKey();
        view.lblStatus.setText("Đã báo mất khóa.");
    }

    private void handleSign() {
        try {
            String invoice = view.txtInvoice.getText();
            String signature = model.signInvoice(invoice);
            view.txtSignature.setText(signature);
            view.lblStatus.setText("Đã ký hóa đơn.");
        } catch (Exception ex) {
            view.lblStatus.setText("Lỗi ký: " + ex.getMessage());
        }
    }

    private void handleVerify() {
        try {
            String invoice = view.txtInvoice.getText();
            String signature = view.txtSignature.getText();
            PublicKey pubKey = model.getPublicKey();

            if (pubKey == null) {
                view.lblStatus.setText("Không có khóa công khai để xác thực.");
                return;
            }

            boolean isValid = model.verifySignature(invoice, signature, pubKey);
            view.lblStatus.setText(isValid ? "Chữ ký hợp lệ." : "Chữ ký không hợp lệ!");
        } catch (Exception ex) {
            view.lblStatus.setText("Lỗi xác thực: " + ex.getMessage());
        }
    }
}