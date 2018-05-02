package POC;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.Security;
import java.security.spec.ECGenParameterSpec;

import javax.crypto.Cipher;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class ECC {
	public static void main(String[] args) throws Exception {
	    Security.addProvider(new BouncyCastleProvider());

	    KeyPairGenerator ecKeyGen = KeyPairGenerator.getInstance("EC", BouncyCastleProvider.PROVIDER_NAME);
//	    KeyPairGenerator ecKeyGen = KeyPairGenerator.getInstance("EC", "ORACLE");
	    ecKeyGen.initialize(new ECGenParameterSpec("brainpoolP384r1"));

	    // doesn't work, which means we are dancing on the leading edge :)
	    // KeyPairGenerator ecKeyGen = KeyPairGenerator.getInstance("EC");
	    // ecKeyGen.initialize(new ECGenParameterSpec("secp384r1"));

	    KeyPair ecKeyPair = ecKeyGen.generateKeyPair();
	    System.out.println("What is slow?");

	    Cipher iesCipher = Cipher.getInstance("ECIESwithAESc");
	    iesCipher.init(Cipher.ENCRYPT_MODE, ecKeyPair.getPublic());

	    byte[] ciphertext = iesCipher.doFinal(com.google.common.base.Strings.repeat("owlstead", 1000).getBytes());

	    iesCipher.init(Cipher.DECRYPT_MODE, ecKeyPair.getPrivate());
	    byte[] plaintext = iesCipher.doFinal(ciphertext);

//	    System.out.println(Hex.toHexString(ciphertext));
	    System.out.println(new String(plaintext));
	}
}
