import java.awt.Color;
import java.awt.font.TextAttribute;

import org.librebiz.pureport.context.TextBuilder;

public class Test2 extends javax.swing.JFrame {

    /** Creates new form Test2 */
    public Test2() {
        initComponents();
        TextBuilder builder = new TextBuilder();
        builder.setAttribute(
                TextAttribute.JUSTIFICATION, TextAttribute.JUSTIFICATION_FULL);
        builder.setAttribute(TextAttribute.FAMILY, "Verdana");
        builder.setAttribute(TextAttribute.SIZE, new Float(24));
        for (int i = 0; i < 20; ++i) {
            builder.append("Verdana ");
        }
        builder.setAttribute(TextAttribute.POSTURE, TextAttribute.POSTURE_OBLIQUE);
        for (int i = 0; i < 20; ++i) {
            builder.append("Oblique ");
        }
        builder.setAttribute(TextAttribute.POSTURE, null);
        builder.setAttribute(TextAttribute.WEIGHT, TextAttribute.WEIGHT_BOLD);
        for (int i = 0; i < 20; ++i) {
            builder.append("Bold ");
        }
        builder.setAttribute(TextAttribute.POSTURE, TextAttribute.POSTURE_OBLIQUE);
        for (int i = 0; i < 20; ++i) {
            builder.append("Bold&Oblique ");
        }
        builder.setAttribute(TextAttribute.BACKGROUND, Color.RED);
        builder.setAttribute(TextAttribute.FOREGROUND, Color.WHITE);
        for (int i = 0; i < 20; ++i) {
            builder.append("White-on-red ");
        }
        textView.setTextBuilder(builder);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        textView = new org.librebiz.pureport.components.TextBuilderView();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().add(textView, java.awt.BorderLayout.CENTER);

        pack();
    }
    // </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Test2().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private org.librebiz.pureport.components.TextBuilderView textView;
    // End of variables declaration//GEN-END:variables
    
}
