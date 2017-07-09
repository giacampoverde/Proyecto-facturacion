package ec.discheca.configuracion;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Ricardo Delgado
 */
public class CargarPropiedades {

    public boolean cargarPropiedades() {
        boolean success = false;
        InputStream in = null;
        try {
            in = this.getClass().getClassLoader().getResourceAsStream(Valores.properties_file);
            Properties p = new Properties();
            if (in != null) {
                p.load(in);
                success = true;
            }
        } catch (Exception ex) {
            Logger.getLogger(CargarPropiedades.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                in.close();
            } catch (IOException ex) {
                Logger.getLogger(CargarPropiedades.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return success;
    }

    private String clean(String propiedad) {
        if (propiedad.equals(".")) {
            return "";
        } else {
            return propiedad;
        }
    }
}
