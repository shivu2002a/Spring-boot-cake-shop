package com.shiva.ecommerce.controller;

import java.time.LocalDate;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.ModelAndView;

import com.paytm.pg.merchant.PaytmChecksum;
import com.shiva.ecommerce.model.Order;
import com.shiva.ecommerce.model.PaytmDetails;
import com.shiva.ecommerce.model.User;
import com.shiva.ecommerce.repository.UserRepository;
import com.shiva.ecommerce.service.OrderService;

@Controller
public class PaymentController {

    @Autowired
    private PaytmDetails paytmDetails;

    @Autowired
    private Environment env;

    @Autowired
    UserRepository userRepo;

    @Autowired
    OrderService orderService;

    @PostMapping("/checkout")
    public ModelAndView getCheckout(@RequestBody MultiValueMap<String, String> body) throws Exception {
        float totalPrice = Float.valueOf(body.getFirst("payable_amount"));
        int user_id = Integer.valueOf(body.getFirst("user_id"));
    
        ModelAndView mav = new ModelAndView("redirect:" + paytmDetails.getPaytmUrl());
        TreeMap<String, String> parameters = new TreeMap<>();
        paytmDetails.getDetails().forEach((k,v) -> parameters.put(k, v));
        parameters.put("MOBILE_NO", env.getProperty("paytm.mobile"));
        parameters.put("EMAIL", env.getProperty("paytm.email"));
        parameters.put("ORDER_ID", "1");
        parameters.put("TXN_AMOUNT", String.valueOf(totalPrice));
        parameters.put("CUST_ID", String.valueOf(user_id));
        String checksum = getCheckSum(parameters);
        parameters.put("CHECKSUMHASH", checksum);
        mav.addAllObjects(parameters);
        return mav;

    }

    private String getCheckSum(TreeMap<String, String> parameters) throws Exception {
        return PaytmChecksum.generateSignature(parameters, env.getProperty("paytm.payment.sandbox.merchantKey"));
    }

    @PostMapping("/pgresponse")
    public String getResponseRedirect(HttpServletRequest request, Model model) {
        Map<String, String[]> mapData = request.getParameterMap();
        TreeMap<String, String> parameters = new TreeMap<String, String>();
        mapData.forEach((key, val) -> parameters.put(key, val[0]));
        String paytmChecksum = "";
        if (mapData.containsKey("CHECKSUMHASH")) {
            paytmChecksum = mapData.get("CHECKSUMHASH")[0];
        }
        String result;

        boolean isValideChecksum = false;
        // System.out.println("RESULT : " + parameters.toString());
        try {
            isValideChecksum = validateCheckSum(parameters, env.getProperty("paytm.payment.sandbox.merchantKey"), paytmChecksum);
            if (isValideChecksum && parameters.containsKey("RESPCODE")) {
                if (parameters.get("RESPCODE").equals("01")) {
                    result = "Payment Successful";
                } else {
                    result = "Payment Failed";
                }
            } else {
                result = "Checksum mismatched";
            }
        } catch (Exception e) {
            result = e.toString();
        }
        model.addAttribute("result", result);
        parameters.remove("CHECKSUMHASH");
        model.addAttribute("parameters", parameters);
        Order order = new Order();
        User user = userRepo.findById(Integer.valueOf(parameters.get("CUST_ID"))).get();
        order.setUser(user);
        order.setOrderdate(LocalDate.now());
        order.setPrice_paid(Double.valueOf(parameters.get("TXN_AMOUNT")));
        orderService.addOrder(order);
        return "orderPlacement";
    }

    private boolean validateCheckSum(TreeMap<String, String> parameters, String merchKey, String paytmChecksum) throws Exception {
        return PaytmChecksum.verifySignature(paytmChecksum, merchKey, paytmChecksum);
    }
    
}
