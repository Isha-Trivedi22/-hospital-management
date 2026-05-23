package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Bill;
import com.example.demo.services.BillingService;

@RestController
@RequestMapping("/api/bills")
public class BillingController {

    @Autowired
    private BillingService billingService;

    @GetMapping
    public List<Bill> getAll() { return billingService.getAllBills(); }

    @PostMapping
    public Bill add(@RequestBody Bill bill) { return billingService.addBill(bill); }

    @GetMapping("/{id}")
    public Bill getById(@PathVariable Long id) { return billingService.getBillById(id); }

    @PutMapping("/{id}")
    public Bill update(@PathVariable Long id, @RequestBody Bill bill) {
        return billingService.updateBill(id, bill);
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        billingService.deleteBill(id);
        return "Bill deleted successfully";
    }
}