/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pelunasan1;

import javax.swing.table.DefaultTableModel;
import pelunasan2.Pelunasan2;

/**
 *
 * @author MurabbiProgrammer
 */
public interface Pelunasan1DAO {

    public DefaultTableModel penyewaan();

    public DefaultTableModel selectAll(int id);

    public DefaultTableModel search(String key);
    

}
