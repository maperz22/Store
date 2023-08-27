package com.maperz.invoiceService.repository;


import com.maperz.invoiceService.model.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    Optional<Invoice> findByOrderNumber(String orderNumber);
}
