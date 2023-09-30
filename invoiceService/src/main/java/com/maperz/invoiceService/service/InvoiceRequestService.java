package com.maperz.invoiceService.service;

import com.maperz.invoiceService.dto.InvoiceMaker;

public interface InvoiceRequestService {

    byte[] requestInvoice(InvoiceMaker invoiceMaker);

}
