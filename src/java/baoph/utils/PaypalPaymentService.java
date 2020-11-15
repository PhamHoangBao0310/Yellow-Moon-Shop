/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package baoph.utils;

import baoph.tblProduct.TblProductDTO;
import com.paypal.api.payments.Amount;
import com.paypal.api.payments.Item;
import com.paypal.api.payments.ItemList;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payer;
import com.paypal.api.payments.PayerInfo;
import com.paypal.api.payments.Payment;
import com.paypal.api.payments.PaymentExecution;
import com.paypal.api.payments.RedirectUrls;
import com.paypal.api.payments.Transaction;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author DELL
 */
public class PaypalPaymentService {

    private static final String CLIENT_ID = "";
    private static final String CLIENT_SECRET = "";
    private static final String MODE = "sandbox";

    public String authorizePayment(List<TblProductDTO> productInCart, Map<Integer, Integer> itemInCart) throws PayPalRESTException {
        Payer payer = getPayerInformation();
        RedirectUrls redirectUrls = getRedirectURLs();
        List<Transaction> listTransaction = getTransaction(productInCart, itemInCart);

        Payment requestPayment = new Payment();
        requestPayment.setTransactions(listTransaction);
        requestPayment.setRedirectUrls(redirectUrls);
        requestPayment.setPayer(payer);
        requestPayment.setIntent("authorize");

        APIContext apiContext = new APIContext(CLIENT_ID, CLIENT_SECRET, MODE);

        Payment approvedPayment = requestPayment.create(apiContext);

        return getApprovalLink(approvedPayment);
    }

    private RedirectUrls getRedirectURLs() {
        RedirectUrls redirectUrl = new RedirectUrls();
        redirectUrl.setCancelUrl("http://localhost:8080/yellowMoonShop/viewCart");
        redirectUrl.setReturnUrl("http://localhost:8080/yellowMoonShop/review_payment");

        return redirectUrl;
    }

    private Payer getPayerInformation() {
        Payer payer = new Payer();
        payer.setPaymentMethod("paypal");

        PayerInfo payerInfo = new PayerInfo();
        payer.setPayerInfo(payerInfo);
        return payer;
    }

    private List<Transaction> getTransaction(List<TblProductDTO> productInCart, Map<Integer, Integer> itemInCart) {
        Transaction transaction = new Transaction();
        ItemList itemList = new ItemList();
        List<Item> items = new ArrayList<>();

        int total = 0;
        for (TblProductDTO dto : productInCart) {
            Item item = new Item();
            item.setCurrency("USD");
            item.setName(dto.getProductName());
            item.setPrice("" + dto.getPrice());
            total = total + dto.getPrice() * itemInCart.get(dto.getProductID());
            item.setQuantity("" + itemInCart.get(dto.getProductID()));
            items.add(item);
        }
        itemList.setItems(items);
        transaction.setItemList(itemList);

        Amount amount = new Amount();
        amount.setCurrency("USD");
        amount.setTotal("" + total);
        transaction.setAmount(amount);
        List<Transaction> listTransaction = new ArrayList<>();
        listTransaction.add(transaction);

        return listTransaction;
    }

    private String getApprovalLink(Payment approvedPayment) {
        List<Links> links = approvedPayment.getLinks();
        String approvalLink = null;

        for (Links link : links) {
            if (link.getRel().equalsIgnoreCase("approval_url")) {
                approvalLink = link.getHref();
                break;
            }
        }

        return approvalLink;
    }

    public Payment getPaymentDetails(String paymentId) throws PayPalRESTException {
        APIContext apiContext = new APIContext(CLIENT_ID, CLIENT_SECRET, MODE);
        return Payment.get(apiContext, paymentId);
    }

    public Payment executePayment(String paymentId, String payerId)
            throws PayPalRESTException {
        PaymentExecution paymentExecution = new PaymentExecution();
        paymentExecution.setPayerId(payerId);

        Payment payment = new Payment().setId(paymentId);

        APIContext apiContext = new APIContext(CLIENT_ID, CLIENT_SECRET, MODE);

        return payment.execute(apiContext, paymentExecution);
    }
}
