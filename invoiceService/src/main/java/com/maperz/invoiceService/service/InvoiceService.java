package com.maperz.invoiceService.service;

import com.maperz.invoiceService.event.InvoiceEvent;

public interface InvoiceService {

    /**
    * Returns invoice path for given order number
    * */
    String findInvoice(String orderNumber);

    /**
     * Creates an invoice for given order
     * */
    void createInvoice(InvoiceEvent event);

}
