package ec.discheca.modelo;
// Generated May 8, 2016 10:10:06 PM by Hibernate Tools 3.6.0


import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * UsuarioAcceso generated by hbm2java
 */
public class UsuarioAcceso  implements java.io.Serializable {


     private Integer idUsuario;
     private Perfil perfil;
     private String identificacionUsuario;
     private String nombreUsuarioAcceso;
     private String claveUsuarioAcceso;
     private String nombreUsuario;
     private String apellidoUsuario;
     private String telefonoPrincipalUsuario;
     private String telefonoAdicionalUsuario;
     private String correoPrincipalUsuario;
     private String correoAdicionalUsuario;
     private Date fechaRegistroUsuario;
     private String estadoUsuario;
     private Set auditorias = new HashSet(0);

    public UsuarioAcceso() {
    }

	
    public UsuarioAcceso(Perfil perfil) {
        this.perfil = perfil;
    }
    public UsuarioAcceso(Perfil perfil, String identificacionUsuario, String nombreUsuarioAcceso, String claveUsuarioAcceso, String nombreUsuario, String apellidoUsuario, String telefonoPrincipalUsuario, String telefonoAdicionalUsuario, String correoPrincipalUsuario, String correoAdicionalUsuario, Date fechaRegistroUsuario, String estadoUsuario, Set auditorias) {
       this.perfil = perfil;
       this.identificacionUsuario = identificacionUsuario;
       this.nombreUsuarioAcceso = nombreUsuarioAcceso;
       this.claveUsuarioAcceso = claveUsuarioAcceso;
       this.nombreUsuario = nombreUsuario;
       this.apellidoUsuario = apellidoUsuario;
       this.telefonoPrincipalUsuario = telefonoPrincipalUsuario;
       this.telefonoAdicionalUsuario = telefonoAdicionalUsuario;
       this.correoPrincipalUsuario = correoPrincipalUsuario;
       this.correoAdicionalUsuario = correoAdicionalUsuario;
       this.fechaRegistroUsuario = fechaRegistroUsuario;
       this.estadoUsuario = estadoUsuario;
       this.auditorias = auditorias;
    }
   
    public Integer getIdUsuario() {
        return this.idUsuario;
    }
    
    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }
    public Perfil getPerfil() {
        return this.perfil;
    }
    
    public void setPerfil(Perfil perfil) {
        this.perfil = perfil;
    }
    public String getIdentificacionUsuario() {
        return this.identificacionUsuario;
    }
    
    public void setIdentificacionUsuario(String identificacionUsuario) {
        this.identificacionUsuario = identificacionUsuario;
    }
    public String getNombreUsuarioAcceso() {
        return this.nombreUsuarioAcceso;
    }
    
    public void setNombreUsuarioAcceso(String nombreUsuarioAcceso) {
        this.nombreUsuarioAcceso = nombreUsuarioAcceso;
    }
    public String getClaveUsuarioAcceso() {
        return this.claveUsuarioAcceso;
    }
    
    public void setClaveUsuarioAcceso(String claveUsuarioAcceso) {
        this.claveUsuarioAcceso = claveUsuarioAcceso;
    }
    public String getNombreUsuario() {
        return this.nombreUsuario;
    }
    
    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }
    public String getApellidoUsuario() {
        return this.apellidoUsuario;
    }
    
    public void setApellidoUsuario(String apellidoUsuario) {
        this.apellidoUsuario = apellidoUsuario;
    }
    public String getTelefonoPrincipalUsuario() {
        return this.telefonoPrincipalUsuario;
    }
    
    public void setTelefonoPrincipalUsuario(String telefonoPrincipalUsuario) {
        this.telefonoPrincipalUsuario = telefonoPrincipalUsuario;
    }
    public String getTelefonoAdicionalUsuario() {
        return this.telefonoAdicionalUsuario;
    }
    
    public void setTelefonoAdicionalUsuario(String telefonoAdicionalUsuario) {
        this.telefonoAdicionalUsuario = telefonoAdicionalUsuario;
    }
    public String getCorreoPrincipalUsuario() {
        return this.correoPrincipalUsuario;
    }
    
    public void setCorreoPrincipalUsuario(String correoPrincipalUsuario) {
        this.correoPrincipalUsuario = correoPrincipalUsuario;
    }
    public String getCorreoAdicionalUsuario() {
        return this.correoAdicionalUsuario;
    }
    
    public void setCorreoAdicionalUsuario(String correoAdicionalUsuario) {
        this.correoAdicionalUsuario = correoAdicionalUsuario;
    }
    public Date getFechaRegistroUsuario() {
        return this.fechaRegistroUsuario;
    }
    
    public void setFechaRegistroUsuario(Date fechaRegistroUsuario) {
        this.fechaRegistroUsuario = fechaRegistroUsuario;
    }
    public String getEstadoUsuario() {
        return this.estadoUsuario;
    }
    
    public void setEstadoUsuario(String estadoUsuario) {
        this.estadoUsuario = estadoUsuario;
    }
    public Set getAuditorias() {
        return this.auditorias;
    }
    
    public void setAuditorias(Set auditorias) {
        this.auditorias = auditorias;
    }




}

