package pelunasan2;

import penyewaanitem.*;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import database.Koneksi;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import pelunasan1.Pelunasan1;
import penyewaan.Penyewaan;

/**
 *
 * @author murabbiprogrammer
 */
public class Pelunasan2DAOImp implements Pelunasan2DAO {

    private Dao<Penyewaanitem, Integer> daoPenyewaanitems;
    private Dao<Pelunasan1, Integer> daoPelunasan1;
    private Dao<Pelunasan2, Integer> daoPelunasan2;
    private Dao<Penyewaan, Integer> daoPenyewaan;

    List<Penyewaanitem> penyewaanitem = new ArrayList<>();

    public Pelunasan2DAOImp() {
        try {
            daoPelunasan1 = DaoManager.createDao(Koneksi.cs(), Pelunasan1.class);
            daoPelunasan2 = DaoManager.createDao(Koneksi.cs(), Pelunasan2.class);
            daoPenyewaanitems = DaoManager.createDao(Koneksi.cs(), Penyewaanitem.class);
            daoPenyewaan = DaoManager.createDao(Koneksi.cs(), Penyewaan.class);
        } catch (SQLException ex) {
            Logger.getLogger(Pelunasan2DAOImp.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void insertPelunasan(Pelunasan1 pelunasan1) {
        try {
            Pelunasan2 pelunasan2 = new Pelunasan2();
            daoPelunasan1.create(pelunasan1);
            for (Penyewaanitem p : penyewaanitem) {
                pelunasan2.setQty(p.getQty());
                pelunasan2.setPelunasan1(pelunasan1);
                pelunasan2.setAlatLap(p.getAlatLap());
                //set penyewa
                pelunasan2.setPelunasan1(pelunasan1);
                daoPelunasan2.create(pelunasan2);
            }
            JOptionPane.showMessageDialog(null, "Transaksi pelunasan telah tersimpan");
        } catch (SQLException ex) {
            Logger.getLogger(Pelunasan2DAOImp.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public List<Penyewaanitem> loadPenyewaanItem(int id) {
        try {
            penyewaanitem = daoPenyewaanitems.queryBuilder().where().eq("penyewaan_id", id).query();
        } catch (Exception e) {
            System.out.println("Kesalahan load " + e);
        }
        return penyewaanitem;
    }

    @Override
    public List<Penyewaan> loadPenyewaan() {
        List<Penyewaan> penyewaanss = null;
        try {
            penyewaanss = daoPenyewaan.queryBuilder().where().gt("sisabayar", 0.0).query();
        } catch (Exception e) {
            System.out.println("Kesalahan load " + e);
        }
        return penyewaanss;
    }

    public DefaultTableModel viewItem(int id) {
        DefaultTableModel dtm;
        String[] header = {"ID", "Nama", "Harga perjam", "Jumlah Jam ", "Qty ", "subtotal"};
        dtm = new DefaultTableModel(null, header);
        try {
            penyewaanitem = daoPenyewaanitems.queryBuilder().where().eq("penyewaan_id", id).query();
            for (Penyewaanitem p : penyewaanitem) {
                Object[] o = new Object[6];
                o[0] = p.getAlatLap().getId();
                o[1] = p.getAlatLap().getNama();
                o[2] = p.getAlatLap().getHargaperjam();
                o[3] = p.getJumlahjam();
                o[4] = p.getQty();
                o[5] = p.getSubtotal();
                dtm.addRow(o);
            }
        } catch (Exception e) {
            System.out.println("gagal menampilkan item : " + e);
        }
        return dtm;
    }
}
