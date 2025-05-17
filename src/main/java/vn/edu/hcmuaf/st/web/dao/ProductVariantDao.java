package vn.edu.hcmuaf.st.web.dao;

import org.jdbi.v3.core.Jdbi;
import vn.edu.hcmuaf.st.web.dao.db.JDBIConnect;
import vn.edu.hcmuaf.st.web.entity.Color;
import vn.edu.hcmuaf.st.web.entity.ProductVariant;
import vn.edu.hcmuaf.st.web.entity.Size;


import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;

public class ProductVariantDao {

    private final Jdbi jdbi;

    public ProductVariantDao() {
        this.jdbi = JDBIConnect.get();
    }

    public List<ProductVariant> getProductVariantsByIdProduct(int idProduct) {
        return jdbi.withHandle(handle ->
                handle.createQuery("""
                    SELECT
                        v.idVariant, v.idProduct, v.stockQuantity,
                        s.idSize, s.size AS sizeName,
                        clr.idColor, clr.color AS colorName, clr.hexCode
                    FROM product_variants v
                    JOIN sizes s ON v.idSize = s.idSize
                    JOIN colors clr ON v.idColor = clr.idColor
                    WHERE v.idProduct = :idProduct
                    ORDER BY v.idVariant ASC;
                    """)
                        .bind("idProduct", idProduct)
                        .reduceRows(new LinkedHashMap<Integer, ProductVariant>(), (map, row) -> {
                            int idVariant = row.getColumn("idVariant", Integer.class);

                            if (!map.containsKey(idVariant)) {
                                ProductVariant variant = new ProductVariant(
                                        idVariant,
                                        row.getColumn("idProduct", Integer.class),
                                        new Size(
                                                row.getColumn("idSize", Integer.class),
                                                row.getColumn("sizeName", String.class)
                                        ),
                                        new Color(
                                                row.getColumn("idColor", Integer.class),
                                                row.getColumn("colorName", String.class),
                                                row.getColumn("hexCode", String.class)
                                        ),
                                        row.getColumn("stockQuantity", Integer.class)
                                );
                                map.put(idVariant, variant);
                            }
                            return map;
                        })
                        .values().stream().toList()
        );
    }

    public static void main(String[] args) {
        ProductVariantDao dao = new ProductVariantDao();
        List<ProductVariant> list = dao.getProductVariantsByIdProduct(1);
        for(ProductVariant variant : list) {
            System.out.println(variant);
        }
    }

}
