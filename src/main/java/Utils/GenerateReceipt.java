    package Utils;

    import Models.CartItem;
    import javafx.stage.FileChooser;
    import javafx.stage.Stage;
    import net.sf.jasperreports.engine.*;
    import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
    import net.sf.jasperreports.engine.design.JasperDesign;
    import net.sf.jasperreports.engine.xml.JRXmlLoader;

    import java.io.File;
    import java.io.InputStream;
    import java.nio.file.Path;
    import java.nio.file.Paths;
    import java.util.ArrayList;
    import java.util.HashMap;
    import java.util.List;
    import java.util.Map;

    public class GenerateReceipt {
        public void generateReceipt(List<CartItem> cartItems) {
            try {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Pilih Folder");
                fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
                fileChooser.getExtensionFilters().addAll(
                        new FileChooser.ExtensionFilter("PDF Files", "*.pdf")
                );

                String outputPath = "D:/output/receipt.pdf";
                File selectedDirectory = fileChooser.showSaveDialog(new Stage());
                if (selectedDirectory != null) {
                    outputPath = selectedDirectory.getAbsolutePath();
                }

                String templatePath = "/Views/Canteen/receipt.jrxml";
                InputStream templateStream = getClass().getResourceAsStream(templatePath);

                JasperDesign jasperDesign = JRXmlLoader.load(templateStream);
                JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);

                List<Map<String, Object>> data = new ArrayList<>();
                for (CartItem cartItem : cartItems) {
                    Map<String, Object> itemData = new HashMap<>();
                    itemData.put("productName", cartItem.getProduct().getName());
                    itemData.put("qty", cartItem.getQty());
                    itemData.put("price", cartItem.getProduct().getPrice());
                    itemData.put("total", cartItem.getQty() * cartItem.getProduct().getPrice());
                    data.add(itemData);
                }
                JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(data);

                Map<String, Object> parameters = new HashMap<>();

                JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

//                String outputFilePath = "D:/output/receipt.pdf";
//                Path outputFile = Paths.get(outputFilePath);
                JasperExportManager.exportReportToPdfFile(jasperPrint, outputPath);

            } catch (JRException e) {
                e.printStackTrace();
            }
        }
    }
