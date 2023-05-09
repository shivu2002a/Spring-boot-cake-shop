package com.shiva.ecommerce.model;

import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@ConfigurationProperties("paytm.payment.sandbox")
public class PaytmDetails {

    private String merchantId;
    
    private String merchantKey;
    
    private String channelId;
    
    private String industryTypeId;
    
    private String website;
    
    private String paytmUrl;
    
    private String callbackUrl;

    private Map<String,String> details;
    
}
