package net.barrage.school.java.ecatalog.app.product_sources;

import lombok.RequiredArgsConstructor;
import net.barrage.school.java.ecatalog.config.ProductSourceProperties;
import net.barrage.school.java.ecatalog.model.Product;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@RequiredArgsConstructor
public class ExcelProductSource implements ProductSource {
    private static final Logger log = LoggerFactory.getLogger(ExcelProductSource.class);
    private static final int NAME_INDEX = 0;
    private static final int PHOTO_INDEX = 1;
    private static final int PRICE_INDEX = 4;
    private final ProductSourceProperties.SourceProperty properties;

    @Override
    public List<Product> getProducts() {

        List<Product> result = new ArrayList<>();
        try (InputStream fileInputStream = URI.create(properties.getUrl()).toURL().openStream()) {
            Workbook workbook = new XSSFWorkbook(fileInputStream);
            Sheet sheet = workbook.getSheetAt(0);

            Iterator<Row> rowIterator = sheet.rowIterator();

            if (rowIterator.hasNext()) {
                rowIterator.next();
            }

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                Iterator<Cell> cellIterator = row.cellIterator();
                Product product = new Product();
                product.setId(UUID.randomUUID());
                while (cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();
                    int columnIndex = cell.getColumnIndex();
                    switch (columnIndex) {
                        case NAME_INDEX:
                            product.setName(cell.getStringCellValue());
                            break;
                        case PHOTO_INDEX:
                            product.setImageUrl(cell.getStringCellValue());
                            break;
                        case PRICE_INDEX:
                            product.setPrice(Double.parseDouble(cell.getStringCellValue()));
                            break;
                    }
                }
                result.add(product);
            }
        } catch (Exception e) {
            log.warn("Oops!", e);
            throw new RuntimeException(e);
        }

        return result;
    }

    @Override
    public String getMerchantName() {
        return properties.getName();
    }

    @Override
    public boolean isRemote() {
        return properties.isRemote();
    }

    @RequiredArgsConstructor
    @Component
    public static class Factory implements ProductSource.Factory {

        @Override
        public Set<String> getSupportedFormats() {
            return Set.of("xlsx", "xls");
        }

        @Override
        public ProductSource create(ProductSourceProperties.SourceProperty property) {
            return new ExcelProductSource(property);
        }
    }

}
