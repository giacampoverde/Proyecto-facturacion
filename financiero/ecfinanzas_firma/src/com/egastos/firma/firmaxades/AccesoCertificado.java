/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.egastos.firma.firmaxades;

import java.lang.reflect.Field;
import java.security.KeyStore;

import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.Security;
import java.security.UnrecoverableKeyException;
import java.security.cert.X509Certificate;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bouncycastle.jce.provider.BouncyCastleProvider;


/**
 *
* @Ricardo Delgado
 */
public class AccesoCertificado {

    private KeyStore keyStore;

    private Provider provider;

    private X509Certificate certificate;

    private PrivateKey privateKey;

    //Path del certificado
    private String path_certificado;

    //Clave del certificado
    private String password_certificado;

    //Clave del keystore
    private String password_key_store;

    private String alias;

    public AccesoCertificado(String path_certificado, String password_certificado, String password_key_store) {
        this.path_certificado = path_certificado;
        this.password_certificado = password_certificado;
        this.password_key_store = password_key_store;
    }

    public boolean accederCertificado() {
        if (Security.getProvider(BouncyCastleProvider.PROVIDER_NAME) == null) {
            try {
                Security.addProvider(new BouncyCastleProvider());
                Field field = Class.forName("javax.crypto.JceSecurity").getDeclaredField("isRestricted");
                field.setAccessible(true);
                field.set(null, java.lang.Boolean.FALSE);
            } catch (Exception e) {

            }
        }
        CertUtils cert_utils = new CertUtils();
        // Obtencion del gestor de claves
        keyStore = cert_utils.getKeyStore(path_certificado, password_key_store.toCharArray());

        if (keyStore == null) {
            System.err.println("No se pudo obtener el almacen de certificado.");
            return false;
        } else {

            alias = cert_utils.getAlias(keyStore);

            if (alias == null) {
                System.err.println("No se pudo obtener el alias de certificado.");
                return false;
            } else {
                // Obtencion del certificado para firmar. Utilizaremos el primer
                // certificado del almacen.           
                certificate = null;
                try {
                    certificate = (X509Certificate) keyStore.getCertificate(alias);
                } catch (KeyStoreException ex) {
                    Logger.getLogger(FirmaXades.class.getName()).log(Level.SEVERE, null, ex);
                }
                if (certificate == null) {
                    System.err.println("No existe ningún certificado para firmar.");
                    return false;
                } else {

                    // Obtención de la clave privada asociada al certificado
                    privateKey = null;
                    KeyStore tmpKs = keyStore;
                    try {
                        privateKey = (PrivateKey) tmpKs.getKey(alias, password_certificado.toCharArray());
                    } catch (UnrecoverableKeyException e) {
                        System.err.println("No existe clave privada para firmar.");
                        e.printStackTrace();
                    } catch (KeyStoreException e) {
                        System.err.println("No existe clave privada para firmar.");
                        e.printStackTrace();
                    } catch (NoSuchAlgorithmException e) {
                        System.err.println("No existe clave privada para firmar.");
                        e.printStackTrace();
                    }
                    if (privateKey != null) {
                        boolean firmado = false;

                        // Obtención del provider encargado de las labores criptográficas
                        provider = keyStore.getProvider();
                        if (provider == null) {
                            return false;
                        } else {
                            return true;
                        }

                    } else {
                        return false;
                    }
                }
            }
        }
    }

    public boolean accederCertificadoRapido() {
        if (Security.getProvider(BouncyCastleProvider.PROVIDER_NAME) == null) {
            try {
                Security.addProvider(new BouncyCastleProvider());
                Field field = Class.forName("javax.crypto.JceSecurity").getDeclaredField("isRestricted");
                field.setAccessible(true);
                field.set(null, java.lang.Boolean.FALSE);
            } catch (Exception e) {

            }
        }
        CertUtils cert_utils = new CertUtils();
        // Obtencion del gestor de claves
        keyStore = cert_utils.getKeyStore(path_certificado, password_key_store.toCharArray());

        if (keyStore == null) {
            System.err.println("No se pudo obtener el almacen de certificado.");
            return false;
        } else {
            
            alias = cert_utils.getAlias(keyStore);

            if (alias == null) {
                System.err.println("No se pudo obtener el alias de certificado.");
                return false;
            } else {
                // Obtencion del certificado para firmar. Utilizaremos el primer
                // certificado del almacen.           
                certificate = null;
                try {
                    certificate = (X509Certificate) keyStore.getCertificate(alias);
                } catch (KeyStoreException ex) {
                    Logger.getLogger(FirmaXades.class.getName()).log(Level.SEVERE, null, ex);
                }
                if (certificate == null) {
                    System.err.println("No existe ningún certificado para firmar.");
                    return false;
                } else {
                    return true;
                }
            }
        }
    }

    public boolean obtenerInformacionDeFirma() {

        // Obtención de la clave privada asociada al certificado
        privateKey = null;
        KeyStore tmpKs = keyStore;
        try {
            privateKey = (PrivateKey) tmpKs.getKey(alias, password_certificado.toCharArray());
        } catch (UnrecoverableKeyException e) {
            System.err.println("No existe clave privada para firmar.");
            e.printStackTrace();
        } catch (KeyStoreException e) {
            System.err.println("No existe clave privada para firmar.");
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            System.err.println("No existe clave privada para firmar.");
            e.printStackTrace();
        }
        if (privateKey != null) {

            // Obtención del provider encargado de las labores criptográficas
            provider = keyStore.getProvider();
        }
        return true;
    }

    public KeyStore getKeyStore() {
        return keyStore;
    }

    public Provider getProvider() {
        return provider;
    }

    public X509Certificate getCertificate() {
        return certificate;
    }

    public PrivateKey getPrivateKey() {
        return privateKey;
    }

}
