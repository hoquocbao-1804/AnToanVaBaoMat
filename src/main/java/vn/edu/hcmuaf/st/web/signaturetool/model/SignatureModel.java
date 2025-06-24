package vn.edu.hcmuaf.st.web.signaturetool.model;
import java.security.*;
import java.util.Base64;

public class SignatureModel {
    private PrivateKey privateKey;
    private PublicKey publicKey;

    public void generateKeys() throws Exception {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(2048);
        KeyPair pair = keyGen.generateKeyPair();
        privateKey = pair.getPrivate();
        publicKey = pair.getPublic();
    }

    public void reportLostKey() {
        privateKey = null;
    }

    public String signInvoice(String invoice) throws Exception {
        if (privateKey == null) throw new Exception("Private key is lost!");
        Signature privateSignature = Signature.getInstance("SHA256withRSA");
        privateSignature.initSign(privateKey);
        privateSignature.update(invoice.getBytes());
        byte[] signature = privateSignature.sign();
        return Base64.getEncoder().encodeToString(signature);
    }

    public boolean verifySignature(String invoice, String signatureStr, PublicKey pubKey) throws Exception {
        byte[] signatureBytes = Base64.getDecoder().decode(signatureStr);
        Signature publicSignature = Signature.getInstance("SHA256withRSA");
        publicSignature.initVerify(pubKey);
        publicSignature.update(invoice.getBytes());
        return publicSignature.verify(signatureBytes);
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }
}
