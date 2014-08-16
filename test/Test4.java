import java.awt.EventQueue;
import java.awt.font.FontRenderContext;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.swing.UIManager;
import org.librebiz.pureport.context.ReportContext;
import org.librebiz.pureport.definition.Report;
import org.librebiz.pureport.reportfile.ReportReader;
import org.librebiz.pureport.run.Formatter;
import org.librebiz.pureport.run.PageStore;
import org.librebiz.pureport.run.PdfGenerator;
import org.librebiz.pureport.ui.PreviewDialog;

public class Test4 {
    private static final Random random = new Random();

    public static void main(String args[]) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                    List items = new ArrayList();
                    for (int i = 0; i < 25; ++i) {
                        items.add(new Item("Item " + i, random.nextDouble(),
                                random.nextDouble(), random.nextDouble(),
                                random.nextInt(10)));
                    }
                    Report report = ReportReader.load(Test4.class.getResource("test4.xml"));
                    ReportContext context = new ReportContext("javascript");
                    context.define("items", items);
//                    PdfGenerator.generatePdf(context, report, new File("out4.pdf"));
                    PageStore pageStore = new PageStore();
                    FontRenderContext frc = new FontRenderContext(null, true, true);
                    Formatter fmt = new Formatter(report, context, frc, pageStore);
                    long tm = System.currentTimeMillis();
                    tm = System.currentTimeMillis()-tm;
                    System.out.println("Time for formatting: " + tm + "ms");
                    PreviewDialog frame = new PreviewDialog(null, fmt, pageStore);
                    frame.setTitle("Print preview");
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static class Item {
        private String label;
        private double quantity;
        private double price;
        private double vat;
        private List<Item> subItems = new ArrayList<Item>();

        public Item(String label, double quantity, double price, double vat,
                int count) {
            this.label = label;
            this.quantity = quantity;
            this.price = price;
            this.vat = vat;
            for (int i = 0; i < count; ++i) {
                subItems.add(new Item(label + "." + i, random.nextDouble(),
                        random.nextDouble(), random.nextDouble(), 0));
            }
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public double getQuantity() {
            return quantity;
        }

        public void setQuantity(double quantity) {
            this.quantity = quantity;
        }

        public double getVat() {
            return vat;
        }

        public void setVat(double vat) {
            this.vat = vat;
        }

        public List<Item> getSubItems() {
            return subItems;
        }
    }
}
