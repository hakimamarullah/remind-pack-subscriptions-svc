package com.starline.subscriptions.dto.midtrans;
/*
@Author hakim a.k.a. Hakim Amarullah
Java Developer
Created on 6/23/2024 1:21 PM
@Last Modified 6/23/2024 1:21 PM
Version 1.0
*/

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class MidtransItemDetails {

    private String id;
    private Integer price;
    private Integer quantity;
    private String name;
    private String brand;
}
