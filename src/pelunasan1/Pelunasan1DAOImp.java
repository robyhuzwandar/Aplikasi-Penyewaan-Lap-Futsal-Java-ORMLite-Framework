/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pelunasan1;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import database.Koneksi;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;
import pelunasan2.Pelunasan2;
import pelunasan2.Pelunasan2DAOImp;
import penyewaan.Penyewaan;
import penyewaan.PenyewaanDAOImp;
import penyewaanitem.Penyewaanitem;

/**
 *
 * @author MurabbiProgrammer
 */
public class Pelunasan1DAOImp implements Pelunasan1DAO {

    private Dao<Pelunasan1, Integer> daoPelunasan;
    private Dao<Pelunasan2, Integer> daoPelunasan2;
    private Dao<Penyewaan, Integer> daoPenyewaan;

    List<Penyewaanitem> penyewaanitems = new ArrayList<>();

    public Pelunasan1DAOImp() {
        try {
            daoPelunasan = DaoManager.createDao(Koneksi.cs(), Pelunasan1.class);
            daoPenyewaan = DaoManager.createDao(Koneksi.cs(), Penyewaan.class);
        } catch (SQLException ex) {
            Logger.getLogger(Pelunasan2DAOImp.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public DefaultTableModel selectAll(int id) {
        DefaultTableModel dtm;
        String[] title = {"ID", "No Bayar", "No Boking", "Team", "TotalBayar", "Uang bayar", "Uang kembali"};
        dtm = new DefaultTableModel(null, title);
        try {
            List<Pelunasan1> pelunasans = daoPelunasan.queryBuilder().where().eq("penyewaan_id", id).query();
            for (Pelunasan1 p : pelunasans) {
                Object[] o = new Object[8];
                o[0] = p.getId();
                o[1] = p.getNorental();
                o[2] = p.getPenyewaan().getNoboking();
                o[3] = p.getPenyewaan().getPenyewa().getTeam();
                o[4] = p.getPenyewaan().getTotalbayar();
                o[5] = p.getUangpelunasan();
                o[6] = p.getUangkembali();

                dtm.addRow(o);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Pelunasan2DAOImp.class.getName()).log(Level.SEVERE, null, ex);
        }
        //kembalikan hasil dtm
        return dtm;
    }

    @Override
    public DefaultTableModel search(String key) {
        DefaultTableModel dtm;
        String[] title = {"ID", "No Bayar", "No Boking", "Team", "TotalBayar", "Uang bayar", "Uang kembali"};
        dtm = new DefaultTableModel(null, title);
        try {
            List<Pelunasan1> pelunasans = daoPelunasan.queryBuilder().where().like("tglbayar", "%" + key + "%").query();
            for (Pelunasan1 p : pelunasans) {
                Object[] o = new Object[8];
                o[0] = p.getId();
                o[1] = p.getNorental();
                o[2] = p.getPenyewaan().getNoboking();
                o[3] = p.getPenyewaan().getPenyewa().getTeam();
                o[4] = p.getPenyewaan().getTotalbayar();
                o[5] = p.getUangpelunasan();
                o[6] = p.getUangkembali();

                dtm.addRow(o);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Pelunasan2DAOImp.class.getName()).log(Level.SEVERE, null, ex);
        }
        //kembalikan hasil dtm
        return dtm;
    }

    @Override
    public DefaultTableModel penyewaan() {
        DefaultTableModel dtm;
        String[] title = {"ID", "No Boking", "Kasir", "Team", "Status", "Total Bayar", "Uang Muka", "Sisa Bayar"};
        dtm = new DefaultTableModel(null, title);
        try {
            List<Penyewaan> penyewaan = daoPenyewaan.queryBuilder().orderBy("id", false).query();
            for (Penyewaan p : penyewaan) {
                Object[] o = new Object[8];
                o[0] = p.getId();
                o[1] = p.getNoboking();
                o[2] = p.getKasir().getNama();
                o[3] = p.getPenyewa().getTeam();
                o[4] = p.getStatus();
                o[5] = p.getTotalbayar();
                o[6] = p.getUangmuka();
                o[7] = p.getSisabayar();

                dtm.addRow(o);
            }
        } catch (SQLException ex) {
            Logger.getLogger(PenyewaanDAOImp.class.getName()).log(Level.SEVERE, null, ex);
        }
        //kembalikan hasil dtm
        return dtm;
    }

}
