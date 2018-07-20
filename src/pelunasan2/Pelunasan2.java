/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pelunasan2;

import alatlap.AlatLap;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import pelunasan1.Pelunasan1;
import penyewaan.Penyewaan;
import penyewaanitem.Penyewaanitem;

/**
 *
 * @author MurabbiProgrammer
 */
@DatabaseTable(tableName = "pelunasan2")
public class Pelunasan2 {

    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField(columnName = "qty")
    private int Qty;

    @DatabaseField(foreign = true, foreignAutoRefresh = true)
    private AlatLap alatLap;
    private Pelunasan1 pelunasan1;

    public Pelunasan1 getPelunasan1() {
        return pelunasan1;
    }

    public void setPelunasan1(Pelunasan1 pelunasan1) {
        this.pelunasan1 = pelunasan1;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getQty() {
        return Qty;
    }

    public void setQty(int Qty) {
        this.Qty = Qty;
    }

    public AlatLap getAlatLap() {
        return alatLap;
    }

    public void setAlatLap(AlatLap alatLap) {
        this.alatLap = alatLap;
    }
}
