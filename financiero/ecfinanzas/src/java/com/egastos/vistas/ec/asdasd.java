package com.egastos.vistas.ec;

import com.egastos.modelo.ec.ComprobanteElectronico;
import java.util.ArrayList;
import java.util.List;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

public class asdasd implements JRDataSource
{
    private List<ComprobanteElectronico> listaParticipantes = new ArrayList<ComprobanteElectronico>();
    private int indiceParticipanteActual = -1;

    public Object getFieldValue(JRField jrf) throws JRException
    {
        Object valor = null;

        if ("razonSocialEmisorComprobanteElectronico".equals(jrf.getName()))
        {
            valor = listaParticipantes.get(indiceParticipanteActual).getRazonSocialEmisorComprobanteElectronico();
        }
        
        return valor;
    }

    public boolean next() throws JRException
    {
        return ++indiceParticipanteActual < listaParticipantes.size();
    }

    public void addParticipante(ComprobanteElectronico participante)
    {
        this.listaParticipantes.add(participante);
    }
}
