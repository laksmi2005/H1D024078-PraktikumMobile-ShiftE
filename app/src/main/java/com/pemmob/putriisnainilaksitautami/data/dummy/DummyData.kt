package com.pemmob.putriisnainilaksitautami.data.dummy

import com.pemmob.putriisnainilaksitautami.data.model.Category
import com.pemmob.putriisnainilaksitautami.data.model.Product

object DummyData {
    val categories = listOf(
        Category(id = 1, name = "Makanan", description = "Aneka Makanan Lokal", products_count = 5),
        Category(id = 2, name = "Minuman", description = "Minuman Segar", products_count = 5),
        Category(id = 3, name = "Kerajinan", description = "Kerajinan Tangan", products_count = 5)
    )

    val products = listOf(
        Product(1, 1, categories[0], "Kripik Singkong", "Kripik gurih", 15000.0, 50, "dummy_product"),
        Product(2, 1, categories[0], "Mendoan", "Mendoan hangat", 20000.0, 30, "dummy_product"),
        Product(3, 1, categories[0], "Sale Pisang", "Sale pisang manis", 25000.0, 20, "dummy_product"),
        Product(4, 1, categories[0], "Getuk Goreng", "Getuk khas", 30000.0, 40, "dummy_product"),
        Product(5, 1, categories[0], "Nopia", "Nopia lezat", 22000.0, 15, "dummy_product"),
        Product(6, 2, categories[1], "Es Dawet", "Dawet segar", 10000.0, 100, "dummy_product"),
        Product(7, 2, categories[1], "Kopi Rempah", "Kopi hangat", 18000.0, 25, "dummy_product"),
        Product(8, 3, categories[2], "Sapu Glagah", "Sapu awet", 35000.0, 10, "dummy_product")
    )
}
