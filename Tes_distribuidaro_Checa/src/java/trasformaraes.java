
import java.util.logging.Level;
import java.util.logging.Logger;
import sun.security.krb5.internal.crypto.Aes256;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Ricardo Delgado
 */
public class trasformaraes {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            // TODO code application logic here
            String _claveUsuarioAcceso = ec.discheca.configuracion.AES256.toAES256("ALCMcheca1954");
            System.out.println(_claveUsuarioAcceso);
        } catch (Exception ex) {
            Logger.getLogger(trasformaraes.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
}
