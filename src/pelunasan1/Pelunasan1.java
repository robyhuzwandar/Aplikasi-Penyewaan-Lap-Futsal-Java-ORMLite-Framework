package pelunasan1;

import penyewaanitem.*;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.dao.ForeignCollection;
import kasir.Kasir;
import pelunasan1.Pelunasan1;
import pelunasan2.Pelunasan2;
import penyewaan.Penyewaan;

/**
 *
 * @author murabbiprogrammer
 */
@DatabaseTable(tableName = "pelunasan1")
public class Pelunasan1 {

    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField
    private String norental;
    @DatabaseField()
    private String tglbayar;
    @DatabaseField()
    private double uangpelunasan;
    @DatabaseField()
    private double uangkembali;
    @DatabaseField(foreign = true, foreignAutoRefresh = true)
    private Kasir kasir;
    @DatabaseField(foreign = true, foreignAutoRefresh = true)
    private Penyewaan penyewaan;

    public Kasir getKasir() {
        return kasir;
    }

    public void setKasir(Kasir kasir) {
        this.kasir = kasir;
    }

    public Penyewaan getPenyewaan() {
        return penyewaan;
    }

    public void setPenyewaan(Penyewaan penyewaan) {
        this.penyewaan = penyewaan;
    }

    public double getUangpelunasan() {
        return uangpelunasan;
    }

    public void setUangpelunasan(double uangpelunasan) {
        this.uangpelunasan = uangpelunasan;
    }

    public String getNorental() {
        return norental;
    }

    public void setNorental(String norental) {
        this.norental = norental;
    }

    public String getTglbayar() {
        return tglbayar;
    }

    public void setTglbayar(String tglbayar) {
        this.tglbayar = tglbayar;
    }

    public double getUangkembali() {
        return uangkembali;
    }

    public void setUangkembali(double uangkembali) {
        this.uangkembali = uangkembali;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
