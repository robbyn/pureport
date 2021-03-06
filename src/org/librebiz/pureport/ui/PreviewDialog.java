package org.librebiz.pureport.ui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.geom.Rectangle2D;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import org.librebiz.pureport.definition.PageLayout;
import org.librebiz.pureport.quantity.Unit;
import org.librebiz.pureport.run.Formatter;
import org.librebiz.pureport.run.Page;
import org.librebiz.pureport.run.PageStore;


public class PreviewDialog extends JDialog {
    private final Formatter formatter;
    private final PageStore pageStore;
    private final PrinterJob job;

    public PreviewDialog(JFrame parent, Formatter formatter,
            PageStore pageStore) {
        super(parent, true);
        initComponents();
        this.formatter = formatter;
        this.pageStore = pageStore;
        pagePreview.setPageLayout(formatter.getPageLayout());
        job = PrinterJob.getPrinterJob();
        formatter.format(job.defaultPage());
        fillPageCombo();
        Rectangle rect;
        if (parent != null) {
            rect = parent.getBounds();
        } else {
            Toolkit tk = Toolkit.getDefaultToolkit();
            Dimension dim = tk.getScreenSize();
            rect = new Rectangle(0,0,dim.width,dim.height);
        }
        setLocation(rect.x+(rect.width-getWidth())/2,
                rect.y+(rect.height-getHeight())/2);
        getRootPane().setDefaultButton(print);
    }

    private void fillPageCombo() {
        pageCombo.removeAllItems();
        for (int i = 1; i <= pageStore.getPageCount(); ++i) {
            pageCombo.addItem(i);
        }
        if (pageCombo.getItemCount() > 0) {
            pageCombo.setSelectedIndex(0);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        pagePreview = new org.librebiz.pureport.components.PagePreview();
        topPanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        pageCombo = new javax.swing.JComboBox();
        print = new javax.swing.JButton();
        setup = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/librebiz/pureport/ui/preview"); // NOI18N
        setTitle(bundle.getString("preview")); // NOI18N

        pagePreview.setMinimumSize(new java.awt.Dimension(100, 150));
        pagePreview.setPreferredSize(new java.awt.Dimension(300, 400));
        getContentPane().add(pagePreview, java.awt.BorderLayout.CENTER);

        topPanel.setLayout(new java.awt.GridBagLayout());

        jLabel1.setText(bundle.getString("page")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 12, 5, 5);
        topPanel.add(jLabel1, gridBagConstraints);

        pageCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pageComboActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 5, 5);
        topPanel.add(pageCombo, gridBagConstraints);

        print.setMnemonic('p');
        print.setText(bundle.getString("print")); // NOI18N
        print.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                printActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 5, 5);
        topPanel.add(print, gridBagConstraints);

        setup.setMnemonic('s');
        setup.setText(bundle.getString("page-setup")); // NOI18N
        setup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                setupActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 5, 11);
        topPanel.add(setup, gridBagConstraints);

        getContentPane().add(topPanel, java.awt.BorderLayout.NORTH);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void setupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_setupActionPerformed
        PageFormat pf = job.pageDialog(formatter.getPageFormat());
        if (pf != null) {
            formatter.format(pf);
            fillPageCombo();
        }
    }//GEN-LAST:event_setupActionPerformed

    private void printActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_printActionPerformed
        try {
            job.setPrintable(new Printable() {
                @Override
                public int print(Graphics g, PageFormat format,
                        int pageIndex) throws PrinterException {
                    if (pageIndex >= pageStore.getPageCount()) {
                        return Printable.NO_SUCH_PAGE;
                    }
                    Page page = pageStore.getPage(pageIndex);
                    page.resolveForwards(formatter.getContext());
                    PageLayout layout = formatter.getPageLayout();
                    Rectangle2D rc = format == null
                            ? layout.getDrawableArea(Unit.PT)
                            : layout.getDrawableArea(format);
                    page.draw((Graphics2D)g, rc.getX(), rc.getY(),
                            rc.getHeight());
                    return Printable.PAGE_EXISTS;
                }
            }, formatter.getPageFormat());
            if (job.printDialog()) {
                job.print();
            }
        } catch (PrinterException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(),
                    "Erreur d'impression", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_printActionPerformed

    private void pageComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pageComboActionPerformed
        Integer pageNumber = (Integer)pageCombo.getSelectedItem();
        if (pageNumber != null && formatter != null) {
            int index = pageNumber-1;
            Page page = pageStore.getPage(index);
            page.resolveForwards(formatter.getContext());
            pagePreview.setPage(page);
            pagePreview.setPageFormat(formatter.getPageFormat());
        }
    }//GEN-LAST:event_pageComboActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JComboBox pageCombo;
    private org.librebiz.pureport.components.PagePreview pagePreview;
    private javax.swing.JButton print;
    private javax.swing.JButton setup;
    private javax.swing.JPanel topPanel;
    // End of variables declaration//GEN-END:variables
}
