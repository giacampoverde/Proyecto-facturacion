/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.egastos.vistas.ec;

import com.egastos.utilidades.ControlSesion;
import com.egastos.dao.ec.DAORecursoPantalla;
import com.egastos.modelo.ec.Recurso;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.DefaultSubMenu;
import org.primefaces.model.menu.MenuModel;


@ManagedBean
@ViewScoped
public class BeanMenu implements Serializable {

    /**
     * Creates a new instance of BeanMenu
     */
    private MenuModel model;
    private List<Recurso> pantallasHijas = new ArrayList<Recurso>();

    public BeanMenu() throws Exception {
        ControlSesion ms = new ControlSesion();
            if (!ms.obtenerEstadoSesionUsuario()) {

                BeanDireccionamiento nosesion=new BeanDireccionamiento();
                nosesion.direccionarLogin();
            }
        model = new DefaultMenuModel();
        DefaultSubMenu subMenu = null;
        /**
         *
         */

        DAORecursoPantalla dao = new DAORecursoPantalla();
        ControlSesion sesion=new ControlSesion();
        List<Recurso> recursos = dao.obtenerPantallasPorPerfil(Integer.parseInt(sesion.obtenerIdPerfilUsuarioSesionActiva()));

        for (int i = 0; i < recursos.size(); i++) {
            subMenu = new DefaultSubMenu(recursos.get(i).getNombreRecurso());
//            subMenu.setIcon("icon-tasks");
            if (recursos.get(i).getPestaniaRecurso() != null && recursos.get(i).getPestaniaRecurso().booleanValue() == true) {
                pantallasHijas = dao.obtenerPantallasPorPestaniaPorPerfil(recursos.get(i).getIdRecurso(), Integer.parseInt(sesion.obtenerIdPerfilUsuarioSesionActiva()));
                if (pantallasHijas != null) {
                    for (int j = 0; j < pantallasHijas.size(); j++) {
                         DefaultMenuItem menutItem = new DefaultMenuItem("- "+pantallasHijas.get(j).getNombreRecurso());                        
                        menutItem.setUrl(pantallasHijas.get(j).getPaginaRecurso().concat(".xhtml"));
//                        menutItem.setIcon("icon-tasks");
                        subMenu.addElement(menutItem);
                    }
                }
                model.addElement(subMenu);
            }
        }
    }

    public MenuModel getModel() {
        return model;
    }

    public void setModel(MenuModel model) {
        this.model = model;
    }

}
