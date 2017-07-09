
import ec.discheca.configuracion.HibernateSessionHandler;
import ec.discheca.dao.DAOPermiso1;
import ec.discheca.dao.DAOProductos;
import ec.discheca.modelo.Producto;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Usuario
 */
public class puntosporcomas {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        List<Producto> productos = new ArrayList<Producto>();
        try {
            HibernateSessionHandler asd = new HibernateSessionHandler();
            DAOPermiso1 daoproductos = new DAOPermiso1();

            productos = daoproductos.obtenerPermiso();
            for (int i = 0; i < productos.size(); i++) {
                 Producto nuevo = new Producto();
                //decimanles

//                String porcentaje1 = productos.get(i).getPorcentajeVenta();
//                nuevo.setPorcentajeVenta(String.format("%.4f", Double.parseDouble(porcentaje1)));
//                String porcentaje2 = productos.get(i).getPorcentajeVentaDos();
//                nuevo.setPorcentajeVentaDos(String.format("%.4f", Double.parseDouble(porcentaje2)));
//                String preciocosto = productos.get(i).getPrecioCosto();
//                nuevo.setPrecioCosto(String.format("%.4f", Double.parseDouble(preciocosto)));
//                String precioventa = productos.get(i).getPrecioVenta();
//                nuevo.setPrecioVenta(String.format("%.4f", Double.parseDouble(precioventa)));
//                String precioventados = productos.get(i).getPrecioVentaDos();
//                nuevo.setPrecioVentaDos(String.format("%.4f", Double.parseDouble(precioventados)));
                 
//punto coma
                String cantidad = productos.get(i).getCantidad().replace(",", ".");
                nuevo.setCantidad(cantidad);
                String porcentaje1 = productos.get(i).getPorcentajeVenta().replace(",", ".");;
                nuevo.setPorcentajeVenta(porcentaje1);
                String porcentaje2 = productos.get(i).getPorcentajeVentaDos().replace(",", ".");;
                nuevo.setPorcentajeVentaDos(porcentaje2);
                String preciocosto = productos.get(i).getPrecioCosto().replace(",", ".");;
                nuevo.setPrecioCosto(preciocosto);
                String precioventa = productos.get(i).getPrecioVenta().replace(",", ".");;
                nuevo.setPrecioVenta(precioventa);
                String precioventados = productos.get(i).getPrecioVentaDos().replace(",", ".");;
                nuevo.setPrecioVentaDos(precioventados);
                daoproductos.actualizarProducto(productos.get(i).getIdproducto(), nuevo);

                if(i==2697){
                          System.out.println(i);  
                }
                System.out.println(i);
                

            }
            System.out.println("fin");
            asd.close();
            System.out.println("finfin");
        } catch (Exception ex) {
            Logger.getLogger(puntosporcomas.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
