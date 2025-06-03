package vn.edu.hcmuaf.st.web.signaturetool;

import vn.edu.hcmuaf.st.web.signaturetool.controller.SignatureController;
import vn.edu.hcmuaf.st.web.signaturetool.model.SignatureModel;
import vn.edu.hcmuaf.st.web.signaturetool.view.SignatureView;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SignatureModel model = new SignatureModel();
            SignatureView view = new SignatureView();
            new SignatureController(model, view);
        });
    }
}
