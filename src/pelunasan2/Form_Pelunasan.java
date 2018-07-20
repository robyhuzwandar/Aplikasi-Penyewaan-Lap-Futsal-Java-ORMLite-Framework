package pelunasan2;

import alatlap.AlatLap;
import penyewaanitem.*;
import auth.Auth;
import com.j256.ormlite.dao.Dao;
import database.KoneksiReport;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import kasir.Kasir;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;
import pelunasan1.Pelunasan1;
import pelunasan1.Pelunasan1DAO;
import pelunasan1.Pelunasan1DAOImp;
import penyewaan.Penyewaan;
import penyewaan.PenyewaanDAOImp;

/**
 *
 * @author murabbiprogrammer
 */
public class Form_Pelunasan extends javax.swing.JPanel {
//List Array

    int ID;
    Pelunasan1DAO dAO1 = new Pelunasan1DAOImp();
    Pelunasan2DAO dAO2 = new Pelunasan2DAOImp();
//    PenyewaanDAO daop = new PenyewaanDAOImp();
    private Dao<Penyewaanitem, Integer> daoPenyewaanitems;
    List<Integer> penyewaanid = new ArrayList<>();
    List<Integer> alatlapid = new ArrayList<>();

//inisialisasi 
    Penyewaan penyewaan = new Penyewaan();
    Penyewaanitem penyewaanitem = new Penyewaanitem();
    Pelunasan1 pelunasan1 = new Pelunasan1();
    Pelunasan2 pelunasan2;
//tanggal 
    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    Date date = new Date();
    AlatLap alatLap;

    public Form_Pelunasan() {
        initComponents();
        tftglBayar.setText(dateFormat.format(date));
        tfKasir.setText(Auth.NAMA);
        viewPelunasan();
        viewpenyewaan();
        loadPenyewaan();
    }

    public void viewpenyewaan() {
        tblPenyewaan.setModel(dAO1.penyewaan());
    }

    public void viewPelunasan() {

        if (tblPenyewaan.getSelectedRowCount() != 0) {
            int selected = tblPenyewaan.getSelectedRow();
            ID = Integer.valueOf(tblPenyewaan.getValueAt(selected, 0).toString());
            tblPelunasan.setModel(dAO1.selectAll(ID));
        } else {
            ID = 0;
            tblPelunasan.setModel(dAO1.selectAll(ID));
        }
    }

    public void loadPenyewaan() {
        cbPenyewaan.removeAllItems();
        cbPenyewaan.addItem("-Pilih-");
        penyewaanid.add(0);
        for (Penyewaan p : dAO2.loadPenyewaan()) {
            cbPenyewaan.addItem(p.getPenyewa().getTeam());
            penyewaanid.add(p.getId());
        }
    }

    public void save() {
        pelunasan1.setNorental(tfnobayar.getText());
        pelunasan1.setUangpelunasan(Double.valueOf(tfUangPelunasan.getText()));
        pelunasan1.setUangkembali(Double.valueOf(tfuangkembali.getText()));
        pelunasan1.setTglbayar(dateFormat.format(date));

        //set kasir
        Kasir kasir = new Kasir();
        kasir.setId(Auth.ID);
        pelunasan1.setKasir(kasir);
        //penyewa
        penyewaan.setId(penyewaanid.get(cbPenyewaan.getSelectedIndex()));
        //setpenyewa
        pelunasan1.setPenyewaan(penyewaan);
        //simpan penyewaan

        dAO2.insertPelunasan(pelunasan1);
//        dAO1.insertp2(pelunasan2);

    }

    public void reset() {
        //reset item
        tfSisaBayar.setText("");
        tfSisaBayarAKhir.setText("");
        tfTotalBayar.setText("");
        tfUangPelunasan.setText("");
        tfnobayar.setText("");
        tfuangkembali.setText(" ");
        viewPelunasan();
    }

    public void getData() {
        if (cbPenyewaan.getSelectedIndex() > 0) {
            penyewaan = dAO2.loadPenyewaan().get(cbPenyewaan.getSelectedIndex() - 1);
            int row = tblPelunasan.getRowCount();
            int row2 = tblPenyewaan.getRowCount();
            tfnobayar.setText("BR" + row + row2 + 1);
            double tb = penyewaan.getTotalbayar();
            double sb = penyewaan.getSisabayar();
            String TB = Double.toString(tb);
            String SB = Double.toString(sb);
            tfTotalBayar.setText(TB);
            tfSisaBayar.setText(SB);
            tblItem.setModel(dAO2.viewItem(penyewaan.getId()));
            dAO2.viewItem(penyewaan.getId());
            dAO2.loadPenyewaanItem(penyewaan.getId());

        }
    }

    public void hitung() {
        double tb = penyewaan.getTotalbayar();
        double sb = penyewaan.getSisabayar();
        double up = Double.valueOf(tfUangPelunasan.getText());
        if (up >= sb) {
            double uk = up - sb;

            tfSisaBayarAKhir.setText("0");
            tfuangkembali.setText(String.valueOf(uk));
        } else {
            JOptionPane.showMessageDialog(null, "Uang Pelunasan Kurang");
        }
    }

    public void getReport() {
        JasperReport report;
        JasperPrint print;
        JasperDesign design;
        HashMap parameters = new HashMap();

        int row = tblPenyewaan.getSelectedRow();
        parameters.put("penyewaan_id", tblPenyewaan.getValueAt(row, 0).toString());
        File sumber = new File("src/report/pelunasan.jrxml");
        try {
            design = JRXmlLoader.load(sumber);
            report = JasperCompileManager.compileReport(design);
            print = JasperFillManager.fillReport(report, parameters, new KoneksiReport().getKon());
            JasperViewer.viewReport(print, false);
        } catch (JRException ex) {
            Logger.getLogger(PenyewaanDAOImp.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        tfKasir = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        tfnobayar = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        tftglBayar = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblPelunasan = new javax.swing.JTable();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblItem = new javax.swing.JTable();
        cbPenyewaan = new javax.swing.JComboBox<>();
        jLabel1 = new javax.swing.JLabel();
        tfTotalBayar = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        tfSisaBayar = new javax.swing.JTextField();
        jButton5 = new javax.swing.JButton();
        tfUangPelunasan = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        tfSisaBayarAKhir = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        tfuangkembali = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jButton6 = new javax.swing.JButton();
        btnSimpan = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblPenyewaan = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();

        jPanel2.setBackground(new java.awt.Color(20, 143, 166));

        jPanel3.setBackground(new java.awt.Color(1, 59, 70));

        jLabel3.setBackground(new java.awt.Color(14, 39, 77));
        jLabel3.setFont(new java.awt.Font("DejaVu Sans", 1, 14)); // NOI18N
        jLabel3.setForeground(javax.swing.UIManager.getDefaults().getColor("Button.highlight"));
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("Pembayaran Uang Pelunasan");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        jPanel1.setBackground(new java.awt.Color(111, 146, 176));
        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel1.setForeground(new java.awt.Color(255, 245, 245));
        jPanel1.setOpaque(false);

        tfKasir.setEditable(false);

        jLabel11.setForeground(new java.awt.Color(254, 242, 242));
        jLabel11.setText("Kasir :");

        tfnobayar.setEditable(false);
        tfnobayar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                tfnobayarKeyTyped(evt);
            }
        });

        jLabel7.setForeground(new java.awt.Color(254, 254, 254));
        jLabel7.setText("No. Rental :");

        jLabel8.setForeground(new java.awt.Color(254, 254, 254));
        jLabel8.setText("Tgl Bayar :");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel11, javax.swing.GroupLayout.Alignment.TRAILING)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel8)))
                .addGap(11, 11, 11)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tfnobayar, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tftglBayar, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tfKasir, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(542, Short.MAX_VALUE))
        );

        jPanel1Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {tfKasir, tfnobayar, tftglBayar});

        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(tfKasir, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tfnobayar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tftglBayar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8))
                .addContainerGap())
        );

        tblPelunasan.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(tblPelunasan);

        jPanel5.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel5.setOpaque(false);

        tblItem.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tblItem.setOpaque(false);
        jScrollPane2.setViewportView(tblItem);

        cbPenyewaan.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cbPenyewaan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbPenyewaanActionPerformed(evt);
            }
        });

        jLabel1.setForeground(new java.awt.Color(254, 254, 254));
        jLabel1.setText("Total Bayar :");

        tfTotalBayar.setEditable(false);

        jLabel4.setForeground(new java.awt.Color(254, 254, 254));
        jLabel4.setText("Sisa Bayar Awal :");

        tfSisaBayar.setEditable(false);

        jButton5.setText("Hitung");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        tfUangPelunasan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                tfUangPelunasanKeyTyped(evt);
            }
        });

        jLabel5.setForeground(new java.awt.Color(254, 254, 254));
        jLabel5.setText("Uang Pelunasan :");

        tfSisaBayarAKhir.setEditable(false);

        jLabel12.setForeground(new java.awt.Color(254, 254, 254));
        jLabel12.setText("Sisa Bayar Akhir :");

        tfuangkembali.setEditable(false);

        jLabel13.setForeground(javax.swing.UIManager.getDefaults().getColor("Button.highlight"));
        jLabel13.setText("Uang Kembali :");

        jButton6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/baruicon/bagiandalam/Synchronize_20px.png"))); // NOI18N
        jButton6.setText("Reset");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        btnSimpan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/baruicon/bagiandalam/Save_20px.png"))); // NOI18N
        btnSimpan.setText("Simpan");
        btnSimpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSimpanActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(cbPenyewaan, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel1)
                                    .addComponent(tfTotalBayar, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel4)
                                    .addComponent(tfSisaBayar, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGap(2, 2, 2)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel5)
                                    .addGroup(jPanel5Layout.createSequentialGroup()
                                        .addComponent(tfUangPelunasan, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel5Layout.createSequentialGroup()
                                    .addComponent(btnSimpan, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(10, 10, 10)
                                    .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel5Layout.createSequentialGroup()
                                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jLabel12)
                                        .addComponent(tfSisaBayarAKhir, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel13)
                                        .addComponent(tfuangkembali, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(cbPenyewaan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tfTotalBayar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tfSisaBayar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tfUangPelunasan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel13)
                    .addComponent(jLabel12))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tfuangkembali, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tfSisaBayarAKhir, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton6)
                    .addComponent(btnSimpan))
                .addGap(65, 65, 65))
        );

        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/baruicon/bagiandalam/Restart_20px.png"))); // NOI18N
        jButton4.setText("Refres");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/baruicon/bagiandalam/Print_20px.png"))); // NOI18N
        jButton7.setText("Cetak");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        tblPenyewaan.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tblPenyewaan.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblPenyewaanMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(tblPenyewaan);

        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Pembayaran");

        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Data Penyewaan ");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 496, Short.MAX_VALUE)
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 496, Short.MAX_VALUE)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jLabel2)
                                    .addComponent(jLabel6))
                                .addGap(0, 0, Short.MAX_VALUE)))))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton4)
                            .addComponent(jButton7))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(5, 5, 5)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                    .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, 349, Short.MAX_VALUE))
                .addGap(13, 13, 13))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void tfnobayarKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tfnobayarKeyTyped

    }//GEN-LAST:event_tfnobayarKeyTyped

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        loadPenyewaan();
        viewPelunasan();
        viewpenyewaan();

    }//GEN-LAST:event_jButton4ActionPerformed

    private void cbPenyewaanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbPenyewaanActionPerformed
        getData();
    }//GEN-LAST:event_cbPenyewaanActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        if (tfSisaBayar.getText().isEmpty() && tfUangPelunasan.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Lengkapi kolom sisa bayar dan uang pelunasan");
        } else {
            hitung();
        }
    }//GEN-LAST:event_jButton5ActionPerformed

    private void tfUangPelunasanKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tfUangPelunasanKeyTyped

    }//GEN-LAST:event_tfUangPelunasanKeyTyped

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed

    }//GEN-LAST:event_jButton6ActionPerformed

    private void btnSimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSimpanActionPerformed
        if (tfKasir.getText().isEmpty() && tfSisaBayar.getText().isEmpty() && tfSisaBayarAKhir.getText().isEmpty()
                && tfTotalBayar.getText().isEmpty() && tfUangPelunasan.getText().isEmpty() && tfnobayar.getText().isEmpty()
                && tftglBayar.getText().isEmpty() && tfuangkembali.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Semua Kolom Tidak Boleh kosong");
        } else {
            save();
            viewPelunasan();
            viewpenyewaan();
        }
    }//GEN-LAST:event_btnSimpanActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        getReport();
    }//GEN-LAST:event_jButton7ActionPerformed

    private void tblPenyewaanMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblPenyewaanMouseClicked
        viewPelunasan();
    }//GEN-LAST:event_tblPenyewaanMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnSimpan;
    private javax.swing.JComboBox<String> cbPenyewaan;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable tblItem;
    private javax.swing.JTable tblPelunasan;
    private javax.swing.JTable tblPenyewaan;
    private javax.swing.JTextField tfKasir;
    private javax.swing.JTextField tfSisaBayar;
    private javax.swing.JTextField tfSisaBayarAKhir;
    private javax.swing.JTextField tfTotalBayar;
    private javax.swing.JTextField tfUangPelunasan;
    public static javax.swing.JTextField tfnobayar;
    private javax.swing.JTextField tftglBayar;
    private javax.swing.JTextField tfuangkembali;
    // End of variables declaration//GEN-END:variables
}
