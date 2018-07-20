/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pelunasan2;

import penyewaanitem.*;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import pelunasan1.Pelunasan1;
import penyewaan.Penyewaan;

/**
 *
 * @author murabbiprogrammer
 */
public interface Pelunasan2DAO {

    public List<Penyewaanitem> loadPenyewaanItem(int id);

    public List<Penyewaan> loadPenyewaan();

    public DefaultTableModel viewItem(int id);

    public void insertPelunasan(Pelunasan1 pelunasan1);

}
