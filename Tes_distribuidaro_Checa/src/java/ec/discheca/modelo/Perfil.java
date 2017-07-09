package ec.discheca.modelo;
// Generated May 8, 2016 10:10:06 PM by Hibernate Tools 3.6.0


import java.util.HashSet;
import java.util.Set;

/**
 * Perfil generated by hbm2java
 */
public class Perfil  implements java.io.Serializable {


     private Integer idPerfil;
     private String nombrePerfil;
     private String descripcionPerfil;
     private Boolean estadoPerfil;
     private Set usuarioAccesos = new HashSet(0);
     private Set permisos = new HashSet(0);

    public Perfil() {
    }

    public Perfil(String nombrePerfil, String descripcionPerfil, Boolean estadoPerfil, Set usuarioAccesos, Set permisos) {
       this.nombrePerfil = nombrePerfil;
       this.descripcionPerfil = descripcionPerfil;
       this.estadoPerfil = estadoPerfil;
       this.usuarioAccesos = usuarioAccesos;
       this.permisos = permisos;
    }
   
    public Integer getIdPerfil() {
        return this.idPerfil;
    }
    
    public void setIdPerfil(Integer idPerfil) {
        this.idPerfil = idPerfil;
    }
    public String getNombrePerfil() {
        return this.nombrePerfil;
    }
    
    public void setNombrePerfil(String nombrePerfil) {
        this.nombrePerfil = nombrePerfil;
    }
    public String getDescripcionPerfil() {
        return this.descripcionPerfil;
    }
    
    public void setDescripcionPerfil(String descripcionPerfil) {
        this.descripcionPerfil = descripcionPerfil;
    }
    public Boolean getEstadoPerfil() {
        return this.estadoPerfil;
    }
    
    public void setEstadoPerfil(Boolean estadoPerfil) {
        this.estadoPerfil = estadoPerfil;
    }
    public Set getUsuarioAccesos() {
        return this.usuarioAccesos;
    }
    
    public void setUsuarioAccesos(Set usuarioAccesos) {
        this.usuarioAccesos = usuarioAccesos;
    }
    public Set getPermisos() {
        return this.permisos;
    }
    
    public void setPermisos(Set permisos) {
        this.permisos = permisos;
    }




}

