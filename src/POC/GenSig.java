package POC;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.SignatureException;

class GenSig {
    int threadNum=4;

	static byte[] realSig=null;
    static int finishedThreads=0;
    static long start, end;
 
    public GenSig(int i) {
		this.threadNum = i;
	}

	public static void main(String[] args) {
		for(int i=1; i<=1<<8; i*=2) {
			GenSig genSig = new GenSig(i);
			genSig.sub(args);
		}
    }
    
    public void sub(String[] args) {
 
        /* Generate a DSA signature */
 
        if (args.length != 1) {
            System.out.println("Usage: GenSig nameOfFileToSign");
            }
        else try{
 
        	KeyPairGenerator keyGen;
        	PrivateKey priv;
        	PublicKey pub;
        	Signature dsa;
        	if(false)
        	{
                /* Generate a key pair */
              keyGen = KeyPairGenerator.getInstance("EC", "SunEC");
              SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
   
              keyGen.initialize(571, random);
   
              KeyPair pair = keyGen.generateKeyPair();
              priv = pair.getPrivate();
              pub = pair.getPublic();
   
   
              /* Create a Signature object and initialize it with the private key */
   
              dsa = Signature.getInstance("SHA256withECDSA", "SunEC"); 
          	} else {
               /* Generate a key pair */
              keyGen = KeyPairGenerator.getInstance("DSA", "SUN");
              SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
   
              keyGen.initialize(1024, random);
   
              KeyPair pair = keyGen.generateKeyPair();
              priv = pair.getPrivate();
              pub = pair.getPublic();
   
   
              /* Create a Signature object and initialize it with the private key */
              dsa = Signature.getInstance("SHA1withDSA", "SUN"); 
          	}
              
 
            dsa.initSign(priv);
 
            /* Update and sign the data */
 
            FileInputStream fis = new FileInputStream(args[0]);
            BufferedInputStream bufin = new BufferedInputStream(fis);
            byte[] buffer = new byte[1024];
            int len;
            while (bufin.available() != 0) {
                len = bufin.read(buffer);
                dsa.update(buffer, 0, len);
                };
 
            bufin.close();
 
            /* Now that all the data to be signed has been read in, 
                    generate a signature for it */
            Thread[] threads = new Thread[threadNum];
            for(int i=0; i<threads.length; i++) {
            	threads[i] = new Thread() {
            		public void run() {
                    	try {
                    		for(int i=0; i<1000; i++) {
                    			realSig = dsa.sign();
                    		}
                            
                            if( ++finishedThreads  == threadNum ) {
                                end = System.currentTimeMillis();
                    			System.out.print("Concurrent Threads are " + threadNum + ". ");
                    			int totalTransactions = threadNum * 1000;
                    			long elapsedTime = end - start;
                                System.out.print( " " + totalTransactions + " times digital sign elpased time " + elapsedTime + " millisecs. ");
//                                String performance = String.format("%f",  new Float((float)totalTransactions * 1000/elapsedTime).toString() );
                                float performance = new Float((float)totalTransactions * 1000/ elapsedTime);
                                String pstr = String.format("%.2f", performance);
//                                System.out.println( " Performance is " + (float)totalTransactions * 1000 / elapsedTime + " per seconds" );
                                System.out.println( " Performance is " + pstr + " per seconds" );
                                
                            }
    					} catch (SignatureException e) {
    						e.printStackTrace();
    					}

            		}
            	};
            }
 
 
            start = System.currentTimeMillis();
            for(int i=0; i<threads.length; i++) {
            	threads[i].start();
            }
            
			while(this.finishedThreads<this.threadNum){
            	Thread.sleep(100);
            }
            /* Save the signature in a file */
//            FileOutputStream sigfos = new FileOutputStream("sig");
//            sigfos.write(realSig);
// 
//            sigfos.close();
// 
 
            /* Save the public key in a file */
            byte[] key = pub.getEncoded();
            FileOutputStream keyfos = new FileOutputStream("suepk");
            keyfos.write(key);
 
            keyfos.close();
 
        } catch (Exception e) {
            System.err.println("Caught exception " + e.toString());
        }
 
    };
 
}

// On my Lenovo Thinkpad T420 Intel(R)Core i7-2630QM @2.0Ghz
// 
// Concurrent Threads are 1.  1000 times digital sign elpased time 3438 millisecs.  Performance is 290.8668 per seconds
// Concurrent Threads are 2.  2000 times digital sign elpased time 3516 millisecs.  Performance is 568.8282 per seconds
// Concurrent Threads are 4.  4000 times digital sign elpased time 3878 millisecs.  Performance is 1031.4595 per seconds
// Concurrent Threads are 8.  8000 times digital sign elpased time 7028 millisecs.  Performance is 1138.304 per seconds
// Concurrent Threads are 16.  16000 times digital sign elpased time 13844 millisecs.  Performance is 1155.7354 per seconds
// Concurrent Threads are 32.  32000 times digital sign elpased time 27914 millisecs.  Performance is 1146.3782 per seconds
// Concurrent Threads are 64.  64000 times digital sign elpased time 55304 millisecs.  Performance is 1157.24 per seconds
//
// ECDSA
//
// Concurrent Threads are 1.  1000 times digital sign elpased time 209302 millisecs.  Performance is 4.7777853 per seconds
// Concurrent Threads are 2.  2000 times digital sign elpased time 24865 millisecs.  Performance is 80.43435 per seconds
// Concurrent Threads are 4.  4000 times digital sign elpased time 31909 millisecs.  Performance is 125.35648 per seconds
// Concurrent Threads are 8.  8000 times digital sign elpased time 50611 millisecs.  Performance is 158.0684 per seconds
// Concurrent Threads are 16.  16000 times digital sign elpased time 101482 millisecs.  Performance is 157.66342 per seconds
// Concurrent Threads are 32.  32000 times digital sign elpased time 205660 millisecs.  Performance is 155.59662 per seconds
// Concurrent Threads are 64.  64000 times digital sign elpased time 405578 millisecs.  Performance is 157.79948 per seconds
//


// On lenovo desktop Intel(R) Core(TM) i3-7100 @3.9GHz 
//
// Concurrent Threads are 1.  1000 times digital sign elpased time 386 millisecs.  Performance is 2590.67 per seconds
// Concurrent Threads are 2.  2000 times digital sign elpased time 320 millisecs.  Performance is 6250.00 per seconds
// Concurrent Threads are 4.  4000 times digital sign elpased time 645 millisecs.  Performance is 6201.55 per seconds
// Concurrent Threads are 8.  8000 times digital sign elpased time 959 millisecs.  Performance is 8342.02 per seconds
// Concurrent Threads are 16.  16000 times digital sign elpased time 1742 millisecs.  Performance is 9184.84 per seconds
// Concurrent Threads are 32.  32000 times digital sign elpased time 3330 millisecs.  Performance is 9609.61 per seconds
// Concurrent Threads are 64.  64000 times digital sign elpased time 6478 millisecs.  Performance is 9879.59 per seconds
//
// Concurrent Threads are 1.  1000 times digital sign elpased time 5988 millisecs.  Performance is 167.00067 per seconds
// Concurrent Threads are 2.  2000 times digital sign elpased time 7722 millisecs.  Performance is 259.00024 per seconds
// Concurrent Threads are 4.  4000 times digital sign elpased time 11735 millisecs.  Performance is 340.8607 per seconds
// Concurrent Threads are 8.  8000 times digital sign elpased time 23651 millisecs.  Performance is 338.25208 per seconds
// Concurrent Threads are 16.  16000 times digital sign elpased time 46546 millisecs.  Performance is 343.74597 per seconds
// Concurrent Threads are 32.  32000 times digital sign elpased time 94177 millisecs.  Performance is 339.78574 per seconds

